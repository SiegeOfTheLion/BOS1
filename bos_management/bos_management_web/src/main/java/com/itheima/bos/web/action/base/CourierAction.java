package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.Standard;
import com.itheima.bos.service.base.CourierService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:CourierAction <br/>
 * Function: <br/>
 * Date: 2018年3月14日 下午5:42:11 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class CourierAction extends CommonAction<Courier> {

    public CourierAction() {

        super(Courier.class);
    }

    @Autowired
    private CourierService courierService;

    // 保存快递员数据courierAction_save
    @Action(value = "courierAction_save", results = {@Result(name = "success",
            location = "/pages/base/courier.html", type = "redirect")})
    public String save() {
        System.out.println("执行了save方法...............................");
        courierService.save(getModel());
        System.out.println(111);
        
        return SUCCESS;
    }

    /**
     * 快递员的分页查询
     * 
     * @throws IOException
     */
    @Action("courierAction_pageQuery")
    public String pageQuery() throws IOException {
        // 使用JpaSpecificationExecutor接口时需要传递两个参数(Specification,Pageable)
        // 创建一个Specification对象
        Specification<Courier> specification = new Specification<Courier>() {
            /**
             * 创建一个查询的where语句
             * 
             * @param root : 根对象.可以简单的认为就是泛型对象
             * @param cb : 构建查询条件
             * @return a {@link Predicate}, must not be {@literal null}.
             */
            @Override
            public Predicate toPredicate(Root<Courier> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 怎么获取表单中的属性
                // model
                String courierNum = getModel().getCourierNum();
                String company = getModel().getCompany();
                String type = getModel().getType();
                // 怎么获取standard中的name属性
                Standard standard = getModel().getStandard();
                // 定义一个list集合
                List<Predicate> list = new ArrayList<Predicate>();
                // 判断当快递员的id不为空的时候,做查询
                if (StringUtils.isNotEmpty(courierNum)) {
                    // 如果不为空的时候,做等值判断
                    // 参数一:where courierNum="001"
                    // 参数二 :具体的要比较的值
                    Predicate p1 =
                            cb.equal(root.get("courierNum").as(String.class),
                                    courierNum);
                    // System.out.println("方法中执行了toPredicate方法");
                    list.add(p1);
                }
                // 公司
                if (StringUtils.isNoneEmpty(company)) {
                    // 如果公司不为空的话,模糊查询
                    // where company like "%"+company+"%"
                    // 具体要比较的值
                    Predicate p2 = cb.like(root.get("company").as(String.class),
                            "%" + company + "%");
                    list.add(p2);
                }
                // 类型
                if (StringUtils.isNotEmpty(type)) {
                    // 等值查询
                    // where type = "小件员"
                    // 具体比较值
                    Predicate p3 =
                            cb.equal(root.get("type").as(String.class), type);
                    list.add(p3);
                }
                // 标准名称
                if (standard != null) {
                    // 获取收派标准的名字
                    String name = standard.getName();
                    // 判断标准名称是否为空
                    if (StringUtils.isNotEmpty(name)) {
                        Join<Object, Object> join = root.join("standard");
                        Predicate p4 = cb
                                .equal(join.get("name").as(String.class), name);
                        list.add(p4);
                    }
                }
                Predicate[] arr = new Predicate[list.size()];
                list.toArray(arr);
                Predicate predicate = cb.and(arr);

                return predicate;
            }

        };

        // EasyUI的页码是从1开始的
        // SPringDataJPA的页码是从0开始的
        // 所以要-1
        Pageable pageable = new PageRequest(page - 1, rows);
        // 调用业务层处理请求
        Page<Courier> page = courierService.findAll(specification, pageable);
        // 使用jsonConfig可以灵活控制输出的内容
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas", "takeTime"});
        page2json(page, jsonConfig);
        return NONE;
    }

    // 使用属性驱动获取要删除的快递员的Id
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    /**
     * 批量删除
     */
    @Action(value = "courierAction_batchDel",
            results = {@Result(name = "success",
                    location = "/pages/base/courier.html", type = "redirect")})
    public String batchdel() {
        courierService.batchdel(ids);
        return SUCCESS;
    }

    /**
     * 批量还原
     */
    @Action(value = "courierAction_batchRes",
            results = {@Result(name = "success",
                    location = "/pages/base/courier.html", type = "redirect")})
    public String batchRes() {
        courierService.batchRes(ids);
        return SUCCESS;
    }

    /**
     * 查询正常在职的快递员
     * 
     * @throws IOException
     */
    @Action("courierAction_listajax")
    public String listajax() throws IOException {
        //System.out.println("执行了程序.........................");
        List<Courier> list = courierService.findAvalible();
        JsonConfig jsonConfig = new JsonConfig();
        // 指定在生成json数据的时候要忽略的字段
        jsonConfig.setExcludes(new String[] {"fixedAreas", "takeTime"});

        list2json(list, jsonConfig);
        return NONE;

    }

    /*@Action("courierAction_listajax")
    public String listajax() throws IOException {
        System.out.println("执行了程序.........................");
        // 查询所有的在职的快递员

        Specification<Courier> specification = new Specification<Courier>() {

            @Override
            public Predicate toPredicate(Root<Courier> root,
                    CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 比较空值
                Predicate predicate =
                        cb.isNull(root.get("deltag").as(Character.class));

                return predicate;
            }
        };
        Page<Courier> p = courierService.findAll(specification, null);
        List<Courier> list = p.getContent();
        System.out.println("list="+list);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"fixedAreas", "takeTime"});
        list2json(list, jsonConfig);
        return NONE;
    }*/

}
