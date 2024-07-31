package nextstep.subway.entity;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 단위테스트")
class FavoriteTest {

    @Test
    @DisplayName("즐겨찾기 유저 일치 확인")
    void 즐겨찾기_유저_일치() {
        // given
        Member 즐겨찾기_주인 = new Member("pilming44@naver.com", "1111", 30);
        Favorite 즐겨찾기 = new Favorite(즐겨찾기_주인, new Station("강남역"), new Station("양재역"));

        // when
        boolean 일치여부 = 즐겨찾기.isSameMember(즐겨찾기_주인);

        // then
        assertThat(일치여부).isTrue();
    }

    @Test
    @DisplayName("즐겨찾기 유저 불일치 확인")
    void 즐겨찾기_유저_불일치() {
        // given
        Member 즐겨찾기_주인 = new Member("pilming44@naver.com", "1111", 30);
        Favorite 즐겨찾기 = new Favorite(즐겨찾기_주인, new Station("강남역"), new Station("양재역"));
        Member 다른_사람 = new Member("pilming@naver.com", "1234", 60);
        // when
        boolean 일치여부 = 즐겨찾기.isSameMember(다른_사람);

        // then
        assertThat(일치여부).isFalse();
    }
}