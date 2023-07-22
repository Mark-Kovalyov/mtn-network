package mayton.network.dns;

import mayton.lib.SofarTracker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static mayton.network.dns.DnsServers.dnsServers;

public class Main {

    static Logger logger = LoggerFactory.getLogger("main");

    public static void main(String[] args) throws IOException {

        SofarTracker tracker = SofarTracker.createUnitLikeTracker("ip", 4412);

        CSVParser parser = CSVParser.parse(
                new FileReader(System.getProperty("udp-groupped")),
                CSVFormat.newFormat(';').withFirstRecordAsHeader());

        CSVPrinter printer = new CSVPrinter(
                new FileWriter(System.getProperty("udp-with-ptr")),
                CSVFormat.EXCEL);

        Random random = new Random();
        logger.info("Start");

        List<CSVRecord> recs = parser.getRecords();
        Iterator<CSVRecord> it = recs.iterator();
        long pos = 0;
        while(it.hasNext()) {
            tracker.update(pos++);
            CSVRecord rec = it.next();
            String ip = rec.get("IP");
            String cnt = rec.get("count");
            String randomDNS = dnsServers[random.nextInt(dnsServers.length)];
            Optional<String> res = SimpleDnsClient.resolvePtr(ip, randomDNS);
            String ptr = res.isPresent() ? res.get() : "null";
            printer.printRecord(ip, cnt, ptr, randomDNS);
            printer.flush();
            logger.info(tracker.toString());
        }
        parser.close();
        printer.close();
        tracker.finish();
        logger.info("End");
    }
}
