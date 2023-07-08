package mayton.network;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.StringArbitrary;
import net.jqwik.engine.properties.arbitraries.DefaultStringArbitrary;

import java.util.BitSet;
import java.util.Random;

@PropertyDefaults(tries = 300, afterFailure = AfterFailureMode.PREVIOUS_SEED)
public class NetworkUtilsProperties {

    static Random random = new Random();

    @Property
    @Report(Reporting.GENERATED)
    boolean ipv4_adress_parsed_correctly(@ForAll("RandomIps") String ip) {
        return ip.equals(NetworkUtils.formatIpV4(NetworkUtils.parseIpV4(ip)));
    }

    @Property
    @Report(Reporting.GENERATED)
    boolean negative_ipv4_octets_parsed_with_empty_result(@ForAll("NegativeIps") String ip) {
        return NetworkUtils.parseIpV4Safe(ip).isEmpty();
    }

    @Property
    @Report(Reporting.GENERATED)
    boolean illegal_ipv4_adress_parsed_with_empty_result(@ForAll("RandomStrings") String ip) {
        return NetworkUtils.parseIpV4Safe(ip).isEmpty();
    }

    @Provide("RandomStrings")
    Arbitrary<String> randomStrings() {
        return new DefaultStringArbitrary();
    }

    @Provide("NegativeIps")
    Arbitrary<String> negativeIps() {
        return Arbitraries.create(() -> {
            int c1 = -random.nextInt(256);
            int c2 = -random.nextInt(256);
            int c3 = -random.nextInt(256);
            int c4 = -random.nextInt(256);
            return String.format("%d.%d.%d.%d", c1,c2,c3,c4);
        });
    }

    @Provide("RandomIps")
    Arbitrary<String> randomIps() {
        return Arbitraries.create(() -> {
            int c1 = random.nextInt(256);
            int c2 = random.nextInt(256);
            int c3 = random.nextInt(256);
            int c4 = random.nextInt(256);
            return String.format("%d.%d.%d.%d", c1,c2,c3,c4);
        });
    }

    @Provide("RandomIpv6s")
    Arbitrary<String> randomIpv6s() {
        return Arbitraries.create(() -> {
            return String.format("%s:%s:%s:%s:%s:%s:%s:%s", 0x0, 0x0, 0x0,0x0, 0x0, 0x0,0x0, 0x0);
        });
    }
}
