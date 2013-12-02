/*
 * Copyright (c) 2012 by Lombard Odier Darier Hentsch, Geneva, Switzerland.
 * This software is subject to copyright protection under the laws of
 * Switzerland and other countries.  ALL RIGHTS RESERVED.
 * 
 */

package org.app.minibank.minibankref1.action;

import java.io.IOException;
import java.util.HashMap;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.AccountException;

/**
 * @version $Revision:$.
 * @author arteconsult.
 * @last.author $Author:$.
 * @last.checkin $Date:$.
 */
public class CallJMXAction1 implements IAction1 {

    public enum JMX_OPERATION {
        GET_ATTRIBUTE, GET_ATTRIBUTES, GET_MBEANCOUNT, SET_ATTRIBUTE, SET_ATTRIBUTES, INVOKE, IS_REGISTERED, IS_INSTANCE_OF, GET_OBJECT_INSTANCE

    };

    private static final long serialVersionUID = 4993789437752912142L;

    private static final Logger log = Logger.getLogger(CallJMXAction1.class);

    private HashMap<String, Object> env = new HashMap<String, Object>();

    private String connectionUrlString;

    private JMX_OPERATION currentJmxOperation;

    private String objectName = "";

    private String attribute = "";

    private String[] attributes;

    private String attributeName = "";

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String[] getAttributes() {
        return attributes;
    }

    public void setAttributes(String[] attributes) {
        this.attributes = attributes;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public AttributeList getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(AttributeList attributeList) {
        this.attributeList = attributeList;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public String[] getSignature() {
        return signature;
    }

    public void setSignature(String[] signature) {
        this.signature = signature;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String attributeValue = "";

    private AttributeList attributeList;

    private Object[] params;

    private String[] signature;

    private String operationName = "";

    private String className = "";

    public HashMap<String, Object> getEnv() {
        return env;
    }

    public void setEnv(HashMap<String, Object> env) {
        this.env = env;
    }

    public String getConnectionUrlString() {
        return connectionUrlString;
    }

    public void setConnectionUrlString(String connectionUrlString) {
        this.connectionUrlString = connectionUrlString;
    }

    public JMX_OPERATION getCurrentJmxOperation() {
        return currentJmxOperation;
    }

    public void setCurrentJmxOperation(JMX_OPERATION currentJmxOperation) {
        this.currentJmxOperation = currentJmxOperation;
    }

    /* (non-Javadoc)
     * @see org.app.minibank.minibankref1.action.IAction1#doIt(org.app.minibank.minibankref1.action.CallContext1)
     */
    @Override
    public Object doIt(CallContext1 cc) throws AccountException {

        JMXServiceURL url;
        JMXConnector jmxc = null;
        try {
            url = new JMXServiceURL(connectionUrlString);
            jmxc = JMXConnectorFactory.connect(url, env);

            jmxc.connect();
            Object jmxResponseObject = null;
            switch (getCurrentJmxOperation()) {
            case GET_ATTRIBUTE:
                jmxResponseObject = getAttribute(jmxc);
                break;

            case GET_ATTRIBUTES:
                jmxResponseObject = getAttributes(jmxc);
                break;

            case GET_MBEANCOUNT:
                jmxResponseObject = getMbeanCount(jmxc);
                break;

            case SET_ATTRIBUTE:
                jmxResponseObject = setAttribute(jmxc);
                break;

            case SET_ATTRIBUTES:
                jmxResponseObject = setAttributes(jmxc);
                break;

            case INVOKE:
                jmxResponseObject = invoke(jmxc);
                break;

            case IS_REGISTERED:
                jmxResponseObject = isRegistered(jmxc);
                break;

            case IS_INSTANCE_OF:
                jmxResponseObject = isInstanceOf(jmxc);
                break;

            case GET_OBJECT_INSTANCE:
                jmxResponseObject = getObjectInstance(jmxc);
                break;
            }
            log.info(jmxResponseObject);
            return jmxResponseObject;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AccountException("Could not do the JMX action '" + getCurrentJmxOperation() + "': " + e.getMessage(), e);
        } finally {
            if (jmxc != null) try {
                jmxc.close();
            } catch (IOException e) {
                log.warn("Could not close jmx connection", e);
            }

        }

    }

    private Object getAttribute(JMXConnector jmxc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException,
            ReflectionException, NullPointerException, IOException {
        return jmxc.getMBeanServerConnection().getAttribute(new ObjectName(getObjectName()), getAttribute());
    }

    private Object getAttributes(JMXConnector jmxc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException,
            ReflectionException, NullPointerException, IOException {
        return jmxc.getMBeanServerConnection().getAttributes(new ObjectName(getObjectName()), getAttributes());
    }

    private Object getMbeanCount(JMXConnector jmxc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException,
            ReflectionException, NullPointerException, IOException {
        return jmxc.getMBeanServerConnection().getMBeanCount();
    }

    private Object setAttribute(JMXConnector jmxc) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException,
            MalformedObjectNameException, MBeanException, ReflectionException, NullPointerException, IOException {
        jmxc.getMBeanServerConnection().setAttribute(new ObjectName(getObjectName()), new Attribute(getAttributeName(), getAttributeValue()));
        return null;
    }

    private Object setAttributes(JMXConnector jmxc) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException,
            MalformedObjectNameException, MBeanException, ReflectionException, NullPointerException, IOException {
        jmxc.getMBeanServerConnection().setAttributes(new ObjectName(getObjectName()), getAttributeList());
        return null;
    }

    private Object invoke(JMXConnector jmxc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException,
            ReflectionException, NullPointerException, IOException {
        return jmxc.getMBeanServerConnection().invoke(new ObjectName(getObjectName()), getOperationName(), getParams(), getSignature());
    }

    private Object isRegistered(JMXConnector jmxc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException,
            ReflectionException, NullPointerException, IOException {
        return new Boolean(jmxc.getMBeanServerConnection().isRegistered(new ObjectName(getObjectName())));
    }

    private Object isInstanceOf(JMXConnector jmxc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException, MBeanException,
            ReflectionException, NullPointerException, IOException {
        return new Boolean(jmxc.getMBeanServerConnection().isInstanceOf(new ObjectName(getObjectName()), getClassName()));
    }

    private Object getObjectInstance(JMXConnector jmxc) throws AttributeNotFoundException, InstanceNotFoundException, MalformedObjectNameException,
            MBeanException, ReflectionException, NullPointerException, IOException {
        return jmxc.getMBeanServerConnection().getObjectInstance(new ObjectName(getObjectName()));

    }

}
