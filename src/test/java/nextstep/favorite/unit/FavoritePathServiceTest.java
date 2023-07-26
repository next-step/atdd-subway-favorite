package nextstep.favorite.unit;


import nextstep.favorite.domain.FavoritePath;
import nextstep.favorite.domain.FavoritePathRepository;
import nextstep.favorite.service.FavoritePathService;
import nextstep.favorite.service.dto.FavoritePathRequest;
import nextstep.favorite.service.dto.FavoritePathResponse;
import nextstep.favorite.unit.fixture.FavoritePathSpec;
import nextstep.member.fixture.MemberSpec;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.service.StationPathService;
import nextstep.subway.service.dto.StationPathResponse;
import nextstep.subway.service.dto.StationResponse;
import nextstep.subway.unit.fixture.StationSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class FavoritePathServiceTest {
    @InjectMocks
    private FavoritePathService favoritePathService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationPathService stationPathService;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private FavoritePathRepository favoritePathRepository;

    private String email;
    private Member member;

    @BeforeEach
    public void setUp() {
        //given
        email = "yuseongan@next.com";
        member = MemberSpec.of();
    }

    @DisplayName("정상적인 즐겨찾기 경로 추가")
    @Test
    void createFavoritePathServiceTest() {
        //given
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        final Long source = 1L;
        final Long target = 2L;
        final List<StationResponse> stationResponses = StationSpec.of(List.of("source", "target")).stream()
                .map(StationResponse::fromEntity)
                .collect(Collectors.toList());
        final StationPathResponse stationPathResponse = StationPathResponse.builder()
                .stations(stationResponses)
                .distance(BigDecimal.TEN)
                .build();

        given(favoritePathRepository.save(any(FavoritePath.class))).willReturn(FavoritePathSpec.of(member, source, target));
        given(stationPathService.searchStationPath(source, target)).willReturn(stationPathResponse);

        //when
        final FavoritePathRequest request = new FavoritePathRequest();
        request.setSource(source);
        request.setTarget(target);

        favoritePathService.createFavoritePath(email, request);

        //then
        then(memberRepository).should(times(1)).findByEmail(email);
        then(favoritePathRepository).should(times(1)).save(any(FavoritePath.class));
        then(stationPathService).should(times(1)).searchStationPath(source, target);
    }

    @DisplayName("정상적인 즐겨찾기 경로 목록 조회")
    @Test
    void getFavoritePaths() {
        //given
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        final Long sourceId = 1L;
        final Long targetId = 2L;
        final FavoritePath favoritePath = FavoritePathSpec.of(member, sourceId, targetId);
        given(favoritePathRepository.findAllByMember(any(Member.class))).willReturn(List.of(favoritePath));

        given(stationRepository.findAllById(List.of(1L, 2L))).willReturn(StationSpec.of(List.of("source", "target")));

        //when
        final List<FavoritePathResponse> response = favoritePathService.getFavoritePaths(email);

        //then
        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());

        then(memberRepository).should(times(1)).findByEmail(email);
        then(favoritePathRepository).should(times(1)).findAllByMember(any(Member.class));
        then(stationRepository).should(times(1)).findAllById(anyList());
    }

    @DisplayName("정상적인 즐겨찾기 경로 삭제")
    @Test
    void deleteFavoritePath() {
        //given
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        final FavoritePath favoritePath = FavoritePathSpec.of(member, 1L, 2L);
        given(favoritePathRepository.findByIdAndMember(anyLong(), any(Member.class))).willReturn(Optional.of(favoritePath));

        //when
        favoritePathService.deleteFavoritePath(favoritePath.getId(), email);

        //then
        then(memberRepository).should(times(1)).findByEmail(email);
        then(favoritePathRepository).should(times(1)).findByIdAndMember(anyLong(), any(Member.class));
        then(favoritePathRepository).should(times(1)).delete(any(FavoritePath.class));
    }
}