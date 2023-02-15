package mayton.network.dht;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class DhtFunctions {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static MagnetLink.Builder generateMagnetLinksPack(byte[] blob) throws NoSuchAlgorithmException, IOException {
        Optional<String> ed2k = DhtFunctions.ed2kHash(blob);
        MagnetLink.Builder magnetLinkBuilder = new MagnetLink.Builder();
        magnetLinkBuilder.withExactLength(blob.length);
        if (ed2k.isPresent())
            magnetLinkBuilder.withExactTopic(new Urn("ed2k",ed2k.get()));
        magnetLinkBuilder.withExactTopic(new Urn("tree:tiger", treeTiger(blob)));
        magnetLinkBuilder.withExactTopic(new Urn("aich", aichHash(blob)));
        return magnetLinkBuilder;
    }

    public static String encodeBinHex(byte[] hash) {
        return Hex.encodeHexString(hash);
    }

    public static ThreadLocal<MessageDigest> md4tl = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("MD4");
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            return null;
        }
    });

    public static Optional<String> ed2kHash(byte[] arr) throws NoSuchAlgorithmException, IOException {
        if (arr.length > 16 * 1024 * 1024) return Optional.empty();
        MessageDigest messageDigest = md4tl.get();
        messageDigest.reset();
        messageDigest.update(arr);
        return Optional.of(encodeBinHex(messageDigest.digest()));
    }

    // TODO: Finish development
    public static String treeTiger(byte[] arr) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        messageDigest.reset();
        messageDigest.update(arr);
        return encodeBinHex(messageDigest.digest());
    }

    // TODO: Finish development
    public static String aichHash(byte[] arr) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        messageDigest.reset();
        messageDigest.update(arr);
        return encodeBinHex(messageDigest.digest());
    }
}
