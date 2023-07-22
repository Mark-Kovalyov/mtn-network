package mayton.network;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.Optional;

public class Ip2locUtils {

    private Ip2locUtils() {}

    public static String extendTo32LeadingZeroes(String s) {
        return StringUtils.leftPad(s, 32, '0');
    }

    public static Optional<String> ipv4toBinaryString(String ip) {
        Optional<Long> optLong = NetworkUtils.parseIpV4Safe(ip);
        return optLong.isPresent() ?
              Optional.of(extendTo32LeadingZeroes(new BigInteger(Long.toString(optLong.get())).toString(2))):
              Optional.empty();
    }


    public static String samePrefix(String s1, String s2) {
        StringBuilder sb = new StringBuilder();
        sb.delete(0, sb.length());
        int i = 0;
        int minlen = Math.min(s1.length(), s2.length());
        while(i < minlen) {
            char c = s1.charAt(i);
            if (c == s2.charAt(i)) sb.append(c);
            i++;
        }
        return sb.toString();
    }

    public static String samePrefix(StringBuilder sb, String s1, String s2) {
        sb.delete(0, sb.length());
        int i = 0;
        int minlen = Math.min(s1.length(), s2.length());
        while(i < minlen) {
            char c = s1.charAt(i);
            if (c == s2.charAt(i)) sb.append(c);
            i++;
        }
        return sb.toString();
    }
}
