package nextstep.subway.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.application.dto.MemberResponse;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("즐겨찾기 관련 테스트 with Mock")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockTest {

    private static final LocalDateTime NOW = LocalDateTime.now();

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private StationService stationService;

    @Mock
    private PathService pathService;

    private FavoriteService favoriteService;

    private Station 강남역;
    private Station 양재역;
    private MemberResponse 사용자;
    private Favorite 즐겨찾기;

    @BeforeEach
    void setUp() {
        this.favoriteService = new FavoriteService(favoriteRepository, memberService, stationService, pathService);

        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        ReflectionTestUtils.setField(강남역, "createdDate", NOW);
        ReflectionTestUtils.setField(강남역, "modifiedDate", NOW);

        양재역 = new Station("양재역");
        ReflectionTestUtils.setField(양재역, "id", 2L);
        ReflectionTestUtils.setField(양재역, "createdDate", NOW);
        ReflectionTestUtils.setField(양재역, "modifiedDate", NOW);

        사용자 = new MemberResponse(1L, "admin@email.com", null);

        즐겨찾기 = new Favorite(사용자.getId(), 강남역, 양재역);
        ReflectionTestUtils.setField(즐겨찾기, "id", 1L);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void saveFavorite() {
        // given
        when(stationService.findById(강남역.getId())).thenReturn(강남역);
        when(stationService.findById(양재역.getId())).thenReturn(양재역);
        when(pathService.getPath(1L, 2L)).thenReturn(new Path(new Sections(List.of())));
        when(memberService.findMember(사용자.getEmail())).thenReturn(사용자);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(즐겨찾기);

        // when
        FavoriteResponse response = favoriteService.saveFavorite(사용자.getEmail(), new FavoriteRequest(강남역.getId(), 양재역.getId()));

        // then
        assertFavorite(response);
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void findFavorite() {
        // given
        when(memberService.findMember(사용자.getEmail())).thenReturn(사용자);
        when(favoriteRepository.findWithById(즐겨찾기.getId())).thenReturn(Optional.of(즐겨찾기));

        // when
        FavoriteResponse response = favoriteService.findFavorite(사용자.getEmail(), 즐겨찾기.getId());

        // then
        assertFavorite(response);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void deleteFavorite() {
        // given
        when(memberService.findMember(사용자.getEmail())).thenReturn(사용자);
        when(favoriteRepository.findWithById(즐겨찾기.getId())).thenReturn(Optional.of(즐겨찾기));

        // when
        favoriteService.deleteFavorite(사용자.getEmail(), 즐겨찾기.getId());

        // then
        verify(favoriteRepository, times(1)).delete(any(Favorite.class));
    }

    private void assertFavorite(FavoriteResponse response) {
        assertThat(response.getId()).isNotNull();
        assertThat(response.getSource()).isNotNull();
        assertThat(response.getTarget()).isNotNull();
    }
}