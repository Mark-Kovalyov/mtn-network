package mayton.network.blacklist;

import java.util.Optional;

public interface EmuleGuardingService {

    Optional<EmuleGuarding> lookup(String ip);

}
