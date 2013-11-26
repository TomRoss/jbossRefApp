package org.app.minibank.minibankref3.ejb.session;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref3.action.CallContext3;
import org.app.minibank.minibankref3.action.IAction3;
import org.app.minibank.minibankref3.ejb.session.Foo3SecureRemote;
import org.jboss.ejb3.annotation.Clustered;

@Stateless
@Clustered
public class Foo3SecureBean implements Foo3SecureRemote {

    @Resource
    SessionContext sessionContext;

    @RolesAllowed("AppRole")
    public Object doSecureAction(IAction3 action) throws AccountException {
        return action.doIt(createCallContext("doSecureAction"));
    }

    CallContext3 createCallContext(String method) {
        return new CallContext3(this, sessionContext, method);
    }
}
