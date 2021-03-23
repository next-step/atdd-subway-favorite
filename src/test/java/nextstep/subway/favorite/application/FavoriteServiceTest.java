package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.exception.IsExistFavoriteException;
import nextstep.subway.favorite.exception.NotFoundFavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Favorites 단위 테스트")
public class FavoriteServiceTest {
    private final Member member = new Member("email@email.com", "username", 20);
    private final Station source  = new Station("source");
    private final Station target  = new Station("target");

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(member, "id", 1L);
        ReflectionTestUtils.setField(source, "id", 1L);
        ReflectionTestUtils.setField(target, "id", 2L);
    }

//    @DisplayName("즐겨찾기를 추가한다.")
//    @Test
//    public void 즐겨찾기_추가_테스트() {
//        // given
//        Favorites favorites = new Favorites();
//
//        // when
//        favorites.add(member, source, target);
//
//        // then
//        Favorite given = favorites.stream().findFirst().orElse(null);
//        assertThat(given).isNotNull();
//        assertThat(given.getMember()).isEqualTo(member);
//        assertThat(given.getTarget()).isEqualTo(target);
//        assertThat(given.getSource()).isEqualTo(source);
//    }
//
//    @DisplayName("즐겨찾기를 중복으로 추가할 경우 Exception이 발생한.")
//    @Test
//    public void 즐겨찾기_중복_추가_테스트() {
//        // given
//        Favorites favorites = new Favorites();
//        favorites.add(member, source, target);
//
//        // when - then
//        assertThatExceptionOfType(IsExistFavoriteException.class)
//            .isThrownBy(() -> favorites.add(member, source, target));
//    }
//
//    @DisplayName("즐겨찾기 목록을 조회한다.")
//    @Test
//    public void 즐겨찾기_목록_조회_테스트() {
//        // when
//        Favorites favorites = new Favorites();
//        favorites.add(member, source, target);
//
//        // then
//        assertThat(favorites.stream().collect(Collectors.toList()))
//            .isEqualTo(Collections.singletonList(new Favorite(member, source, target)));
//    }
//
//    @DisplayName("즐겨찾기를 삭제한다.")
//    @Test
//    public void 즐겨찾기_삭제_테스트() {
//        // given
//        Favorites favorites = new Favorites();
//        favorites.add(member, source, target);
//        Favorite favorite = favorites.stream()
//                                     .findFirst()
//                                     .orElseThrow(NotFoundFavoriteException::new);
//        ReflectionTestUtils.setField(favorite, "id", 1L);
//
//        // when
//        favorites.remove(1L);
//
//        // then
//        assertThat(favorites.stream().count()).isEqualTo(0L);
//
//    }
//
//    @DisplayName("존재하지 않는 즐겨찾기를 삭제할 경우 Exception이 발생한다.")
//    @Test
//    public void 즐겨찾기_삭제_실패_테스트() {
//        // given
//        Favorites favorites = new Favorites();
//        favorites.add(member, source, target);
//        Favorite favorite = favorites.stream()
//            .findFirst()
//            .orElseThrow(NotFoundFavoriteException::new);
//        ReflectionTestUtils.setField(favorite, "id", 1L);
//
//        // when - then
//        assertThatExceptionOfType(NotFoundFavoriteException.class)
//            .isThrownBy(() -> favorites.remove(2L));
//
//    }
}
