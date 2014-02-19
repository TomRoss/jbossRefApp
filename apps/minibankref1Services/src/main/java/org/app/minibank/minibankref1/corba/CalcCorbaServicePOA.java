package org.app.minibank.minibankref1.corba;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref1.Arte.ICalcPOA;

public class CalcCorbaServicePOA extends ICalcPOA {

    private static final Logger log = Logger.getLogger(CalcCorbaServicePOA.class);

    @Override
    public void sayHello(String s) {
        log.info("sayHello " + s);
    }

}
