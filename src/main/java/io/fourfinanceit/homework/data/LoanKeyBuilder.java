package io.fourfinanceit.homework.data;
public class LoanKeyBuilder {
    public static String buildKey(String firstName, String lastName, String IPaddress){
        return firstName + "_" + lastName+ "_" + IPaddress;
    }

}
