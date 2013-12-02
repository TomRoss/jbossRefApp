package org.app.minibank.minibankref1.mbean;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class FooAdmin implements Serializable, FooAdminMBean {

    private static final long serialVersionUID = 2328204868492121343L;

    private static final Logger log = Logger.getLogger(FooAdmin.class);

    private String myStringAttribute = "My Value";

    private String myAttrWithoutSetter = "Value not settable";

    private int myIntAttribute = 120;

    private long myLongAttribute = 12002548785213l;

    private byte myByteAttribute = -12;

    private boolean myBooleanAttribute = false;

    private double myDoubleAttribute = 0.1;

    private float myFloatAttribute = 0.5f;

    private double myRandomAttribute = Math.random();

    private ArrayList<FooAdmin> fooAdmins = new ArrayList<FooAdmin>();

    private FooAdmin myself;

    public FooAdmin() {
        fooAdmins.add(this);
        myself = this;
    }

    public FooAdmin getMyself() {
        return myself;
    }

    public ArrayList<FooAdmin> getFooAdmins() {
        return fooAdmins;
    }

    public String getMyAttrWithoutSetter() {
        return myAttrWithoutSetter;
    }

    public String getMyStringAttribute() {
        return myStringAttribute;
    }

    public void setMyStringAttribute(String myStringAttribute) {
        this.myStringAttribute = myStringAttribute;
    }

    public int getMyIntAttribute() {
        return myIntAttribute;
    }

    public void setMyIntAttribute(int myIntAttribute) {
        this.myIntAttribute = myIntAttribute;
    }

    public long getMyLongAttribute() {
        return myLongAttribute;
    }

    public void setMyLongAttribute(long myLongAttribute) {
        this.myLongAttribute = myLongAttribute;
    }

    public byte getMyByteAttribute() {
        return myByteAttribute;
    }

    public void setMyByteAttribute(byte myByteAttribute) {
        this.myByteAttribute = myByteAttribute;
    }

    public boolean isMyBooleanAttribute() {
        return myBooleanAttribute;
    }

    public void setMyBooleanAttribute(boolean myBooleanAttribute) {
        this.myBooleanAttribute = myBooleanAttribute;
    }

    public double getMyDoubleAttribute() {
        return myDoubleAttribute;
    }

    public void setMyDoubleAttribute(double myDoubleAttribute) {
        this.myDoubleAttribute = myDoubleAttribute;
    }

    public float getMyFloatAttribute() {
        return myFloatAttribute;
    }

    public void setMyFloatAttribute(float myFloatAttribute) {
        this.myFloatAttribute = myFloatAttribute;
    }

    public double getMyRandomAttribute() {
        return myRandomAttribute;
    }

    public void setMyRandomAttribute(double myRandomAttribute) {
        this.myRandomAttribute = myRandomAttribute;
    }

    public void doSomething() {
        log.info("doSomething() called");
    }

    public void doSomethingWithParam(String param) {
        log.info("doSomethingWithParam() called, with parameter : " + param);
    }

    public void doSomethingWithTwoHorizParam(String param1, String param2) {
        log.info("doSomethingWithTwoHorizParam() called, with parameters : " + param1 + " and " + param2);
    }

    public void doSomethingWithTwoVertParam(String param1, String param2) {
        log.info("doSomethingWithTwoVertParam() called, with parameters : " + param1 + " and " + param2);
    }

    public String doAndReturnSomething() {
        log.info("doAndReturnSomething() called");
        return "Yes, we can.";
    }

    public String complexMethod(int primitiveInt, Integer boxedInt, boolean bool, Double boxedDouble, String str) {
        return "Primitive int : " + primitiveInt + ", boxed int : " + boxedInt + ", bool : " + bool + ", boxed double : " + boxedDouble + ", string : " + str;
    }

    @Override
    public String toString() {
        return "I'm Foo Admin Bean";
    }

}
