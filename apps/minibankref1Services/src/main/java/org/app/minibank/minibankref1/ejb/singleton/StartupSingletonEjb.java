package org.app.minibank.minibankref1.ejb.singleton;

import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref1.Arte.ICalc;
import org.app.minibank.minibankref1.corba.CalcCorbaServicePOA;
import org.app.minibank.minibankref1.mbean.FooAdmin;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;

@Startup
@Singleton
public class StartupSingletonEjb {

    private static final Logger log = Logger.getLogger(StartupSingletonEjb.class);

    private MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();

    ObjectName fooAdminObjectName = null;

    byte[] servantPOAKey;

    CalcCorbaServicePOA servant = new CalcCorbaServicePOA();

    @PostConstruct
    public void startService() {
        log.info("Post Start Application");
        log.info("Post Construct - start register MBean");

        try {
            FooAdmin fooAdminMbean = new FooAdmin();
            fooAdminObjectName = new ObjectName("org.app.minibank:type=" + fooAdminMbean.getClass().getName());
            platformMBeanServer.registerMBean(fooAdminMbean, fooAdminObjectName);
            log.info("Post Construct - register MBean completed");
        } catch (Exception e) {
            throw new IllegalStateException("Problem during registration of Monitoring into JMX:" + e);
        }

        servantPOAKey = register(servant, ICalc.DEFAULT_NS_PATH);
    }

    @PreDestroy
    public void stopService() {
        log.info("Pre Stop Application");
        log.info("Pre Destroy - start un-register MBean");

        try {
            platformMBeanServer.unregisterMBean(fooAdminObjectName);
            log.info("Pre Destroy - un-register MBean completed");
        } catch (Exception e) {
            throw new IllegalStateException("Problem during unregistration of Monitoring into JMX:" + e);
        }

        unregisterServant(servant, ICalc.DEFAULT_NS_PATH, servantPOAKey);
    }

    private static byte[] register(Servant servant, String nsPath) {
        byte[] servantPOAKey = null;
        try {
            InitialContext ctx = new InitialContext();
            NamingContextExt rootNC = null;
            POA poa = null;
            rootNC = (NamingContextExt) ctx.lookup("java:jboss/corbanaming");
            poa = (POA) ctx.lookup("java:jboss/poa");

            log.info("Register the servant in the ROOT POA");
            servantPOAKey = poa.activate_object(servant);
            poa.the_POAManager().activate();
            org.omg.CORBA.Object o = poa.servant_to_reference(servant);

            createContext(nsPath, rootNC);
            rootNC.rebind(rootNC.to_name(nsPath), o);

            log.info("bound corba object " + servant.getClass().getName() + " to " + nsPath);
            return servantPOAKey;
        } catch (Exception e) {
            throw new RuntimeException("Could not rebind object to " + nsPath, e);
        }
    }

    public static void createContext(String nsPath, NamingContextExt root) throws Exception {
        NameComponent[] fullName = root.to_name(nsPath);
        NamingContext ctx = root;
        for (int i = 0; i < fullName.length - 1; i++) {
            NameComponent[] subName = new NameComponent[1];
            subName[0] = fullName[i];
            try {
                ctx = NamingContextHelper.narrow(ctx.resolve(subName));
            } catch (Exception e) {
                ctx = ctx.bind_new_context(subName);
            }
        }
    }

    public static void unregisterServant(Servant servant, String nsPath, byte[] servantPOAKey) {
        try {
            InitialContext ctx = new InitialContext();
            NamingContextExt rootNC = (NamingContextExt) ctx.lookup("java:JBossCorbaNaming");
            POA poa = (POA) ctx.lookup("java:JBossCorbaPOA");
            poa.deactivate_object(servantPOAKey);
            log.info("desactivated object " + servant.getClass().getName() + " from root POA");
            rootNC.unbind(rootNC.to_name(nsPath));
            log.info("unbound corba object " + servant.getClass().getName() + " from " + nsPath);
        } catch (Exception e) {
            throw new RuntimeException("Could not unbind/desactivate object  " + nsPath, e);
        }
    }

}
