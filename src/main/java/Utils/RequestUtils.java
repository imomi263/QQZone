package Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class RequestUtils {


    public static CloseableHttpResponse doGet(String url){
        CloseableHttpClient httpclient = new DefaultHttpClient();
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
