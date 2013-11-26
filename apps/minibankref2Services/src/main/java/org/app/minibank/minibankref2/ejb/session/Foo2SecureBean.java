package org.app.minibank.minibankref2.ejb.session;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref2.action.CallContext2;
import org.app.minibank.minibankref2.action.IAction2;
import org.app.minibank.minibankref2.ejb.session.Foo2SecureRemote;
import org.jboss.ejb3.annotation.Clustered;

@Stateless
@Clustered
public class Foo2SecureBean implements Foo2SecureRemote {

    @Resource
    SessionContext sessionContext;

    @RolesAllowed("AppRole")
    public Object doSecureAction(IAction2 action) throws AccountException {
        return action.doIt(createCallContext("doSecureAction"));
    }

    CallContext2 createCallContext(String method) {
        return new CallContext2(this, sessionContext, method);
    }
}
