package mayton.network.blacklist;

import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import mayton.network.Ip2locUtils;
import mayton.network.NetworkUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Optional;
import java.util.regex.Pattern;

public class EmuleGuardingServiceRadix implements EmuleGuardingService {

    static Logger logger = LoggerFactory.getLogger("main.emule");

    public RadixTree<EmuleGuarding> radixTree;

    private static class Singleton {
        public static EmuleGuardingServiceRadix INSTANCE = new EmuleGuardingServiceRadix();
    }

    public static EmuleGuardingServiceRadix getInstance() {
        return Singleton.INSTANCE;
    }

    public EmuleGuardingServiceRadix() {
        String path = System.getProperty("mayton.network.blacklist.EmuleGuardingServiceRadix.path", "~/emule.csv");
        logger.info("path = {}", path);
        try {
            logger.info("constructor");
            radixTree = new ConcurrentRadixTree<>(new DefaultCharArrayNodeFactory());
            InputStream is = new FileInputStream(path);
            CSVParser csvParser = CSVParser.parse(
                    is,
                    StandardCharsets.UTF_8,
                    CSVFormat.newFormat(','));
            Iterator<CSVRecord> i = csvParser.iterator();
            int cnt = 0;
            while(i.hasNext()) {
                CSVRecord r = i.next();
                String range   = r.get(0);
                String rank    = r.get(1);
                String name    = r.get(2);
                logger.trace("range = {}, rank = {}, name = {}", range, rank, name);
                String[] ips = range.split(Pattern.quote("-"));
                long beginIp = NetworkUtils.parseIpV4(StringUtils.trim(ips[0]));
                long endIp   = NetworkUtils.parseIpV4(StringUtils.trim(ips[1]));
                String key = Ip2locUtils.samePrefix(
                        Ip2locUtils.extendTo32LeadingZeroes(String.valueOf(beginIp)),
                        Ip2locUtils.extendTo32LeadingZeroes(String.valueOf(endIp)));

                EmuleGuarding emuleGuarding = new EmuleGuarding(
                        NetworkUtils.formatIpV4(beginIp),
                        NetworkUtils.formatIpV4(endIp),
                        0,
                        name);
                radixTree.put(key, emuleGuarding);
                logger.trace("key = {}, value = {}", key, emuleGuarding);
                cnt++;
            }
            logger.info("parsed CSV rows = {}", count());
            csvParser.close();
        } catch (Exception ex) {
            logger.error("{}", ex);
            throw new RuntimeException(ex);
        }
    }


    @Override
    public Optional<EmuleGuarding> lookup(String ip) {
        return Optional.empty();
    }

    public int count() {
        return radixTree.size();
    }
}
