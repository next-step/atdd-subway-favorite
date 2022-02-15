package nextstep.subway.unit.service;

import nextstep.member.domain.MemberRepository;
import nextstep.subway.applicaion.FavoriteService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.FavoriteResponse;
import nextstep.subway.domain.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static nextstep.subway.unit.FavoriteUnitTestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private FavoriteService favoriteService;

    @BeforeEach
    void setFixtures() {
        givens();
    }

    @Test
    void 즐겨찾기_등록() {
        when(stationService.findStationById(강남역.getId())).thenReturn(강남역);
        when(stationService.findStationById(역삼역.getId())).thenReturn(역삼역);
        when(favoriteRepository.save(any(강남_TO_역삼.getClass()))).then(AdditionalAnswers.returnsFirstArg());
        FavoriteResponse response = favoriteService.saveFavorite(member.getId(), favoriteRequest);
        assertThat(response.getSource().getName()).isEqualTo(강남역.getName());
        assertThat(response.getTarget().getName()).isEqualTo(역삼역.getName());
    }

    @Test
    void 즐겨찾기_목록_조회() {
        when(favoriteRepository.findAllByMember(member)).thenReturn(favorites);
        List<FavoriteResponse> favoriteResponses = favoriteService.findAllFavorites(member.getId());
        assertThat(favoriteResponses).hasSize(2);
        assertThat(favoriteResponses.get(0).getSource().getName()).isEqualTo(강남역.getName());
        assertThat(favoriteResponses.get(0).getTarget().getName()).isEqualTo(역삼역.getName());
        assertThat(favoriteResponses.get(1).getSource().getName()).isEqualTo(정자역.getName());
        assertThat(favoriteResponses.get(1).getTarget().getName()).isEqualTo(판교역.getName());
    }

    private void givens() {
        createLines();
        createStations();
        createSections();
        createMember();
        createFavorites();
        createFavoriteRequest();
        createFavoriteResponse();
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
    }
}
