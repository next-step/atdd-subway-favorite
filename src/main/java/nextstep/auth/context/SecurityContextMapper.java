package nextstep.auth.context;

import java.util.List;


public class SecurityContextMapper {

    private SecurityContextMapper() {
    }

    public static void setContext(String email, List<String> authorities) {
        Authentication authentication = new Authentication(email, authorities);
        SecurityContext securityContext = new SecurityContext(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

}
