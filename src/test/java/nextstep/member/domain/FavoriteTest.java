package nextstep.member.domain;

import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {

    @DisplayName("즐겨찾기 추가")
    @Test
    void addFavorite() {
        Station source = new Station("강남역");
        Station target = new Station("양재역");

        Member user = new Member("email@email.com", "password", 10);

        user.addFavorite(Favorite.of(source, target));

        List<Favorite> allFavorites = user.allFavorites();

        assertAll(
                () -> assertThat(allFavorites).hasSize(1),
                () -> assertThat(allFavorites).extracting("source").containsExactly(source),
                () -> assertThat(allFavorites).extracting("target").containsExactly(target)
        );
    }

    @DisplayName("여러 명의 사용자가 즐겨찾기 추가")
    @Test
    void multipleUserAddFavorite() {
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");

        Member user1 = new Member("email1@email.com", "password", 10);
        Member user2 = new Member("email2@email.com", "password", 15);

        user1.addFavorite(Favorite.of(강남역, 양재역));
        user1.addFavorite(Favorite.of(양재역, 정자역));

        user2.addFavorite(Favorite.of(강남역, 정자역));

        List<Favorite> firstFavorites = user1.allFavorites();
        List<Favorite> secondFavorites = user2.allFavorites();

        assertAll(
                () -> assertThat(firstFavorites).hasSize(2),
                () -> assertThat(firstFavorites).extracting("source").containsExactly(강남역, 양재역),
                () -> assertThat(firstFavorites).extracting("target").containsExactly(양재역, 정자역),
                () -> assertThat(secondFavorites).hasSize(1),
                () -> assertThat(secondFavorites).extracting("source").containsExactly(강남역),
                () -> assertThat(secondFavorites).extracting("target").containsExactly(정자역)
        );
    }
}