package org.app.minibank.minibankref1.action;

import java.util.ArrayList;
import java.util.List;

import org.app.minibank.minibankref.AccountException;

public class SequenceAction1 implements IAction1 {

    private static final long serialVersionUID = 7623593990894656630L;

    List<IAction1> actions = new ArrayList<IAction1>();

    @Override
    public Object doIt(CallContext1 cc) throws AccountException {

        int count = 0;

        for (IAction1 action : actions) {
            cc.getResult().put("action" + count++, action.doIt(cc));
        }

        return cc.getResult();
    }

    public List<IAction1> getActions() {
        return actions;
    }

}
