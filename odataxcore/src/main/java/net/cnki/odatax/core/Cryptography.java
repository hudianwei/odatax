package net.cnki.odatax.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * 网络数据传输加解密规范：<br>
 * <b>A->B举例</b>:<br>
 * <b>A:</b><br>
 * 1.A提取消息m的消息摘要h(m),并使用<b>自己(A)</b>的私钥对摘要h(m)进行加密,生成签名s<br>
 * 2.A将签名s和消息m一起,使用<b>B的公钥</b>进行加密,生成密文c,发送给B<br>
 *
 * <b>B:</b><br>
 * 1. B接收到密文c,使用<b>自己(B)</b>的私钥解密c得到明文m和数字签名s<br>
 * 2. B使用<b>A</b>的公钥解密数字签名s解密得到H(m) <br>
 * 3.B使用相同的方法提取消息m的消息摘要h(m) <br>
 * 4.B比较两个消息摘要。相同则验证成功;不同则验证失败<br>
 *
 * @ClassName Cryptography
 * @Description 加密
 */
public class Cryptography {

    private static final String RSA = "RSA";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PUBLIC_KEY_B64 = "publicKeyBase64";
    public static final String PRIVATE_KEY = "privateKey";
    public static final String PRIVATE_KEY_B64 = "privateKeyBase64";
    private static final String SHA1_WITH_RSA = "SHA1WithRSA";
    /**
     * 加密算法
     */
    private static final String CIPHER_DE = "RSA";
    /**
     * 解密算法
     */
    private static final String CIPHER_EN = "RSA";
    /**
     * 密钥长度
     */
    private static final int RSA_SIZE_2048 = 2048;
    private static final int RSA_SIZE_1024 = 1024;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static Map<String, Object> create1024Key()
            throws NoSuchAlgorithmException {
        return createKey(RSA_SIZE_1024);
    }

    public static Map<String, Object> create2048Key()
            throws NoSuchAlgorithmException {
        return createKey(RSA_SIZE_2048);
    }

    public static Map<String, Object> createKey(int keySize)
            throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = null;
        keyGen = KeyPairGenerator.getInstance(RSA);
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair key = keyGen.generateKeyPair();
        PublicKey pubKey = key.getPublic();
        PrivateKey priKey = key.getPrivate();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(PUBLIC_KEY, pubKey);
        map.put(PRIVATE_KEY, priKey);

