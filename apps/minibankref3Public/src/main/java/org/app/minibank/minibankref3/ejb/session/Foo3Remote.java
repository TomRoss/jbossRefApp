package org.app.minibank.minibankref3.ejb.session;

import javax.ejb.Remote;

import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref3.action.IAction3;

@Remote
public interface Foo3Remote {

    public Object required(IAction3 action) throws AccountException;

    public Object mandatory(IAction3 action) throws AccountException;

    public Object supports(IAction3 action) throws AccountException;

    public Object notSupported(IAction3 action) throws AccountException;

    public Object never(IAction3 action) throws AccountException;

    public Object requiresNew(IAction3 action) throws AccountException;

    public Object defaultTxPropagation(IAction3 action) throws AccountException;

}
