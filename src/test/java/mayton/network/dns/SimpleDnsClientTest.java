package mayton.network.dns;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleDnsClientTest {

    @Test
    @Tag("db")
    @Disabled
    void test() throws IOException {
        Path tempDir = Files.createTempDirectory("mtn-network");
        String tempDirPath = tempDir.toString();
        System.setProperty("mayton.network.dns.RocksDbDnsClient.dbPath", tempDirPath);
        SimpleDnsClient rocksDbDnsClient = new SimpleDnsClient("8.8.8.8", 53, 15);
        Optional<String> str = rocksDbDnsClient.resolvePtr("93.74.150.2");
        assertTrue(str.isPresent());
        assertEquals(str.get(),"irregular-expander.volia.net.");
    }

}
