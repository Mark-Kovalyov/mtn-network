package mayton.network;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetworkUtilsTest {

    @Test
    void testToByteArr() {
        assertArrayEquals(new byte[]{95, (byte) (139 - 256), (byte) (214 - 256), 82}, NetworkUtils.toByteArray("95.139.214.82"));
    }

    @Test
    public void testFormat() {
        assertEquals("0.0.0.0", NetworkUtils.formatIpV4(0));
        assertEquals("255.255.255.255", NetworkUtils.formatIpV4(4294967295L));
    }

    @Test
    public void test() {
        assertEquals(0L, NetworkUtils.parseIpV4("0.0.0.0"));
        assertEquals(16843009L, NetworkUtils.parseIpV4("1.1.1.1"));
        assertEquals(4294967295L, NetworkUtils.parseIpV4("255.255.255.255"));
        assertEquals(17394362L, NetworkUtils.parseIpV4("001.009.106.186"));
    }

    @Test void testSigned() throws UnknownHostException {
        assertEquals("103.251.50.17", NetworkUtils.formatIpV4((Inet4Address) Inet4Address.getByAddress(new byte[] {103,-5,50,17})));
        assertEquals("103.5.50.17", NetworkUtils.formatIpV4((Inet4Address) Inet4Address.getByAddress(new byte[] {103,5,50,17})));
    }

    @Test
    @Disabled
    public void testExceptionEmpty(ExtensionContext context, Throwable ex) throws Throwable {
        NetworkUtils.parseIpV4("");
        throw(ex);
    }

    @Test
    @Disabled
    public void testExceptionToLong(ExtensionContext context, Throwable ex) throws Throwable {
        NetworkUtils.parseIpV4("1000.1000.1000.100");
    }

    @Test
    @Disabled
    public void testExceptionNull() {
        NetworkUtils.parseIpV4(null);
    }

    @Test
    @Disabled
    public void testExceptionIllegalSymbol() {
        NetworkUtils.parseIpV4("a.b.c.d");
    }

    @Test
    @Disabled
    public void testDetectNetwork() {
        assertEquals("217.43.26.0/25", NetworkUtils.detectNetwork("217.43.26.0", "217.43.26.127"));
    }

}

