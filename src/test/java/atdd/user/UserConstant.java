package atdd.user;

import atdd.user.application.dto.LoginUserRequestView;
import atdd.user.domain.User;

public class UserConstant {

    public static final Long USER_ID = 1L;
    public static final Long USER_ID_2 = 2L;

    public static final String USER_NAME = "브랴운";
    public static final String gUSER_NAME_2 = "크리스";

    public static final String USER_PASSWORD = "123@";
    public static final String USER_PASSWORD_2 = "345@";

    public static final String USER_EMAIL = "brown12@naver.com";
    public static final String USER_EMAIL_2 = "chris34@gmail.com";


    public static final User TEST_USER = User.builder()
            .name(USER_NAME)
            .password(USER_PASSWORD)
            .email(USER_EMAIL)
            .build();

    public static final User TEST_USER_2 = User.builder()
            .name(gUSER_NAME_2)
            .password(USER_PASSWORD_2)
            .email(USER_EMAIL_2)
            .build();

    public static final LoginUserRequestView USER_REQEUST_VIEW = new LoginUserRequestView(USER_PASSWORD, USER_EMAIL);

}
