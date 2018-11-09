package csye6225Web.config;

import org.springframework.core.io.support.ResourcePropertySource;
import java.io.IOException;

public class PropertyUtil {

    public static String ACCESS_KEY;
    public static String SECRET_KEY;

    private static PropertyUtil instance = null;

    private PropertyUtil() {
        ResourcePropertySource propertySource2 = null; //name, location
        try {
            propertySource2 = new ResourcePropertySource("resources", "classpath:application.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ACCESS_KEY = propertySource2.getProperty("ACCESS_KEY").toString();
        SECRET_KEY = propertySource2.getProperty("SECRET_KEY").toString();

    }

    public static PropertyUtil getInstance() {
        if (instance == null)
            instance = new PropertyUtil();
        return instance;
    }





}
