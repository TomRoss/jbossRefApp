package org.app.minibank.minibankref1.action;

import static javax.ejb.TransactionAttributeType.*;

import javax.ejb.TransactionAttributeType;

import org.app.minibank.minibankref.AccountException;

public class CallLocalAction1 extends BaseAction1 {

    private static final long serialVersionUID = 1918982184903771399L;

    IAction1 localAction = new BaseAction1();

    TransactionAttributeType transactionAttributeType = null;

    int repeat = 1;

    protected Object doItBasic(CallContext1 cc) throws AccountException {

        if (repeat == 1) {
            Object result = makeCalls(cc);
            cc.getResult().put("CALL", result);
        } else {
            for (int i = 0; i < repeat; i++) {
                Object result = makeCalls(cc);
                cc.getResult().put("CALL" + i, result);
            }
        }

        return cc.getResult();
    }

    private Object makeCalls(CallContext1 cc) throws AccountException {

        Object result;

        if (transactionAttributeType == REQUIRED) {
            result = cc.getBar1().required(localAction);
        } else if (transactionAttributeType == REQUIRES_NEW) {
            result = cc.getBar1().requiresNew(localAction);
        } else if (transactionAttributeType == NEVER) {
            result = cc.getBar1().never(localAction);
        } else if (transactionAttributeType == MANDATORY) {
            result = cc.getBar1().mandatory(localAction);
        } else if (transactionAttributeType == NOT_SUPPORTED) {
            result = cc.getBar1().notSupported(localAction);
        } else if (transactionAttributeType == SUPPORTS) {
            result = cc.getBar1().supports(localAction);
        } else {
            result = cc.getBar1().defaultTxPropagation(localAction);
        }

        return result;
    }

    public IAction1 getLocalAction() {
        return localAction;
    }

    public void setLocalAction(IAction1 localAction) {
        this.localAction = localAction;
    }

    public TransactionAttributeType getTransactionAttributeType() {
        return transactionAttributeType;
    }

    public void setTransactionAttributeType(TransactionAttributeType transactionAttributeType) {
        this.transactionAttributeType = transactionAttributeType;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

}
