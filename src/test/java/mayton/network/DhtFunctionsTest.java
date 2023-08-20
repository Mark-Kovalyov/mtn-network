package mayton.network;

import mayton.network.dht.MagnetHelper;
import mayton.network.dht.MagnetLink;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class DhtFunctionsTest {

    @Disabled
    @Test
    void packTest() throws NoSuchAlgorithmException, IOException {
        MagnetLink.Builder magnetLinksPack = MagnetHelper.generateMagnetLinksPack(new byte[0]);
        assertEquals("magnet:?xt=urn:ed2k:31d6cfe0d16ae931b73c59d7e0c089c0&xt=urn:tree:tiger:da39a3ee5e6b4b0d3255bfef95601890afd80709&xt=urn:aich:da39a3ee5e6b4b0d3255bfef95601890afd80709&xl=0", magnetLinksPack.build().toString());
    }


}
