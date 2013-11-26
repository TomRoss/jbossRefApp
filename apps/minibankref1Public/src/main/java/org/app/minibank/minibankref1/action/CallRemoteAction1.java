package org.app.minibank.minibankref1.action;

import java.util.Properties;

import javax.ejb.TransactionAttributeType;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.SystemException;

@SuppressWarnings("serial")
public abstract class CallRemoteAction1 extends BaseAction1 {

    Properties remoteLookupProperties;

    TransactionAttributeType transactionAttributeType = null;

    String jndi;

    boolean narrow = false;

    protected Object doItBasic(CallContext1 cc) throws AccountException {

        EAP6NamingContext ctx = null;

        try {
            try {
                long start = System.currentTimeMillis();
                ctx = new EAP6NamingContext(remoteLookupProperties);
                long duration = System.currentTimeMillis() - start;
                cc.getResult().put("remoteCtxMs", duration);
            } catch (NamingException e) {
                throw new SystemException("Could not connect to EJB Service", e);
            }

            Object service = null;

            try {
                long start = System.currentTimeMillis();
                service = ctx.lookup(jndi);
                long duration = System.currentTimeMillis() - start;
                cc.getResult().put("remoteLookupMs", duration);
            } catch (NamingException e) {
                throw new SystemException("unable to get service", e);
            }

            long start = System.currentTimeMillis();
            Object res = makeCall(cc, service);
            long duration = System.currentTimeMillis() - start;
            cc.getResult().put("remoteCallMs", duration);
            return res;

        } finally {
            try {
                if (ctx != null) ctx.close();
            } catch (NamingException e) {
                throw new SystemException("Could not close context", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T narrowObject(Object obj, Class<T> cl) {
        return (T) (narrow ? PortableRemoteObject.narrow(obj, cl) : obj);
    }

    protected abstract Object makeCall(CallContext1 cc, Object service) throws AccountException;

    public Properties getRemoteLookupProperties() {
        return remoteLookupProperties;
    }

    public CallRemoteAction1 setRemoteLookupProperties(Properties remoteLookupProperties) {
        this.remoteLookupProperties = remoteLookupProperties;
        return this;
    }

    public TransactionAttributeType getTransactionAttributeType() {
        return transactionAttributeType;
    }

    public CallRemoteAction1 setTransactionAttributeType(TransactionAttributeType transactionAttributeType) {
        this.transactionAttributeType = transactionAttributeType;
        return this;
    }

    public String getJndi() {
        return jndi;
    }

    public CallRemoteAction1 setJndi(String jndi) {
        this.jndi = jndi;
        return this;
    }

    public boolean isNarrow() {
        return narrow;
    }

    public CallRemoteAction1 setNarrow(boolean narrow) {
        this.narrow = narrow;
        return this;
    }

}
