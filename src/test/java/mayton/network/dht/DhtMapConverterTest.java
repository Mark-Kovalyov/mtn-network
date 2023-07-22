package mayton.network.dht;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import org.junit.jupiter.api.Test;

import java.net.DatagramPacket;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DhtMapConverterTest {

    @Test
    void test_convert_empty_datagram_packet() {
        DhtMapConverter dhtMapConverter = new DhtMapConverter();
        Optional<Map<String, Object>> res = dhtMapConverter.convert(new DatagramPacket(new byte[0], 0));
        assertNotNull(res);
    }

    @Test
    void test_dump_dht_encoded_empty_map() {
        assertEquals("{}",
                MapJsonConverter.dumpBencodedMapWithJackson(Collections.EMPTY_MAP, new MinimalPrettyPrinter()).get());
    }

    @Test
    void testDumpDEncodedMapJackson2() {
        assertEquals("{\"key1\":\"value1\",\"key2\":1}",
                MapJsonConverter.dumpBencodedMapWithJackson(
                        new LinkedHashMap() {{
                                              put("key1", "value1");
                                              put("key2", Integer.valueOf(1));
                                          }},
                new MinimalPrettyPrinter()).get());
    }

    @Test
    void testDumpDEncodedMapJackson6() {
        assertEquals("{\"key1\":{\"sub-key1\":\"sub-value1\"}}",
                MapJsonConverter.dumpBencodedMapWithJackson(
                        new LinkedHashMap() {{
                            put("key1", new LinkedHashMap() {{
                                put("sub-key1", "sub-value1");
                            }});
                        }},
                        new MinimalPrettyPrinter()).get());
    }


/*
    @Test
    public void testDumpDEncodedMapJackson3() {
        assertEquals("{\"key1\":[]}", Utils.dumpBencodedMapWithJackson(new LinkedHashMap() {{
                                                                           put("key1", Collections.EMPTY_LIST);
                                                                       }},
                new MinimalPrettyPrinter()));
    }

    @Test
    public void testDumpDEncodedMapJackson4() {
        assertEquals("{\"key1\":[1,2]}", Utils.dumpBencodedMapWithJackson(new LinkedHashMap() {{
                                                                              put("key1", Arrays.asList(1, 2));
                                                                          }},
                new MinimalPrettyPrinter()));
    }

    @Test
    public void testDumpDEncodedMapJackson5() {
        assertEquals("{\"key1\":[\"1\",\"2\"]}", Utils.dumpBencodedMapWithJackson(new LinkedHashMap() {{
                                                                                      put("key1", Arrays.asList("1", "2"));
                                                                                  }},
                new MinimalPrettyPrinter()));
    }

*/

}