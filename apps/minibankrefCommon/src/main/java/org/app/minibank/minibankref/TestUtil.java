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

    public static Properties createProperties1(String user, String pass) {
        return createProperties("5101", "5201", "ejb", user, pass);
    }

    public static Properties createProperties2() {
        return createProperties2(userguest, passguest);
    }

    public static Properties createProperties3() {
        return createProperties3(userguest, passguest);
    }

    public static Properties createProperties2(String user, String pass) {
        return createProperties("5301", "5401", "ejb34", user, pass);
    }

    public static Properties createProperties3(String user, String pass) {
        return createProperties("5501", "5601", "ejb56", user, pass);
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

    public static Properties createJmsProperties1() {
        return createProperties("5101", "5201", null, userguest, passguest, false);
    }

    public static Properties createJmsProperties2() {
        return createProperties("5301", "5401", null, userguest, passguest, false);
    }

}
