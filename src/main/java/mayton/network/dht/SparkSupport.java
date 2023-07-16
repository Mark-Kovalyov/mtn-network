package mayton.network.dht;

import org.apache.commons.lang3.Validate;

public class SparkSupport {

    public static String dumpDhtMessageToJson(String binHex) {
        Validate.notNull(binHex, "binHex argument must not be null");
        return "{}";
    }

}
