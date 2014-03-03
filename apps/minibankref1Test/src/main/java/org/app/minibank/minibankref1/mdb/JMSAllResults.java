package org.app.minibank.minibankref1.mdb;

import java.util.LinkedList;

public class JMSAllResults {

    private final String name;

    private final int loopNumber;

    private long timeInMs;

    private LinkedList<JMSResult> results = new LinkedList<JMSResult>();

    public JMSAllResults(String name, int loopNumber) {
        this.name = name;
        this.loopNumber = loopNumber;
    }

    public boolean isAllDelivered() {
        return results.isEmpty() ? false : results.getLast().isAllDelivered();
    }

    public void add(JMSResult res) {
        results.addLast(res);
    }

    public void addAll(LinkedList<JMSResult> results) {
        this.results.addAll(results);
    }

    public String getName() {
        return name;
    }

    public LinkedList<JMSResult> getResults() {
        return results;
    }

    public long getTimeInMs() {
        return timeInMs;
    }

    public void setTimeInMs(long timeInMs) {
        this.timeInMs = timeInMs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("results for ").append(name).append(" loop=").append(loopNumber).append(" executed in ").append(timeInMs).append(" ms.").append(" :")
                .append(JMSTestUtil.CR);
        for (JMSResult res : results) {
            sb.append("   ").append(res).append(JMSTestUtil.CR);
        }
        return sb.toString();
    }

}
