package Utils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.apache.http.Header;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class CommonUtils {


    public static Map<String,String> getHeadersString(Header[] headers){
        Map<String,String> map=new HashMap<String,String>();
        for(Header header : headers){
            String[] strs=header.getValue().split(";");
            //System.out.println(header);
            for(String str : strs){
                if(str.contains("=")){
                    String[] str_key=str.split("=");
                    String key = str.split("=")[0];
                    if(str_key.length>1){
                        // trim可以去除字符串开头和结尾的空格
                        map.put(key,str_key[1].trim());
                    }

                }
            }
        }
        return map;
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



}
