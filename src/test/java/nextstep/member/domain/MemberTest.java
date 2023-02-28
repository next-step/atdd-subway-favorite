package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.member.application.exception.UnAuthorizedException;
import nextstep.member.domain.exception.PasswordMismatchException;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("멤버 관련 기능")
class MemberTest {

    @DisplayName("멤버 비밀번호가 일치하지 않으면 예외 처리한다.")
    @Test
    void validatePassword() {
        Member member = new Member("admin@gmail.com", "1234", 25);

        assertAll(
                () -> assertThatCode(() -> member.validatePassword("1234"))
                        .doesNotThrowAnyException(),
                () -> assertThatThrownBy(() -> member.validatePassword("1235"))
                        .isInstanceOf(PasswordMismatchException.class)
        );
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    public void add() {
        Member member = new Member();
        Favorite favorite = new Favorite(new Station("강남역"), new Station("양재역"));

        member.addFavorite(favorite);

        assertThat(member.getFavorites()).hasSize(1).containsExactly(favorite);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    public void delete() {
        Member member = new Member();
        Favorite favorite = new Favorite(new Station("강남역"), new Station("양재역"));
        ReflectionTestUtils.setField(favorite, "id", 1L);
        member.addFavorite(favorite);

        member.deleteFavorite(1L);

        assertThat(member.getFavorites()).hasSize(0).doesNotContain(favorite);
    }

    @DisplayName("즐겨찾기 삭제시 포함되지 않은 아이디이면 예외처리힌다.")
    @Test
    public void deleteDoesNotContainId() {
        Member member = new Member();
        Favorite favorite = new Favorite(new Station("강남역"), new Station("양재역"));
        ReflectionTestUtils.setField(favorite, "id", 1L);
        member.addFavorite(favorite);

        assertThatThrownBy(() -> member.deleteFavorite(3L)).isInstanceOf(UnAuthorizedException.class);
    }
}
