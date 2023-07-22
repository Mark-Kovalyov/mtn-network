package mayton.network.dns;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RocksDbDnsClientTest {

    @Test
    void test() {
        System.setProperty("mayton.network.dns.RocksDbDnsClient.dbPath", "/tmp/rocksdb");
        RocksDbDnsClient rocksDbDnsClient = RocksDbDnsClient.getInstance();
        assertEquals(215, rocksDbDnsClient.count());
        rocksDbDnsClient.export().forEach(pair -> System.out.printf("%s : %s\n", pair.getKey(),pair.getValue()));
    }

}
