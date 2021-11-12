package ru.erminson.lc.configuration;

import java.util.List;

//@Configuration
//@ConfigurationProperties(prefix = "logging")
public class LoggingPackageProperties {
    private List<String> pointcuts;

    public List<String> getPointcuts() {
        return pointcuts;
    }

    public void setPointcuts(List<String> pointcuts) {
        this.pointcuts = pointcuts;
    }
}
