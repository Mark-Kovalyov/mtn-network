package mayton.network;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.annotation.concurrent.ThreadSafe;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.BitSet;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

@ThreadSafe
public class NetworkUtils {

    private static final Pattern IPV_4_PATTERN = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

    private static final Pattern IPV_6_PATTERN = Pattern.compile(
            "[:xdigit:]{0,4}:[:xdigit:]{0,4}:[:xdigit:]{0,4}:[:xdigit:]{0,4}:[:xdigit:]{0,4}:[:xdigit:]{0,4}:[:xdigit:]{0,4}:[:xdigit:]{0,4}",
            Pattern.CASE_INSENSITIVE);

    private NetworkUtils() {
        // no inst
    }

    @NotNull
    public static String reverseIp(@NotNull String ip) {
        String[] o = ip.split(Pattern.quote("."));
        return o[3] + "." + o[2] + "." + o[1] + "." + o[0];
    }

    public static String trimRightAfter(String s, char c) {
        int i = s.indexOf(c);
        return s.substring(0, i);
    }

    /**
     * Input: 12.0.0.0/8 (AT&T Services)
     *
     * @param networkAddress
     * @return IP range
     */
    public Pair<Long,Long> detectIpRangeByNetwork(String networkAddress) {
        long mask = 0b11111111_00000000_00000000_00000000;
        long addr = parseIpV4(trimRightAfter(networkAddress,'/'));
        return Pair.of(addr, addr);
    }

    public String detectNetworkByIpRange(long ip1, long ip2) {

        return "12.0.0.0/8";
    }

    private static int toUnsigned(byte v) {
        return ((int) v) < 0 ? (int) v + 256 : v;
    }

    @NotNull
    public static byte[] toByteArray(@NotNull String ipv4) {
        Validate.notNull(ipv4, "Unable to parse null argument!");
        Validate.inclusiveBetween(7, 15, ipv4.length(), "Unable to parse " + ipv4 + " like an IPv4 address. Length is incorrect");
        String[] parts = StringUtils.split(ipv4, '.');
        if (parts.length != 4) {
            throw new IllegalArgumentException("Unable to parse " + ipv4 + " like an IPv4 address. Not enought digits");
        }
        try {
            byte[] res = new byte[4];
            for (int i = 0; i < 4; i++) {
                int ri = Integer.parseInt(parts[i]);
                res[i] = (byte) ri;
            }
            return res;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Unable to parse " + ipv4 + " like an IPv4 address. Number format exception.");
        }
    }

    @NotNull
    public static String formatIpV4(@NotNull Inet4Address inet4Address) {
        byte[] addr = inet4Address.getAddress();
        return "" + toUnsigned(addr[0]) + "." +
                    toUnsigned(addr[1]) + "." +
                    toUnsigned(addr[2]) + "." +
                    toUnsigned(addr[3]);
    }

    @Range(from = 0, to = 4294967295L)
    public static long fromIpv4toLong(@NotNull InetAddress inetAddress) {
        byte[] addr = inetAddress.getAddress();
        if (inetAddress instanceof Inet4Address) {
            long multiplier = 256L * 256L * 256L;
            long res = 0;
            for (int i = 0; i < 4; i++) {
                long positive = (addr[i] < 0) ? 256 + (long) addr[i] : (long) addr[i];
                res += positive * multiplier;
                multiplier >>= 8;
            }
            Validate.inclusiveBetween(0L, 4_294_967_295L, res, "The IPv4 long representation must be in range [0..4294967295]");
            return res;
        } else {
            throw new RuntimeException("Unsupported non IPv4 address!");
        }
    }

    @NotNull
    public static String formatIpV4(@Range(from = 0, to = 4294967295L) long ipv4) {
        return "" + ((ipv4 & 0xFF_00_00_00L) >> 24) + "." +
                  + ((ipv4 & 0xFF_00_00) >> 16) + "." +
                  + ((ipv4 & 0xFF_00) >> 8) + "." +
                  + (ipv4 & 0xFF);
    }

