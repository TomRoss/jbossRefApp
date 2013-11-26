package org.app.minibank.minibankref1.action;

import static javax.ejb.TransactionAttributeType.*;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref2.action.BaseAction2;
import org.app.minibank.minibankref2.action.IAction2;
import org.app.minibank.minibankref2.ejb.session.Foo2Remote;

public class CallRemote2UnsecuredAction1 extends CallRemoteAction1 {

    private static final long serialVersionUID = 6277331890801782155L;

    int repeat = 1;

    IAction2 action2 = new BaseAction2();

    @Override
    protected Object makeCall(CallContext1 cc, Object service) throws AccountException {
        Foo2Remote remote2 = narrowObject(service, Foo2Remote.class);

        if (repeat == 1) {
            Object result = makeCalls(remote2);
            cc.getResult().put("CALL", result);
        } else {
            for (int i = 0; i < repeat; i++) {
                Object result = makeCalls(remote2);
                cc.getResult().put("CALL" + i, result);
            }
        }

        return cc.getResult();
    }

    private Object makeCalls(Foo2Remote remote2) throws AccountException {

        Object result;

        if (transactionAttributeType == REQUIRED) {
            result = remote2.required(action2);
        } else if (transactionAttributeType == REQUIRES_NEW) {
            result = remote2.requiresNew(action2);
        } else if (transactionAttributeType == NEVER) {
            result = remote2.never(action2);
        } else if (transactionAttributeType == MANDATORY) {
            result = remote2.mandatory(action2);
        } else if (transactionAttributeType == NOT_SUPPORTED) {
            result = remote2.notSupported(action2);
        } else if (transactionAttributeType == SUPPORTS) {
            result = remote2.supports(action2);
        } else {
            result = remote2.defaultTxPropagation(action2);
        }

        return result;
    }

    public CallRemote2UnsecuredAction1 setRepeat(int repeat) {
        this.repeat = repeat;
        return this;
    }

    public int getRepeat() {
        return repeat;
    }

    public IAction2 getAction2() {
        return action2;
    }

    public CallRemote2UnsecuredAction1 setAction2(IAction2 action2) {
        this.action2 = action2;
        return this;
    }

}
