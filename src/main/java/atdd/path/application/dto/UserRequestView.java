package atdd.path.application.dto;

import lombok.Builder;
import lombok.experimental.Accessors;

@Builder
@Accessors(chain = true)
public class UserRequestView {
    String email;
    String name;
    String password;

    public String getEmail() { return email; }

    public String getName() { return name; }

    public String getPassword() { return password; }
}
