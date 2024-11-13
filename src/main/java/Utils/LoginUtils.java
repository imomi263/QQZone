package Utils;


import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.http.Header;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import Utils.RequestUtils.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static Utils.RequestUtils.doGet;



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
        String[] strs=headers[0].getValue().split(";");
        Map<String,String> map=new HashMap<String,String>();
        for (String string : strs) {

            if(string.contains("=")){
                String key = string.split("=")[0];
                String value = string.split("=")[1];
                String key1 = key.trim();
                String value1 = value.trim();
                map.put(key1, value1);
            }
        }try{
            response.close();
        }catch(Exception e){
            e.printStackTrace();
        }


        return map.get("qrsig");
    }

    public void getQRImage(byte[] bytes) throws IOException {

        //CloseableHttpResponse response = doGet(QR_URL);
        //byte[] bytes=response.getEntity().getContent().readAllBytes();
        String filepath=Properties.get("QR-url");
        File file=new File(filepath);
        file.mkdirs();
        File newfile=new File(Properties.get("QR-url") + Properties.get("QR-name"));
        if(newfile.exists()){
            System.out.println("11");
            newfile.delete();
        }

        OutputStream os = Files.newOutputStream(Paths.get(Properties.get("QR-url") + Properties.get("QR-name")));
        os.write(bytes, 0, bytes.length);
        os.flush();
        os.close();


    }


    public void loadQRImage(){
//        File sourceimage = new File(Properties.get("QR-url")+Properties.get("QR-name"));  //source.gif图片要与HelloJava.java同在一目录下
//        BufferedImage image=null;
//        try{
//            image = ImageIO.read(sourceimage);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//
//        int height=image.getHeight();
//        int width=image.getWidth();

        JFrame frame = new JFrame();

        frame.setSize(450, 450);
        frame.setLocation(500,300);
        JLabel label = new JLabel();

        label.setIcon(new ImageIcon(Properties.get("QR-url")+Properties.get("QR-name")));
        frame.getContentPane().add(label);

        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //return image;

    }


    public static String decode(BufferedImage image) throws Exception {
        if (image == null) {
            return null;
        }

        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Hashtable hints = new Hashtable();
        //设置编码方式
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        //优化精度，解决com.google.zxing.NotFoundException
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        //开启PURE_BARCODE模式(复杂模式)
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);

        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }


    public int ptqrToken(String qrsig){
        int n=qrsig.length();
        int i=0;
        int e=0;
        while(n>i){
            e=e+(e<<5)+ Integer.valueOf(qrsig.charAt(i));
            i+=1;
        }
        return 2147483647 & e;
    }

    public Map<String,String> getCookies() throws InterruptedException, URISyntaxException, IOException {
        String qrsig=this.getQrsig();
        loadQRImage();

        int ptqrtoken=ptqrToken(qrsig);
        while(true){

            String url="https://ssl.ptlogin2.qq.com/ptqrlogin?" +
                    "u1=https%3A%2F%2Fqzs.qq.com%2Fqzone%2Fv5%2Floginsucc.html%3Fpara"+
                    "%3Dizone&ptqrtoken="+ ptqrtoken +"&ptredirect=0&h=1&t=1&g=1&from_ui=1&ptlang=2052&action=0-0-"+
                    Instant.now().toEpochMilli()/1000+"."+Instant.now().toEpochMilli()%1000 +"&js_ver=20032614&js_type=1&login_sig=&pt_uistyle=40&aid=549000912&daid=5&";


            CloseableHttpClient httpClient=new DefaultHttpClient();
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
                    HttpEntity responseEntity = response.getEntity();
                    byte[] bytes=EntityUtils.toByteArray(responseEntity);
                    String respStr=new String(bytes,"utf-8");
                    if(respStr.contains("二维码认证中")){
                        System.out.println(LocalDateTime.now() + "二维码认证中");
                    }else if(respStr.contains("二维码已失效")){
                        System.out.println(LocalDateTime.now() + "二维码已失效");
                    }else if(respStr.contains("登录成功")){
                        System.out.println(LocalDateTime.now() + "登录成功");

                        break;
                    }
                }

                response.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            Thread.sleep(3000);
        }
        return null;
    }
}
