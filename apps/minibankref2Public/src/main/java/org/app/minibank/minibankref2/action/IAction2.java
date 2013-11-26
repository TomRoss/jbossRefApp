package org.app.minibank.minibankref2.action;

import java.io.Serializable;

import org.app.minibank.minibankref.AccountException;

public interface IAction2 extends Serializable {

    Object doIt(CallContext2 cc) throws AccountException;

}
