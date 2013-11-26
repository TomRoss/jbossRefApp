package org.app.minibank.minibankref3.action;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.app.minibank.minibankref.AccountException;

@SuppressWarnings("serial")
public class BaseAction3 implements IAction3 {

    private static final Logger log = Logger.getLogger(BaseAction3.class);

    private static AtomicInteger globalCount = new AtomicInteger();

    boolean setRollbackOnly = false;

    boolean dolog = true;

    private int displaylogEvery = 1;

    boolean throwCheckedException = false;

    String message = "hello";

    long pauseBefore = 0;

    long pauseAfter = 0;

    int callId = 0;;

    @Override
    public Object doIt(CallContext3 cc) throws AccountException {
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

    protected Object doItBasic(CallContext3 cc) throws AccountException {
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

    public BaseAction3 setSetRollbackOnly(boolean setRollbackOnly) {
        this.setRollbackOnly = setRollbackOnly;
        return this;
    }

    public boolean isDolog() {
        return dolog;
    }

    public BaseAction3 setDolog(boolean dolog) {
        this.dolog = dolog;
        return this;
    }

    public boolean isThrowCheckedException() {
        return throwCheckedException;
    }

    public BaseAction3 setThrowCheckedException(boolean throwCheckedException) {
        this.throwCheckedException = throwCheckedException;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BaseAction3 setMessage(String message) {
        this.message = message;
        return this;
    }

    public long getPauseBefore() {
        return pauseBefore;
    }

    public BaseAction3 setPauseBefore(long pauseBefore) {
        this.pauseBefore = pauseBefore;
        return this;
    }

    public long getPauseAfter() {
        return pauseAfter;
    }

    public BaseAction3 setPauseAfter(long pauseAfter) {
        this.pauseAfter = pauseAfter;
        return this;
    }

    public int getCallId() {
        return callId;
    }

    public BaseAction3 setCallId(int callId) {
        this.callId = callId;
        return this;
    }

}
