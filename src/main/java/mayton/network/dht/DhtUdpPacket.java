package mayton.network.dht;

import java.io.Serializable;

public class DhtUdpPacket implements Serializable {

    private String ts;
    private String ip;
    private int sourcePort;
    private String tag;
    private int packetSize;
    private String packetBody;

    public DhtUdpPacket() {}

    public DhtUdpPacket(String ts, String ip, int sourcePort, String tag, int packetSize, String packetBody) {
        this.ts = ts;
        this.ip = ip;
        this.sourcePort = sourcePort;
        this.tag = tag;
        this.packetSize = packetSize;
        this.packetBody = packetBody;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }

    public String getPacketBody() {
        return packetBody;
    }

    public void setPacketBody(String packetBody) {
        this.packetBody = packetBody;
    }

    @Override
    public String toString() {
        return "UDPRecord{" +
                "ts='" + ts + '\'' +
                ", ip='" + ip + '\'' +
                ", sourcePort=" + sourcePort +
                ", tag='" + tag + '\'' +
                ", packetSize=" + packetSize +
                ", packetBody='" + packetBody + '\'' +
                '}';
    }
}
