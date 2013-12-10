package org.app.minibank.minibankref1.action;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.AccountException;

@SuppressWarnings("serial")
public class BaseAction1 implements IAction1 {

    private static final Logger log = Logger.getLogger(BaseAction1.class);

    private static AtomicInteger globalCount = new AtomicInteger();

    boolean setRollbackOnly = false;

    boolean dolog = true;

    private int displaylogEvery = 1;

    boolean throwCheckedException = false;

    String message = "hello";

    long pauseBefore = 0;

    long pauseAfter = 0;

    int callId = 0;

    @Override
    public Object doIt(CallContext1 cc) throws AccountException {
        int currentCount = globalCount.incrementAndGet();
        cc.getResult().put("globalCount", currentCount);
        cc.getResult().put("message", message);
        cc.getResult().put("callId", callId);

        if (dolog && callId % displaylogEvery == 0) log.info("Enter doAction '" + currentCount + "' with " + cc.getResult());
        if (throwCheckedException) throw new AccountException(message);
        if (setRollbackOnly) cc.getEjbContext().setRollbackOnly();
        pause(pauseBefore, "before");
        Object result = doItBasic(cc);
        pause(pauseAfter, "after");
        if (dolog && callId % displaylogEvery == 0) log.info("Exit doAction '" + currentCount + "' with " + cc.getResult());
        return result;
    }

    protected Object doItBasic(CallContext1 cc) throws AccountException {
        return cc.getResult();
    }

    private void pause(long p, String name) {
        if (p != 0) {
            log.info("start pausing " + name + " " + p + " ms");
            try {
                Thread.sleep(p);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public boolean isSetRollbackOnly() {
        return setRollbackOnly;
    }

    public BaseAction1 setSetRollbackOnly(boolean setRollbackOnly) {
        this.setRollbackOnly = setRollbackOnly;
        return this;
    }

    public boolean isDolog() {
        return dolog;
    }

    public BaseAction1 setDolog(boolean dolog) {
        this.dolog = dolog;
        return this;
    }

    public boolean isThrowCheckedException() {
        return throwCheckedException;
    }

    public BaseAction1 setThrowCheckedException(boolean throwCheckedException) {
        this.throwCheckedException = throwCheckedException;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BaseAction1 setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getPauseBefore() {
        return pauseBefore;
    }

    public BaseAction1 setPauseBefore(long pauseBefore) {
        this.pauseBefore = pauseBefore;
        return this;
    }

    public long getPauseAfter() {
        return pauseAfter;
    }

    public BaseAction1 setPauseAfter(long pauseAfter) {
        this.pauseAfter = pauseAfter;
        return this;
    }

    public int getCallId() {
        return callId;
    }

    public BaseAction1 setCallId(int callId) {
        this.callId = callId;
        return this;
    }

    public int getDisplaylogEvery() {
        return displaylogEvery;
    }

    public BaseAction1 setDisplaylogEvery(int displaylogEvery) {
        this.displaylogEvery = displaylogEvery;
        return this;
    }

}
