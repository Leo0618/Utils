package com.leo618.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


/**
 * function : 签名算法摘要工具类.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SignUtil {

    /**
     * MD5
     */
    public static String md5(String origin) {
        return md5(origin, "UTF-8");
    }

    /**
     * MD5
     *
     * @param str         加密字符串
     * @param charsetname 字符集
     * @return 加密后的字符串, 异常则返回null
     */
    public static String md5(String str, String charsetname) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes(charsetname));
            byte[] byteDigest = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            for (byte aByteDigest : byteDigest) {
                i = aByteDigest;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 摘要文件的MD5值
     *
     * @param file 需要被加密的文件
     * @return 返回加密后的MD5密文摘要字符串
     */
    public static String md5File(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            byte[] result = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                String str = Integer.toHexString(b & 0xff);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            fis.close();
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /** http请求的参数进行URL编码 */
    public static String URLEncode(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return "";
    }

    /** 将GET请求的url参数进行批量URL编码 */
    public static String URLEncodeRequestAllParamValue(String url) {
        if (url == null || url.length() == 0 || !url.startsWith("http")) {
            return url;
        }
        String[] hostAndParamArr = url.split("\\?");
        if (hostAndParamArr.length != 2) {
            throw new RuntimeException("url params included char '?' ");
        }
        String result = hostAndParamArr[0] + "?";
        String[] paramPairArr = hostAndParamArr[1].split("&");
        for (int x = 0; x < paramPairArr.length; x++) {
            String[] paramKeyValueArr = paramPairArr[x].split("=");
            if (paramKeyValueArr.length != 2) {
                throw new RuntimeException("url param one key must have one value! ");
            }
            String urlEncodedValue = URLEncode(paramKeyValueArr[1]);
            result += (paramKeyValueArr[0] + "=" + urlEncodedValue + (x == paramPairArr.length - 1 ? "" : "&"));
        }
        return result;
    }

    /**
     * 使用公钥进行RSA加密
     */
    @SuppressLint("GetInstance")
    public static String encryptByRSAWithPublic(String rsa_publice, String content) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(rsa_publice, Base64.DEFAULT));
            PublicKey pubkey = keyFactory.generatePublic(x509EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubkey);
            byte[] output = cipher.doFinal(content.getBytes("UTF-8"));
            return new String(Base64.encode(output, Base64.DEFAULT));
        } catch (Exception e) {
            return content;
        }
    }

}