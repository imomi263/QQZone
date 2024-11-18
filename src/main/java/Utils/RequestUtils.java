package Utils;

import Config.UserProperties;
import org.apache.http.Header;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Utils.CommonUtils.getHeadersString;
import static java.util.Map.entry;

public class RequestUtils {

    private static Map<String,String> headers=Map.ofEntries(
            entry("authority","user.qzone.qq.com"),
            entry("accept","text/html,application/xhtml+xml,application/xml;" +
                    "q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7"),
            entry("accept-language","zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6"),
            entry("cache-control","no-cache"),
            entry("pragma","no-cache"),
            entry("sec-ch-ua","\"Not A(Brand\";v=\"99\", \"Microsoft Edge\";v=\"121\", \"Chromium\";v=\"121\""),
            entry("sec-ch-ua-mobile","?0"),
            entry("sec-ch-ua-platform","Windows"),
            entry("sec-fetch-dest","document"),
            entry("sec-fetch-mode","navigate"),
            entry("sec-fetch-site","none"),
            entry("sec-fetch-user","?1"),
            entry("upgrade-insecure-requests","1"),
            entry("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:83.0) Gecko/20100101 Firefox/83.0")

    );

    public static Map<String,Object> params=Map.ofEntries(
            //entry("uin", UserProperties.getUserUin()),
            entry("begin_time","0"),
            entry("end_time","0"),
            entry("getappnotification","1"),
            entry("getnotifi","1"),
            entry("has_get_key","0"),
            //entry("offset",)
            entry("set","0"),
            //entry("count",)
            entry("useutf8","1"),
            entry("outputhtmlfeed","1"),
            entry("scope","1"),
            entry("format","jsonp")
            //entry("g_tk", List.of(UserProperties.getG_tk(),UserProperties.getG_tk()))
    );

    public static HttpGet addParams(HttpGet httpget){

        params.forEach(
                (key,value)->httpget.addHeader(key,value.toString())
        );
        httpget.addHeader("uin",UserProperties.getUserUin());
        httpget.addHeader("g_tk",UserProperties.getG_tk().toString());
        httpget.addHeader("g_tk",UserProperties.getG_tk().toString());

        return httpget;
    }

    public static URIBuilder addParams(URIBuilder uriBuilder){

        params.forEach(
                (key,value)->uriBuilder.addParameter(key,value.toString())
        );
        // 这里的uin也需要去掉前面的o
        uriBuilder.addParameter("uin",UserProperties.getUserUin().substring(1));
        uriBuilder.addParameter("g_tk",UserProperties.getG_tk().toString());
        uriBuilder.addParameter("g_tk",UserProperties.getG_tk().toString());

        return uriBuilder;
    }

    public static HttpGet addHeaders(String url){
        HttpGet request = new HttpGet(url);
        headers.forEach(
                (key,value)->request.addHeader(key,value)

        );
        return request;
    }

    public static HttpGet addHeaders(HttpGet httpget){
        //HttpGet request = new HttpGet(url);
        headers.forEach(
                (key,value)->httpget.addHeader(key,value)
        );

        return httpget;
    }
    public static RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build();


    public static CloseableHttpResponse doGet(String url){
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = null;
        try{
            response = httpclient.execute(httpget);
        }catch (IOException e) {
            e.printStackTrace();
        }  finally {
            try{
                httpclient.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }



}
