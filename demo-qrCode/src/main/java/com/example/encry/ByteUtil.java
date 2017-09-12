package com.example.encry;


/**
 * @author kui
 */
public class ByteUtil {

    public static byte[] int2byte(int i) {
        int h = (i >>> 8) & 0xff;
        int l = i & 0xff;
        return new byte[] {(byte) (h + Byte.MIN_VALUE), (byte) (l + Byte.MIN_VALUE)};
    }

    public static int byte2int(byte[] array) {
        int h = array[0] - Byte.MIN_VALUE;
        int l = array[1] - Byte.MIN_VALUE;
        return (h << 8) + l;
    }
}
