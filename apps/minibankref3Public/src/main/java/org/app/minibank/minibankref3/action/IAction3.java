package org.app.minibank.minibankref3.action;

import java.io.Serializable;

import org.app.minibank.minibankref.AccountException;

public interface IAction3 extends Serializable {

    Object doIt(CallContext3 cc) throws AccountException;

}
