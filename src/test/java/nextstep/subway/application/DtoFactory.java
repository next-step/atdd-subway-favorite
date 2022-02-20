package nextstep.subway.application;

import nextstep.subway.application.dto.FavouriteRequest;
import nextstep.subway.application.dto.member.MemberRequest;

public class DtoFactory {
    private static final String CHANGED_PASSWORD = "password123";
    public static final String CHANGED_EMAIL = "test@email.com";
    public static final int CHANGED_AGE = 20;


    public static MemberRequest createMemberRequest() {
        return new MemberRequest(CHANGED_EMAIL, CHANGED_PASSWORD, CHANGED_AGE);
    }

    public static FavouriteRequest createFavouriteRequest(Long source, Long target) {
        return new FavouriteRequest(source, target);
    }
}
