package com.itheima.activemq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**  
 * ClassName:Cstomer <br/>  
 * Function:  <br/>  
 * Date:     2018年3月25日 下午5:09:21 <br/>       
 */
@Component
public class SmsConsumer implements MessageListener{
    
    
    @Override
    public void onMessage(Message message) {
          MapMessage mapMessage = (MapMessage) message;
          try {
            String telephone = mapMessage.getString("telephone");
            String code = mapMessage.getString("code");
            System.out.println(code + "========" + telephone);
            //发送给后台
            
        } catch (JMSException e) {
            e.printStackTrace();  
        }
          while(true){
              
          }
    }

}
  
