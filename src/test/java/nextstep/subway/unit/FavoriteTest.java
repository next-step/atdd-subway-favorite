package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.Station;

class FavoriteTest {

    private Member 본인;
    private Member 타인;
    private Station 수서역;
    private Station 복정역;

    @BeforeEach
    void setUp() {
        본인 = new Member("member1@gmail.com", "pass1234", 20, List.of(RoleType.ROLE_MEMBER.name()));
        타인 = new Member("member2@gmail.com", "pass5678", 30, List.of(RoleType.ROLE_MEMBER.name()));
        수서역 = new Station("수서역");
        복정역 = new Station("복정역");

        ReflectionTestUtils.setField(본인, "id", 1L);
        ReflectionTestUtils.setField(타인, "id", 2L);
        ReflectionTestUtils.setField(수서역, "id", 1L);
        ReflectionTestUtils.setField(복정역, "id", 2L);
    }

    @DisplayName("즐겨찾기 구간을 특정 회원이 생성했는지의 여부를 확인한다.")
    @Test
    void isCreatedBy() {
        Favorite favorite = new Favorite(본인.getId(), 수서역.getId(), 복정역.getId());

        assertAll(
            () -> assertThat(favorite.isCreatedBy(본인.getId())).isTrue(),
            () -> assertThat(favorite.isCreatedBy(타인.getId())).isFalse()
        );
    }
}
