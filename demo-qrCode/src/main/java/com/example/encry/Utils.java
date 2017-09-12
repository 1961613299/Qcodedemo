package com.example.encry;

import sun.misc.BASE64Decoder;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

	public static String getBASE64(byte[] s) { 
		if (s == null) return null; 
		return (new sun.misc.BASE64Encoder()).encode(s); 
		} 
		 
		// 将 BASE64 编码的字符串 s 进行解码 
	public static byte[] getFromBASE64(String s) { 
		if (s == null) return null; 
			BASE64Decoder decoder = new BASE64Decoder(); 
		try { 
			byte[] b = decoder.decodeBuffer(s); 
			return b; 
		} catch (Exception e) { 
			return null; 
		} 
	}


	public static String makeMac(String json,String macKey){
		Map<String, String> contentData = Tools.parserToMap(json);
		String macStr="";
		Object[] key_arr = contentData.keySet().toArray();
		Arrays.sort(key_arr);
		for (Object key : key_arr) {
			Object value = contentData.get(key);
			if (value != null ) {
				if (!key.equals("mac") ) {
					macStr+= value.toString();
				}
			}
		}
		String rMac = DESUtil.mac(macStr,macKey);
		return rMac;
	}
	private static byte asc_to_bcd(byte asc) {
		byte bcd;

		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	private static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static byte[] ASCII_To_BCD(byte[] ascii) {
		int asc_len = ascii.length;
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++) {
			bcd[i] = asc_to_bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	public static String bcd2Str(byte[] bytes) {
		char temp[] = new char[bytes.length * 2], val;

		for (int i = 0; i < bytes.length; i++) {
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}
	
	public static String bytes2Str(byte[] baSrc, int iStart, int length) {
		if ((iStart + length) > baSrc.length)
			return null;

		String sRtn = "";
		for (int i = 0; i < length; i++)
			sRtn += baSrc[iStart + i];

		return sRtn;
	}
	public static HashMap<String, String> getFields(String data) {

		HashMap<String, String> map = new HashMap<String, String>();
		try {
			if (data.length() < 6) {
				return map;
			}
			int total_Len = 0;
			String tlv_tag = data.substring(0, 2);
			if ("9F".equalsIgnoreCase(tlv_tag) || "5F".equalsIgnoreCase(tlv_tag)
					|| "BF".equalsIgnoreCase(tlv_tag) || "DF".equalsIgnoreCase(tlv_tag)) {
				tlv_tag = data.substring(0, 4);
			}
			total_Len += tlv_tag.length();
			// 标签后一个字节内容
			String s = data.substring(tlv_tag.length(), tlv_tag.length() + 2);
			String tlv_l;
			// s 与 0x80 进行与运算，判断第一个比特位是否为 1
			int first_bit = Integer.parseInt(s, 16) & 0x80;
			if (first_bit == 0) {
				// 说明第一个比特位是0，则s即为长度
				tlv_l = s;
				total_Len += s.length();
			} else {
				// 否则s后7个比特位的值为长度值L所占几个字节
				// s 与 0x7f进行与运算，计算s后7个比特位的值
				int i = Integer.parseInt(s, 16) & 0x7F;
				tlv_l = data.substring(tlv_tag.length() + s.length(),
						tlv_tag.length() + s.length() + i * 2);
				total_Len += 2 + tlv_l.length();
			}
			int tlv_len = 0;
			tlv_len = Integer.parseInt(tlv_l, 16)*2;
			String tlv_value;
			if (first_bit == 0) {
				tlv_value = data.substring(tlv_tag.length() + 2, tlv_tag.length()
						+ 2 + tlv_len);
			} else {
				tlv_value = data.substring(tlv_tag.length() + 2 + tlv_l.length(),
						tlv_tag.length() + 2 + tlv_l.length() + tlv_len);
			}
			total_Len += tlv_value.length();
			map.put(tlv_tag, tlv_value);

			// 用tlv_tag的第一个字节与0x20进行与运算，判断第三个比特位是否为1
			int third_bit = Integer.parseInt(tlv_tag.substring(0, 2), 16) & 0x20;
			if (third_bit == 0) {
				// 说明tlv_tag第一个字节的第三个比特位是0。也就说明tlv_value为基本数据对象
				if (total_Len == data.length()) {

				} else {
					data = data.substring(total_Len, data.length());
					HashMap<String, String> hashMap = getFields(data);
					map.putAll(hashMap);
				}
			} else {
				// 说明tlv_tag第一个字节的第三个比特位是1。也就说明tlv_value为结构数据对象
				HashMap<String, String> hashMap = getFields(tlv_value);
				map.putAll(hashMap);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	//取指定标签的数据
	public static ArrayList<String> getFieldsList(String data,String tag) {

		ArrayList<String> list = new ArrayList<String>();
		int total_Len = 0;
		String tlv_tag = data.substring(0, 2);
		if ("9F".equals(tlv_tag) || "5F".equals(tlv_tag)
				|| "BF".equals(tlv_tag) || "DF".equals(tlv_tag)) {
			tlv_tag = data.substring(0, 4);
		}
		total_Len += tlv_tag.length();
		// 标签后一个字节内容
		String s = data.substring(tlv_tag.length(), tlv_tag.length() + 2);
		String tlv_l;
		// s 与 0x80 进行与运算，判断第一个比特位是否为 1
		int first_bit = Integer.parseInt(s, 16) & 0x80;
		if (first_bit == 0) {
			// 说明第一个比特位是0，则s即为长度
			tlv_l = s;
			total_Len += s.length();
		} else {
			// 否则s后7个比特位的值为长度值L所占几个字节
			// s 与 0x7f进行与运算，计算s后7个比特位的值
			int i = Integer.parseInt(s, 16) & 0x7F;
			tlv_l = data.substring(tlv_tag.length() + s.length(),
					tlv_tag.length() + s.length() + i * 2);
			total_Len += 2 + tlv_l.length();
		}
		int tlv_len = 0;
		tlv_len = Integer.parseInt(tlv_l, 16)*2;
		String tlv_value;
		if (first_bit == 0) {
			tlv_value = data.substring(tlv_tag.length() + 2, tlv_tag.length()
					+ 2 + tlv_len);
		} else {
			tlv_value = data.substring(tlv_tag.length() + 2 + tlv_l.length(),
					tlv_tag.length() + 2 + tlv_l.length() + tlv_len);
		}
		total_Len += tlv_value.length();
		if (tlv_tag.equals(tag)) {
			list.add(tlv_value);
		}

		// 用tlv_tag的第一个字节与0x20进行与运算，判断第三个比特位是否为1
		int third_bit = Integer.parseInt(tlv_tag.substring(0, 2), 16) & 0x20;
		if (third_bit == 0) {
			// 说明tlv_tag第一个字节的第三个比特位是0。也就说明tlv_value为基本数据对象
			if (total_Len == data.length() || total_Len == data.length() - 6) {

			} else {
				data = data.substring(total_Len, data.length());
				ArrayList<String> arrayList = getFieldsList(data,tag);
				list.addAll(arrayList);
			}
		} else {
			// 说明tlv_tag第一个字节的第三个比特位是1。也就说明tlv_value为结构数据对象
			ArrayList<String> arrayList = getFieldsList(tlv_value,tag);
			list.addAll(arrayList);
		}
		return list;
	}
	
	public static String[] getOrders(String data) {
		// 18 01 04 01
		String s = "00B2";
		String str1 = data.substring(0, 2);
		String str2 = data.substring(2, 4);
		String str3 = data.substring(4, 6);
		String hex = Integer.toHexString(Integer.parseInt(str1, 16) ^ 4);
		hex = get2HexString(hex);
		int len = Integer.parseInt(str3, 16) - Integer.parseInt(str2, 16);
		String[] orders = new String[len + 1];
		for (int i = 0; i < len + 1; i++) {
			String strLen = Integer.toHexString(Integer.parseInt(str2, 16) + i);
			orders[i] = s + get2HexString(strLen) + hex + "00";
		}
		return orders;
	}
	
	public static ArrayList<String> jieXi(byte[] bytes, boolean b)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		String s = "";
		ArrayList<String> fields = new ArrayList<String>();
		int len = 0;
		int offSet = 46;
		byte[] b2;
		for (int i = 0; i < 4; i++) {
			len = Integer.parseInt(bytes[offSet + 1] + "");
			b2 = new byte[len];
			offSet += 2;
			System.arraycopy(bytes, offSet, b2, 0, len);
			offSet += len;
			s = new String(b2, "gbk");
			fields.add(s);
		}
		if (b) { // 得到充值码
			b2 = new byte[32];
			System.arraycopy(bytes, offSet, b2, 0, 32);
			s = new String(b2, "gbk");
			fields.add(s);
		} else { // 得到应用终端号
			b2 = new byte[23];
			System.arraycopy(bytes, offSet, b2, 0, 23);
			s = new String(b2, "gbk");
			fields.add(s);
		}

		return fields;
	}

	// 判断输入的金额是否合法
	public static boolean isMoneyLegal(String amount) {

		if (amount.startsWith(".") || amount.endsWith(".")
				|| amount.trim().equals("")) {
			return false;
		} else if (amount.contains(".")) {
			if (amount.split("\\.").length > 2) {
				return false;
			}
		} else if (amount.startsWith("0") && !amount.startsWith("0.")) {
			return false;
		}
		return true;
	}


	// 保留小数点后两位
	public static String get2PointNum(double d) {
		DecimalFormat format = new DecimalFormat("0.00");
		return format.format(d);
	}

	public static String get2PointNum(String s) {
		DecimalFormat format = new DecimalFormat("0.00");
		double d = Double.parseDouble(s) / 100;
		return format.format(d);
	}

	// 12位金额
	public static String get12Money(double d) {
		DecimalFormat format = new DecimalFormat("000000000000");
		return format.format(d);
	}

	public static String get12Money(String money) {
		double d = Double.parseDouble(money);
		return get12Money(d);
	}

	// 10位金额
	public static String get10Money(double d) {
		DecimalFormat format = new DecimalFormat("0000000000");
		return format.format(d);
	}

	public static String get10Money(String money) {
		double d = Double.parseDouble(money);
		return get10Money(d);
	}

	public static String getShowCardNum(String cardNum) {

		String num = "";
		for (int i = 0; i < cardNum.length(); i++) {
			if (i * 4 + 4 > cardNum.length()) {
				num += cardNum.substring(i * 4, cardNum.length());
				break;
			}
			num += cardNum.substring(i * 4, i * 4 + 4) + "-";
		}
		if (num.endsWith("-")) {
			num = num.substring(0, num.length() - 1);
		}
		return num;
	}
	public static String getShowCardNum2(String cardNum) {
		
		String num = "";
		for (int i = 0; i < cardNum.length(); i++) {
			if (i * 4 + 4 > cardNum.length()) {
				num += cardNum.substring(i * 4, cardNum.length());
				break;
			}
			num += cardNum.substring(i * 4, i * 4 + 4) + "  ";
		}
		if (num.endsWith("  ")) {
			num = num.substring(0, num.length() - 1);
		}
		return num;
	}

	// 生成16位订单号
	public static String generateBillID(String num) {
		// ddHHmmss
		SimpleDateFormat format = new SimpleDateFormat("ddHHmmss");
		String s1 = format.format(new Date());
		// num后4位
		if (num.length() < 4) {
			int len = num.length();
			for (int i = 0; i < 4 - len; i++) {
				num += "0";
			}
		} else {
			num = num.substring(num.length() - 4);
		}
		// 4位随机数
		DecimalFormat format1 = new DecimalFormat("0000");
		double d = Math.random() * 9000 + 1000;
		String s2 = format1.format(d);
		return s1 + num + s2;
	}

	// 生成流水号
	public static String generateSerNo() {
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		String serNo = format.format(new Date());
		return serNo;
	}

	public static byte[] getResponseCmd(byte len) {
		byte[] b = new byte[5];
		b[0] = 0x00;
		b[1] = (byte) 0xC0;
		b[2] = 0x00;
		b[3] = 0x00;
		b[4] = len;
		return b;
	}

	public static byte[] getResponseData(byte[] data) {
		int len = data.length;
		byte[] b = new byte[len - 2];
		System.arraycopy(data, 0, b, 0, len - 2);
		return b;
	}

	public static String get2HexString(String hex) {
		int len = hex.length();
		if (len == 2) {
			return hex;
		} else if (len == 1) {
			return "0" + hex;
		} else {
			return hex.substring(len - 2, len);
		}
	}
	
	public static String get4HexString(String hex) {
		int len = hex.length();
		for(int i=len;i<4;i++){
			hex = "0"+hex;
		}
		return hex;
	}

	public static byte[] string2Bytes(String str) {
		byte[] b = new byte[str.length() / 2];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}
	
	public static String  GenXorData(byte[] bBuf,int iStart)
	{
		
		int i = 0 ;
		int nLen = 0 ;
		int nDataLen =  0;
		int nXorDataLen = 0 ;
		byte[] s1 = new byte[8] ;
		byte[] s2 = new byte[8] ;
		byte[] buf = bBuf;
		nDataLen= buf.length;
		nLen = 8 - (nDataLen%8);
		nLen = (nLen == 8) ? 0 : nLen ;
		nXorDataLen = (nDataLen+nLen) ;	//不足8的倍数，用0x00补齐。
		byte[] pBuf = new byte[nXorDataLen];
		
		System.arraycopy(buf, 0, pBuf, 0, nDataLen) ;
		System.arraycopy(pBuf, 0, s1, 0, 8) ;
		for(i = 8; i<nXorDataLen; i+=8)
		{			
			System.arraycopy(pBuf, i, s2, 0, 8) ;
			s1=setxor(s1, s2);
		}
		return Utils.bcd2Str(s1);
	}
	public static byte[] setxor(byte[] b1, byte[] b2) {

		byte[] snbyte = new byte[b1.length];
		for (int i = 0, j = 0; i < b1.length; i++, j++) {
			snbyte[i] = (byte) (b1[i] ^ b2[j]);
		}
		return snbyte;
	}
	
	public static String fromatAmount(String amount, int length) {
			Long amountLong=Long.parseLong(amount);
			return String.format("%0"+length+"d", amountLong);
		
	}
	
	public static Long fromatAmountToLong(String amount) {
		return Long.parseLong(amount);
	}
	public static Map<String, String> paraseTerminalInfo(String terminalInfo){
		Map<String, String> mapT=new HashMap<String, String>();
		
		String[] temp1=null;
		if(terminalInfo.lastIndexOf('|')>0){
			temp1=terminalInfo.split("\\|");
		}else{
			temp1= new String[1];
			temp1[0]=terminalInfo;
		}	
		for(int i=0;i<temp1.length;i++){
			String[] temp2=temp1[i].split("=");
			mapT.put(temp2[0], temp2[1]);
		}
		return mapT;
	}
}
