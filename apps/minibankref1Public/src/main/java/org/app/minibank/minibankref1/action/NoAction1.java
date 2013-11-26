package org.app.minibank.minibankref1.action;

import org.app.minibank.minibankref.AccountException;

public class NoAction1 implements IAction1 {

    private static final long serialVersionUID = 2231921790132608136L;

    @Override
    public Object doIt(CallContext1 cc) throws AccountException {
        return "no action from " + cc.getMethod();
    }

}
