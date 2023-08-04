package nextstep.unit;

import nextstep.domain.FavoritePath;

import nextstep.domain.Station;
import nextstep.domain.member.Member;
import nextstep.domain.member.MemberRepository;
import nextstep.dto.FavoritePathRequest;
import nextstep.repository.FavoritePathRepository;
import nextstep.service.FavoritePathService;
import nextstep.service.PathService;
import nextstep.service.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class FavoritePathServiceTest {

    @Mock
    private FavoritePathRepository favoritePathRepository;
    @Mock
    private  PathService pathService;
    @Mock
    private  StationService stationService;
    @Mock
    private  MemberRepository memberRepository ;

    @InjectMocks
    private FavoritePathService favoritePathService;

    private Station sourceStation;
    private Station targetStation;

    private Long sourceStationId = 1L;
    private Long targetStationId = 2L;
    private String email = "example@example.com";
    private Member member;

    @BeforeEach
    public void setGivenData(){
        sourceStation = new Station(sourceStationId, "교대역");
        targetStation = new Station(targetStationId, "강남역");
        member = new Member(email, "password", 30);
    }
    @DisplayName("즐겨찾기 생성 테스트")
    @Test
    void createFavoritePath() {

        //given
        FavoritePath favoritePath = new FavoritePath(sourceStation, targetStation, member);

        when(stationService.findStation(sourceStationId)).thenReturn(sourceStation);
        when(stationService.findStation(targetStationId)).thenReturn(targetStation);
        doNothing().when(pathService).validatePath(any(),any());
        when(memberRepository.findByEmail(email)).thenReturn(Optional.ofNullable(member));
        when(favoritePathRepository.save(any())).thenReturn(favoritePath);

        //when
        favoritePathService.createFavoritePath(new FavoritePathRequest(sourceStationId, targetStationId), email);

        //then
        assertThat(favoritePath.getSource()).isEqualTo(sourceStation);
        assertThat(favoritePath.getTarget()).isEqualTo(targetStation);
        assertThat(favoritePath.getMember()).isEqualTo(member);
    }
}
