package atdd.path.fixture;

import atdd.path.application.dto.user.FindByEmailResponseView;
import atdd.path.application.dto.user.UserLoginRequestView;
import atdd.path.application.dto.user.UserSighUpRequestView;
import atdd.path.domain.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static atdd.path.dao.UserDao.*;

public class UserFixture {
    public static final Long KIM_ID = 1L;
    public static final String KIM_NAME = "김상구";
    public static final String KIM_EMAIL = "sgkim94@github.com";
    public static final String KIM_PASSWORD = "password";
    public static final UserSighUpRequestView USER_SIGH_UP_REQUEST_DTO = new UserSighUpRequestView(KIM_EMAIL, KIM_NAME, KIM_PASSWORD);
    public static final UserLoginRequestView USER_LOGIN_REQUEST_DTO = new UserLoginRequestView(KIM_EMAIL, KIM_PASSWORD);
    public static final FindByEmailResponseView FIND_BY_EMAIL_RESPONSE_VIEW = new FindByEmailResponseView(KIM_ID, KIM_EMAIL, KIM_PASSWORD);
    public static final User NEW_USER = new User(KIM_ID, KIM_EMAIL, KIM_NAME, KIM_PASSWORD);

    public static final User LOGIN_USER = User.builder().email(KIM_EMAIL).password(KIM_PASSWORD).build();

    public static List<Map<String, Object>> getDaoUsers() {
        return Collections.singletonList(getDaoUser());
    }

    public static Map<String, Object> getDaoUser() {
        Map<String, Object> savedUser = new HashMap<>();
        savedUser.put(ID_KEY, KIM_ID);
        savedUser.put(NAME_KEY, KIM_NAME);
        savedUser.put(EMAIL_KEY, KIM_EMAIL);
        return savedUser;
    }

}
