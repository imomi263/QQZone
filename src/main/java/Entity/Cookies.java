package Entity;

import org.apache.http.client.methods.HttpGet;

import java.util.Map;

public  class Cookies {
    private static String sket=null;
    private static String uin=null;
    private static String p_skey=null;
    private static String p_uin=null;
    private static String pt4_token=null;

    public static HttpGet getCookies(HttpGet httpGet){
        StringBuilder builder=new StringBuilder();
        builder.append("skey"+"="+sket+";");
        builder.append("uin"+"="+uin+";");
        builder.append("p_skey"+"="+p_skey+";");
        builder.append("p_uin"+"="+p_uin+";");
        builder.append("pt4_token"+"="+pt4_token+";");
        httpGet.addHeader("Cookie",builder.toString());
        return httpGet;
    }

    public static void setCookies(Map<String,String>map){
        sket=map.get("skey");
        uin=map.get("uin");
        p_skey=map.get("p_skey");
        p_uin=map.get("p_uin");
        pt4_token=map.get("pt4_token");
    }

}
