package org.app.minibank.minibankref2.ejb.session;

import javax.ejb.Remote;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref2.action.IAction2;

@Remote
public interface Foo2SecureRemote {

    public Object doSecureAction(IAction2 action) throws AccountException;

}
