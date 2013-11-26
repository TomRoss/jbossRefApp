package org.app.minibank.minibankref2.ejb.session;

import javax.ejb.Remote;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref2.action.IAction2;

@Remote
public interface Foo2Remote {

    public Object required(IAction2 action) throws AccountException;

    public Object mandatory(IAction2 action) throws AccountException;

    public Object supports(IAction2 action) throws AccountException;

    public Object notSupported(IAction2 action) throws AccountException;

    public Object never(IAction2 action) throws AccountException;

    public Object requiresNew(IAction2 action) throws AccountException;

    public Object defaultTxPropagation(IAction2 action) throws AccountException;

}
