package org.app.minibank.minibankref2.ejb.session;

import static javax.ejb.TransactionAttributeType.*;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref2.action.CallContext2;
import org.app.minibank.minibankref2.action.IAction2;
import org.app.minibank.minibankref2.ejb.session.Foo2Remote;
import org.jboss.ejb3.annotation.Clustered;

@Stateless
@Clustered
public class Foo2Bean implements Foo2Remote {

    @Resource
    SessionContext sessionContext;

    @Override
    @TransactionAttribute(value = REQUIRED)
    public Object required(IAction2 action) throws AccountException {
        return action.doIt(createCallContext("required"));
    }

    @Override
    @TransactionAttribute(value = MANDATORY)
    public Object mandatory(IAction2 action) throws AccountException {
        return action.doIt(createCallContext("mandatory"));
    }

    @Override
    @TransactionAttribute(value = SUPPORTS)
    public Object supports(IAction2 action) throws AccountException {
        return action.doIt(createCallContext("supports"));
    }

    @Override
    @TransactionAttribute(value = NOT_SUPPORTED)
    public Object notSupported(IAction2 action) throws AccountException {
        return action.doIt(createCallContext("notSupported"));
    }

    @Override
    @TransactionAttribute(value = NEVER)
    public Object never(IAction2 action) throws AccountException {
        return action.doIt(createCallContext("never"));
    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public Object requiresNew(IAction2 action) throws AccountException {
        return action.doIt(createCallContext("requiresNew"));
    }

    @Override
    public Object defaultTxPropagation(IAction2 action) throws AccountException {
        return action.doIt(createCallContext("default"));
    }

    CallContext2 createCallContext(String method) {
        return new CallContext2(this, sessionContext, method);
    }
}
