package org.app.minibank.minibankref1.ejb.singleton;

import java.lang.management.ManagementFactory;

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

    private static final Logger log = Logger.getLogger(StartupSingletonEjb.class);

    private MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();

    ObjectName fooAdminObjectName = null;

    @PostConstruct
    public void startService() {
        log.info("Post Start Application");
        log.info("Post Construct - start register MBean");
        try {

            FooAdmin fooAdminMbean = new FooAdmin();
            fooAdminObjectName = new ObjectName("org.app.minibank:type=" + fooAdminMbean.getClass().getName());
            platformMBeanServer.registerMBean(fooAdminMbean, fooAdminObjectName);

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
            platformMBeanServer.unregisterMBean(fooAdminObjectName);

        } catch (Exception e) {
            throw new IllegalStateException("Problem during unregistration of Monitoring into JMX:" + e);
        }
        log.info("Pre Destroy - un-register MBean completed");
    }

}
