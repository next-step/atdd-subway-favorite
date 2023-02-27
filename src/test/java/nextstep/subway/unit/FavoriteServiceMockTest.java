package nextstep.subway.unit;

import nextstep.auth.LoginMember;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.member.domain.exception.FavoriteIsNotYoursException;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteRequest;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.Favorite;
import nextstep.subway.domain.FavoriteRepository;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FavoriteServiceMockTest extends MockTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;

    private FavoriteService favoriteService;

    private Long memberId;
    private Member member;
    private LoginMember loginMember;

    private Long sourceId;
    private Long targetId;
    private Station source;
    private Station target;

    private FavoriteRequest favoriteRequest;
    private FavoriteResponse favoriteResponse;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationService, memberService);

        memberId = 1L;
        member = new Member("admin@email.com");
        ReflectionTestUtils.setField(member, "id", memberId);
        loginMember = new LoginMember(memberId);

        sourceId = 1L;
        targetId = 2L;

        source = new Station("source");
        target = new Station("target");
        ReflectionTestUtils.setField(source, "id", sourceId);
        ReflectionTestUtils.setField(target, "id", targetId);

        favoriteRequest = new FavoriteRequest(sourceId, targetId);
        favorite = new Favorite(source.getId(), target.getId(), member.getId());
        ReflectionTestUtils.setField(favorite, "id", 1L);
        favoriteResponse = new FavoriteResponse(favorite, source, target);
    }

    @DisplayName("로그인 후 즐겨찾기를 추가할 수 있다.")
    @Test
    void addFavorite() {
        given(stationService.findById(sourceId)).willReturn(source);
        given(stationService.findById(targetId)).willReturn(target);
        given(memberService.findById(memberId)).willReturn(member);
        given(favoriteRepository.save(any(Favorite.class))).willAnswer(invocation -> {
            var savedFavorite = invocation.getArgument(0, Favorite.class);
            ReflectionTestUtils.setField(savedFavorite, "id", favorite.getId());
            return savedFavorite;
        });

        final var response = favoriteService.addFavorite(loginMember, favoriteRequest);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getSource().getId()).isEqualTo(sourceId),
                () -> assertThat(response.getTarget().getId()).isEqualTo(targetId)
        );

        verify(favoriteRepository, times(1)).save(any(Favorite.class));
    }

    @DisplayName("경로 즐겨찾기를 추가하고 즐겨찾기 목록을 조회하면 추가한 즐겨찾기를 찾을 수 있다.")
    @Test
    void findFavorites() {
        given(stationService.findById(sourceId)).willReturn(source);
        given(stationService.findById(targetId)).willReturn(target);
        given(memberService.findById(memberId)).willReturn(member);
        given(favoriteRepository.findByMemberId(member.getId())).willReturn(List.of(favorite));

        final var favorites = favoriteService.findFavorites(loginMember);

        assertThat(favorites).containsExactly(favoriteResponse);
    }

    @DisplayName("즐겨찾기로 추가한 경로를 삭제할 수 있다.")
    @Test
    void removeFavorite() {
        given(memberService.findById(memberId)).willReturn(member);
        given(favoriteRepository.findById(favorite.getId())).willReturn(Optional.of(favorite));

        favoriteService.removeFavorite(loginMember, favorite.getId());

        //assertThat(member.getFavorites()).isEmpty();
    }

    @DisplayName("다른 사람의 즐겨찾기를 삭제하면 오류가 발생한다.")
    @Test
    void removeOtherMemberFavorite() {
        given(memberService.findById(memberId)).willReturn(new Member("aonther@email.com"));
        given(favoriteRepository.findById(favorite.getId())).willReturn(Optional.of(favorite));

        assertThatThrownBy(() -> favoriteService.removeFavorite(loginMember, favorite.getId()))
                .isInstanceOf(FavoriteIsNotYoursException.class);
    }
}
