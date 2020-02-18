package atdd.path.fixture;

import atdd.path.application.dto.UserSighUpRequestView;
import atdd.path.domain.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFixture {
    public static final String KIM_NAME = "김상구";
    public static final String KIM_EMAIL = "sgkim94@github.com";
    public static final String KIM_PASSWORD = "password";
    public static final UserSighUpRequestView USER_SIGH_UP_REQUEST_DTO = new UserSighUpRequestView(KIM_EMAIL, KIM_NAME, KIM_PASSWORD);
    public static final User NEW_USER = new User(1L, KIM_EMAIL, KIM_NAME, KIM_PASSWORD);

    public static List<Map<String, Object>> getDaoUser() {
        Map<String, Object> savedUser = new HashMap<>();
        savedUser.put("ID", 1L);
        savedUser.put("NAME", KIM_NAME);
        savedUser.put("EMAIL", KIM_EMAIL);
        return Collections.singletonList(savedUser);
    }

}
