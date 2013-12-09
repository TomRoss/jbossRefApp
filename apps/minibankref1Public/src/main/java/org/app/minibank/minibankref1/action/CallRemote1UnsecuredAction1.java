package org.app.minibank.minibankref1.action;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref1.ejb.session.Foo1Remote;

public class CallRemote1UnsecuredAction1 extends CallRemoteAction1 {

    private static final long serialVersionUID = 6277331890801782155L;

    @Override
    protected Object makeCall(CallContext1 cc, Object service) throws AccountException {
        Foo1Remote remote1 = narrowObject(service, Foo1Remote.class);
        Object result = remote1.defaultTxPropagation(new BaseAction1());
        cc.getResult().put("CALL", result);
        // mytest5
        return cc.getResult();
    }

}
