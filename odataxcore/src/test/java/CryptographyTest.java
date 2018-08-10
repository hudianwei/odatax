import net.cnki.odatax.core.Cryptography;
import net.cnki.odatax.core.TextUtil;
import org.apache.commons.net.util.Base64;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:加密测试(公钥加密私钥解密，签名验证数据完整性)
 * @author: HU
 * @date: 2018/8/8 14:13
 */
public class CryptographyTest {
    @Test
    public void CryptographyTest() throws Exception {
        Cryptography.createKey("D:\\JavaTest\\Rsa\\Public\\public.key", "D:\\JavaTest\\Rsa\\private\\private.key", 1024);
        PublicKey publicKey = Cryptography.resolvePublicKey("D:\\JavaTest\\Rsa\\Public\\public.key");
        PrivateKey privateKey = Cryptography.resolvePrivateKey("D:\\JavaTest\\Rsa\\private\\private.key");
        System.out.println("公钥：" + Base64.encodeBase64String(publicKey.getEncoded()));
        System.out.println("私钥：" + Base64.encodeBase64String(privateKey.getEncoded()));
        String data = "";
        String signData = Cryptography.sign(data, privateKey);
        System.out.println("签名值为：" + signData);
        boolean result = Cryptography.vertiy(data, signData, publicKey);
        System.out.println("验证结果为：" + result);
    }

    @Test
    public void TestCheck() throws Exception {
        PublicKey publicKey = Cryptography.resolvePublicKey("D:\\JavaTest\\Rsa\\Public\\public.key");
        PrivateKey privateKey = Cryptography.resolvePrivateKey("D:\\JavaTest\\Rsa\\private\\private.key");

        //region 明文加密
        Map<String, Object> jsonMap = new HashMap<String, Object>() {
            {
                put("name", "x学生");
                put("sex", "男");
                put("age", "时间好快");
            }
        };
        Map<String, String> sendData = new HashMap<String, String>();
        String json = TextUtil.toJSON(jsonMap);
        sendData.put("data", json);
        System.out.println("明文：" + json);
        //摘要
        String md5 = Cryptography.md5(json);
        System.out.println("摘要：" + md5);
        String sign = Cryptography.sign(md5, privateKey);
        //签名
        System.out.println("签名：" + sign);
        sendData.put("sign", sign);
        //加密
        byte[] encryptStr = Cryptography.encryptByPublicKey(TextUtil.toJSON(sendData).getBytes(), Cryptography.getBase64PublicKeyString(publicKey));
        System.out.println("密文：" + new String(encryptStr));
        //endregion
        //region 解密
        byte[] decryptStr = Cryptography.decryptByPrivateKey(encryptStr, Cryptography.getBase64PrivateKeyString(privateKey));
        Map map = TextUtil.parseFromJSON(new String(decryptStr), Map.class);
        String data = (String) map.get("data");
        sign = (String) map.get("sign");
        System.out.println("解析后data：" + data);
        System.out.println("解析后sign：" + sign);
        System.out.println("验证结果：" + Cryptography.vertiy(md5, sign, publicKey));
        //endregion
    }
}
