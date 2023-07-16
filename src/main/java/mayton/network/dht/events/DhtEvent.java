package mayton.network.dht.events;

import mayton.network.NetworkUtils;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;

public abstract class DhtEvent implements Serializable {

    private InetAddress inetAddress;

    private int port;

    public DhtEvent(InetAddress inetAddress, int port) {
        this.inetAddress = inetAddress;
        this.port = port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    public String getHostAndPort() {
        return NetworkUtils.formatIpV4((Inet4Address) inetAddress) + ":" + port;
    }
}

