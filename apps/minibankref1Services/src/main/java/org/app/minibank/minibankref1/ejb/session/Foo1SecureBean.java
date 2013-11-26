package org.app.minibank.minibankref1.ejb.session;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref1.action.CallContext1;
import org.app.minibank.minibankref1.action.IAction1;
import org.app.minibank.minibankref1.ejb.session.Bar1Local;
import org.app.minibank.minibankref1.ejb.session.Foo1SecureRemote;
import org.jboss.ejb3.annotation.Clustered;

@Stateless
@Clustered
public class Foo1SecureBean implements Foo1SecureRemote {

    @Resource
    SessionContext sessionContext;

    @EJB
    Bar1Local bar1;

    @RolesAllowed("AppRole")
    public Object doSecureAction(IAction1 action) throws AccountException {
        return action.doIt(createCallContext("doSecureAction"));
    }

    CallContext1 createCallContext(String method) {
        return new CallContext1(this, sessionContext, method, bar1);
    }

}
