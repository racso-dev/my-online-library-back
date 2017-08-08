package com.marketpay.utils;

import com.marketpay.exception.MarketPayException;
import org.springframework.http.HttpStatus;

import java.security.SecureRandom;

/**
 * Created by etienne on 08/08/17.
 */
public class RandomUtils {

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Get a random byte[]
     * @param length
     * @return
     */
    private static byte[] getRandomBytes(int length) throws MarketPayException {
        try {
            // Uses a secure Random not a simple Random
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] bSalt = new byte[length];
            random.nextBytes(bSalt);
            return bSalt;
        } catch(Exception e) {
            throw new MarketPayException(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur est survenue lors de la génération du random");
        }
    }

    /**
     * From a byte[] returns a Hexa representation
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Get an random String of length specified
     * @param length
     * @return
     */
    public static String getRandowString(int length) throws MarketPayException {
        byte[] bytes = getRandomBytes(length);
        return bytesToHex(bytes);
    }

}
