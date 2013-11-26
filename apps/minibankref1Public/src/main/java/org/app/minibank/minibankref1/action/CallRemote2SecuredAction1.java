package org.app.minibank.minibankref1.action;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref2.action.BaseAction2;
import org.app.minibank.minibankref2.ejb.session.Foo2SecureRemote;

public class CallRemote2SecuredAction1 extends CallRemoteAction1 {

    private static final long serialVersionUID = 6277331890801782155L;

    @Override
    protected Object makeCall(CallContext1 cc, Object service) throws AccountException {
        Foo2SecureRemote remote2 = narrowObject(service, Foo2SecureRemote.class);
        return remote2.doSecureAction(new BaseAction2());
    }

}
