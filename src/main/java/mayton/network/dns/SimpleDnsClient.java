package mayton.network.dns;

import mayton.network.NetworkUtils;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Optional;

public class SimpleDnsClient {

    private Resolver resolver;

    /**
     *
     * @param dns
     * @param port
     * @param timeout (seconds)
     * @throws UnknownHostException
     */
    public SimpleDnsClient(String dns, int port, int timeout) throws UnknownHostException {
        resolver = new SimpleResolver(dns);
        resolver.setPort(port);
        resolver.setTCP(false);
        resolver.setTimeout(Duration.ofSeconds(timeout));
    }

    private static Name tryToGetPtrRecord(Message message, int i) {
        if (i > 3 || i == Section.QUESTION) return null;
        for (Record rec : message.getSection(i)) {
            if (rec instanceof PTRRecord) {
                PTRRecord ptrRecord = (PTRRecord) rec;
                return ptrRecord.getTarget();
            }
        }
        return null;
    }

    private static Optional<Name> tryToGetPtr(Message resp) {
        for(int i = 0; i < 4 ; i++) {
            Name res = tryToGetPtrRecord(resp, i);
            if (res != null) return Optional.of(res);
        }
        return Optional.empty();
    }

    public Optional<String> resolvePtr(String input) {
        try {
            Name in = Name.fromString(NetworkUtils.reverseIp(input) + ".in-addr.arpa.");
            Message resp = resolver.send(
                    Message.newQuery(Record.newRecord(
                            in,
                            Type.PTR,
                            DClass.IN))
            );
            Optional<Name> name = tryToGetPtr(resp);
            return name.isPresent() ? Optional.of(name.get().toString()) : Optional.empty();
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

    public static Optional<String> resolvePtr(String input, String dns) {
        return resolvePtr(input, 53, dns, 15);
    }

    public static Optional<String> resolvePtr(String input, int port, String dns) {
        return resolvePtr(input, port, dns, 15);
    }

    public static Optional<String> resolvePtr(String input, int port, String dns, int timeout) {
        try {
            Resolver resolver = new SimpleResolver(dns);
            resolver.setPort(port);
            resolver.setTCP(false);
            resolver.setTimeout(Duration.ofSeconds(timeout));
            Name in = Name.fromString(NetworkUtils.reverseIp(input) + ".in-addr.arpa.");
            Message resp = resolver.send(
                    Message.newQuery(Record.newRecord(
                            in,
                            Type.PTR,
                            DClass.IN))
            );
            Optional<Name> name = tryToGetPtr(resp);
            return name.isPresent() ? Optional.of(name.get().toString()) : Optional.empty();
        } catch (IOException ex) {
            return Optional.empty();
        }
    }

}
