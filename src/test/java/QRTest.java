import Utils.LoginUtils;
import Utils.MessageUtils;

public class QRTest {


    public static void main(String[] args) throws Exception {
        //System.out.println(Properties.get("QR-url"));
        LoginUtils loginUtils = new LoginUtils();
        loginUtils.getUserInfo();
        MessageUtils messageUtils = new MessageUtils();
        messageUtils.getMessageCount();

    }
}
