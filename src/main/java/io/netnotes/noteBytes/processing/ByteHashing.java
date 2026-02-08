package io.netnotes.noteBytes.processing;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;



public class ByteHashing {


    public int integersToSignedHashCode(int[] integers, int signum) {
      int hashCode = 0;

      for(int i = 0; i < integers.length; ++i) {
         hashCode = (int)((long)(31 * hashCode) + ((long)integers[i] & 4294967295L));
      }

      return hashCode * signum;
   }

   public int integersToHashCode(int[] integers) {
      int hashCode = 0;

      for(int i = 0; i < integers.length; ++i) {
         hashCode = (int)((long)(31 * hashCode) + ((long)integers[i] & 4294967295L));
      }

      return hashCode;
   }


    public static long[] makeLongArrayEven(long[] longs){
        int longsLen = longs.length;
        boolean isLongsMod2Zero  = longsLen % 2 == 0;
        long[] l = isLongsMod2Zero ? longs : new long[longs.length + 1];
        
        int startPoint = l.length == longs.length ? longs.length : 0; 

        for(int i = startPoint; i < longsLen ; i++){
            l[i] = longs[i];
        }
        return l;
    }

    public static int[] longsToIntegersHash(long[] longs){
        longs = makeLongArrayEven(longs);

        int[] ints = new int[longs.length / 2];
        int j = 0;
        for(int i = 0 ; i < ints.length; i++){
            long long1 = longs[j];
            j++;
            long long2 = longs[j];
            j++;
            long hilo = long1 ^ long2;
        
            ints[i] = (int)(hilo >> 32) ^ (int)hilo;
        }
        
        return ints;
    }

    public static long[] bytesToLongs(byte[] bytes){
        int byteLen = bytes.length;
        int longLen = (int) Math.ceil(byteLen / 8);
        long[] longs = new long[longLen];
        int j = 0;
        int k = 0;
        for(int i = 0; i < bytes.length ; i++){
            longs[j] =  longs[j] << 8 | (long)( bytes[i] & 255);
            boolean isAdvance = (k + 1 ==8);
            k = isAdvance ? 0 : k + 1;
        }
        return longs;
    }




    
    public static int[] UTF16BytesToInts(byte[] value, boolean islittleEndian) {
        int length = value.length >> 1;
        int[] ints = new int[length];

        for (int i = 0; i < length; i++) {
            ints[i] = (int) ByteDecoding.getCharUTF16(value, i, !islittleEndian);
            
        }
        return ints;
    }

  


   
    public int hashLongToInt(long l){
        return (int)(l ^ l >>> 32);
    }
  
    public static int getHashCode(byte[] bytes, byte type){
        if(bytes.length == 0){
            return Integer.MIN_VALUE;
        }
        
        int code = -1;
    
        switch(type){
            case NoteBytesMetaData.BYTE_TYPE:
                code = bytes[0];
            case NoteBytesMetaData.SHORT_TYPE:
                code =  ByteDecoding.bytesToShortBigEndian(bytes) << 32;
                break;
            case NoteBytesMetaData.SHORT_LE_TYPE:
                code =  ByteDecoding.bytesToShortLittleEndian(bytes) << 32;
                break;
            case NoteBytesMetaData.INTEGER_TYPE:
                code = ByteDecoding.bytesToIntBigEndian(bytes);
                break;
            case NoteBytesMetaData.INTEGER_LE_TYPE:
                code =  ByteDecoding.bytesToIntLittleEndian(bytes);
                break;
            case NoteBytesMetaData.LONG_TYPE:
                code = Long.hashCode(ByteDecoding.bytesToLongBigEndian(bytes));
                break;
            case NoteBytesMetaData.LONG_LE_TYPE:
                code = Long.hashCode(ByteDecoding.bytesToLongLittleEndian(bytes));
                break;
            case NoteBytesMetaData.DOUBLE_TYPE:
                code = Double.hashCode(ByteDecoding.bytesToDoubleBigEndian(bytes));
                break;
             case NoteBytesMetaData.DOUBLE_LE_TYPE:
                code = Double.hashCode( ByteDecoding.bytesToDoubleLittleEndian(bytes));
                break;
            case NoteBytesMetaData.FLOAT_TYPE:
                code = Float.hashCode(ByteDecoding.bytesToFloatBigEndian(bytes));
                break;
             case NoteBytesMetaData.FLOAT_LE_TYPE:
                code = Float.hashCode(ByteDecoding.bytesToFloatLittleEndian(bytes));
                break;
            case NoteBytesMetaData.BIG_INTEGER_TYPE:
                code =  bytes.length < 1 ? -1 : new BigInteger(bytes).hashCode();
                break;
            case NoteBytesMetaData.BIG_DECIMAL_TYPE:
                code = bytes.length < 5 ? -1 : new BigDecimal(new BigInteger(bytes, 4, bytes.length-4), ByteDecoding.bytesToIntBigEndian(bytes, 0)).hashCode();
                break;
            case NoteBytesMetaData.STRING_TYPE:
                code = utf8HashCode(bytes);
                break;
            case NoteBytesMetaData.STRING_US_ASCII_TYPE:
            case NoteBytesMetaData.STRING_ISO_8859_1_TYPE:
                code = asciiHashCode(bytes);
                break;
            case NoteBytesMetaData.STRING_UTF16_TYPE:
                code = utf16HashCode(bytes);
                break;
            case NoteBytesMetaData.STRING_UTF16_LE_TYPE:
                code = utf16LEHashCode(bytes);
                break;
            default:
                code = Arrays.hashCode(bytes);
              
        }
        

        return code;
    }

