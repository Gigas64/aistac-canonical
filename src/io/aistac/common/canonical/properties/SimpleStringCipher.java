/*
 * @(#)SimpleStringCipher.java
 *
 * Copyright:       Copyright (c) 2017
 * Organisation:    opengrass.io aistac.io oatridge.io
 * Schema:          Adaptive, Intelligent, Single Task Application Concern (AI-STAC)
 */
package io.aistac.common.canonical.properties;

import io.aistac.common.canonical.exceptions.ObjectBeanException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

/**
 * The {@code SimpleStringCipher} Class
 *
 * @author Darryl Oatridge
 * @version 1.00 Nov 6, 2012
 */
public class SimpleStringCipher {

    private final static byte[] LINEBREAK = {}; // Remove Base64 encoder default linebreak
    private final static String SECRET; //must be 16 characters
    private final static SecretKey KEY;
    private final static Cipher CIPHER;
    private final static Base64 CODER;

    static {
        try {
            SECRET = StringUtils.substring(TaskPropertiesService.getProp("microlib.base.security.cipher.key","9£5nH3") + TaskPropertiesService.CODE + "9Uj&S", -16);
            KEY = new SecretKeySpec(SECRET.getBytes(), "AES"); // AES is a 128-bit block cipher supporting keys of 128, 192, and 256 bits.
            CIPHER = Cipher.getInstance("AES/ECB/PKCS5Padding", "SunJCE");
            CODER = new Base64(32, LINEBREAK, true);
        } catch(NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex) {
            throw new ExceptionInInitializerError("Unable to create String Cipher");
        }
    }

    public static synchronized String encrypt(String plainText) throws ObjectBeanException {
        byte[] cipherText;
        try {
            CIPHER.init(Cipher.ENCRYPT_MODE, KEY);
            cipherText = CIPHER.doFinal(plainText.getBytes());
        } catch( InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new ObjectBeanException("Unable to encrypt ciphered data: " +  ex.toString());
        }
        return new String(CODER.encode(cipherText));
    }

    public static synchronized String decrypt(String codedText) throws ObjectBeanException {
        byte[] encypted = CODER.decode(codedText.getBytes());
        byte[] decrypted;
        try {
            CIPHER.init(Cipher.DECRYPT_MODE, KEY);
            decrypted = CIPHER.doFinal(encypted);
        } catch( InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new ObjectBeanException("Decryption of the ObjectBean failed. The cipher key might have changed: " + ex.toString());
        }
        return new String(decrypted);
    }

    private SimpleStringCipher() {
    }
}
