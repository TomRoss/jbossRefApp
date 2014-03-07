package org.app.minibank.minibankref;

import java.util.Properties;

import javax.naming.Context;

public class TestUtil {
    public static String HOSTNAME = System.getenv("COMPUTERNAME");

    public static final String userguest = "guest";

    public static final String passguest = "Passw0rd!";

    public static final String userapp1 = "appuser1";

    public static final String passapp1 = "Passw0rd!";

    public static final String userapp2 = "appuser2";

    public static final String passapp2 = "Passw0rd!";

    public static final String userapp3 = "appuser3";

    public static final String passapp3 = "Passw0rd!";

    public static final String PATH_SEPARATOR = System.getProperty("file.separator");

    public static final String NODE_BASE_PATH = ".." + PATH_SEPARATOR + ".." + PATH_SEPARATOR + "servers";

    public static final String SCRIPT_START = isWindows() ? "start_node.cmd" : "start_node.sh";

    public static final String SCRIPT_SHUTDOWN = isWindows() ? "stop_node.cmd" : "stop_node.sh";

    public static final String SCRIPT_KILL = isWindows() ? "kill_node.cmd" : "kill_node.sh";

    public static final String SCRIPT_START_DB = isWindows() ? "db_start.cmd" : "db_start.sh";

    public static final String SCRIPT_STOP_DB = isWindows() ? "db_stop.cmd" : "db_stop.sh";

    public static final JBNode node1A = new JBNode("node_1A", "localhost", "100", "org/app/minibank/minibankref1/jms/QueueA",
            "org/app/minibank/minibankref1/jms/QueueB");

    public static final JBNode node1B = new JBNode("node_1B", "localhost", "200", "org/app/minibank/minibankref1/jms/QueueA",
            "org/app/minibank/minibankref1/jms/QueueB");

    public static final JBNode node2A = new JBNode("node_2A", "localhost", "300", "org/app/minibank/minibankref1/jms/QueueC",
            "org/app/minibank/minibankref1/jms/QueueD");

    public static final JBNode node2B = new JBNode("node_2B", "localhost", "400", "org/app/minibank/minibankref1/jms/QueueC",
            "org/app/minibank/minibankref1/jms/QueueD");

    public static final JBNode node3A = new JBNode("node_3A", "localhost", "500", "", "");

    public static final JBNode node3B = new JBNode("node_3B", "localhost", "600", "", "");

    public static boolean isWindows() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("windows");

    }

    public static String getJndi1() {
        return "minibankref1/minibankref1Services/Foo1Bean!org.app.minibank.minibankref1.ejb.session.Foo1Remote";
    }

    public static String getJndi2() {
        return "minibankref2/minibankref2Services/Foo2Bean!org.app.minibank.minibankref2.ejb.session.Foo2Remote";
    }

    public static String getJndi3() {
        return "minibankref3/minibankref3Services/Foo3Bean!org.app.minibank.minibankref3.ejb.session.Foo3Remote";
    }

    public static String getSecureJndi1() {
        return "minibankref1/minibankref1Services/Foo1SecureBean!org.app.minibank.minibankref1.ejb.session.Foo1SecureRemote";
    }

    public static String getSecureJndi2() {
        return "minibankref2/minibankref2Services/Foo2SecureBean!org.app.minibank.minibankref2.ejb.session.Foo2SecureRemote";
    }

    public static String getSecureJndi3() {
        return "minibankref3/minibankref3Services/Foo3SecureBean!org.app.minibank.minibankref3.ejb.session.Foo3SecureRemote";
    }

    public static Properties createProperties1() {
        return createProperties1(userguest, passguest);
    }

    public static Properties createProperties2() {
        return createProperties2(userguest, passguest);
    }

    public static Properties createProperties3() {
        return createProperties3(userguest, passguest);
    }

    public static Properties createProperties1(String user, String pass) {
        return createProperties(node1A.getPortEjb(), node1B.getPortEjb(), "ejb", user, pass);
    }

    public static Properties createProperties2(String user, String pass) {
        return createProperties(node2A.getPortEjb(), node2B.getPortEjb(), "ejb34", user, pass);
    }

    public static Properties createProperties3(String user, String pass) {
        return createProperties(node3A.getPortEjb(), node3B.getPortEjb(), "ejb56", user, pass);
    }

    public static Properties createProperties(String portNode1, String portNode2, String ejbClusterName, String user, String pass) {
        return createProperties(portNode1, portNode2, ejbClusterName, user, pass, true);
    }

    public static Properties createProperties(String portNode1, String portNode2, String ejbClusterName, String user, String pass, boolean isejb) {
        Properties properties = new Properties();

        properties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");

        if (isejb) {
            properties.put("org.jboss.ejb.client.scoped.context", "true");

            properties.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
            properties.put("remote.connections", "one,two");

            properties.put("remote.connection.one.host", HOSTNAME);
            properties.put("remote.connection.one.port", portNode1);
            properties.put("remote.connection.one.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true");
            properties.put("remote.connection.one.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
            if (user != null) properties.put("remote.connection.one.username", user);
            if (pass != null) properties.put("remote.connection.one.password", pass);

            properties.put("remote.connection.two.host", HOSTNAME);
            properties.put("remote.connection.two.port", portNode2);
            properties.put("remote.connection.two.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true");
            properties.put("remote.connection.two.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
            if (user != null) properties.put("remote.connection.two.username", user);
            if (pass != null) properties.put("remote.connection.two.password", pass);

            properties.put("remote.clusters", ejbClusterName);
            properties.put("remote.cluster." + ejbClusterName + ".connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true");
            properties.put("remote.cluster." + ejbClusterName + ".connect.options.org.xnio.Options.SSL_ENABLED", "false");
            properties.put("remote.cluster." + ejbClusterName + ".connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
            if (user != null) properties.put("remote.cluster." + ejbClusterName + ".username", user);
            if (pass != null) properties.put("remote.cluster." + ejbClusterName + ".password", pass);

        } else {
            properties.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
            properties.put(Context.PROVIDER_URL, "remote://" + HOSTNAME + ":" + portNode1 + "," + HOSTNAME + ":" + portNode2);
            if (user != null) properties.put(Context.SECURITY_PRINCIPAL, user);
            if (pass != null) properties.put(Context.SECURITY_CREDENTIALS, pass);

            properties.put("jboss.naming.client.remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
            properties.put("jboss.naming.client.connect.options.org.xnio.Options.SSL_STARTTLS", "false");
            properties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "true");
            properties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        }
        return properties;
    }

    public static Properties createJmsProperties(JBNode[] nodesIn) {
        JBNode firstMember = null;
        JBNode secondMember = null;
        if (nodesIn.length > 0) firstMember = nodesIn[0];
        if (nodesIn.length > 1) secondMember = nodesIn[1];
        else secondMember = nodesIn[0];
        return createProperties(firstMember.getPortEjb(), secondMember.getPortEjb(), null, userguest, passguest, false);
    }

}
