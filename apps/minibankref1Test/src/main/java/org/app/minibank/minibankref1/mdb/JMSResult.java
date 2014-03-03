package org.app.minibank.minibankref1.mdb;

public final class JMSResult {

    private final String name;

    private final int retry;

    private final int loop;

    private final boolean allDelivered;

    private final String info;

    private long time;

    public JMSResult(String name, int loopNumber, int retryNumber, boolean allDelivered, String info) {
        this.name = name;
        this.retry = retryNumber;
        this.info = info;
        this.allDelivered = allDelivered;
        this.loop = loopNumber;
    }

    public String getName() {
        return name;
    }

    public long getLime() {
        return time;
    }

    public void setLime(long lime) {
        this.time = lime;
    }

    public boolean isAllDelivered() {
        return allDelivered;
    }

    public int getLoop() {
        return loop;
    }

    public int getRetry() {
        return retry;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "[" + name + ", loop=" + loop + ", retry=" + retry + ", isAllDelivered=" + isAllDelivered() + ", info=" + getInfo() + "]";
    }
}
