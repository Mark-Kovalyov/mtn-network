package mayton.network;

import mayton.network.dht.MagnetLink;
import mayton.network.dht.Urn;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MagnetLinkTest {

    @Test
    public void testEd2k() throws MalformedURLException {
        MagnetLink link = new MagnetLink.Builder()
                .withDisplayName("JavaScript language specification.V1.1.pdf")
                .withExactLength(306004)
                .withExactTopic(new Urn("ed2k", "5ae809cea87724d34a3aecbb638631b1"))
                .withExactTopic(new Urn("ed2khash", "5ae809cea87724d34a3aecbb638631b1"))
                .build();

        assertEquals("magnet:?dn=JavaScript%20language%20specification.V1.1.pdf" +
                "&xt=urn:ed2k:5ae809cea87724d34a3aecbb638631b1" +
                "&xt=urn:ed2khash:5ae809cea87724d34a3aecbb638631b1" +
                "&xl=306004", link.toString());
    }

    @Test
    public void testAll() {
        MagnetLink link = new MagnetLink.Builder()
                .withDisplayName("ovr-show.mp4")
                .withExactLength(42911730)
                .withExactTopic(new Urn("tree:tiger", "kdwc44vxooycfqhws6byw5zaunmypxqj2uhabii"))
                .withExactTopic(new Urn("ed2k", "b0c2cb94303c3ec7fec069595a9c0d79"))
                .withExactTopic(new Urn("aich", "ih6kme5y3v2nmm2qqpnnub4beaialuf6"))
                .build();

        assertEquals("magnet:?dn=ovr-show.mp4" +
                "&xt=urn:tree:tiger:kdwc44vxooycfqhws6byw5zaunmypxqj2uhabii" +
                "&xt=urn:ed2k:b0c2cb94303c3ec7fec069595a9c0d79" +
                "&xt=urn:aich:ih6kme5y3v2nmm2qqpnnub4beaialuf6" +
                "&xl=42911730", link.toString());
    }

    @Test
    public void testSha1() throws MalformedURLException {

        MagnetLink link = new MagnetLink.Builder()
                .withDisplayName("Magnet URI scheme")
                .withExactLength(3200)
                .withExactTopic(new Urn("sha1", "YNCKHTQCWBTRNJIV4WNAE52SJUQCZO5C"))
                .withExactTopic(new Urn("sha1", "TXGCZQTH26NL6OUQAJJPFALHG2LTGBC7"))
                .withManifestTopic(new URL("https://en.wikipedia.org/wiki/Magnet_URI_scheme"))
                .withKeywordTopic("magnet")
                .withKeywordTopic("DHT")
                .withPeerAddress(Pair.of("127.0.0.1", 8082))
                .withPeerAddress(Pair.of("colombo.info", 8083))
                .build();

        assertEquals("magnet:?" +
                "dn=Magnet%20URI%20scheme&" +
                "xt=urn:sha1:YNCKHTQCWBTRNJIV4WNAE52SJUQCZO5C&" +
                "xt=urn:sha1:TXGCZQTH26NL6OUQAJJPFALHG2LTGBC7&" +
                "kt=magnet&" +
                "kt=DHT&" +
                "mt=https://en.wikipedia.org/wiki/Magnet_URI_scheme&" +
                "xl=3200&" +
                "pe=127.0.0.1:8082&" +
                "pe=colombo.info:8083", link.toString());
    }

}