    @NotNull
    public static String formatIpV6dence(long left, long right) {
        StringBuilder sb = new StringBuilder(39);
        // 0::::::ae21:ad12
        for(int i=0;i<4;i++) {
            // TODO:
            //sb.append(format("%04X",(left >> 16 * 4 * i) & 0xFFFF));
            sb.append(":");
        }
        for(int i=0;i<4;i++) {
            // TODO:
            //sb.append(format("%04X",(right >> 16 * 4 * i) & 0xFFFF));
            sb.append(":");
        }
        return sb.substring(0,39);
    }

    @NotNull
    public static String formatIpV6(long left, long right) {
        StringBuilder sb = new StringBuilder(39);
        // 0000:0000:0000:0000:0000:0000:ae21:ad12
        for(int i=0;i<4;i++) {
            sb.append(format("%04X",(left >> 16 * 4 * i) & 0xFFFF));
            sb.append(":");
        }
        for(int i=0;i<4;i++) {
            sb.append(format("%04X",(right >> 16 * 4 * i) & 0xFFFF));
            sb.append(":");
        }
        return sb.substring(0,39);
    }

    public static Optional<Long> parseIpV4Safe(String ipv4) {
        if (ipv4 == null || ipv4.isBlank() || ipv4.length() < 7 || ipv4.length() > 15)
            return Optional.empty();
        String[] parts = StringUtils.split(ipv4, '.');
        if (parts.length != 4) {
            return Optional.empty();
        }
        try {
            long r1 = parseLong(parts[0]);
            long r2 = parseLong(parts[1]);
            long r3 = parseLong(parts[2]);
            long r4 = parseLong(parts[3]);
            if (r1 >= 256 || r2 >= 256 || r3 >= 256 || r4 >= 256) {
                return Optional.empty();
            }
            return  Optional.of(256L * 256 * 256 * r1 +
                    256L * 256 * r2 +
                    256L * r3 +
                    r4);
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    @Range(from = 0, to = 4294967295L)
    public static long parseIpV4(@NotNull String ipv4) {
        Validate.notNull(ipv4, "Unable to parse null argument!");
        Validate.inclusiveBetween(7,15,ipv4.length(),"Unable to parse " + ipv4 + " like an IPv4 address. Length is incorrect");
        String[] parts = StringUtils.split(ipv4, '.');
        if (parts.length != 4) {
            throw new IllegalArgumentException("Unable to parse " + ipv4 + " like an IPv4 address. Not enought digits");
        }
        try {
            long r1 = parseLong(parts[0]);
            long r2 = parseLong(parts[1]);
            long r3 = parseLong(parts[2]);
            long r4 = parseLong(parts[3]);
            if (r1 >= 256 || r2 >= 256 || r3 >= 256 || r4 >= 256) {
                throw new IllegalArgumentException("Something wrong in IPv4 components! One of number expressions is out of range!");
            }
            return  256L * 256 * 256 * r1 +
                    256L * 256 * r2 +
                    256L * r3 +
                    r4;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Unable to parse " + ipv4 + " like an IPv4 address. Number format exception.");
        }
    }

    @NotNull
    public static BitSet parseIpV6(@NotNull String ipv6) {
        return new BitSet(128);
    }

    @NotNull
    public static Pair<Long, Long> parseIpV6toLongs(@NotNull String ipv6) {
        return Pair.of(0x0000_0000_0000_0000L,0x0000_0000_0000_0000L);
    }

    /**
     * The same as ipcalc:
     * <pre>
     * $ ipcalc 217.43.26.0 - 217.43.26.127
     * deaggregate 217.43.26.0 - 217.43.26.127
     * 217.43.26.0/25
     * </pre>
     *
     *
     * @param ipv4begin
     * @param ipv4end
     * @return
     */
    @NotNull
    public static String detectNetwork(@NotNull String ipv4begin, @NotNull String ipv4end) {
        long b = parseIpV4(ipv4begin);
        long e = parseIpV4(ipv4end);
        long xor = b ^ e;
        return NetworkUtils.formatIpV4(xor) + "/";
    }

}

