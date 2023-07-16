package mayton.network.dht.events;

import javax.annotation.concurrent.Immutable;
import java.net.InetAddress;
import java.util.Optional;

@Immutable
public class Ping extends DhtEvent {

    private final String id;

    public Ping(String id, InetAddress inetAddress, int port) {
        super(inetAddress, port);
        this.id = id;
    }

    public String getId() {
        return id;
    }


    @Override
    public String toString() {
        return "Ping{" +
                "id='" + id + '\'' +
                '}';
    }
}