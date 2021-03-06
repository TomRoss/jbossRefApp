package org.app.minibank.minibankref1.ejb.session;

import javax.ejb.Remote;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref1.action.IAction1;

@Remote
public interface Foo1Remote {

    public Object required(IAction1 action) throws AccountException;

    public Object mandatory(IAction1 action) throws AccountException;

    public Object supports(IAction1 action) throws AccountException;

    public Object notSupported(IAction1 action) throws AccountException;

    public Object never(IAction1 action) throws AccountException;

    public Object requiresNew(IAction1 action) throws AccountException;

    public Object defaultTxPropagation(IAction1 action) throws AccountException;

}
