package org.app.minibank.minibankref3.action;

import org.app.minibank.minibankref.AccountException;

public class NoAction3 implements IAction3 {

    private static final long serialVersionUID = 2231921790132608136L;

    @Override
    public Object doIt(CallContext3 cc) throws AccountException {
        return "no action from " + cc.getMethod();
    }

}
