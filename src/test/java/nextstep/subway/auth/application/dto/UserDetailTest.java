package nextstep.subway.auth.application.dto;

import nextstep.subway.testhelper.fixture.MemberFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserDetailTest {

    @Test
    @DisplayName("UserDetail 을 생성한다")
    void createUserDetail() {
        Assertions.assertDoesNotThrow(() ->new UserDetail(MemberFixture.EMAIL, MemberFixture.PASSWORD,
                MemberFixture.AGE));
    }

}
