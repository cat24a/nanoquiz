package nanoquiz.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
	public static void readBytesOrWait(InputStream is, byte[] b, int off, int len) throws EOFException, IOException {
        int bytesRead = 0;
        while(bytesRead < len) {
            int count = is.read(b, off + bytesRead,  off + len - bytesRead);
            if(count == -1) {
                throw new EOFException("end of stream");
            }
            bytesRead += count;
        }
    }

    public static byte[] readBytesOrWait(InputStream is, int len) throws EOFException, IOException {
        byte[] b = new byte[len];
        int bytesRead = 0;
        while(bytesRead < len) {
            int count = is.read(b, bytesRead,  len - bytesRead);
            if(count == -1) {
                throw new EOFException("end of stream");
            }
            bytesRead += count;
        }
        return b;
    }

	public static int byteArrayToInt(byte[] bytes) {
        // Initialize result as 0
        int result = 0;

        // Convert bytes to int (little endian order)
        for(byte i = 0; i < Math.min(bytes.length, 4); i++) {
            result |= (bytes[i] & 0xFF) << (8 * i);
        }

        return result;
    }

    public static byte[] intToByteArray(int value) {
        byte[] bytes = new byte[4]; // Allocate a byte array of 4 bytes

        // Convert lint to bytes (little endian order)
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >> 24) & 0xFF);

        return bytes;
    }

	public static long byteArrayToLong(byte[] bytes) {
        // Initialize result as 0
        long result = 0;

        // Convert bytes to long (little endian order)
        for(byte i = 0; i < Math.min(bytes.length, 8); i++) {
            result |= (bytes[i] & 0xFFL) << (8 * i);
        }

        return result;
    }

    public static byte[] longToByteArray(long value) {
        byte[] bytes = new byte[8]; // Allocate a byte array of 8 bytes

        // Convert long to bytes (little endian order)
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >> 24) & 0xFF);
        bytes[4] = (byte) ((value >> 32) & 0xFF);
        bytes[5] = (byte) ((value >> 40) & 0xFF);
        bytes[6] = (byte) ((value >> 48) & 0xFF);
        bytes[7] = (byte) ((value >> 56) & 0xFF);

        return bytes;
    }
}
