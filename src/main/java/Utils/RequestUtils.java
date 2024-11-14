package Utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Map;

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
            entry("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0")

    );

    public static HttpGet addParams(HttpGet httpget){
        // todo
        return null;
    }

    public static HttpGet addHeaders(String url){
        HttpGet request = new HttpGet(url);
        headers.forEach(
                (key,value)->request.addHeader(key,value)
        );
        return request;
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
