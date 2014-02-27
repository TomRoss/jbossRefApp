package org.app.minibank.minibankref;

public final class JBNode {

    private final String name;

    private final String host;

    private final String offset;

    private final String portJmx;

    private final String portEjb;

    private final String path;

    private final String queueIn;

    private final String queueOut;

    public JBNode(String name, String host, String offset, String queueIn, String queueOut) {
        super();
        this.name = name;
        this.host = host;
        this.offset = offset;
        this.queueIn = queueIn;
        this.queueOut = queueOut;
        this.portJmx = "" + (Integer.parseInt(offset) + 5004);
        this.portEjb = "" + (Integer.parseInt(offset) + 5001);
        this.path = TestUtil.NODE_BASE_PATH + TestUtil.PATH_SEPARATOR + name;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getOffset() {
        return offset;
    }

    public String getQueueIn() {
        return queueIn;
    }

    public String getQueueOut() {
        return queueOut;
    }

    public String getPortJmx() {
        return portJmx;
    }

    public String getUrlJmx() {
        return "service:jmx:remoting-jmx://" + host + ":" + portJmx;
    }

    public String getPortEjb() {
        return portEjb;
    }

    public String getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((offset == null) ? 0 : offset.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        JBNode other = (JBNode) obj;
        if (host == null) {
            if (other.host != null) return false;
        } else if (!host.equals(other.host)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (offset == null) {
            if (other.offset != null) return false;
        } else if (!offset.equals(other.offset)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "JBNode [name=" + name + ", host=" + host + ", offset=" + offset + "]";
    }

}
