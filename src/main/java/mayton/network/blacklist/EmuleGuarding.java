package mayton.network.blacklist;

public final class EmuleGuarding {

    public final String beginIp;
    public final String bendIp;
    public final int    rank;
    public final String name;

    public EmuleGuarding(String beginIp, String bendIp, int rank, String name) {
        this.beginIp = beginIp;
        this.bendIp = bendIp;
        this.rank = rank;
        this.name = name;
    }

    @Override
    public String toString() {
        return "EmuleGuardingEntity{" +
                "beginIp='" + beginIp + '\'' +
                ", bendIp='" + bendIp + '\'' +
                ", rank=" + rank +
                ", name='" + name + '\'' +
                '}';
    }
}
