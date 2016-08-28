package ru.bormoshka.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("mockDB")
public class SystemConfig {
    @Value("${mockDB.user:}")
    private String dbUser;
    @Value("${mockDB.password:}")
    private String dbPassword;
    @Value("${mockDB.name:mock}")
    private String dbName;
    @Value("${mockDB.host:localhost}")
    private String dbHost;
    @Value("${mockDB.port:0}")
    private int dbPort;

    public SystemConfig() {
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public int getDbPort() {
        return dbPort;
    }

    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }
}
