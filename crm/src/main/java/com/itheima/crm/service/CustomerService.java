package com.itheima.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.itheima.crm.domain.Customer;

/**
 * ClassName:CustomerService <br/>
 * Function: <br/>
 * Date: 2018年3月18日 下午4:33:48 <br/>
 */
@Consumes(MediaType.APPLICATION_JSON) // 参数类型
@Produces(MediaType.APPLICATION_JSON) // 返回值类型
public interface CustomerService {
    // http://localhost:8180/crm/webService/customerService?_wadl
    /**
     * 查询所有
     */
    @GET
    @Path("/findAll")
    public List<Customer> findAll();

    /**
     * 查询未关联定区的用户
     */
    @GET
    @Path("/findCustomersUnAssociated")
    public List<Customer> findCustomersUnAssociated();

    /**
     * 查询关联到指定定区的用户
     */
    @GET
    @Path("/findCustomersAssociated2FixedArea")
    public List<Customer> findCustomersAssociated2FixedArea(
            @QueryParam("fixedAreaId") String fixedAreaId);

    /**
     * 把未关联的添加到定区
     */
    @PUT
    @Path("/associatedCustomer2FixedArea")
    void associatedCustomer2FixedArea(
            @QueryParam("fixedAreaId") String fixedAreaId,
            @QueryParam("customerIds") Long[] customerIds);

    /**
     * 保存用户
     */
    @POST
    @Path("/save")
    void save(Customer customer);

    /**
     * 用户激活也就是做登录跳转
     */
    @PUT
    @Path("/active")
    void active(@QueryParam("telephone") String telephone);

    /**
     * 校验用户是否已经激活
     */
    @GET
    @Path("/isActive")
    Customer isActive(@QueryParam("telephone") String telephone);

    /**
     * 登入
     */
    @GET
    @Path("/login")
    Customer login(@QueryParam("telephone") String telephone,
            @QueryParam("password") String password);

    @GET
    @Path("/lostBlurs")
    Customer lostBlurs(@QueryParam("telephone") String telephone);

    /**
     * 查询定区ID
     */
    @GET
    @Path("/findFixedAreaIdByAddress")
    String findFixedAreaIdByAddress(@QueryParam("address") String address);
}
