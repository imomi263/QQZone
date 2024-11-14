package Utils;

public class MathUtils {

    public static int ptqrToken(String qrsig){
        int n=qrsig.length();
        int i=0;
        int e=0;
        while(n>i){
            e=e+(e<<5)+ Integer.valueOf(qrsig.charAt(i));
            i+=1;
        }
        return 2147483647 & e;
    }

    public static int bkn(String pSkey){

        int t=5381,n=0,o=pSkey.length();
        while(n<o){
            t=t+(t<<5)+Integer.valueOf(pSkey.charAt(n));
            n+=1;
        }
        return t & 2147483647;
    }

}
