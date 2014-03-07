package org.app.minibank.minibankref1.action;

import javax.jms.JMSException;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref1.jpa.MyEntity1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CallJPAAction1 extends BaseAction1 {

    private static final long serialVersionUID = 6277331890801782155L;

    @Override
    protected Object doItBasic(CallContext1 cc) throws AccountException {
        try {
            Object result = cc.getResult();
            ObjectMapper mapper = new ObjectMapper();
            String resultJson = mapper.writeValueAsString(result);

            MyEntity1 myentity = new MyEntity1();
            String corId = cc.getMessage().getJMSCorrelationID();
            myentity.setCorrelationId(corId);
            myentity.setName("MyName-" + corId);
            myentity.setJson(resultJson);
            cc.getEntityManager().persist(myentity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error when Processing JSon message: " + e, e);
        } catch (JMSException e) {
            throw new RuntimeException("Error when sending JMS message processing: " + e, e);
        }
        return cc.getResult();
    }

}
