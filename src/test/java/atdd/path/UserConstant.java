package atdd.path;

import atdd.path.domain.User;

public class UserConstant {

    public static final Long USER_ID = 1L;
    public static final Long USER_ID_2 = 2L;

    public static final String USER_NAME = "브랴운";
    public static final String gUSER_NAME_2 = "크리스";

    public static final String USER_PASSWORD = "123@";
    public static final String USER_PASSWORD_2 = "345@";
    public static final User TEST_USER = User.builder()
            .name(USER_NAME)
            .password(USER_PASSWORD)
            .build();

    public static final User TEST_USER_2 = User.builder()
            .name(gUSER_NAME_2)
            .password(USER_PASSWORD_2)
            .build();

}
