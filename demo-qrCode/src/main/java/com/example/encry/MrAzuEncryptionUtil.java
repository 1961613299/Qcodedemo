package com.example.encry;


import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author Zerml
 * @date 2016年12月13日下午3:10:17
 * @description 加密工具类
 */
public class MrAzuEncryptionUtil {
	
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	
	/**
	 * @author Zerml
	 * @date 2016年12月13日下午3:14:53
	 * @description Base64常规编码
	 */
	public static String encodeBase64(String s) {
		if (s == null) return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	/**
	 * @author Zerml
	 * @date 2016年12月13日下午3:15:07
	 * @description Base64常规解码
	 */
	public static String decodeBase64(String s) {
		if (s == null) return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
	
    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws InvalidAlgorithmParameterException 
     * @throws Exception 
     */
    public static String encodeDes(String key,String data) {
    	if(data == null)
    		return null;
    	try{
	    	DESKeySpec dks = new DESKeySpec(key.getBytes());	    	
	    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
	        //key的长度不能够小于8位字节
	        Key secretKey = keyFactory.generateSecret(dks);
	        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
	        IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
	        AlgorithmParameterSpec paramSpec = iv;
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);           
	        byte[] bytes = cipher.doFinal(data.getBytes());            
	        return byte2hex(bytes);
    	}catch(Exception e){
    		e.printStackTrace();
    		return data;
    	}
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static String decodeDes(String key,String data) {
    	if(data == null)
    		return null;
        try {
	    	DESKeySpec dks = new DESKeySpec(key.getBytes());
	    	SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            //key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e){
    		e.printStackTrace();
    		return data;
        }
    }
    
    /**
     * 对字符串md5加密
     *
     * @param str
     * @return 32位小写
     */
    public static String encodeMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
    }
    
    /**
     * 对字符串md5加密
     *
     * @param str
     * @return 32位大写
     */
    public final static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = s.getBytes(Charset.forName("UTF-8"));
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

	/**
	 * 二行制转字符串
	 * @param b
	 * @return
	 */
    private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b!=null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}
    
