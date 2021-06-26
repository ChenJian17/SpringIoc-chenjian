package org.springframework.xml;

import java.io.IOException;
import java.util.Properties;

/**
 * @BelongsProject: SpringIoc-chenjian
 * @BelongsPackage: org.springframework.xml
 * @CreateTime: 2020-10-13 10:33
 * @Description: TODO
 */
public class PropertiesPaser {

    static String jdbc_driver;

    static {
        try {
            Properties properties = new Properties();
            properties.load(PropertiesPaser.class.getClassLoader().getResourceAsStream("db.properies"));
            jdbc_driver = properties.getProperty("jdbc.driver");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

}
