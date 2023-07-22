package mayton.network.dht;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;
import the8472.bencode.BDecoder;
import the8472.bencode.Tokenizer;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;

public class SparkSupport {

    private static ThreadLocal<BDecoder> decoderThreadLocal = ThreadLocal.withInitial(() -> new BDecoder());

    public static Optional<String> dumpDhtMessageToPlainJson(String binHexDhtMessage) {
        Validate.notNull(binHexDhtMessage, "binHex argument must not be null");
        byte[] data = null;
        try {
            data = Hex.decodeHex(binHexDhtMessage);
        } catch (DecoderException e) {
            return Optional.empty();
        }
        Map<String, Object> res = null;
        try {
            res = decoderThreadLocal.get().decode(ByteBuffer.wrap(data));
        } catch (ArrayIndexOutOfBoundsException | Tokenizer.BDecodingException e) {
            return Optional.empty();
        }
        return MapJsonConverter.convertPlain(res);
    }

    public static Optional<String> dumpDhtMessageToPlainJsonDebug(String binHexDhtMessage) {
        Validate.notNull(binHexDhtMessage, "binHex argument must not be null");
        byte[] data = null;
        try {
            data = Hex.decodeHex(binHexDhtMessage);
        } catch (DecoderException e) {
            return Optional.of("DHT:ERR:001:" + e.getMessage());
        }
        Map<String, Object> res = null;
        try {
            res = decoderThreadLocal.get().decode(ByteBuffer.wrap(data));
        } catch (Tokenizer.BDecodingException e) {
            return Optional.of("DHT:ERR:002:" + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            return Optional.of("DHT:ERR:003:" + e.getMessage());
        }
        return MapJsonConverter.convertPlain(res);
    }

}
