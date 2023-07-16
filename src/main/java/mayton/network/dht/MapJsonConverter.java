package mayton.network.dht;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MapJsonConverter {

    private static final Logger logger = LoggerFactory.getLogger(MapJsonConverter.class);

    public static Optional<String> convertPlain(Map<String, Object> stringObjectMap) {
        Validate.notNull(stringObjectMap);
        return dumpBencodedMapWithJackson(stringObjectMap, new MinimalPrettyPrinter());
    }

    public static Optional<String> convertPretty(Map<String, Object> stringObjectMap) {
        Validate.notNull(stringObjectMap);
        return dumpBencodedMapWithJackson(stringObjectMap, new DefaultPrettyPrinter());
    }

    private static Optional<String> dumpBencodedMapWithJackson(Map<String, Object> res, PrettyPrinter prettyPrinter) {
        try {
            return dumpBencodedMapWithJacksonEx(res, prettyPrinter);
        } catch (IOException e) {
            logger.trace("IOException", e);
        }
        return Optional.empty();
    }

    private static void dumpBencodedMapWithJacksonEx(String rootName, Map<String, Object> res, JsonGenerator jGenerator) throws IOException {
        if (rootName == null) {
            jGenerator.writeStartObject();
        } else {
            jGenerator.writeObjectFieldStart(rootName);
        }

        for (Map.Entry<String, Object> item : res.entrySet()) {
            Object value = item.getValue();
            if (value instanceof String) {
                jGenerator.writeStringField(item.getKey(), (String) value);
            } else if (value instanceof Integer) {
                jGenerator.writeNumberField(item.getKey(), (Integer) item.getValue());
            } else if (value instanceof Long) {
                jGenerator.writeNumberField(item.getKey(), (Long) item.getValue());
            } else if (value instanceof List) {
                jGenerator.writeArrayFieldStart(item.getKey());
                for (Object o : (List) value) {
                    if (o instanceof Integer) {
                        jGenerator.writeNumber((Integer) o);
                    } else if (o instanceof String) {
                        jGenerator.writeString((String) o);
                    } else {
                        logger.error("Unable to detect value class = {} during array generation", o.getClass().toString());
                    }
                }
                jGenerator.writeEndArray();
            } else if (value instanceof Map) {
                dumpBencodedMapWithJacksonEx(item.getKey(), (Map) value, jGenerator);
            } else if (value instanceof byte[]) {
                // Sometimes it's possible that byte[] is a String, for example
                // "q" : "ping"
                jGenerator.writeStringField(item.getKey(), beautifyBlob((byte[]) value));
            } else {
                logger.error("Unable to detect value class = {} during map generation", value.getClass());
            }
        }
        jGenerator.writeEndObject();
    }

    private static String beautifyBlob(byte[] arr) {
        if (allContainsAscii(arr)) {
            return Hex.encodeHexString(arr) + " ( '" + new String(arr) + "' )";
        } else {
            return Hex.encodeHexString(arr);
        }
    }

    private static boolean allContainsAscii(byte[] arr) {
        for(int i = 0 ;i<arr.length;i++) {
            if (arr[i] > 32) continue;
            else return false;
        }
        return true;
    }

    private static Optional<String> dumpBencodedMapWithJacksonEx(Map<String, Object> res, PrettyPrinter prettyPrinter) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        JsonFactory jfactory = new JsonFactory();
        JsonGenerator jGenerator = jfactory.createGenerator(stream, JsonEncoding.UTF8);
        jGenerator.setPrettyPrinter(prettyPrinter);
        dumpBencodedMapWithJacksonEx(null, res, jGenerator);
        jGenerator.flush();
        stream.flush();
        return Optional.of(stream.toString(StandardCharsets.UTF_8));
    }



}
