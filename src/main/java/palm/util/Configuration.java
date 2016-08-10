package palm.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    public static int startAge;
    public static int endAge;
    public static int stepAge;
    public static int imageNum;
    public static int imageUrlThreadNum;
    public static int imageSaveThreadNum;
    public static String imageAjaxUrlHead;
    public static String imageAjaxUrlBody;
    public static String imageAjaxUrlEnd;
    public static String imageSavePath;

    public Configuration(String file) throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(file);
        Properties properties = new Properties();
        properties.load(inputStream);
        startAge = Integer.valueOf(properties.getProperty("startAge"));
        endAge = Integer.valueOf(properties.getProperty("endAge"));
        stepAge = Integer.valueOf(properties.getProperty("stepAge"));
        imageNum = Integer.valueOf(properties.getProperty("imageNum"));
        imageUrlThreadNum = Integer.valueOf(properties.getProperty("imageUrlThreadNum"));
        imageSaveThreadNum = Integer.valueOf(properties.getProperty("imageSaveThreadNum"));
        imageAjaxUrlHead = properties.getProperty("imageAjaxUrlHead");
        imageAjaxUrlBody = properties.getProperty("imageAjaxUrlBody");
        imageAjaxUrlEnd = properties.getProperty("imageAjaxUrlEnd");
        imageSavePath = properties.getProperty("imageSavePath");
    }
}