    private static byte[] hex2byte(byte[] b) {
        if((b.length%2)!=0)
            throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length/2];
		for (int n = 0; n < b.length; n+=2) {
		    String item = new String(b,n,2);
		    b2[n/2] = (byte)Integer.parseInt(item,16);
		}
        return b2;
    }
    
    /**
     * @author Zerml
     * @date 2016年12月15日下午1:55:31
     * @description 接口默认加密
     */
    public static boolean validationOpenApi() throws Exception{
    	boolean b = false;
    	return b;
    }
    
    private static final String KEY = "201612153DSMRAZUHHLJYHMYM5V1";
    /**
     * @author Zerml
     * @date 2016年12月15日下午5:35:58
     * @description 默认数据三重加密
     */
    public static String getThreeEncry(String str) throws UnsupportedEncodingException{
    	StringBuffer sb = new StringBuffer();
    	sb.append(URLEncoder.encode(encodeBase64(encodeDes(KEY, str)), "UTF-8"));
    	return sb.toString();
    }
    /**
     * @author Zerml
     * @date 2016年12月15日下午5:36:20
     * @description 默认数据三重解密
     */
    public static String getDeThreeEncry(String key, String str) throws UnsupportedEncodingException{
    	StringBuffer sb = new StringBuffer();
    	sb.append(decodeDes(key, decodeBase64(URLDecoder.decode(str, "UTF-8"))));
    	return sb.toString();
    }
	public static String getDeThreeEncry( String str) throws UnsupportedEncodingException{
		StringBuffer sb = new StringBuffer();
		sb.append(decodeDes(KEY, decodeBase64(URLDecoder.decode(str, "UTF-8"))));
		return sb.toString();
	}
    /**
     * @author Zerml
     * @date 2016年12月20日下午2:51:39
     * @description 接口专用提取Md5验证方式
     */
    public static boolean validateMd5Data(String data) throws Exception{
    	boolean b = false;
    	try {
    		/*JSONObject obj = JSONObject.fromObject(data);
    		String inMd5 = (String) obj.get("sep");
    		obj.discard("sep");
    		String outMd5 = encodeMD5(URLEncoder.encode(obj.toString(), "UTF-8"));
    		
    		if (inMd5.equals(outMd5)) {
				b = true;
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return b;
    }
	
	public static void main(String[] args) throws Exception {
		String str = "这是一段需要加密的数据";
		String key = "829438743752984395274357243";
		String b = encodeDes(key, str);//第一步
		String c = encodeBase64(b);//第二步
		String d = URLEncoder.encode(c, "UTF-8");//第三步
		System.out.println(d);//最终加密
		System.out.println(decodeDes(key, decodeBase64(URLDecoder.decode(d, "UTF-8"))));//最终解密
		System.out.println(encodeMD5(decodeDes(key, decodeBase64(URLDecoder.decode(d, "UTF-8")))));
		System.out.println(encodeMD5(str));//MD5
		//IP:port/demo/interface?data=d&sep=md5
		/*String a = "NzJDMDk5OEMwNTFCOUQ5MUEwQTJCQ0RDMUUzOEI3NjY5RkFDQ0YxNTkxQUFBMDM5MUU5QjhCNDhF%0D%0AQjgyNTY4QUM2QTM3REZFMDMzNTNBNUIyRTU4NjY2QzZFNUU4NDEwNDE0REU3OUVCMTM3OTc5MEU2%0D%0AODBFRDY0MTJFMEY3OURGNTQ4QUUyRDBFNzkwMjhCRjY5RDhDQ0ZGMzZGREZDRTI1RTY4QkNFQzIw%0D%0AQjA3NDRGMDE5MEYwMUIzNjFBQkFCQzkyRUM0ODVFQ0E4NTRGMThBRDBGNTFBN0ZDNDE2QjJEODRB%0D%0ARDRERTQ3RTc3NTkyNEE4NDAyQkEwMUQ5RjU3MUE3RDNFOTA1MkIyRDYyMTNGNDUyODdBMDNFNUI1%0D%0AN0JERTlEN0I2QjI3RDMxMEM3QTc2QjUwRTYwNEJGOEJBNEFCNjAyRjJBRTk0ODlDMUJDQkI2Rjk5%0D%0AODA1MTRGRjczQ0JFODdGMDE4RDA0RTYxQkM2NTA3Nzk5NTIzRjdCODk4NzJDM0VEMERDNEQxMTE3%0D%0AQzYxMjgxQTg2OENERkRGNTVCOEY2QTgxNUVBN0Y5NENEOEQ5Q0FDNkZGQ0NDNDM4MEFFOEFGODA0%0D%0AQkYyQTA0OEUyQTkxRTY3RDQ0NDAzQTkxOUVEMjNEMjM3RENGMDc4RDMzNzZBM0VGOEU4MjBBM0Q0%0D%0AQTA2RTRERTMxQTA2QkIxMTg1MTE4NERBRTE1RUY1QkI3MjU0MzIxMzAzQTczQzBGMEY4NTQwQ0Ix%0D%0ARkM2MkUwOEFFNUQ4OEUyMDk2QUNDQUQwRUI1OTA2RjBFM0RFMjdFMTQzQzg4NTUzRTYxMEY5RjBC%0D%0ARTZFRDEwRjhEMzY0QjFEQjQwODkzQUI5OTA2NzgyMjVBRTg1QkI3RERGNzZCNDkyQzZFRTlEQjIy%0D%0AMzNBNUUwMDQzQTFGMzM2NjAxMThDOTI2OEYxMzIyQUU4MTdEMjc3RDMzQzFBQ0ZFNDE0RTMyNDFE%0D%0ARTg1QTg3NEI4OTEyNjBFMkNFQTJDMkIzMTM0QzZEQTAzMzE5RURDRjUzOA%3D%3D";

		String b = getDeThreeEncry(a);
		System.out.println(b); //三重解码后
		String c = URLDecoder.decode(b, "UTF-8");
		System.out.println(c);//具体数据
		System.out.println("validate:"+validateMd5Data(c));
		JSONObject obj = JSONObject.fromObject(c);
		obj.discard("sep");
		System.out.println(obj.toString());//去掉md5后
		System.out.println(encodeMD5(URLEncoder.encode(obj.toString(), "UTF-8")));*///MD5加密后
		
		/*String a = "{'type':'insData','dataType':'wx','phone':'13381340569','name':'祖孟雷','identifier':'2301241998908310521','selNo':'6225880133994856','bankName':'招商银行','bankAddress':'北京市丰台区方庄支行','traderName':'钱宝','bankCode':''}";
		String b = URLEncoder.encode(a, "UTF-8");
		System.out.println(b);
		String c = encodeBase64(b);
		System.out.println(c);
		String key = PropertiesUtil.getProperty("param", "DESKey");
		String d = encodeDes(key, c);
		System.out.println(getThreeEncry(a));
		System.out.println(getDeThreeEncry(a));*/
	}
}
