package com.study.pattern.behavior.iterator.in;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class CipherUtil {
    /**
     * 加密
     *
     * @param paramArrayOfByte
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] paramArrayOfByte) throws Exception {
        return encrypt("DES", "DES/ECB/PKCS5Padding", CipherUtil.getDESKey(), null, paramArrayOfByte);
    }

    /**
     * 加密算法
     *
     * @param algogithmName    算法名字
     * @param trans            DES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
     * @param key              秘钥
     * @param iv
     * @param paramArrayOfByte 需要加密的数据
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String algogithmName, String trans, byte[] key, byte[] iv, byte[] paramArrayOfByte) throws Exception {
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(key, algogithmName);
        Cipher localCipher = Cipher.getInstance(trans);
        if (iv != null) {
            IvParameterSpec localIvParameterSpec = new IvParameterSpec(iv);
            localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec, localIvParameterSpec);
        } else {
            localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec);
        }
        return localCipher.doFinal(paramArrayOfByte);
    }

    /**
     * 解密
     *
     * @param paramArrayOfByte
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] paramArrayOfByte) throws Exception {
        return decrypt("DES", "DES/ECB/PKCS5Padding", CipherUtil.getDESKey(), null, paramArrayOfByte);
    }

    /**
     * 解密算法
     *
     * @param algogithmName    算法名字
     * @param trans            DES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
     * @param key              秘钥
     * @param iv
     * @param paramArrayOfByte 需要解密的数据
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(String algogithmName, String trans, byte[] key, byte[] iv, byte[] paramArrayOfByte) throws Exception {
        SecretKeySpec localSecretKeySpec = new SecretKeySpec(key, algogithmName);
        Cipher localCipher = Cipher.getInstance(trans);
        if (iv != null) {
            IvParameterSpec localIvParameterSpec = new IvParameterSpec(iv);
            localCipher.init(Cipher.DECRYPT_MODE, localSecretKeySpec, localIvParameterSpec);
        } else {
            localCipher.init(Cipher.DECRYPT_MODE, localSecretKeySpec);
        }
        return localCipher.doFinal(paramArrayOfByte);
    }

    public static byte[] getDESKey() {
        //dI*T->ts
        byte[] magicByte = {'d', 'I', '*', 'T', '-', '>', 't', 'S'};

        byte[] outBytes = new byte[8];
        for (int i = 0; i < 8; i++) {
            outBytes[i] = (byte) (Math.abs(127 * magicByte[i]) % 95 + 32);
        }
        return outBytes;
    }
}
