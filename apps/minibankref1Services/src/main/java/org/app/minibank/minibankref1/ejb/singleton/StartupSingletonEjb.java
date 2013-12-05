package org.app.minibank.minibankref1.ejb.singleton;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref1.mbean.FooAdmin;

@Startup
@Singleton
public class StartupSingletonEjb {

    public static final String JMX_PREFIX = "org.app.minibank:type=";

    private static final String[] MBEAN_LIST = new String[] { FooAdmin.class.getName() };

    private static final Logger log = Logger.getLogger(StartupSingletonEjb.class);

    private MBeanServer platformMBeanServer;

    private List<ObjectName> jmxObjectNameList = new ArrayList<ObjectName>();

    @PostConstruct
    public void startService() {
        log.info("Post Start Application");
        log.info("Post Construct - start register MBean");
        try {

            for (String mBeanName : MBEAN_LIST) {
                Object mbean = Class.forName(mBeanName).newInstance();
                ObjectName objectName = new ObjectName(JMX_PREFIX + mBeanName);
                platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
                platformMBeanServer.registerMBean(mbean, objectName);
            }

        } catch (Exception e) {
            throw new IllegalStateException("Problem during registration of Monitoring into JMX:" + e);
        }
        log.info("Post Construct - register MBean completed");
    }

    @PreDestroy
    public void stopService() {
        log.info("Pre Stop Application");
        log.info("Pre Destroy - start un-register MBean");
        try {
            for (ObjectName jmxObjectName : jmxObjectNameList) {
                platformMBeanServer.unregisterMBean(jmxObjectName);
            }

        } catch (Exception e) {
            throw new IllegalStateException("Problem during unregistration of Monitoring into JMX:" + e);
        }
        log.info("Pre Destroy - un-register MBean completed");
    }

}
