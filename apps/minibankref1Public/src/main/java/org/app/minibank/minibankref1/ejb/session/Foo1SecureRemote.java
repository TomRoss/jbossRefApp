package org.app.minibank.minibankref1.ejb.session;

import javax.ejb.Remote;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref1.action.IAction1;

@Remote
public interface Foo1SecureRemote {

    public Object doSecureAction(IAction1 action) throws AccountException;

}
