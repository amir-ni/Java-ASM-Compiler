package Core.util;

public class Binary {

    /**
     * Translate int value into a String consisting of '1's and '0's.
     *
     * @param value  The int value to convert.
     * @param length The number of bit positions, starting at least significant, to process.
     * @return String consisting of '1' and '0' characters corresponding to the requested binary sequence.
     **/
    public static String intToBinaryString(int value, int length) {
        char[] result = new char[length];
        int index = length - 1;
        for (int i = 0; i < length; i++) {
            result[index] = (bitValue(value, i) == 1) ? '1' : '0';
            index--;
        }
        return new String(result);
    }

    /**
     * Returns the bit value of the given bit position of the given int value.
     *
     * @param value The value to read the bit from.
     * @param bit   bit position in range 0 (least significant) to 31 (most)
     * @return 0 if the bit position contains 0, and 1 otherwise.
     */
    public static int bitValue(int value, int bit) {
        return 1 & (value >> bit);
    }

    /**
     * Translate String consisting of '1's and '0's into an int value having that binary representation.
     * The String is assumed to be at most 32 characters long.  No error checking is performed.
     * String position 0 has most-significant bit, position length-1 has least-significant.
     *
     * @param value The String value to convert.
     * @return int whose binary value corresponds to decoded String.
     **/
    public static int binaryStringToInt(String value) {
        int result = value.charAt(0) - 48;
        for (int i = 1; i < value.length(); i++) {
            result = (result << 1) | (value.charAt(i) - 48);
        }
        return result;
    }

    /**
     * Translate String consisting of hexadecimal digits into String consisting of
     * corresponding binary digits ('1's and '0's).  No length limit.
     * String position 0 will have most-significant bit, position length-1 has least-significant.
     *
     * @param value String containing '0', '1', ...'f'
     *              characters which form hexadecimal.  Letters may be either upper or lower case.
     *              Works either with or without leading "Ox".
     * @return String with equivalent value in binary.
     **/
    public static String hexStringToBinaryString(String value) {
        String result = "";
        // slice off leading Ox or 0X
        if (value.indexOf("0x") == 0 || value.indexOf("0X") == 0) {
            value = value.substring(2);
        }
        for (int digs = 0; digs < value.length(); digs++) {
            switch (value.charAt(digs)) {
                case '0':
                    result += "0000";
                    break;
                case '1':
                    result += "0001";
                    break;
                case '2':
                    result += "0010";
                    break;
                case '3':
                    result += "0011";
                    break;
                case '4':
                    result += "0100";
                    break;
                case '5':
                    result += "0101";
                    break;
                case '6':
                    result += "0110";
                    break;
                case '7':
                    result += "0111";
                    break;
                case '8':
                    result += "1000";
                    break;
                case '9':
                    result += "1001";
                    break;
                case 'a':
                case 'A':
                    result += "1010";
                    break;
                case 'b':
                case 'B':
                    result += "1011";
                    break;
                case 'c':
                case 'C':
                    result += "1100";
                    break;
                case 'd':
                case 'D':
                    result += "1101";
                    break;
                case 'e':
                case 'E':
                    result += "1110";
                    break;
                case 'f':
                case 'F':
                    result += "1111";
                    break;
                default:
                    throw new NumberFormatException();
            }
        }
        return result;
    }

    public static String sign_extend(String binary16){
        String binary32 = "";

        if (binary16.charAt(0) == '0')
            binary32 = "0000000000000000";
        else
            binary32 = "1111111111111111";
        binary32 += binary16;
        return binary32;
    }

}
