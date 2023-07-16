package mayton.network.dht;

import mayton.network.dht.events.Ping;
import org.apache.commons.codec.binary.Hex;

import java.net.DatagramPacket;
import java.util.Map;
import java.util.Optional;

public class DhtDetector {

    public static Optional<Ping> tryToExtractPingCommand(Map<String, Object> res, DatagramPacket packet) {
        if (res.containsKey("q") && (new String((byte[]) res.get("q")).equals("ping"))) {
            if (res.containsKey("a")) {
                Map<String, Object> a = (Map<String, Object>) res.get("a");
                if (a.containsKey("id")) {
                    return Optional.of(new Ping(
                            Hex.encodeHexString((byte[]) a.get("id")),
                            packet.getAddress(),
                            packet.getPort()));
                } else {
                    return Optional.empty();
                }
            } else {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }
    }

}
