package org.app.minibank.minibankref1.action;

import java.io.Serializable;

import org.app.minibank.minibankref.AccountException;

public interface IAction1 extends Serializable {

    Object doIt(CallContext1 cc) throws AccountException;

}
