package mayton.network.dns;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RocksDbDnsClientTest {

    @Test
    @Tag("db")
    void test() throws IOException {
        RocksDbDnsClient rocksDbDnsClient = new RocksDbDnsClient("/tmp/" + UUID.randomUUID().toString());
        Optional<String> str = rocksDbDnsClient.resolvePtr("45.128.216.200");
        assertTrue(str.isPresent());
    }

}
