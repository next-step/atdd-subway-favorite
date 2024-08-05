package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.line.entity.Line;
import nextstep.line.repository.LineRepository;
import nextstep.line.service.LineService;
import nextstep.member.application.MemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.member.exception.UnAuthorizedException;
import nextstep.path.service.DijkstraShortestPathService;
import nextstep.path.service.PathFinder;
import nextstep.path.service.PathService;
import nextstep.section.entity.Section;
import nextstep.section.entity.Sections;
import nextstep.section.repository.SectionRepository;
import nextstep.section.service.SectionService;
import nextstep.station.entity.Station;
import nextstep.station.repository.StationRepository;
import nextstep.station.service.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static nextstep.converter.StationConverter.stationToStationResponse;
import static nextstep.favorite.acceptance.FavoriteAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private FavoriteRepository favoriteRepository;

    private StationService stationService;
    private LineService lineService;
    private SectionService sectionService;
    private FavoriteService favoriteService;
    private MemberService memberService;
    private PathService pathService;
    private PathFinder pathFinder;

    private Station 강남역;
    private Station 역삼역;
    private Sections 신분당선_구간 = new Sections();
    private Section 강남역_역삼역_구간;
    private Line 신분당선;

    private Member 사용자;
    private LoginMember 로그인멤버;

    @BeforeEach
    public void setup() {
        stationService = new StationService(stationRepository, lineService);
        lineService = new LineService(lineRepository, stationService);
        sectionService = new SectionService(sectionRepository, stationService, lineService);
        memberService = new MemberService(memberRepository);
        pathService = new DijkstraShortestPathService();
        pathFinder = new PathFinder(stationService, lineService, pathService);
        favoriteService = new FavoriteService(favoriteRepository, memberService, stationService, pathFinder);

        강남역 = Station.of(1L, "강남역");
        역삼역 = Station.of(2L, "역삼역");

        강남역_역삼역_구간 = Section.of(1L, 강남역, 역삼역, 1L);
        신분당선_구간.addSection(강남역_역삼역_구간);
        신분당선 = Line.of(1L, "신분당선", "Red", 10L, 신분당선_구간);

        사용자 = Member.of(1L, EMAIL, PASSWORD, AGE);
        로그인멤버 = new LoginMember(EMAIL);

    }

    @DisplayName("[createFavorite] 즐겨찾기를 생성한다.")
    @Test
    public void createFavorite_success() {
        // given
        var 즐겨찾기 = Favorite.of(1L, 사용자, 강남역, 역삼역);
        var 즐겨찾기_요청_강남역_역삼역 = FavoriteRequest.of(강남역.getId(), 역삼역.getId());

        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(강남역));
        when(stationRepository.findById(2L)).thenReturn(Optional.ofNullable(역삼역));
        when(memberRepository.findByEmail(로그인멤버.getEmail())).thenReturn(Optional.of(사용자));
        when(lineRepository.findAll()).thenReturn(List.of(신분당선));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(즐겨찾기);

        // when
        favoriteService.createFavorite(로그인멤버, 즐겨찾기_요청_강남역_역삼역);

        // then
        verify(favoriteRepository, times(1)).save(any(Favorite.class));

    }

    @DisplayName("[createFavorite] 즐겨찾기를 생성 실패한다. 사용자 인증 정보가 없다.")
    @Test
    public void createFavorite_fail() {
        // given
        var 즐겨찾기_요청_강남역_역삼역 = FavoriteRequest.of(강남역.getId(), 역삼역.getId());

        when(memberRepository.findByEmail(로그인멤버.getEmail())).thenReturn(Optional.ofNullable(null));

        // when
        assertAll(
                () -> assertThrows(UnAuthorizedException.class, () -> favoriteService.createFavorite(로그인멤버, 즐겨찾기_요청_강남역_역삼역))
        );

    }

    @DisplayName("[findFavorites] 즐겨찾기를 조회한다.")
    @Test
    public void findFavorites_success() {
        // given
        var 즐겨찾기 = Favorite.of(1L, 사용자, 강남역, 역삼역);
        var 강남역_응답 = stationToStationResponse(강남역);
        var 역삼역_응답 = stationToStationResponse(역삼역);
        var 즐겨찾기_조회_응답_예상 = FavoriteResponse.of(1L, 강남역_응답, 역삼역_응답);

        when(memberRepository.findByEmail(로그인멤버.getEmail())).thenReturn(Optional.of(사용자));
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(List.of(즐겨찾기));

        // when
        var 즐겨찾기_조회_응답 = favoriteService.findFavorites(로그인멤버);

        // then
        assertAll(
                () -> assertThat(즐겨찾기_조회_응답).containsExactlyInAnyOrder(즐겨찾기_조회_응답_예상),
                () -> assertThat(즐겨찾기_조회_응답.size()).isEqualTo(1)
        );

    }

    @DisplayName("[findFavorites] 로그인한 사용자 정보를 찾을 수 없다.")
    @Test
    public void findFavorites_fail1() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.ofNullable(null));

        // when & then
        assertAll(
                () -> assertThrows(UnAuthorizedException.class, () -> favoriteService.findFavorites(로그인멤버))
        );

    }

    @DisplayName("[deleteFavorite] 즐겨찾기를 삭제한다.")
    @Test
    public void deleteFavorite_success() {
        // given
        var 즐겨찾기 = Favorite.of(1L, 사용자, 강남역, 역삼역);

        when(memberRepository.findByEmail(로그인멤버.getEmail())).thenReturn(Optional.of(사용자));
        when(favoriteRepository.findByIdAndMemberId(즐겨찾기.getId(), 사용자.getId())).thenReturn(Optional.of(즐겨찾기));

        // when
        favoriteService.deleteFavorite(로그인멤버, 즐겨찾기.getId());

        // then
        verify(favoriteRepository, times(1)).deleteById(즐겨찾기.getId());

    }

    @DisplayName("[deleteFavorite] 로그인한 사용자 정보를 찾을 수 없다. ")
    @Test
    public void deleteFavorite_fail1() {
        // given
        when(memberRepository.findByEmail(로그인멤버.getEmail())).thenReturn(Optional.ofNullable(null));

        // when & then
        assertAll(
                () -> assertThrows(UnAuthorizedException.class, () -> favoriteService.deleteFavorite(로그인멤버, anyLong()))
        );

    }

    @DisplayName("[deleteFavorite] 로그인 사용자가 해당 즐겨찾기 ID를 소유하고 있지 않다. ")
    @Test
    public void deleteFavorite_fail2() {
        // given
        var 존재하지_않는_즐겨찾기_ID = 1L;

        when(memberRepository.findByEmail(로그인멤버.getEmail())).thenReturn(Optional.of(사용자));
        when(favoriteRepository.findByIdAndMemberId(존재하지_않는_즐겨찾기_ID, 사용자.getId())).thenReturn(Optional.ofNullable(null));

        // when & then
        assertAll(
                () -> assertThrows(UnAuthorizedException.class, () -> favoriteService.deleteFavorite(로그인멤버, 존재하지_않는_즐겨찾기_ID))
        );

    }
}