    public static int utf16LEHashCode(byte[] bytes) {
        int h = 0;
        for (int i = 0; i + 1 < bytes.length; i += 2) {
            int ch = ((bytes[i + 1] & 0xFF) << 8) | (bytes[i] & 0xFF);
            h = 31 * h + ch;
        }
        return h;
    }

    public static int utf16HashCode(byte[] bytes) {
        int h = 0;
        int i = 0;

        // BOM detection
        boolean le = false;
        if (bytes.length >= 2) {
            int b0 = bytes[0] & 0xFF;
            int b1 = bytes[1] & 0xFF;
            if (b0 == 0xFE && b1 == 0xFF) {
                i = 2;
            } else if (b0 == 0xFF && b1 == 0xFE) {
                le = true;
                i = 2;
            }
        }

        for (; i + 1 < bytes.length; i += 2) {
            int ch = le
                ? ((bytes[i + 1] & 0xFF) << 8) | (bytes[i] & 0xFF)
                : ((bytes[i] & 0xFF) << 8) | (bytes[i + 1] & 0xFF);

            h = 31 * h + ch;
        }
        return h;
    }


    public static int asciiHashCode(byte[] bytes) {
        int h = 0;
        for (byte b : bytes) {
            h = 31 * h + (b & 0xFF);
        }
        return h;
    }

    public static int utf8HashCode(byte[] bytes) {
        int h = 0;
        int i = 0;

        while (i < bytes.length) {
            int b = bytes[i++] & 0xFF;
            int cp;

            if (b < 0x80) {
                cp = b;
            } else if (b < 0xE0) {
                cp = ((b & 0x1F) << 6) | (bytes[i++] & 0x3F);
            } else if (b < 0xF0) {
                cp = ((b & 0x0F) << 12)
                | ((bytes[i++] & 0x3F) << 6)
                |  (bytes[i++] & 0x3F);
            } else {
                cp = ((b & 0x07) << 18)
                | ((bytes[i++] & 0x3F) << 12)
                | ((bytes[i++] & 0x3F) << 6)
                |  (bytes[i++] & 0x3F);
            }

            if (cp <= 0xFFFF) {
                h = 31 * h + cp;
            } else {
                // surrogate pair
                cp -= 0x10000;
                h = 31 * h + (0xD800 | (cp >>> 10));
                h = 31 * h + (0xDC00 | (cp & 0x3FF));
            }
        }
        return h;
    }


 
    
    public static int hashCodeUTF16(byte[] value, boolean islittleEndian) {
        int h = 0;
        int length = value.length >> 1;
        for (int i = 0; i < length; i++) {
            char c =  ByteDecoding.getCharUTF16(value, i, !islittleEndian);
            h = 31 * h + c;
        }
        return h;
    }





    public static long[] hashBytes16ToMsbLsb(byte[] data){

        long msb = 0L;
        long lsb = 0L;
        int i;
        for(i = 0; i < 8; ++i) {
            msb = msb << 8 | (long)(data[i] & 255);
        }

        for(i = 8; i < 16; ++i) {
            lsb = lsb << 8 | (long)(data[i] & 255);
        }

        return new long[] {msb, lsb};

    }

    public static long bytesToLong(byte[] bytes){
        long l = 0;
        for(int i = 0; i < 8; ++i) {
            l = l << 8 | (long)(bytes[i] & 255);
        }

        return l;
    }

    public static long[] hashBytes32ToMsbLsb(byte[] bytes){

        int i;
        long msb = 0L;
        for(i = 0; i < 8; ++i) {
            msb = msb << 8 | (long)(bytes[i] & 255);
        }

        long msb1 = 0L;
        for(i = 16; i < 24; ++i) {
            msb1 = msb1 << 8 | (long)(bytes[i] & 255);
        }

        long lsb = 0L;
        for(i = 8; i < 16; ++i) {
            lsb = lsb << 8 | (long)(bytes[i] & 255);
        }

        long lsb1 = 0L;
        for(i = 24; i < 32; ++i) {
            lsb1 = lsb1 << 8 | (long)(bytes[i] & 255);
        }


        return new long[]{msb,msb1, lsb, lsb1};
        
    }
}
