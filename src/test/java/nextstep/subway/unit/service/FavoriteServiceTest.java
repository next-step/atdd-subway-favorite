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

import java.util.Optional;

import static nextstep.subway.unit.FavoriteUnitTestHelper.createFavorite;
import static nextstep.subway.unit.FavoriteUnitTestHelper.createFavoriteRequest;
import static nextstep.subway.unit.FavoriteUnitTestHelper.createFavoriteResponse;
import static nextstep.subway.unit.FavoriteUnitTestHelper.createLines;
import static nextstep.subway.unit.FavoriteUnitTestHelper.createMember;
import static nextstep.subway.unit.FavoriteUnitTestHelper.createSections;
import static nextstep.subway.unit.FavoriteUnitTestHelper.createStations;
import static nextstep.subway.unit.FavoriteUnitTestHelper.favorite;
import static nextstep.subway.unit.FavoriteUnitTestHelper.favoriteRequest;
import static nextstep.subway.unit.FavoriteUnitTestHelper.member;
import static nextstep.subway.unit.FavoriteUnitTestHelper.강남역;
import static nextstep.subway.unit.FavoriteUnitTestHelper.역삼역;
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
        createLines();
        createStations();
        createSections();
        createMember();
        createFavorite();
        createFavoriteRequest();
        createFavoriteResponse();
    }

    @Test
    void 즐겨찾기_등록() {
        when(stationService.findStationById(강남역.getId())).thenReturn(강남역);
        when(stationService.findStationById(역삼역.getId())).thenReturn(역삼역);
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(favoriteRepository.save(any(favorite.getClass()))).then(AdditionalAnswers.returnsFirstArg());
        FavoriteResponse response = favoriteService.saveFavorite(member.getId(), favoriteRequest);
        assertThat(response.getSource().getName()).isEqualTo(강남역.getName());
        assertThat(response.getTarget().getName()).isEqualTo(역삼역.getName());
    }
}