        map.put(PUBLIC_KEY_B64, Base64.encodeBase64String(pubKey.getEncoded()));
        map.put(PRIVATE_KEY_B64, Base64.encodeBase64String(priKey.getEncoded()));
        return map;
    }

    public static void createKey(String publicFilePath, String privateFilePath,
                                 int keySize) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(RSA);
        keyGen.initialize(keySize, new SecureRandom());
        KeyPair pair = keyGen.generateKeyPair();
        write(publicFilePath, pair.getPublic());
        write(privateFilePath, pair.getPrivate());
    }

    private static void write(String path, Object key) throws Exception {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            boolean oos = file.getParentFile().mkdirs();
            if (!oos) {
                System.out.println("创建文件目录异常！");
                return;
            }
        }

        ObjectOutputStream oos1 = null;

        try {
            oos1 = new ObjectOutputStream(new FileOutputStream(path));
            oos1.writeObject(key);
        } catch (Exception arg11) {
            throw new Exception("密钥写入异常", arg11);
        } finally {
            if (null != oos1) {
                try {
                    oos1.close();
                } catch (IOException arg10) {
                    oos1 = null;
                }
            }

        }

    }

    /**
     * 通过公钥文件，得到公钥对象
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static PublicKey resolvePublicKey(String path) throws Exception {
        PublicKey pubkey = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        PublicKey e;
        try {
            fis = FileUtils.openInputStream(new File(path));
            ois = new ObjectInputStream(fis);
            pubkey = (PublicKey) ois.readObject();
            e = pubkey;
        } catch (Exception arg7) {
            throw new Exception("解析异常", arg7);
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }

        return e;
    }

    /**
     * 通过私钥文件得到私钥对象
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static PrivateKey resolvePrivateKey(String path) throws Exception {
        PrivateKey prikey = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        PrivateKey e;
        try {
            fis = FileUtils.openInputStream(new File(path));
            ois = new ObjectInputStream(fis);
            prikey = (PrivateKey) ois.readObject();
            e = prikey;
        } catch (Exception arg7) {
            throw new Exception("解析异常", arg7);
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(fis);
        }

        return e;
    }

    /**
     * 公钥对象转公钥串
     *
     * @param pubKey
     * @return
     */
    public static String getBase64PublicKeyString(PublicKey pubKey) {
        return Base64.encodeBase64String(pubKey.getEncoded()).trim();
    }

    /**
     * 私钥对象转私钥串
     *
     * @param priKey
     * @return
     */
    public static String getBase64PrivateKeyString(PrivateKey priKey) {
        return Base64.encodeBase64String(priKey.getEncoded()).trim();
    }

    /**
     * 公钥文件转公钥串
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String getBase64PublicKeyString(String path) throws Exception {
        PublicKey pubKey = resolvePublicKey(path);
        return getBase64PublicKeyString(pubKey);
    }

    /**
     * 私钥文件转私钥串
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String getBase64PrivateKeyString(String path)
            throws Exception {
        PrivateKey priKey = resolvePrivateKey(path);
        return getBase64PrivateKeyString(priKey);
    }

    /**
     * 公钥验签
     *
     * @param data
     * @param sign
     * @param pubk
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     */
    public static boolean vertiy(String data, String sign, PublicKey pubk)
            throws InvalidKeyException, NoSuchAlgorithmException,
            SignatureException {
        return vertiy(data.getBytes(), Base64.decodeBase64(sign), pubk);
    }

    public static boolean vertiy(byte[] data, byte[] sign, PublicKey pubk)
            throws NoSuchAlgorithmException, InvalidKeyException,
            SignatureException {
        Signature signature = Signature.getInstance(SHA1_WITH_RSA);
        signature.initVerify(pubk);
        signature.update(data);
        return signature.verify(sign);
    }

    public static byte[] sign(byte[] data, PrivateKey prik)
            throws NoSuchAlgorithmException, InvalidKeyException,
            SignatureException {
        Signature signature = Signature.getInstance(SHA1_WITH_RSA);
        signature.initSign(prik);
        signature.update(data);
        return signature.sign();
    }

    /**
     * 私钥签名
     *
     * @param data
     * @param prik
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     */
    public static String sign(String data, PrivateKey prik)
            throws InvalidKeyException, NoSuchAlgorithmException,
            SignatureException {
        return Base64.encodeBase64String(sign(data.getBytes(), prik)).trim();
    }

    /**
     * md5加密
     *
     * @param message
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String md5(String message)
            throws UnsupportedEncodingException {
        return DigestUtils.md5Hex(message.getBytes("utf-8"));
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws InvalidKeySpecException
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        // 得到公钥
        byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        Key key = keyFactory.generatePublic(x509EncodedKeySpec);
        // 加密数据，分段加密
        Cipher cipher = Cipher.getInstance(CIPHER_EN);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLength - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    public static String encrypt(String message, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_EN);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] data = message.getBytes();
        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLength - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return new String(encryptedData);
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        // 得到私钥
        byte[] keyBytes = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec pKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(
                keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        Key key = keyFactory.generatePrivate(pKCS8EncodedKeySpec);
        // 解密数据，分段解密
        Cipher cipher = Cipher.getInstance(CIPHER_DE);
        cipher.init(Cipher.DECRYPT_MODE, key);
        int inputLength = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLength - offset);
            }
            out.write(cache);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * SHA1加密
     *
     * @param s
     * @return
     */
    public static String SHA1(String s) {
        String ret = "";
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(s.getBytes());
            byte messageDigest[] = sha1.digest();
            ret = toHexString(messageDigest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    private static String toHexString(byte[] keyData) {
        if (keyData == null) {
            return null;
        }
        int expectedStringLen = keyData.length * 2;
        StringBuilder sb = new StringBuilder(expectedStringLen);
        for (int i = 0; i < keyData.length; i++) {
            String hexStr = Integer.toString(keyData[i] & 0x00FF, 16);
            if (hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }
            sb.append(hexStr);
        }
        return sb.toString();
    }
}