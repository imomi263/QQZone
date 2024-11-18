package Config;

public class UserProperties {
    private static String userNickName=null;
    private  static String userUin=null;
    private static String g_tk=null;

    public static String getUserUin() {
        return userUin;
    }

    public static String getUserNickName() {
        return userNickName;
    }

    public static void setUserNickName(String userNickName) {
        UserProperties.userNickName = userNickName;
    }

    public static void setUserUin(String userUin) {
        UserProperties.userUin = userUin;
    }

    public static void setG_tk(String g_tk) {
        UserProperties.g_tk = g_tk;
    }

    public static String getG_tk() {
        return g_tk;
    }
}
