package atdd.path.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Builder
@Accessors(chain = true)
public class User {
    Long id;
    String email;
    String name;
    String password;
}
