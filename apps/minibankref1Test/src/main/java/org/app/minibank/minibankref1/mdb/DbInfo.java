package org.app.minibank.minibankref1.mdb;

import java.util.HashMap;
import java.util.Map;

public final class DbInfo {
    private final DbType type;

    private final String dialect;

    private final String url;

    private final String user;

    private final String pwd;

    private final String driverName;

    private final String driverClass;

    private final String noTxSeparatePools;

    private final Map<String, String> map;

    public DbInfo(DbType type, String dialect, String url, String user, String password, String driverName, String noTxSeparatePools) {
        super();
        this.type = type;
        this.dialect = dialect;
        this.url = url;
        this.user = user;
        this.pwd = password;
        this.driverName = driverName;
        this.noTxSeparatePools = noTxSeparatePools;
        this.map = new HashMap<String, String>();
        // map.put("minibank.db.type", type);
        // map.put("minibank.db.dialect", dialect);
        // map.put("minibank.db.url", url);
        // map.put("minibank.db.user", user);
        // map.put("minibank.db.pwd", pwd);
        // map.put("minibank.db.driver.name", driver);
        // map.put("minibank.db.no.tx.separate.pools", noTxSeparatePools);

        String driverClassTempo = "org.h2.Driver";
        if (isOracle()) driverClassTempo = "oracle.jdbc.OracleDriver";
        if (isDerby()) driverClassTempo = "org.apache.derby.jdbc.ClientDriver";
        this.driverClass = driverClassTempo;

        map.put("hibernate.dialect", dialect);
        map.put("javax.persistence.jdbc.url", url);
        map.put("javax.persistence.jdbc.driver", driverClass);
        map.put("hibernate.connection.username", user);
        map.put("hibernate.connection.password", pwd);
    }

    public DbType getType() {
        return type;
    }

    public String getDialect() {
        return dialect;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getNoTxSeparatePools() {
        return noTxSeparatePools;
    }

    public Map<String, String> getPersistenceUnitProperties() {
        return map;
    }

    public boolean isOracle() {
        return DbType.ORACLE.equals(this.type);
    }

    public boolean isH2() {
        return DbType.H2.equals(this.type);
    }

    public boolean isDerby() {
        return DbType.DERBY.equals(this.type);
    }
}
