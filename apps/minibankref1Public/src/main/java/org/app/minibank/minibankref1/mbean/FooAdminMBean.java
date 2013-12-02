package org.app.minibank.minibankref1.mbean;

import java.util.ArrayList;

public interface FooAdminMBean {

    public ArrayList<FooAdmin> getFooAdmins();

    public FooAdmin getMyself();

    public String getMyAttrWithoutSetter();

    public String getMyStringAttribute();

    public void setMyStringAttribute(String myStringAttribute);

    public int getMyIntAttribute();

    public void setMyIntAttribute(int myIntAttribute);

    public long getMyLongAttribute();

    public void setMyLongAttribute(long myLongAttribute);

    public byte getMyByteAttribute();

    public void setMyByteAttribute(byte myByteAttribute);

    public boolean isMyBooleanAttribute();

    public void setMyBooleanAttribute(boolean myBooleanAttribute);

    public double getMyDoubleAttribute();

    public void setMyDoubleAttribute(double myDoubleAttribute);

    public float getMyFloatAttribute();

    public void setMyFloatAttribute(float myFloatAttribute);

    public double getMyRandomAttribute();

    public void setMyRandomAttribute(double myRandomAttribute);

    public void doSomething();

    public void doSomethingWithParam(String param);

    public void doSomethingWithTwoHorizParam(String param1, String param2);

    public void doSomethingWithTwoVertParam(String param1, String param2);

    public String complexMethod(int primitiveInt, Integer boxedInt, boolean bool, Double boxedDouble, String str);

}
