package org.app.minibank.minibankref1.ejb.singleton;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.log4j.Logger;

@Startup
@Singleton
public class StartupSingletonEjb {

    private static final Logger log = Logger.getLogger(StartupSingletonEjb.class);

    @PostConstruct
    public void startService() {
        log.info("Post Start Application");
    }

    @PreDestroy
    public void stopService() {
        log.info("Pre Stop Application");
    }

}
