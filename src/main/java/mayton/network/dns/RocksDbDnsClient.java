package mayton.network.dns;

import mayton.lib.Uniconf;
import org.apache.commons.lang3.tuple.Pair;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class RocksDbDnsClient implements Resolvable, RocksDbDnsClientMXBean {

    private AtomicInteger requests = new AtomicInteger();

    private AtomicInteger missed = new AtomicInteger();

    static Logger logger = LoggerFactory.getLogger("rocks-db-dns-client");

    @Override
    public int getRequests() {
        return requests.get();
    }

    @Override
    public int getMissed() {
        return missed.get();
    }

    public static class Singleton {
        public static RocksDbDnsClient INSTANCE = new RocksDbDnsClient();
    }

    public static RocksDbDnsClient getInstance() {
        return RocksDbDnsClient.Singleton.INSTANCE;
    }

    private RocksDB rocksDB;
    private ReentrantLock dbLock = new ReentrantLock();
    private ReentrantLock dnsClientLock = new ReentrantLock();
    private SimpleDnsClient simpleDnsClient;

    private RocksDbDnsClient() {
        logger.info("Constructor...");
        Uniconf uniconf = new Uniconf();
        String dbPath      = uniconf.lookupProperty("mayton.network.dns.RocksDbDnsClient.dbPath", "/tmp/mayton/network/dns/rocksdb");
        String dns         = uniconf.lookupProperty("mayton.network.dns.RocksDbDnsClient.dns", "8.8.8.8");
        int    port        = Integer.parseInt(uniconf.lookupProperty("mayton.network.dns.RocksDbDnsClient.port", "53"));
        int    timeout     = Integer.parseInt(uniconf.lookupProperty("mayton.network.dns.RocksDbDnsClient.timeout", "15"));
        Options options = new Options().setCreateIfMissing(true);
        try {
            logger.info("Opening rocksdb by path = {}", dbPath);
            rocksDB = RocksDB.open(options, dbPath);
            logger.info("Opening simple DNS client with params : {}:{}, timeout : {}", dns, port, timeout);
            simpleDnsClient = new SimpleDnsClient(dns, port, timeout);
            ObjectName objectName = new ObjectName("mayton.network.dns:type=basic,name=RocksDbDnsClient");
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(this, objectName);
        } catch (RocksDBException e) {
            logger.error("{}", e.getMessage());
        } catch (UnknownHostException e) {
            logger.error("{}", e.getMessage());
        } catch (MalformedObjectNameException | NotCompliantMBeanException | InstanceAlreadyExistsException e) {
            logger.error("{}", e.getMessage());
        } catch (MBeanRegistrationException e) {
            logger.error("{}", e.getMessage());
        }
    }

    private void put(String key, String value) {
        dbLock.lock();
        try {
            rocksDB.put(key.getBytes(StandardCharsets.UTF_8), value.getBytes(StandardCharsets.UTF_8));
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } finally {
            dbLock.unlock();
        }
    }

    private String get(String key) {
        dbLock.lock();
        try {
            byte[] value = rocksDB.get(key.getBytes(StandardCharsets.UTF_8));
            return value == null ? null : new String(value);
        } catch (RocksDBException e) {
            throw new RuntimeException(e);
        } finally {
            dbLock.unlock();
        }
    }

    public int count() {
        RocksIterator itr = rocksDB.newIterator();
        itr.seekToFirst();
        int count = 0;
        while(itr.isValid()) {
            itr.next();
            count++;
        }
        return count;
    }

    public List<Pair<String,String>> export() {
        try(RocksIterator itr = rocksDB.newIterator()) {
            itr.seekToFirst();
            List<Pair<String, String>> pairs = new ArrayList<>();
            while (itr.isValid()) {
                itr.next();
                pairs.add(Pair.of(new String(itr.key()), new String(itr.value())));
            }
            return pairs;
        }
    }

    public Optional<String> resolvePtr(String input) {
        requests.incrementAndGet();
        String key = get(input);
        if (key == null) {
            missed.incrementAndGet();
            Optional<String> ptr = null;
            dnsClientLock.lock();
            try {
                ptr = simpleDnsClient.resolvePtr(input);
            } finally {
                dnsClientLock.unlock();
            }
            if (ptr.isPresent()) {
                put(input, ptr.get());
            }
            return ptr;
        } else {
            return Optional.of(key);
        }
    }

}
