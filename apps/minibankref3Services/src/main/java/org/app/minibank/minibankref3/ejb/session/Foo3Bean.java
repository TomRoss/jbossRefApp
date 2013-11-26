package org.app.minibank.minibankref3.ejb.session;

import static javax.ejb.TransactionAttributeType.*;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref3.action.CallContext3;
import org.app.minibank.minibankref3.action.IAction3;
import org.app.minibank.minibankref3.ejb.session.Foo3Remote;
import org.jboss.ejb3.annotation.Clustered;

@Stateless
@Clustered
public class Foo3Bean implements Foo3Remote {

    @Resource
    SessionContext sessionContext;

    @Override
    @TransactionAttribute(value = REQUIRED)
    public Object required(IAction3 action) throws AccountException {
        return action.doIt(createCallContext("required"));
    }

    @Override
    @TransactionAttribute(value = MANDATORY)
    public Object mandatory(IAction3 action) throws AccountException {
        return action.doIt(createCallContext("mandatory"));
    }

    @Override
    @TransactionAttribute(value = SUPPORTS)
    public Object supports(IAction3 action) throws AccountException {
        return action.doIt(createCallContext("supports"));
    }

    @Override
    @TransactionAttribute(value = NOT_SUPPORTED)
    public Object notSupported(IAction3 action) throws AccountException {
        return action.doIt(createCallContext("notSupported"));
    }

    @Override
    @TransactionAttribute(value = NEVER)
    public Object never(IAction3 action) throws AccountException {
        return action.doIt(createCallContext("never"));
    }

    @Override
    @TransactionAttribute(value = REQUIRES_NEW)
    public Object requiresNew(IAction3 action) throws AccountException {
        return action.doIt(createCallContext("requiresNew"));
    }

    @Override
    public Object defaultTxPropagation(IAction3 action) throws AccountException {
        return action.doIt(createCallContext("default"));
    }

    CallContext3 createCallContext(String method) {
        return new CallContext3(this, sessionContext, method);
    }
}
