package atdd.path;

import atdd.path.domain.User;

public class UserTestConstant {
    public static final Long USER_ID_1 = 1L;
    public static final Long USER_ID_2 = 1L;

    public static final String USER_EMAIL_1 = "user1@aaa.com";
    public static final String USER_EMAIL_2 = "user2@aaa.com";

    public static final String USER_NAME_1 = "user1";
    public static final String USER_NAME_2 = "user2";

    public static final String USER_PASSWORD_1 = "user1";
    public static final String USER_PASSWORD_2 = "user2";

    public static User TEST_USER_1 = new User(USER_ID_1, USER_EMAIL_1, USER_NAME_1, USER_PASSWORD_1);
    public static User TEST_USER_2 = new User(USER_ID_2, USER_EMAIL_2, USER_NAME_2, USER_PASSWORD_2);


}
