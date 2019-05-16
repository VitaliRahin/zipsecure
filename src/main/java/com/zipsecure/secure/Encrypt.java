package com.zipsecure.secure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static com.zipsecure.errors.ErrorMessages.EXCEPTION_DECRYPT_PASS;
import static com.zipsecure.utils.EncryptUtils.encrypt;
import static com.zipsecure.utils.EncryptUtils.readPublicKey;


public class Encrypt {
    private static final Logger LOGGER = LoggerFactory.getLogger(Encrypt.class);
    private static final Charset CHARSET = Charset.forName("UTF-8");
    private static Encrypt encrypt;
    private String keyNamePath;
    private PublicKey publicKey;

    private Encrypt(String keyNamePath){
        this.keyNamePath = keyNamePath;
    }

    public static Encrypt getInstance(String keyNamePath){
        if (encrypt != null && keyNamePath.equals(encrypt.keyNamePath)) {
            return encrypt;
        } else {
            encrypt = new Encrypt(keyNamePath);
            encrypt.readPublicKeyToInstance();
            return encrypt;
        }
    }

    public byte[] encryptPassword(char[] publicPass){
        byte[] secretPass = null;
        try {
            secretPass = encrypt(publicKey, toBytes(publicPass));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException e) {
            LOGGER.error(EXCEPTION_DECRYPT_PASS);
            e.printStackTrace();
        }
        return secretPass;
    }

    private void readPublicKeyToInstance(){
        try {
            publicKey = readPublicKey(keyNamePath);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error(EXCEPTION_DECRYPT_PASS);
            e.printStackTrace();
        }
    }

    private byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }
}
