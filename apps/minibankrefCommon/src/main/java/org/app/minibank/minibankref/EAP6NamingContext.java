package org.app.minibank.minibankref;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EAP6NamingContext {

    Context jndiCtx;

    Context ejbRootNamingContext;

    boolean isEjbContext = false;

    public EAP6NamingContext(Properties props) throws NamingException {
        jndiCtx = new InitialContext(props);
        // if (props.containsValue("org.jboss.ejb.client.naming")) {
        if (props.containsKey("org.jboss.ejb.client.scoped.context")) {
            isEjbContext = true;
            ejbRootNamingContext = (Context) jndiCtx.lookup("ejb:");
        }
    }

    public Object lookup(String name) throws NamingException {
        if (isEjbContext) {
            return name.startsWith("ejb:") ? jndiCtx.lookup(name) : ejbRootNamingContext.lookup(name);
        } else {
            return jndiCtx.lookup(name);
        }
    }

    public void close() throws NamingException {
        if (jndiCtx != null) jndiCtx.close();
        if (ejbRootNamingContext != null) ejbRootNamingContext.close();
    }

}
