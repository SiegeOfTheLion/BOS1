package com.itheima.bos.service.take_delivery.OrderService;  
/**  
 * ClassName:OrderService <br/>  
 * Function:  <br/>  
 * Date:     2018年3月23日 下午12:19:07 <br/>       
 */

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;


@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface OrderService {
    
    @POST
    @Path("/saveOrder")
    void saveOrder(Order order);
    
   
    
}
  
