package org.app.minibank.minibankref1.jmx;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref.EAP6NamingContext;
import org.app.minibank.minibankref.TestUtil;
import org.app.minibank.minibankref1.action.CallJMXAction1;
import org.app.minibank.minibankref1.ejb.session.Foo1Remote;
import org.app.minibank.minibankref1.mbean.FooAdmin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClientJmxRolesTest {

    private static final Logger log = Logger.getLogger(ClientJmxRolesTest.class);

    private static final String ADMINISTRATOR_PASSWORD = "Passw0rd";

    private static final String ADMINISTRATOR_USER = "admin";

    private static final String SUPERUSER_PASSWORD = "Passw0rd!";

    private static final String SUPERUSER_USER = "root";

    private static final String MONITORING_PASSWORD = "Passw0rd!";

    private static final String MONITORING_USER = "monitoring";

    private static final String JMX_REMOTING_URL = TestUtil.getJmxUrl1A();

    public static final String UNAUTHORIZED = "Unauthorized access";

    public static final String NOT_AUTHORIZED = "Not authorized";

    EAP6NamingContext ctx;

    @Before
    public void before() throws NamingException {
        ctx = new EAP6NamingContext(TestUtil.createProperties1());
    }

    @After
    public void after() throws NamingException {
        if (ctx != null) ctx.close();
    }

    private Foo1Remote getFoo1Remote() throws NamingException {
        return (Foo1Remote) ctx.lookup(TestUtil.getJndi1());
    }

    @Test
    public void testMonitorGetAttributeJvm() throws Exception {
        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.GET_ATTRIBUTE);
        action.setObjectName("java.lang:type=Memory");
        action.setAttribute("HeapMemoryUsage");

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        assertNotNull(obj);
        log.info(obj);
        log.info(((javax.management.openmbean.CompositeData) obj).get("committed"));
    }

    @Test
    public void testMonitorGetAttributesJvm() throws Exception {
        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.GET_ATTRIBUTES);
        action.setObjectName("java.lang:type=Memory");
        action.setAttributes(new String[] { "HeapMemoryUsage", "NonHeapMemoryUsage" });

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        assertNotNull(obj);
        log.info(obj);

    }

    @Test
    public void testMonitorGetMbeanCount() throws Exception {
        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.GET_MBEANCOUNT);

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        assertNotNull(obj);
        log.info(obj);

    }

    @Test
    public void testMonitorSetAttributeJboss() throws Exception {
        boolean exceptionThrown = false;
        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.SET_ATTRIBUTE);
        action.setObjectName("jboss.as:interface=management");
        action.setAttributeName("inetAddress");
        action.setAttributeValue("0.0.0.0");

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();
        try {
            Object obj = service.defaultTxPropagation(action);
            log.info(obj);

        } catch (AccountException e) {
            log.info(e.getMessage());
            assertTrue(e.getMessage().contains(NOT_AUTHORIZED));
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testMonitorSetAttributesJboss() throws Exception {
        boolean exceptionThrown = false;

        AttributeList attributeList = new AttributeList();

        attributeList.add(new Attribute("inetAddress", "0.0.0.0"));
        attributeList.add(new Attribute("name", "management"));

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.SET_ATTRIBUTES);
        action.setObjectName("jboss.as:interface=management");
        action.setAttributeList(attributeList);

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();
        try {
            Object obj = service.defaultTxPropagation(action);
            log.info(obj);

        } catch (AccountException e) {
            log.info(e.getMessage());
            assertTrue(e.getMessage().contains(NOT_AUTHORIZED));
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testMonitorInvokeJvm() throws Exception {
        boolean exceptionThrown = false;

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.INVOKE);
        action.setObjectName("java.util.logging:type=Logging");
        action.setOperationName("getLoggerLevel");
        action.setParams(new Object[] { "org.jboss.as.config" });
        action.setSignature(new String[] { String.class.getName() });

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();
        try {
            Object obj = service.defaultTxPropagation(action);
            log.info(obj);
        } catch (AccountException e) {
            log.info(e.getMessage());
            assertTrue(e.getMessage().contains(UNAUTHORIZED));
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

    }

    @Test
    public void testMonitorIsRegisteredJboss() throws Exception {

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.IS_REGISTERED);
        action.setObjectName("jboss.as:interface=management");

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        log.info(obj);

    }

    @Test
    public void testMonitorIsInstanceOfJboss() throws Exception {
        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.IS_INSTANCE_OF);
        action.setObjectName("jboss.as:interface=management");
        action.setClassName("javax.management.ObjectInstance");

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        log.info(obj);

    }

    @Test
    public void testMonitorGetObjectInstanceJboss() throws Exception {

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.GET_OBJECT_INSTANCE);
        action.setObjectName("jboss.as:interface=management");

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        log.info(obj);
        log.info(obj.getClass().getName());

    }

    @Test
    public void testAdminInvokeJvm() throws Exception {

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.INVOKE);
        action.setObjectName("java.util.logging:type=Logging");
        action.setOperationName("getLoggerLevel");
        action.setParams(new Object[] { "org.jboss.as.config" });
        action.setSignature(new String[] { String.class.getName() });

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = ADMINISTRATOR_USER;
        credentials[1] = ADMINISTRATOR_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        log.info(obj);

    }

    @Test
    public void testMonitorGetAttributeJboss() throws Exception {
        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.GET_ATTRIBUTE);
        action.setObjectName("jboss.as:subsystem=jmx");
        action.setAttribute("showModel");

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        log.info(obj);
    }

    @Test
    public void testMonitorInvokeJboss() throws Exception {
        boolean exceptionThrown = false;

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.INVOKE);
        action.setObjectName("jboss.as:subsystem=jmx");
        action.setOperationName("addConfigurationAuditLog");
        action.setParams(new Object[] { true, true, true });
        action.setSignature(new String[] { boolean.class.getName(), boolean.class.getName(), boolean.class.getName() });

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = MONITORING_USER;
        credentials[1] = MONITORING_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();
        try {
            Object obj = service.defaultTxPropagation(action);
            log.info(obj);
        } catch (AccountException e) {
            log.info(e.getMessage());
            assertTrue(e.getMessage().contains(NOT_AUTHORIZED));
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

    }

    @Test
    public void testAdminInvokeJboss() throws Exception {
        boolean exceptionThrown = false;

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.INVOKE);
        action.setObjectName("jboss.as:subsystem=jmx");
        action.setOperationName("addConfigurationAuditLog");
        action.setParams(new Object[] { true, true, true });
        action.setSignature(new String[] { boolean.class.getName(), boolean.class.getName(), boolean.class.getName() });

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = ADMINISTRATOR_USER;
        credentials[1] = ADMINISTRATOR_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();
        try {
            Object obj = service.defaultTxPropagation(action);
            log.info(obj);
        } catch (AccountException e) {
            log.info(e.getMessage());
            assertTrue(e.getMessage().contains(NOT_AUTHORIZED));
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

    }

    @Test
    public void testSuperUserInvokeJboss() throws Exception {

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.INVOKE);
        action.setObjectName("jboss.as:subsystem=jmx");
        action.setOperationName("addConfigurationAuditLog");
        action.setParams(new Object[] { true, true, true });
        action.setSignature(new String[] { boolean.class.getName(), boolean.class.getName(), boolean.class.getName() });

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = SUPERUSER_USER;
        credentials[1] = SUPERUSER_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();
        try {
            Object obj = service.defaultTxPropagation(action);
            log.info(obj);
        } catch (AccountException e) {
            log.info(e.getMessage());
            assertTrue(!e.getMessage().contains(NOT_AUTHORIZED));
            assertTrue(!e.getMessage().contains(UNAUTHORIZED));
        }

    }

    @Test
    public void testAdminInvokeApp() throws Exception {

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.INVOKE);
        action.setObjectName("org.app.minibank:type=" + FooAdmin.class.getName());
        action.setOperationName("complexMethod");
        action.setParams(new Object[] { 1, new Integer(2), true, new Double(3.0), "Some Text" });
        action.setSignature(new String[] { int.class.getName(), Integer.class.getName(), boolean.class.getName(), Double.class.getName(),
                String.class.getName() });

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = ADMINISTRATOR_USER;
        credentials[1] = ADMINISTRATOR_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        log.info(obj);

    }

    @Test
    public void testAdminGetAttributeApp() throws Exception {

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.GET_ATTRIBUTE);
        action.setObjectName("org.app.minibank:type=" + FooAdmin.class.getName());
        action.setAttribute("FooAdmins");

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = ADMINISTRATOR_USER;
        credentials[1] = ADMINISTRATOR_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);

        Foo1Remote service = getFoo1Remote();

        Object obj = service.defaultTxPropagation(action);
        log.info(obj);

    }

    @Test
    public void testAdminSetAttributeApp() throws Exception {

        String testValue = new Double(Math.random()).toString();

        CallJMXAction1 action = new CallJMXAction1();

        action.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.SET_ATTRIBUTE);
        action.setObjectName("org.app.minibank:type=" + FooAdmin.class.getName());
        action.setAttributeName("MyStringAttribute");
        action.setAttributeValue(testValue);

        HashMap<String, Object> env = new HashMap<String, Object>();
        String[] credentials = new String[2];
        credentials[0] = ADMINISTRATOR_USER;
        credentials[1] = ADMINISTRATOR_PASSWORD;
        env.put("jmx.remote.credentials", credentials);
        action.setEnv(env);

        action.setConnectionUrlString(JMX_REMOTING_URL);
        Foo1Remote service = getFoo1Remote();
        Object obj = service.defaultTxPropagation(action);

        // now read the attribute again

        CallJMXAction1 actionGet = new CallJMXAction1();
        actionGet.setCurrentJmxOperation(CallJMXAction1.JMX_OPERATION.GET_ATTRIBUTE);
        actionGet.setObjectName("org.app.minibank:type=" + FooAdmin.class.getName());
        actionGet.setAttribute("MyStringAttribute");

        actionGet.setEnv(env);

        actionGet.setConnectionUrlString(JMX_REMOTING_URL);

        service = getFoo1Remote();

        obj = service.defaultTxPropagation(actionGet);
        log.info(obj);
        assertTrue(testValue.equals(obj));
    }

    @Test
    public void testDirectJMXCallFromJunit() throws Exception {
        JMXConnector jmxc = null;
        try {
            JMXServiceURL url = new JMXServiceURL(JMX_REMOTING_URL);
            HashMap<String, Object> env = new HashMap<String, Object>();
            String[] credentials = new String[2];
            credentials[0] = ADMINISTRATOR_USER;
            credentials[1] = ADMINISTRATOR_PASSWORD;
            env.put("jmx.remote.credentials", credentials);
            jmxc = JMXConnectorFactory.connect(url, env);
            jmxc.connect();
            ObjectName objName = new ObjectName("org.app.minibank:type=" + FooAdmin.class.getName());
            Object value = jmxc.getMBeanServerConnection().getAttribute(objName, "MyStringAttribute");

            log.info("MyStringAttribute=" + value);

        } finally {
            if (jmxc != null) try {
                jmxc.close();
            } catch (IOException e) {
                log.warn("Could not close jmx connection", e);
            }

        }

    }

}
