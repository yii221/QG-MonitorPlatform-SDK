package com.pmpsdk.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * @Description: 加密工具类  // 类说明
 * @ClassName: CryptoUtil    // 类名
 * @Author: lrt          // 创建者
 * @Date: 2025/8/5 17:32   // 时间
 * @Version: 1.0     // 版本
 */
public class CryptoUtil {

    private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuIsxakJdfey1bMqM4exeRLiG3PfNr5ycYSjgi1Dsi26GEKPuqQOfcQaru/R08iOOlpaUa+Y99BMeoE2FjodDuwAHV0Pwmr1hETtvA6WMeB7cG/IbxzZ7nWQ50h3LidL8D6KQRHHM+awN9PE2kRvlVEidHuN7TwqCqNkybZ54373mc6GogoWLRktIuLhF+yxARWrlUXbEzRJmbdEl2oRI52zFkweanAAiRKn/ATTlAx0yA6EAqgJYS0Uv2q8ymFVeXciaDNegoCPillpa/zPm531uYaalyoS5r/0aDur/8hfeSvCp35z8Uj+lVAX/ZYvQkzvRL5mOxjmRKv9pYG1XdwIDAQAB";
    private static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC4izFqQl197LVsyozh7F5EuIbc982vnJxhKOCLUOyLboYQo+6pA59xBqu79HTyI46WlpRr5j30Ex6gTYWOh0O7AAdXQ/CavWERO28DpYx4Htwb8hvHNnudZDnSHcuJ0vwPopBEccz5rA308TaRG+VUSJ0e43tPCoKo2TJtnnjfveZzoaiChYtGS0i4uEX7LEBFauVRdsTNEmZt0SXahEjnbMWTB5qcACJEqf8BNOUDHTIDoQCqAlhLRS/arzKYVV5dyJoM16CgI+KWWlr/M+bnfW5hpqXKhLmv/RoO6v/yF95K8KnfnPxSP6VUBf9li9CTO9EvmY7GOZEq/2lgbVd3AgMBAAECggEADcepr0iOA0rQmp4JT7lpqfH+yAiM8vxbxPmGGCu5GLRDuIG1NCj8F+F/+K9JUudgIf3TzrbI5i0jT/mvQQmiqhesyfxdvqro0POXgjXeEP4k62/0WQzIGuQ9WQ42gO/LOJOn+CRol4rHGVowUTznA+3O0Fv7fNceaI3yBl4bTSxCcXpXfGUvq0b5TA2lPm5hREkSCMeA6C7kJ31XDTSMqbARw4pL97M36qLAc2TFrnqUPSNYjvtO/wLo7Es9p2iMoPB/KutPtb/HjlkKG9r+Iy2CHlahqLPoL0izNTFJmUtMPNLVbTituziFsQBVyPi2yyQPIKLYwSM+f6ZKRXWHwQKBgQD8mjENrSDAX+wWgxzc7lpGAxpv1aJlnpEAT++NC3xPo5d9n/MMcao8g8fFvwahBMT55dBYP+KsYE5sl2Np0GJ9TDQ+N2csPTKerTfWYkf3d+QTpUfFCINkuCP6EAOcG/VfOTwD0T2j05MFwRxN78ErGOme6MfifVQ3I9DXG0RN4QKBgQC7BqYsur+5zeiFdGKVWtIHO4xf+UOoLIurW8sb7Xr/lRal5+Ha+k2MKsyLWMApSJjo4LflIdoqtOUngBwGlJojRU9ez61iG9vn4uVbtcg9uK5DkcQ1Gl3GJR83KIlBfTEC7NtCd7luKE9+hRVipN06yQqr0IY8P+XyNfFjJlLgVwKBgGTgPs6zYOzyYZnr6k7gcPScsKYQrrXeSRHS2lt9RruXHRxyD+HcUpuwFMuHqeILwY36lOVpPeCQ5UDAhFbxfSOElyhxf6ZPGjP8ANyAizTskAy4aJzq9W33i4aFdquVDrxkYu8zVGZkS8Z0hBUrtfy8CjAHzitI2aTbjL8aiICBAoGBAJ3t3nMU+4N94eSXxAuU1pgu67m7ditT+lkoOVoK7ntWPPq51ZmhFczP80uUFSiHWkAESqVdMaowePUjFAdmgsZX588LFcEr/0VP7xfDaCTeh6EdPQ8OhbMlc7eTLlwzyChNNFVrXhUsSvxuiwFycDIMxEsT2daUqqsebAiquv8fAoGBAKLKezItXDkBkjXa1WN9IF/5ClducVRlepY3/HHTSzJGV4A16rI6aY95RAaI91eO0hoiL3XnfPSFSpcX92ndsUsBxUzVD+q2k/iUnBsF2TpzFqt51M5PPERis/kv18Su3KG/6wdY20nQMUfvfCtfGvBO805xZtOSGwS+2YPUvwfk";


    private CryptoUtil() {
        // TODO：私有构造函数，防止实例化
    }

    public static PublicKey getPublicKey() throws Exception {
        return getPublicKeyFromString(PUBLIC_KEY);
    }

    // TODO：生成AES密钥
    public static SecretKey generateAESKey(int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keySize);
        return keyGen.generateKey();
    }

    // TODO：AES加密
    public static String aesEncrypt(String data, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // TODO：AES解密
    public static String aesDecrypt(String encryptedData, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decoded = Base64.getDecoder().decode(encryptedData);
        return new String(cipher.doFinal(decoded));
    }


    // TODO：用RSA公钥加密AES密钥
    public static String rsaEncryptAESKey(SecretKey aesKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = cipher.doFinal(aesKey.getEncoded());
        return Base64.getEncoder().encodeToString(encryptedKey);
    }

    // TODO：用RSA私钥解密AES密钥
    public static SecretKey rsaDecryptAESKey(String encryptedKey, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedKey = Base64.getDecoder().decode(encryptedKey);
        byte[] aesKeyBytes = cipher.doFinal(decodedKey);
        return new SecretKeySpec(aesKeyBytes, "AES");
    }

    // TODO：公钥字符串转 PublicKey
    public static PublicKey getPublicKeyFromString(String key) throws Exception {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
        java.security.spec.X509EncodedKeySpec spec = new java.security.spec.X509EncodedKeySpec(keyBytes);
        java.security.KeyFactory kf = java.security.KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    // TODO：私钥字符串转 PrivateKey
    public static PrivateKey getPrivateKeyFromString(String key) throws Exception {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
        java.security.spec.PKCS8EncodedKeySpec spec = new java.security.spec.PKCS8EncodedKeySpec(keyBytes);
        java.security.KeyFactory kf = java.security.KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

}
