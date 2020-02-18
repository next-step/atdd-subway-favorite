package atdd.path.fixture;

import atdd.path.application.dto.UserSighUpRequestView;
import atdd.path.domain.User;

public class UserFixture {
    public static final String KIM_NAME = "김상구";
    public static final String KIM_EMAIL = "sgkim94@github.com";
    public static final String KIM_PASSWORD = "password";
    public static final UserSighUpRequestView USER_SIGH_UP_REQUEST_DTO = new UserSighUpRequestView(KIM_EMAIL, KIM_NAME, KIM_PASSWORD);
    public static final User NEW_USER = new User(0L, KIM_EMAIL, KIM_NAME, KIM_PASSWORD);
}
