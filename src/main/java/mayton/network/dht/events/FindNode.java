package mayton.network.dht.events;

import javax.annotation.concurrent.Immutable;
import java.net.InetAddress;

@Immutable
public class FindNode extends DhtEvent {

    private final String id;
    private final String target;

    public FindNode(String id, String target, InetAddress inetAddress, int port) {
        super(inetAddress, port);
        this.id = id;
        this.target = target;
    }

    public String getId() {
        return id;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "FindNode{" +
                "id='" + id + '\'' +
                ", target='" + target + '\'' +
                '}';
    }

}
