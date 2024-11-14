import Utils.LoginUtils;
import Utils.Properties;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;

public class QRTest {


    public static void main(String[] args) throws Exception {
        //System.out.println(Properties.get("QR-url"));
        LoginUtils loginUtils = new LoginUtils();
        loginUtils.getUserInfo();


    }
}
