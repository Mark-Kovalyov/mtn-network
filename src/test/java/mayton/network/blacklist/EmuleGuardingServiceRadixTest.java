package mayton.network.blacklist;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmuleGuardingServiceRadixTest {

    @Test
    @Tag("db")
    @Disabled
    void test() {
        EmuleGuardingService emuleGuardingService = new EmuleGuardingServiceRadix();
        Optional<EmuleGuarding> res = emuleGuardingService.lookup("192.168.2.1");
        assertTrue(res.isPresent());
        assertEquals(res.get().name, "");
    }

}
