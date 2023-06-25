package mayton.network.dns;

import mayton.lib.SofarTracker;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Record;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Main {

    static Logger logger = LoggerFactory.getLogger("main");

    static String[] dnsServers = {
            "8.8.8.8",
            "8.8.4.4",
            "76.76.2.0",
            "76.76.10.0",
            "9.9.9.9",
            "149.112.112.112",
            "208.67.222.222",
            "208.67.220.220",
            "1.1.1.1",
            "1.0.0.1",
            "185.228.168.9",
            "185.228.169.9",
            "76.76.19.19",
            "76.223.122.150",
            "94.140.14.14",
            "94.140.15.15"
    };


    public static void main(String[] args) throws IOException {

        SofarTracker tracker = SofarTracker.createUnitLikeTracker("ip", 4412);

        CSVParser parser = CSVParser.parse(
                new FileReader("C:/db/ip2loc/v2/udp-groupped.csv"),
                CSVFormat.newFormat(';').withFirstRecordAsHeader());

        CSVPrinter printer = new CSVPrinter(
                new FileWriter("C:/db/ip2loc/v2/udp-with-ptr.csv"),
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
            //logger.info("DNS = {}, ip = {}, ptr = {}", randomDNS, ip, ptr);
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
