package nextstep.subway.unit;

import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.RoleType;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotDeleteFavoriteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static nextstep.subway.domain.fixture.StationFixture.GANGNAM;
import static nextstep.subway.domain.fixture.StationFixture.YEOKSAM;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    @Mock
    private MemberService memberService;
    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private LoginMember loginMember;
    private Member member;
    private Member owner;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        loginMember = new LoginMember("member@email.com", null, List.of(RoleType.ROLE_MEMBER.name()));
        member = new Member(1L, "member@email.com", "password", 20, List.of(RoleType.ROLE_MEMBER.name()));
        owner = new Member(2L, "onwner@email.com", "password", 25, List.of(RoleType.ROLE_MEMBER.name()));
        favorite = createFavorite(1L, owner.getId(), GANGNAM, YEOKSAM);
    }

    @Test
    @DisplayName("즐겨찾기의 소유자만 즐겨찾기를 삭제할 수 있다.")
    void invalid_delete() {
        when(memberService.findMember(loginMember.getEmail())).thenReturn(member);
        when(favoriteRepository.findById(favorite.id())).thenReturn(Optional.of(favorite));

        assertThatThrownBy(() -> favoriteService.delete(loginMember, favorite.id()))
                .isInstanceOf(NotDeleteFavoriteException.class)
                .hasMessage("즐겨찾기 소유자가 아니여서 삭제할 수 없습니다. memberId=1");
    }

    private Favorite createFavorite(Long id, Long memberId, Station source, Station target) {
        Favorite favorite = Favorite.create(memberId, source, target);
        ReflectionTestUtils.setField(favorite, "id", id);
        return favorite;
    }
}
