package mayton.network.dht;

import com.google.common.net.UrlEscapers;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.net.URL;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * <pre>
 *     dn (Display Name) — имя файла.
 *     xl (eXact Length) — размер файла в байтах.
 *     dl (Display Length) — отображаемый размер в байтах. ?????
 *     xt (eXact Topic) — URN, содержащий хеш файла.
 *
 *     as (Acceptable Source) — веб-ссылка на файл в Интернете.
 *     xs (eXact Source) — P2P ссылка. ???
 *     kt (Keyword Topic) — ключевые слова для поиска.
 *     mt (Manifest Topic) — ссылка на метафайл, который содержит список магнетов (MAGMA).
 *
 *     tr (TRacker) — адрес трекера для BitTorrent клиентов.
 *     pe Specifies fixed peer addresses to connect to. Used to bootstrap discovery of
 *        peers in the absence of (e.g.) trackers or DHT
 *        x.pe=hostname:port
 *        x.pe=ipv4-literal:port
 *        x.pe=[ipv6-literal]:port
 * </pre>
 */
public class MagnetLink implements GenericDhtLink {

    private List<String> dn;
    private Optional<Long> dl;
    private Optional<Long> xl;
    private List<Urn> xt;
    private List<URL> as;
    private List<String> xs;
    private List<String> kt;
    private List<URL> mt;
    private List<URL> tr;
    private List<Pair<String, Integer>> pe;

    public static MagnetLink parse(@NotNull String magnet) {
        // TODO:
        return new MagnetLink.Builder().build();
    }

    public MagnetLink(List<String> dn, Optional<Long> xl, Optional<Long> dl, List<Urn> xt, List<URL> as, List<String> xs, List<String> kt, List<URL> mt, List<URL> tr, List<Pair<String, Integer>> pe) {
        this.xl = xl;
        this.dl = dl;
        this.dn = dn == null ? Collections.EMPTY_LIST : dn;
        this.xt = xt == null ? Collections.EMPTY_LIST : xt;
        this.as = as == null ? Collections.EMPTY_LIST : as;
        this.xs = xs == null ? Collections.EMPTY_LIST : xs;
        this.kt = kt == null ? Collections.EMPTY_LIST : kt;
        this.mt = mt == null ? Collections.EMPTY_LIST : mt;
        this.tr = tr == null ? Collections.EMPTY_LIST : tr;
        this.pe = pe == null ? Collections.EMPTY_LIST : pe;
    }
    
    private static String esc(String exp) {
        return com.google.common.net.UrlEscapers.urlFragmentEscaper().escape(exp);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner("&");
        for (String s : dn) sj.add("dn=" + esc(s));
        for (Urn urn : xt) sj.add("xt=" + urn.toString());
        for (URL url : as) sj.add("as=" + url.getRef());
        for (String s : xs) sj.add("xs=" + esc(s));
        for (String s : kt) sj.add("kt=" + esc(s));
        for (URL url : mt) sj.add("mt=" + url);
        for (URL url : tr) sj.add("tr=" + url);
        if (xl.isPresent()) sj.add("xl=" + xl.get());
        if (dl.isPresent()) sj.add("dl=" + dl.get());
        for (Pair<String,Integer> p : pe) sj.add(String.format("pe=%s:%d", p.getLeft(), p.getRight()));
        return "magnet:?" + sj;
    }

    // magnet:?xt=urn:btih:9DF5428ABD92D6BD058994CE15AFEC27BD01BEFF
    //  &tr=http%3A%2F%2Fbt3.t-ru.org%2Fann%3Fmagnet
    //  &dn=%D0%9B%D1%8E%D0%B4%D0%B8%20%D0%98%D0%BA%D1%81%20%2F%20X-men%20(%D0%9B%D0%B0%D1%80%D1%80%D0%B8%20%D0%A5%D1%8C%D1%8E%D1%81%D1%82%D0%BE%D0%BD%20%2F%20Larry%20Houston)%20(%D0%A1%D0%B5%D1%80%D0%B8%D0%B8%3A%2033%2C%2071)%20%5B1992%2C%20%D0%9C%D1%83%D0%BB%D1%8C%D1%82%D1%81%D0%B5%D1%80%D0%B8%D0%B0%D0%BB%2C%20TVRip%5D
    public static class Builder {

        private List<String> dn;
        private Optional<Long> dl = Optional.empty();
        private Optional<Long> xl = Optional.empty();
        private List<Urn> xt;
        private List<URL> as;
        private List<String> xs;
        private List<String> kt;
        private List<URL> mt;
        private List<URL> tr;
        private List<Pair<String, Integer>> pe;

        public Builder() {
        }

        public MagnetLink build() {
            return new MagnetLink(dn, xl, dl, xt, as, xs, kt, mt, tr, pe);
        }

        public Builder withDisplayName(String name) {
            if (isNotEmpty(name)) {
                if (dn == null) dn = new ArrayList<>();
                dn.add(name);
            }
            return this;
        }

        public Builder withExactTopic(Urn urn) {
            if (xt == null) xt = new ArrayList<>();
            xt.add(urn);
            return this;
        }

        public Builder withAcceptableSource(URL url) {
            if (as == null) as = new ArrayList<>();
            as.add(url);
            return this;
        }

        public Builder withExactSource(String exactSource) {
            if (isNotEmpty(exactSource)) {
                if (xs == null) xs = new ArrayList<>();
                xs.add(exactSource);
            }
            return this;
        }

        public Builder withKeywordTopic(String keywordTopic) {
            if (isNotEmpty(keywordTopic)) {
                if (kt == null) kt = new ArrayList<>();
                kt.add(keywordTopic);
            }
            return this;
        }

        public Builder withManifestTopic(URL manifestTopicUrl) {
            if (mt == null) mt = new ArrayList<>();
            mt.add(manifestTopicUrl);
            return this;
        }

        public Builder withExactLength(long exactLength) {
            xl = Optional.of(exactLength);
            return this;
        }

        public Builder withDisplayLength(long displayLength) {
            dl = Optional.of(displayLength);
            return this;
        }

        public Builder withTracker(URL tracker) {
            if (tr == null) tr = new ArrayList<>();
            tr.add(tracker);
            return this;
        }

        public Builder withPeerAddress(Pair<String, Integer> peer) {
            if (pe == null) pe = new ArrayList<>();
            pe.add(peer);
            return this;
        }

    }

}
