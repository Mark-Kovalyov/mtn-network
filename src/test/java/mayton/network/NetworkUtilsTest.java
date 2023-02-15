package mayton.network;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import static mayton.network.NetworkUtils.fromIpv4toLong;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetworkUtilsTest {

    @Test
    void testToByteArr() {
        assertArrayEquals(new byte[]{95, (byte) 139, (byte) 214, 82}, NetworkUtils.toByteArray("95.139.214.82"));
        assertArrayEquals(new byte[]{(byte) 202,99,83,29}, NetworkUtils.toByteArray("202.99.83.29"));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "0:0.0.0.0",
            "4294967295:255.255.255.255",
            "2155905152:128.128.128.128"
    },
    delimiter = ':')
    void testFormatIpv4(String input, String expected) {
        assertEquals(expected, NetworkUtils.formatIpV4(Long.parseLong(input)));
    }

    @Test
    public void test() {
        assertEquals(0L,          NetworkUtils.parseIpV4("0.0.0.0"));
        assertEquals(16843009L,   NetworkUtils.parseIpV4("1.1.1.1"));
        assertEquals(2155905152L, NetworkUtils.parseIpV4("128.128.128.128"));
        assertEquals(4294967295L, NetworkUtils.parseIpV4("255.255.255.255"));
        assertEquals(17394362L,   NetworkUtils.parseIpV4("001.009.106.186"));
        assertEquals(3395506973L, NetworkUtils.parseIpV4("202.99.83.29"));
    }

    @Test
    public void test1() throws UnknownHostException {
        assertEquals(4294967295L, fromIpv4toLong(Inet4Address.getByAddress(new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255})));
        assertEquals(0L, fromIpv4toLong(Inet4Address.getByAddress(new byte[]{0, 0, 0, 0})));
        assertEquals(2155905152L, fromIpv4toLong(Inet4Address.getByAddress(new byte[]{(byte) 128, (byte) 128, (byte) 128, (byte) 128})));
        assertEquals(2139062143L, fromIpv4toLong(Inet4Address.getByAddress(new byte[]{127, 127, 127, 127})));
    }

    @Test void testSigned() throws UnknownHostException {
        assertEquals("103.251.50.17", NetworkUtils.formatIpV4((Inet4Address) Inet4Address.getByAddress(new byte[]{103, -5, 50, 17})));
        assertEquals("103.5.50.17",   NetworkUtils.formatIpV4((Inet4Address) Inet4Address.getByAddress(new byte[]{103, 5, 50, 17})));
        assertEquals("202.99.83.29",  NetworkUtils.formatIpV4((Inet4Address) Inet4Address.getByAddress(new byte[]{(byte) 202, 99, 83, 29})));
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

