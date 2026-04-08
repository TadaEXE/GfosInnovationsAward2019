package Util;

/**
 * 1.1
 * @author Robin Sauerborn
 */

public class Hashcode {
    public static String encrypt(String password) {
        char[] hashcodeLetter = 
                Integer.toHexString(password.hashCode()).toCharArray();         //Convert the password to a HEX presentation of the hashcode
        password = "";                                                          //Hash has been calculated and an be used to reconstruct the encrypted String
        int[] values = new int[hashcodeLetter.length];                          //int values for safe value-conversion encryption

        //Encryption
        for(int count = 0; count < hashcodeLetter.length; count++){
            values[count] = hashcodeLetter[count];
            values[count] = values[count] + 1 + hashcodeLetter.length + (count * count);
            values[count] = values[count] % 256;                                //Prevent overflow of char
            password += (char)values[count];
        }

        return password;
    }
}