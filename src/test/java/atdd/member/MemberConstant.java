package atdd.member;

import atdd.member.application.dto.CreateMemberRequestView;
import atdd.member.application.dto.LoginMemberRequestView;

public class MemberConstant {

    public static final String MEMBER_BASE_URL = "members";
    public static final String MEMBER_EMAIL = "seok2@naver.com";
    public static final String MEMBER_NAME = "이재석";
    private static final String MEMBER_PASSWORD = "1234";
    public static final CreateMemberRequestView MEMBER_VIEW = CreateMemberRequestView.of(MEMBER_EMAIL, MEMBER_NAME,
        MEMBER_PASSWORD);
    public static final LoginMemberRequestView LOGIN_MEMBER_VIEW = new LoginMemberRequestView(MEMBER_EMAIL,
        MEMBER_PASSWORD);


}
