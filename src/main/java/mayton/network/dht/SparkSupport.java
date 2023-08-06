package mayton.network.dht;

import io.vavr.control.Either;
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
        Either<String, String> either = dumpDhtMessageToPlainJsonDebug(binHexDhtMessage);
        return either.isRight() ? Optional.of(either.get()) : Optional.empty();
    }

    public static Either<String, String > dumpDhtMessageToPlainJsonDebug(String binHexDhtMessage) {
        Validate.notNull(binHexDhtMessage, "binHex argument must not be null");
        byte[] data;
        try {
            data = Hex.decodeHex(binHexDhtMessage);
        } catch (DecoderException e) {
            return Either.left(e.getMessage());
        }
        Map<String, Object> res;
        try {
            res = decoderThreadLocal.get().decode(ByteBuffer.wrap(data));
        } catch (Tokenizer.BDecodingException | ArrayIndexOutOfBoundsException e) {
            return Either.left(e.getMessage());
        }
        return Either.right(MapJsonConverter.convertPlain(res).get());
    }

}
