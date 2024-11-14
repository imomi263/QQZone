package Utils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Properties extends HashMap<String,String> {





    private static Map<String,Object> map=null;

    static {
        loadProperties();
    }

    private static void loadProperties(){
        //System.out.println("Properties init");
        Yaml yaml=new Yaml();
        InputStream inputStream = Properties.class.
                getClassLoader().getResourceAsStream("application.yml");
        map=yaml.load(inputStream);

    }



    public static String get(String key){
        Object value=getProperty(key);
        if(value!=null){
            return value.toString();
        }
        return null;
    }

    public static Object getProperty(String key){
        String[] keys=key.split("-");
        //System.out.println(keys);
        Object value = map;
        for (String k : keys) {
            if (value instanceof Map) {
                value = ((Map<?, ?>) value).get(k);
            } else {
                return null;
            }
        }
        return value;
    }
}
