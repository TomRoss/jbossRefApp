package org.app.minibank.minibankref1.action;

import static javax.ejb.TransactionAttributeType.*;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref3.action.BaseAction3;
import org.app.minibank.minibankref3.action.IAction3;
import org.app.minibank.minibankref3.ejb.session.Foo3Remote;

public class CallRemote3UnsecuredAction1 extends CallRemoteAction1 {

    private static final long serialVersionUID = 6277331890801782155L;

    int repeat = 1;

    IAction3 action3 = new BaseAction3();

    @Override
    protected Object makeCall(CallContext1 cc, Object service) throws AccountException {
        Foo3Remote remote3 = narrowObject(service, Foo3Remote.class);

        if (repeat == 1) {
            Object result = makeCalls(remote3);
            cc.getResult().put("CALL", result);
        } else {
            for (int i = 0; i < repeat; i++) {
                Object result = makeCalls(remote3);
                cc.getResult().put("CALL" + i, result);
            }
        }

        return cc.getResult();
    }

    private Object makeCalls(Foo3Remote remote3) throws AccountException {

        Object result;

        if (transactionAttributeType == REQUIRED) {
            result = remote3.required(action3);
        } else if (transactionAttributeType == REQUIRES_NEW) {
            result = remote3.requiresNew(action3);
        } else if (transactionAttributeType == NEVER) {
            result = remote3.never(action3);
        } else if (transactionAttributeType == MANDATORY) {
            result = remote3.mandatory(action3);
        } else if (transactionAttributeType == NOT_SUPPORTED) {
            result = remote3.notSupported(action3);
        } else if (transactionAttributeType == SUPPORTS) {
            result = remote3.supports(action3);
        } else {
            result = remote3.defaultTxPropagation(action3);
        }

        return result;
    }

    public CallRemote3UnsecuredAction1 setRepeat(int repeat) {
        this.repeat = repeat;
        return this;
    }

    public int getRepeat() {
        return repeat;
    }

    public IAction3 getAction3() {
        return action3;
    }

    public CallRemote3UnsecuredAction1 setAction3(IAction3 action3) {
        this.action3 = action3;
        return this;
    }

}
