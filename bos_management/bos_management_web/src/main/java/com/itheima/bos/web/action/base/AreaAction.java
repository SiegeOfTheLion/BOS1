package com.itheima.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.utils.PinYin4jUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * ClassName:AreaAction <br/>
 * Function: <br/>
 * Date: 2018年3月15日 下午4:42:15 <br/>
 */
@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class AreaAction extends CommonAction<Area> {

    public AreaAction(){
        super(Area.class);
    }


    @Autowired
    private AreaService areaService;

    /**
     * 文件上传 importXLS:. <br/>
     * 
     * @return
     */
    // 使用属性驱动获取文件
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    @Action(value = "areaAction_importXLS", results = {@Result(name = "success",
            location = "/pages/base/area.html", type = "redirect")})
    public String importXLS() {
        // 创建一个集合,用来封装数据的集合
        List<Area> list = new ArrayList<Area>();

        try {
            // 创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));

            // 读取工作簿,从哪一页开始
            HSSFSheet sheet = workbook.getSheetAt(0);
            // 对工作簿里面的内容取出来
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                // 省会
                String province = row.getCell(1).getStringCellValue();
                // 城市
                String city = row.getCell(2).getStringCellValue();
                // 区域
                String district = row.getCell(3).getStringCellValue();
                // 邮政
                String postcode = row.getCell(4).getStringCellValue();

                // System.out.println(city);

                // 截掉省会的最后一个字
                province = province.substring(0, province.length() - 1);
                // 城市的最后一个
                city = city.substring(0, city.length() - 1);
                district.substring(0, district.length() - 1);
                // 使用工具类把城市变成大写的拼音
                String citycode =
                        PinYin4jUtils.hanziToPinyin(city, "").toUpperCase();
                // 省+市+区的首字母
                String[] headByString = PinYin4jUtils
                        .getHeadByString(province + city + district);
                System.out.println(headByString);
                String shortcode =
                        PinYin4jUtils.stringArrayToString(headByString);

                // 构建一个Area
                Area area = new Area();
                area.setCity(city);
                area.setProvince(province);
                area.setDistrict(district);
                area.setCitycode(citycode);
                area.setShortcode(shortcode);
                area.setPostcode(postcode);

                list.add(area);

            }
            // 保存
            areaService.save(list);
            // 关闭资源
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SUCCESS;
    }
    //属性
    private int page;
    private int rows;
    public void setPage(int page) {
        this.page = page;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action("areaAction_pageQuery")
    public String pageQuery() throws IOException {
        
        //创建pageable对象
        Pageable pageable = new PageRequest(page-1, rows);
        //查询
        Page<Area> page = areaService.findAll(pageable);
        
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas"});
        
        page2json(page, jsonConfig);
        
        return NONE;
    }

}
