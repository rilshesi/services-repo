package co.uk.webapi.basesClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseClass {

    String propPath = "\\src\\test\\resources\\material.properties";
    private String cType;

    public String getProperties() throws IOException {
        Properties base = new Properties();
        FileInputStream f = new FileInputStream(System.getProperty("user.dir")+propPath);
        base.load(f);

        this.cType = base.getProperty("Content-Type");
        return cType;
    }
}
