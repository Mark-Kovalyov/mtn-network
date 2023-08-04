package mayton.network.dns;

import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public interface Resolvable {

    Optional<String> resolvePtr(@NotNull String input);

}
