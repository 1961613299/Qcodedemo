// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   DESede.java

package com.example.encry;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class DESUtil
{

    public static byte[] encrypt3(String data, String keyStr)
        throws Exception
    {
    	byte[] keyByte = Utils.ASCII_To_BCD(keyStr.getBytes());
        byte input[] = Utils.ASCII_To_BCD(data.getBytes());
        byte keyBytes[] = new byte[24];
        System.arraycopy(keyByte, 0, keyBytes, 0, 16);
        System.arraycopy(keyByte, 0, keyBytes, 16, 8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/NOPADDING");
        cipher.init(1, key);
        byte cipherText[] = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        return cipherText;
    }

    public static byte[] decrypt3(String data, String keyStr)
        throws Exception
    {
    	byte[] keyByte = Utils.ASCII_To_BCD(keyStr.getBytes());
        byte input[] = Utils.ASCII_To_BCD(data.getBytes());
        byte keyBytes[] = new byte[24];
        System.arraycopy(keyByte, 0, keyBytes, 0, 16);
        System.arraycopy(keyByte, 0, keyBytes, 16, 8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/NOPADDING");
        cipher.init(2, key);
        byte cipherText[] = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        return cipherText;
    }

    public static byte[] encrypt(String data, String keyStr)
        throws Exception
    {
        byte input[] = Utils.ASCII_To_BCD(data.getBytes());
        byte keyBytes[] = new byte[8];
        byte[] key8 = Utils.ASCII_To_BCD(keyStr.getBytes());
        System.arraycopy(key8, 0, keyBytes, 0, 8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NOPADDING");
        cipher.init(1, key);
        byte cipherText[] = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        return cipherText;
    }

    public static byte[] decrypt(String data, String keyStr)
        throws Exception
    {
        byte input[] = Utils.ASCII_To_BCD(data.getBytes());
        byte keyBytes[] = new byte[8];
        byte[] key8 = Utils.ASCII_To_BCD(keyStr.getBytes());
        System.arraycopy(key8, 0, keyBytes, 0, 8);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NOPADDING");
        cipher.init(2, key);
        byte cipherText[] = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        return cipherText;
    }
    public static String mac(String macStr,String key){
		
		
		System.out.println("macStr"+macStr);
		String initKey= "2";
		return mac(macStr,key,initKey);
	} 
 
    public static String mac(String macStr,String key,String keyIndex){
		String macByte = "";
		try {
			macByte = Utils.GenXorData(macStr.getBytes("GBK"),0);
		} catch (UnsupportedEncodingException e1) {
			
			e1.printStackTrace();
		}
		String macAsc=Utils.bcd2Str(macByte.getBytes());
		//加密
		try {
			
			
			String initKey= "22222222222222222222222222222222";
			//解析密钥明文
			String keyde = Utils.bcd2Str(DESUtil.decrypt3(key, initKey));
			byte[] leftByte=DESUtil.encrypt3(macAsc.substring(0,16), keyde);
			byte[] macByteAll=new byte[16];
			System.arraycopy(leftByte, 0, macByteAll, 0, 8);
			System.arraycopy(Utils.string2Bytes(macAsc.substring(16,32)), 0, macByteAll, 8, 8);
			
			String temp=Utils.GenXorData(macByteAll,0);
			byte[] mac=DESUtil.encrypt3(temp, keyde);
			return Utils.bcd2Str(Utils.bcd2Str(mac).getBytes()).substring(0,8);
		} catch (Exception e) {
					
			e.printStackTrace();
			return "";
		}
	}   
 
}
