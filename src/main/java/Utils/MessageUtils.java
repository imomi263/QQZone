package Utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import static Utils.RequestUtils.addHeaders;

public class MessageUtils {
    public int getMessageCount(){
        int lower_bound=0;
        int upper_bound=100000;
        int total = upper_bound /2;
        while(lower_bound < upper_bound){
            String message=getMessage(lower_bound,upper_bound);
            String url="https://user.qzone.qq.com/proxy/domain/" +
                    "ic2.qzone.qq.com/cgi-bin/feeds/feeds2_html_pav_all";


            CloseableHttpClient httpClient= HttpClientBuilder.create().build();
            HttpGet httpGet=addHeaders(url);

            CloseableHttpResponse response = null;

        }

        return 0;
    }

    private String getMessage(int lower_bound, int upper_bound){
        return null;
    }
}
