package mayton.network.dns;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RocksDbDnsClientTest {

    @Test
    @Tag("db")
    @Disabled
    void test() throws IOException {
        Path tempDir = Files.createTempDirectory("mtn-network");
        String tempDirPath = tempDir.toString();
        System.setProperty("mayton.network.dns.RocksDbDnsClient.dbPath", tempDirPath);
        RocksDbDnsClient rocksDbDnsClient = RocksDbDnsClient.getInstance();
        Optional<String> str = rocksDbDnsClient.resolvePtr("45.128.216.200");
        assertTrue(str.isPresent());
        //assertEquals(215, rocksDbDnsClient.count());
        rocksDbDnsClient.export().forEach(pair -> System.out.printf("%s : %s\n", pair.getKey(),pair.getValue()));
    }

}
