package org.app.minibank.minibankref2.action;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJBContext;
import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class CallContext2 {

    private static final Pattern TX_ID = Pattern.compile(", BasicAction: (.*) status: ");

    static TransactionManager tm;

    Map<String, Object> result = new LinkedHashMap<String, Object>();

    Object receiver;

    private EJBContext ejbContext;

    String method;

    static {
        try {
            tm = (TransactionManager) new InitialContext().lookup("java:/TransactionManager");
        } catch (Throwable e) {
            // ignore
        }
    }

    public CallContext2(Object receiver, EJBContext sessionContext, String method) {
        this.receiver = receiver;
        this.setEjbContext(sessionContext);
        this.method = method;
        result.put("name", receiver.getClass().getSimpleName());
        result.put("method", method);
        result.put("node", System.getProperty("jboss.node.name"));
        result.put("tx", getTransactionId());
        result.put("user", sessionContext.getCallerPrincipal().getName());
    }

    public String getTransactionId() {
        try {
            if (tm == null) return null;
            Transaction tx = tm.getTransaction();
            if (tx.getStatus() == Status.STATUS_UNKNOWN || tx.getStatus() == Status.STATUS_NO_TRANSACTION) return null;
            String s = tx.toString();
            Matcher m = TX_ID.matcher(s);
            return m.find() ? m.group(1) : s;
        } catch (Exception e) {
            return null;
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public Object getReceiver() {
        return receiver;
    }

    public void setReceiver(Object receiver) {
        this.receiver = receiver;
    }

    public EJBContext getEjbContext() {
        return ejbContext;
    }

    public void setEjbContext(EJBContext ejbContext) {
        this.ejbContext = ejbContext;
    }

}
