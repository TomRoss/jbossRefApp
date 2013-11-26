package org.app.minibank.minibankref1.ejb.session;

import static javax.ejb.TransactionAttributeType.*;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref1.action.CallContext1;
import org.app.minibank.minibankref1.action.IAction1;
import org.app.minibank.minibankref1.ejb.session.Bar1Local;
import org.app.minibank.minibankref1.ejb.session.Foo1Remote;
import org.jboss.ejb3.annotation.Clustered;

@Stateless
@Clustered
public class Foo1Bean implements Foo1Remote {

    @Resource
    SessionContext sessionContext;

    @EJB
    Bar1Local bar1;

    @Override
    @TransactionAttribute(value = REQUIRED)
    public Object required(IAction1 action) throws AccountException {
        return action.doIt(createCallContext("required"));
    }

    @Override
    @TransactionAttribute(value = MANDATORY)
    public Object mandatory(IAction1 action) throws AccountException {
        return action.doIt(createCallContext("mandatory"));
    }

    @Override
    @TransactionAttribute(value = SUPPORTS)
    public Object supports(IAction1 action) throws AccountException {
        return action.doIt(createCallContext("supports"));
    }

    @Override
    @TransactionAttribute(value = NOT_SUPPORTED)
    public Object notSupported(IAction1 action) throws AccountException {
        return action.doIt(createCallContext("notSupported"));
    }

    @Override
    @TransactionAttribute(value = NEVER)
    public Object never(IAction1 action) throws AccountException {
        return action.doIt(createCallContext("never"));
    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public Object requiresNew(IAction1 action) throws AccountException {
        return action.doIt(createCallContext("requiresNew"));
    }

    @Override
    public Object defaultTxPropagation(IAction1 action) throws AccountException {
        return action.doIt(createCallContext("default"));
    }

    CallContext1 createCallContext(String method) {
        return new CallContext1(this, sessionContext, method, bar1);
    }

}
