package org.app.minibank.minibankref1.action;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJBContext;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.app.minibank.minibankref1.ejb.session.Bar1Local;

public class CallContext1 {

    private static final Pattern TX_ID = Pattern.compile(", BasicAction: (.*) status: ");

    static TransactionManager tm;

    Map<String, Object> result = new LinkedHashMap<String, Object>();

    Object receiver;

    Bar1Local bar1;

    private EJBContext ejbContext;

    String method;

    private ConnectionFactory connectionFactoryLocal;

    private ConnectionFactory connectionFactoryRemote;

    private Queue localQueue;

    private Message message;

    static {
        try {
            tm = (TransactionManager) new InitialContext().lookup("java:/TransactionManager");
        } catch (Throwable e) {
            // ignore
        }
    }

    public CallContext1(Object receiver, EJBContext ejbContext, String method, Bar1Local bar1, Message message) {
        this.receiver = receiver;
        this.setEjbContext(ejbContext);
        this.method = method;
        this.bar1 = bar1;
        this.message = message;
        result.put("name", receiver.getClass().getSimpleName());
        result.put("method", method);
        result.put("node", System.getProperty("jboss.node.name"));
        result.put("tx", getTransactionId());
        result.put("user", ejbContext.getCallerPrincipal().getName());
    }

    public CallContext1(Object receiver, EJBContext ejbContext, String method, Bar1Local bar1) {
        this(receiver, ejbContext, method, bar1, null);

    }

    public CallContext1(Object receiver, EJBContext ejbContext, String method, ConnectionFactory localCF, ConnectionFactory remoteCF, Queue q, Message message) {
        this(receiver, ejbContext, method, null, message);
        this.connectionFactoryLocal = localCF;
        this.connectionFactoryRemote = remoteCF;
        this.localQueue = q;
    }

    public CallContext1(Object receiver, EJBContext ejbContext, String method, ConnectionFactory localCF, ConnectionFactory remoteCF, Queue q) {
        this(receiver, ejbContext, method, null);
        this.connectionFactoryLocal = localCF;
        this.connectionFactoryRemote = remoteCF;
        this.localQueue = q;
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

    public Bar1Local getBar1() {
        return bar1;
    }

    public void setBar1(Bar1Local bar1) {
        this.bar1 = bar1;
    }

    public EJBContext getEjbContext() {
        return ejbContext;
    }

    public void setEjbContext(EJBContext ejbContext) {
        this.ejbContext = ejbContext;
    }

    public ConnectionFactory getConnectionFactoryLocal() {
        return connectionFactoryLocal;
    }

    public void setConnectionFactoryLocal(ConnectionFactory connectionFactoryLocal) {
        this.connectionFactoryLocal = connectionFactoryLocal;
    }

    public ConnectionFactory getConnectionFactoryRemote() {
        return connectionFactoryRemote;
    }

    public void setConnectionFactoryRemote(ConnectionFactory connectionFactoryRemote) {
        this.connectionFactoryRemote = connectionFactoryRemote;
    }

    public Queue getLocalQueue() {
        return localQueue;
    }

    public void setLocalQueue(Queue localQueue) {
        this.localQueue = localQueue;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
