package org.app.minibank.minibankref2.action;

import org.app.minibank.minibankref.AccountException;

public class NoAction2 implements IAction2 {

    private static final long serialVersionUID = 2231921790132608136L;

    @Override
    public Object doIt(CallContext2 cc) throws AccountException {
        return "no action from " + cc.getMethod();
    }

}
