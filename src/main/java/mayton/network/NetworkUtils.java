package mayton.network;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import javax.annotation.concurrent.ThreadSafe;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.BitSet;

import static java.lang.Long.parseLong;
import static java.lang.String.format;

@ThreadSafe
public class NetworkUtils {

    private NetworkUtils() {
        // no inst
    }


    private static int toUnsigned(byte v) {
        return ((int) v) < 0 ? (int) v + 256 : v;
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
            return addr[0] * 256L * 256L * 256L + addr[1] * 256L * 256L + addr[2] * 256 + addr[3];
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

    @Range(from = 0, to = 4294967295L)
    public static long parseIpV4(@NotNull String ipv4) {
        if (ipv4 == null) {
            throw new IllegalArgumentException("Unable to parse null argument!");
        } else if (ipv4.length() < 7) {
            throw new IllegalArgumentException("Unable to parse " + ipv4 + " like an IPv4 address. Not enought string length");
        } else if (ipv4.length() > 15) {
            throw new IllegalArgumentException("Unable to parse " + ipv4 + " like an IPv4 address. String length too long");
        }
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
        // TODO:
        throw new RuntimeException("Not implemented yet");
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

