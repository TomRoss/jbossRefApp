package org.app.minibank.minibankref3.ejb.session;

import javax.ejb.Remote;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref3.action.IAction3;

@Remote
public interface Foo3SecureRemote {

    public Object doSecureAction(IAction3 action) throws AccountException;

}
