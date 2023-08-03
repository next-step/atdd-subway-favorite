package nextstep.favorite.domain;


import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 도메인 테스트")
class FavoriteTest {

    private Member member;
    private Station source, target;
    @BeforeEach
    void setUp() {
        member = createMember();
        source = createStation("강남역");
        target = createStation("역삼역");
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {
        // given : 선행조건 기술

        // when : 기능 수행
        Favorite favorite = new Favorite(member, source, target);

        // then : 결과 확인
        assertThat(favorite.getMember()).isEqualTo(member);
        assertThat(favorite.getSource()).isEqualTo(source);
        assertThat(favorite.getTarget()).isEqualTo(target);
    }

    private Member createMember() {
        return new Member(
                "email@email.com",
                "password",
                20,
                "ROLE_MEMBER"
        );
    }

    private Station createStation(String name) {
        return new Station(name);
    }
}