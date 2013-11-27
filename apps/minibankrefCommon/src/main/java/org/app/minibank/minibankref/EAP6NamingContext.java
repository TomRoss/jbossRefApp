package org.app.minibank.minibankref;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EAP6NamingContext {

    Context context;

    Context jndiCtx;

    boolean isEjbContext = false;

    public EAP6NamingContext(Properties props) throws NamingException {
        context = new InitialContext(props);
        // if (props.containsValue("org.jboss.ejb.client.naming")) {
        if (props.containsKey("org.jboss.ejb.client.scoped.context")) {
            isEjbContext = true;
            jndiCtx = (Context) context.lookup("ejb:");
        }
    }

    public Object lookup(String name) throws NamingException {
        if (isEjbContext) {
            return name.startsWith("ejb:") ? context.lookup(name) : jndiCtx.lookup(name);
        } else {
            return context.lookup(name);
        }
    }

    public void close() throws NamingException {
        if (context != null) context.close();
        if (jndiCtx != null) jndiCtx.close();
    }

}
