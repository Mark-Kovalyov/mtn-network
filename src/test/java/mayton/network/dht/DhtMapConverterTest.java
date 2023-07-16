package mayton.network.dht;

import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import org.junit.Test;

import java.net.DatagramPacket;
import java.util.*;

import static org.junit.Assert.assertNotNull;

public class DhtMapConverterTest {

    @Test
    public void test2() {
        DhtMapConverter dhtMapConverter = new DhtMapConverter();
        Optional<Map<String, Object>> res = dhtMapConverter.convert(new DatagramPacket(new byte[0], 0));
        assertNotNull(res);
    }

    /*@Test
    public void testDumpDEncodedMapJackson1() {
        assertEquals("{}", Utils.dumpBencodedMapWithJackson(Collections.EMPTY_MAP, new MinimalPrettyPrinter()));
    }

    @Test
    public void testDumpDEncodedMapJackson2() {
        assertEquals("{\"key1\":\"value1\",\"key2\":1}", Utils.dumpBencodedMapWithJackson(new LinkedHashMap() {{
                                                                                              put("key1", "value1");
                                                                                              put("key2", Integer.valueOf(1));
                                                                                          }},
                new MinimalPrettyPrinter()));
    }

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

    @Test
    public void testDumpDEncodedMapJackson6() {
        assertEquals("{\"key1\":{\"sub-key1\":\"sub-value1\"}}",
                Utils.dumpBencodedMapWithJackson(
                        new LinkedHashMap() {{
                            put("key1", new LinkedHashMap() {{
                                put("sub-key1", "sub-value1");
                            }});
                        }},
                        new MinimalPrettyPrinter()));
    }*/

}