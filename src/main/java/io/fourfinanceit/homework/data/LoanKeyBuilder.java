package io.fourfinanceit.homework.data;
public class LoanKeyBuilder {
    public static String buildKey(String username, String IPaddress){
        return username + "_" + IPaddress;
    }

}
