package Utils;

import Entity.Cookies;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static Utils.CommonUtils.getHeadersString;
import static Utils.RequestUtils.addHeaders;
import static Utils.RequestUtils.addParams;

public class MessageUtils {
    // todo 获取消息数量
    // 获取失败默认自动获取1000条
    public int getMessageCount() throws Exception {
        int lower_bound=0;
        int upper_bound=100000;
        int total = upper_bound /2;
        while(lower_bound < upper_bound){
            String message=getMessage(total,100);
            if(message == null){
                System.out.println("获取消息数量失败");
                return 1000;
            }
            if(message.contains("div")){
                lower_bound=total+1;
            }else{
                upper_bound=total-1;
            }

            total=(lower_bound+upper_bound)/2;
        }
        System.out.println("total:"+total);
        return total;
    }


    // 这里返回了501响应码，不太清楚具体是什么情况
    private String getMessage(int lower_bound, int count) throws Exception {
        String url="https://user.qzone.qq.com/proxy/domain/" +
                "ic2.qzone.qq.com/cgi-bin/feeds/feeds2_html_pav_all";

        CloseableHttpClient httpClient= HttpClientBuilder.create().build();


        URIBuilder  uriBuilder=new URIBuilder(url);
        uriBuilder.addParameter("offset",String.valueOf(lower_bound));
        uriBuilder.addParameter("count",String.valueOf(count));
        uriBuilder=addParams(uriBuilder);

        HttpGet httpGet=new HttpGet(uriBuilder.build());
        httpGet=addHeaders(httpGet);

        httpGet=Cookies.getCookies(httpGet);

//        for(Header header : httpGet.getAllHeaders()) {
//            String strs = header.getValue();
//            System.out.println(strs);
//        }

        System.out.println(uriBuilder);

        try{
            CloseableHttpResponse response = httpClient.execute(httpGet);

            if(response.getStatusLine().getStatusCode()!=200){

                System.out.println(response.getStatusLine().getStatusCode());
                httpClient.close();
                response.close();
                return null;
            }

            String str= EntityUtils.toString(response.getEntity());
            System.out.println(str);

            httpClient.close();
            response.close();
            return str;
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
