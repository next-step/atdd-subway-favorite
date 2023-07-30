package nextstep.favorite.application.dto;

import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.application.MemberService;
import nextstep.member.domain.Member;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class FavoriteServiceTest {

    private FavoriteRepository favoriteRepository = mock(FavoriteRepository.class);
    private StationService stationService = mock(StationService.class);
    private PathService pathService = mock(PathService.class);
    private MemberService memberService = mock(MemberService.class);

    private FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationService, pathService, memberService);

    private Station station1;
    private Station station2;

    private Long station1Id = 1L;
    private Long station2Id = 2L;
    private String email = "username";
    private Member member;


    @BeforeEach
    void init() {

        station1 = spy(Station.class);
        station2 = spy(Station.class);
        member = spy(Member.class);

        when(station1.getId()).thenReturn(station1Id);
        when(station2.getId()).thenReturn(station2Id);

        Line line = new Line();
        line.addSection(station1, station2, 2);
    }

    @DisplayName("즐겨찾기 저장")
    @Test
    void saveFavorite() {

        // given
        when(memberService.findByEmail(email)).thenReturn(member);
        when(stationService.findById(station1Id)).thenReturn(station1);
        when(stationService.findById(station2Id)).thenReturn(station2);

        doNothing().when(pathService).validatePath(any(), any());
        when(favoriteRepository.save(any())).thenReturn(any());

        // when
        favoriteService.saveFavorite(new FavoriteSaveRequest(station1Id, station2Id), email);

        // then
        verify(favoriteRepository).save(any());
        verify(pathService).validatePath(any(), any());
    }
    @DisplayName("즐겨찾기 저장 실패 - 존재하지 않는 경로")
    @Test
    void saveFavorite_fail() {

        // given
        when(memberService.findByEmail(email)).thenReturn(member);
        when(stationService.findById(station1Id)).thenReturn(station1);
        when(stationService.findById(station2Id)).thenReturn(station2);

        doThrow(new RuntimeException("Test")).when(pathService).validatePath(any(), any());

        // when & then
        Assertions.assertThatThrownBy(() -> favoriteService.saveFavorite(new FavoriteSaveRequest(station1Id, station2Id), email))
                .isInstanceOf(RuntimeException.class);
    }

}