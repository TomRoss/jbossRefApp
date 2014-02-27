package org.app.minibank.minibankref1.action;

import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.AccountException;
import org.app.minibank.minibankref.JBNode;
import org.app.minibank.minibankref.TestUtil;

public class Case01043891Action extends BaseAction1 {

    private static final long serialVersionUID = -8248799100548732712L;

    private static final Logger log = Logger.getLogger(Case01043891Action.class);

    @Override
    public Object doIt(CallContext1 cc) throws AccountException {
        try {

            JBNode[] nodes2 = { TestUtil.node2A, TestUtil.node2B };
            InitialContext context = new InitialContext(TestUtil.createJmsProperties(nodes2));
            try {
                Queue queue = (Queue) context.lookup("org/app/minibank/minibankref1/jms/QueueC");
                log.info("found " + queue);
                return queue.toString();
            } finally {
                context.close();
            }
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
