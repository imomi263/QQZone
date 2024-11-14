package Utils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Utils.CommonUtils.getHeadersString;
import static Utils.MathUtils.bkn;
import static Utils.MathUtils.ptqrToken;
import static Utils.RequestUtils.*;


public class LoginUtils {
    // 获取二维码
    private String QR_URL="https://ssl.ptlogin2.qq.com/ptqrshow?" +
            "appid=549000912&e=2&l=M&s=3&d=72&v=4&t=0.8692955245720428&daid=5&pt_3rd_aid=0";

    public String getQrsig() throws IOException {

        CloseableHttpResponse response = doGet(QR_URL);
        Header[] headers = response.getHeaders("Set-Cookie");
        //Header[] headers=response.getAllHeaders();
        byte[] bytes=response.getEntity().getContent().readAllBytes();
        getQRImage(bytes);
        //String[] strs=headers[0].getValue().split(";");
        //Map<String,String> map=new HashMap<String,String>();

        try{
            response.close();
        }catch(Exception e){
            e.printStackTrace();
        }


        return getHeadersString(headers).get("qrsig");
    }

    public void getQRImage(byte[] bytes) throws IOException {


        String filepath=Properties.get("QR-url");
        File file=new File(filepath);
        file.mkdirs();
        File newfile=new File(Properties.get("QR-url") + Properties.get("QR-name"));
        if(newfile.exists()){

            newfile.delete();
        }

        OutputStream os = Files.newOutputStream(Paths.get(Properties.get("QR-url") + Properties.get("QR-name")));
        os.write(bytes, 0, bytes.length);
        os.flush();
        os.close();


    }


    public void loadQRImage(){

        JFrame frame = new JFrame();

        frame.setSize(450, 450);
        frame.setLocation(500,300);
        JLabel label = new JLabel();
        ImageIcon icon = new ImageIcon(Properties.get("QR-url")+Properties.get("QR-name"));

        icon=new ImageIcon(icon.getImage().getScaledInstance(300,300, Image.SCALE_DEFAULT));
        label.setIcon(icon);
        frame.getContentPane().add(label);

        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //return image;

    }







    private Header[] getCookies() throws Exception {
        String qrsig=this.getQrsig();
        loadQRImage();
        Header[] cookieHeader=null;
        int ptqrtoken=ptqrToken(qrsig);
        while(true){

            String url="https://ssl.ptlogin2.qq.com/ptqrlogin?" +
                    "u1=https%3A%2F%2Fqzs.qq.com%2Fqzone%2Fv5%2Floginsucc.html%3Fpara"+
                    "%3Dizone&ptqrtoken="+ ptqrtoken +"&ptredirect=0&h=1&t=1&g=1&from_ui=1&ptlang=2052&action=0-0-"+
                    Instant.now().toEpochMilli()/1000+"."+Instant.now().toEpochMilli()%1000 +"&js_ver=20032614&js_type=1&login_sig=&pt_uistyle=40&aid=549000912&daid=5&";


            CloseableHttpClient httpClient=HttpClientBuilder.create().build();
            HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);
            // 消除警报


            HttpGet httpget = new HttpGet(url);

            httpget.addHeader("Cookie","qrsig="+qrsig+";");
            CloseableHttpResponse response = null;

            try{
                response = httpClient.execute(httpget);

                int code=response.getStatusLine().getStatusCode();
                if(code!=200){
                    System.out.println("请求cookie失败");
                    byte[] bytes=EntityUtils.toByteArray(response.getEntity());
                    String respStr=new String(bytes,"utf-8");
                    System.out.println("响应内容为:" + respStr);
                }else{
                    Header[] headers=response.getHeaders("Set-Cookie");
                    HttpEntity responseEntity = response.getEntity();
                    byte[] bytes=EntityUtils.toByteArray(responseEntity);
                    String respStr=new String(bytes,"utf-8");

                    if(respStr.contains("二维码认证中")){
                        System.out.println(LocalDateTime.now() + "二维码认证中");
                    }else if(respStr.contains("二维码已失效")){
                        System.out.println(LocalDateTime.now() + "二维码已失效");
                    }else if(respStr.contains("登录成功")){
                        System.out.println(LocalDateTime.now() + "登录成功");
                        //System.out.println(respStr);

                        Map<String,String>mp=getHeadersString(headers);

                        String uin=mp.get("uin");

                        Pattern pattern=Pattern.compile("(ptsigx=(.*?)&)");
                        Matcher matcher=pattern.matcher(respStr);
                        //while(matcher.find()){
                        //    System.out.println(matcher.group(2));
                        //}
                        matcher.find();
                        String sigx=matcher.group(2);


                        String new_url = "https://ptlogin2.qzone.qq.com/check_sig?pttype=1&uin=" + uin + "&service=ptqrlogin&nodirect=0" +
                        "&ptsigx=" + sigx +
                        "&s_url=https%3A%2F%2Fqzs.qq.com%2Fqzone%2Fv5%2Floginsucc.html%3Fpara%3Dizone&f_url=&ptlang" +
                        "=2052&ptredirect=100&aid=549000912&daid=5&j_later=0&low_login_hour=0&regmaster=0&pt_login_type" +
                        "=3&pt_aid=0&pt_aaid=16&pt_light=0&pt_3rd_aid=0";

                        System.out.println(new_url);

                        // 禁止重定向
                        CloseableHttpClient httpclient= HttpClientBuilder.create().disableRedirectHandling().build();

                        HttpGet httpget2 = new HttpGet(new_url);
                        httpget2.setConfig(defaultConfig);

                        CloseableHttpResponse response2 = httpclient.execute(httpget2);




                        cookieHeader=response2.getHeaders("Set-Cookie");
                        Map<String,String>map=getHeadersString(cookieHeader);
                        JSONObject jsonObject=new JSONObject(map);
                        OutputStream os = Files.newOutputStream(Paths.get(Properties.get("User-saveUrl")+Properties.get("User-name")));
                        JSON.writeTo(os,jsonObject);

                        os.close();
                        httpClient.close();
                        response2.close();
                        break;
                    }
                }
                response.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            Thread.sleep(3000);
        }
        return cookieHeader;
    }


    public String[] getUserInfo() throws Exception{
        Header[] cookies=getCookies();

        Map<String,String>map=getHeadersString(cookies);
        String uin=map.get("uin");

        UserProperties.setUserUin(uin);

        String g_tk=String.valueOf(bkn(map.get("p_skey")));

        UserProperties.setG_tk(g_tk);
        // 这里的uin需要去掉那个'o'
        String url="https://r.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?g_tk=" +
                g_tk + "&uins=" + uin.substring(1);

        //System.out.println(url);

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = addHeaders(url);
        StringBuilder stringBuilder=new StringBuilder();

        String[] strList={"skey","uin","p_skey","p_uin","pt4_token"};


        for(String s:strList){
            stringBuilder.append(s+"="+map.get(s)+";");
        }
        System.out.println(stringBuilder);
        httpget.addHeader("Cookie",stringBuilder.toString());

        HttpResponse response = httpclient.execute(httpget);

        String info=new String(EntityUtils.toByteArray(response.getEntity()),"GBK");
        //System.out.println(info);

        Pattern pattern=Pattern.compile("\\[(.*)\\]");
        Matcher matcher=pattern.matcher(info);
        matcher.find();
        //System.out.println(matcher.group(1));
        String[] strs=matcher.group(1).split(",");


        UserProperties.setUserNickName(strs[6]);

        return strs;

    }
}
