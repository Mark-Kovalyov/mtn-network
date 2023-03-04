package mayton.network;

import mayton.network.dht.DhtFunctions;
import mayton.network.dht.MagnetLink;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static mayton.network.dht.DhtFunctions.generateMagnetLinksPack;
import static org.junit.jupiter.api.Assertions.*;

public class DhtFunctionsTest {

    @Ignore
    @Test
    void packTest() throws NoSuchAlgorithmException, IOException {
        MagnetLink.Builder magnetLinksPack = DhtFunctions.generateMagnetLinksPack(new byte[0]);
        assertEquals("magnet:?xt=urn:ed2k:31d6cfe0d16ae931b73c59d7e0c089c0&xt=urn:tree:tiger:da39a3ee5e6b4b0d3255bfef95601890afd80709&xt=urn:aich:da39a3ee5e6b4b0d3255bfef95601890afd80709&xl=0", magnetLinksPack.build().toString());
    }


    @Test
    void looksLikeTreeTigerTest() {
        assertFalse(DhtFunctions.looksLikeTreeTiger(""));
        assertFalse(DhtFunctions.looksLikeTreeTiger("x"));

        assertTrue(DhtFunctions.looksLikeTreeTiger("6d12a41e72e644f017b6f0e2f7b44c6285f06dd5d2c5b075"));
    }

}
