package mayton.network;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetworkUtilsTest {

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

