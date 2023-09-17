package mayton.network.dht;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class MagnetHelper {

    public static MagnetLink.Builder generateMagnetLinksPack(byte[] blob) throws NoSuchAlgorithmException, IOException {
        MagnetLink.Builder magnetLinkBuilder = new MagnetLink.Builder();
        magnetLinkBuilder.withExactLength(blob.length);
        /*magnetLinkBuilder.withExactTopic(new Urn("ed2k",
        magnetLinkBuilder.withExactTopic(new Urn("tree:tiger",
        magnetLinkBuilder.withExactTopic(new Urn("aich", a*/
        return magnetLinkBuilder;
    }

}
