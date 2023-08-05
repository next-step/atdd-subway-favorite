package nextstep.favorite.unit;

import nextstep.favorite.adapters.persistence.FavoriteJpaAdapter;
import nextstep.favorite.dto.request.FavoriteRequest;
import nextstep.favorite.repository.FavoriteRepository;
import nextstep.favorite.service.FavoriteService;
import nextstep.global.error.code.ErrorCode;
import nextstep.global.error.exception.InvalidFavoriteException;
import nextstep.global.error.exception.InvalidPathException;
import nextstep.global.error.exception.NotEntityFoundException;
import nextstep.member.adapters.persistence.MemberJpaAdapter;
import nextstep.member.repository.MemberRepository;
import nextstep.subway.line.adapters.persistence.LineJpaAdapter;
import nextstep.subway.line.entity.Line;
import nextstep.subway.line.repository.LineRepository;
import nextstep.subway.path.service.PathService;
import nextstep.subway.section.entity.Section;
import nextstep.subway.station.adapters.persistence.StationJpaAdapter;
import nextstep.subway.station.repository.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static nextstep.member.fixture.MemberFixture.회원_정보;
import static nextstep.subway.fixture.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceMockTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    LineRepository lineRepository;

    @Mock
    StationRepository stationRepository;

    @Mock
    FavoriteRepository favoriteRepository;

    private FavoriteService favoriteService;

    private static final Long 까치산역_아이디 = 까치산역.getId();

    private static final Long 신도림역_아이디 = 신도림역.getId();

    private static final Long 신촌역_아이디 = 신촌역.getId();

    private static final Long 잠실역_아이디 = 잠실역.getId();

    private Line 이호선;

    /**
     * <pre>
     *            신촌역    -------------------   *2호선(19)*
     *            |                               |
     *            *2호선(6)*                      |
     *            |                              |
     * 까치산역    신도림역  --- *2호선(18)* --- 잠실역
     * </pre>
     */
    @BeforeEach
    void setUp() {
        MemberJpaAdapter memberJpaAdapter = new MemberJpaAdapter(memberRepository);
        LineJpaAdapter lineJpaAdapter = new LineJpaAdapter(lineRepository);
        StationJpaAdapter stationJpaAdapter = new StationJpaAdapter(stationRepository);
        FavoriteJpaAdapter favoriteJpaAdapter = new FavoriteJpaAdapter(favoriteRepository);
        favoriteService = new FavoriteService(
                memberJpaAdapter,
                stationJpaAdapter,
                favoriteJpaAdapter,
                new PathService(stationJpaAdapter, lineJpaAdapter));

        // given
        이호선 = Line.builder()
                .name("2호선")
                .color("#52c41a")
                .upStation(신도림역)
                .downStation(신촌역)
                .distance(6)
                .build();

        Section 신촌역_잠실역_구간 = Section.builder()
                .upStation(신촌역)
                .downStation(잠실역)
                .distance(19)
                .build();
        Section 잠실역_신도림역_구간 = Section.builder()
                .upStation(잠실역)
                .downStation(신도림역)
                .distance(18)
                .build();

        Stream.of(신촌역_잠실역_구간, 잠실역_신도림역_구간).forEach(이호선::addSection);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경로를 즐겨찾기에 등록하려할 때 실패한다.")
    void createFavoriteSameDepartureAndArrivalStations() {
        // given
        FavoriteRequest 출발역_신촌역_도착역_신촌역_요청 = FavoriteRequest.builder()
                .source(신촌역_아이디)
                .target(신촌역_아이디)
                .build();

        // when & then
        assertThatThrownBy(() -> favoriteService.saveFavorite(회원_정보.getEmail(), 출발역_신촌역_도착역_신촌역_요청))
                .isInstanceOf(InvalidFavoriteException.class)
                .hasMessageContaining(ErrorCode.SAME_DEPARTURE_AND_ARRIVAL_STATIONS.getMessage());
    }

    @Test
    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경로를 즐겨찾기에 등록하면 실패한다.")
    void createFavoriteUnlinkedDepartureAndArrivalStations() {
        // given
        given(stationRepository.findById(신촌역_아이디)).willReturn(Optional.of(신촌역));
        given(stationRepository.findById(까치산역_아이디)).willReturn(Optional.of(까치산역));
        given(stationRepository.findAll()).willReturn(
                List.of(까치산역, 신도림역, 신촌역, 잠실역)
        );
        given(lineRepository.findAll()).willReturn(List.of(이호선));

        FavoriteRequest 출발역_신촌역_도착역_까치산역_요청 = FavoriteRequest.builder()
                .source(신촌역_아이디)
                .target(까치산역_아이디)
                .build();

        // when & then
        assertThatThrownBy(() -> favoriteService.saveFavorite(회원_정보.getEmail(), 출발역_신촌역_도착역_까치산역_요청))
                .isInstanceOf(InvalidPathException.class)
                .hasMessageContaining(ErrorCode.UNLINKED_DEPARTURE_AND_ARRIVAL_STATIONS.getMessage());
    }

    @Test
    @DisplayName("존재하지 않은 회원이 즐겨찾기 등록을 하려하면 실패한다.")
    void createFavoriteWithNotExistMember() {
        // given
        given(stationRepository.findById(신도림역_아이디)).willReturn(Optional.of(신도림역));
        given(stationRepository.findById(잠실역_아이디)).willReturn(Optional.of(잠실역));
        given(stationRepository.findAll()).willReturn(
                List.of(까치산역, 신도림역, 신촌역, 잠실역)
        );
        given(lineRepository.findAll()).willReturn(List.of(이호선));
        given(memberRepository.findByEmail(회원_정보.getEmail())).willReturn(Optional.empty());

        FavoriteRequest 출발역_신도림역_도착역_잠실역_요청 = FavoriteRequest.builder()
                .source(신도림역_아이디)
                .target(잠실역_아이디)
                .build();

        // when & then
        assertThatThrownBy(() -> favoriteService.saveFavorite(회원_정보.getEmail(), 출발역_신도림역_도착역_잠실역_요청))
                .isInstanceOf(NotEntityFoundException.class)
                .hasMessageContaining(ErrorCode.NOT_EXIST_MEMBER.getMessage());
    }

    @Test
    @DisplayName("등록되어 있지 않은 역이 출발역인 경로를 즐겨찾기 등록을 하려하면 실패한다.")
    void createFavoriteWithNotExistSourceStation() {
        // given
        given(stationRepository.findById(신도림역_아이디)).willReturn(Optional.empty());

        FavoriteRequest 출발역_신도림역_도착역_잠실역_요청 = FavoriteRequest.builder()
                .source(신도림역_아이디)
                .target(잠실역_아이디)
                .build();

        // when & then
        assertThatThrownBy(() -> favoriteService.saveFavorite(회원_정보.getEmail(), 출발역_신도림역_도착역_잠실역_요청))
                .isInstanceOf(NotEntityFoundException.class)
                .hasMessageContaining(ErrorCode.NOT_EXIST_STATION.getMessage());
    }

    @Test
    @DisplayName("등록되어 있지 않은 역이 도착역인 경로를 즐겨찾기 등록을 하려하면 실패한다.")
    void createFavoriteWithNotExistTargetStation() {
        // given
        given(stationRepository.findById(신촌역_아이디)).willReturn(Optional.of(신촌역));
        given(stationRepository.findById(신도림역_아이디)).willReturn(Optional.empty());

        FavoriteRequest 출발역_신도림역_도착역_잠실역_요청 = FavoriteRequest.builder()
                .source(신촌역_아이디)
                .target(신도림역_아이디)
                .build();

        // when & then
        assertThatThrownBy(() -> favoriteService.saveFavorite(회원_정보.getEmail(), 출발역_신도림역_도착역_잠실역_요청))
                .isInstanceOf(NotEntityFoundException.class)
                .hasMessageContaining(ErrorCode.NOT_EXIST_STATION.getMessage());
    }
    
}
