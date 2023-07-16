package mayton.network.dht;

import org.apache.commons.lang3.Validate;
import the8472.bencode.BDecoder;
import the8472.bencode.Tokenizer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;

public class DhtMapConverter {



    public static Optional<Map<String, Object>> convert(byte[] data) {
        Validate.notNull(data, "data argument must not be null");
        BDecoder decoder = new BDecoder();
        try {
            Map<String, Object> res = decoder.decode(ByteBuffer.wrap(data));
            return Optional.of(res);
        } catch (Tokenizer.BDecodingException ex) {
            return Optional.empty();
        }
    }

    public static Optional<Map<String, Object>> convert(DatagramPacket datagramPacket) {
        Validate.notNull(datagramPacket, "datagramPacket argument must not be null");
        return convert(datagramPacket.getData());
    }
}
