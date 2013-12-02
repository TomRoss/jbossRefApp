package org.app.minibank.minibankref;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

public class EAP6NamingContext {

    private static final Logger log = Logger.getLogger(EAP6NamingContext.class);

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
        if (ejbRootNamingContext != null) {
            try {
                ejbRootNamingContext.close();
            } catch (Exception e) {
                // log and ignore
                log.error(e.getMessage(), e);
            }
        }
        if (jndiCtx != null) jndiCtx.close();
    }

}
