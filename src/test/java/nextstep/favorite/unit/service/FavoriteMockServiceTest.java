package nextstep.favorite.unit.service;

import nextstep.common.Constant;
import nextstep.exception.UnconnectedFindPathStationsException;
import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteDto;
import nextstep.favorite.application.request.AddFavoriteRequest;
import nextstep.favorite.application.response.ShowAllFavoriteResponse;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.application.StationService;
import nextstep.subway.application.dto.StationDto;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static nextstep.common.Constant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteMockServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    private final Member 홍길동 = Member.of(홍길동_이메일, 홍길동_비밀번호, 홍길동_나이);

    private final Station 논현역 = Station.from(Constant.논현역);
    private final Long 논현역_ID = 1L;
    private final Station 신논현역 = Station.from(Constant.신논현역);
    private final Long 신논현역_ID = 2L;
    private final Station 강남구청역 = Station.from(Constant.강남구청역);
    private final Long 강남구청역_ID = 3L;
    private final Station 압구정로데오역 = Station.from(Constant.압구정로데오역);
    private final Long 압구정로데오역_ID = 4L;
    private Line 신분당선;
    private Line 수인분당선;

    @BeforeEach
    protected void setUp() {
        신분당선 = Line.of(Constant.신분당선, Constant.빨간색);
        신분당선.addSection(Section.of(논현역, 신논현역, Constant.역_간격_5));
        수인분당선 = Line.of(Constant.수인분당선, Constant.노란색);
        수인분당선.addSection(Section.of(강남구청역, 압구정로데오역, Constant.역_간격_10));
    }

    /**
     * 논현역    --- *신분당선* (5) ---   신논현역
     *
     * 압구정로데오 --- *수인분당선*(10) --- 강남구청역
     */

    @DisplayName("구간을 즐겨찾기에 추가한다.")
    @Test
    void 구간을_즐겨찾기에_추가() {
        // given
        when(memberRepository.findByEmail(홍길동_이메일)).thenReturn(Optional.of(홍길동));
        when(lineRepository.findAll()).thenReturn(List.of(신분당선));
        when(stationService.findById(논현역_ID)).thenReturn(논현역);
        when(stationService.findById(신논현역_ID)).thenReturn(신논현역);

        // when
        favoriteService.createFavorite(LoginMember.from(홍길동_이메일), AddFavoriteRequest.of(논현역_ID, 신논현역_ID));

        // then
        ShowAllFavoriteResponse 즐겨찾기_조회_응답 = favoriteService.findFavorites(LoginMember.from(홍길동_이메일));
        List<FavoriteDto> 즐겨찾기 = 즐겨찾기_조회_응답.getFavorites();
        즐겨찾기_추가_검증(즐겨찾기, 1, 논현역, 신논현역);
    }

    @DisplayName("경로가 없는 구간을 즐겨찾기로 추가하면 예외가 발생한다.")
    @Test
    void 경로가_없는_구간을_즐겨찾기에_추가() {
        // given
        when(memberRepository.findByEmail(홍길동_이메일)).thenReturn(Optional.of(홍길동));
        when(lineRepository.findAll()).thenReturn(List.of(신분당선, 수인분당선));
        when(stationService.findById(논현역_ID)).thenReturn(논현역);
        when(stationService.findById(압구정로데오역_ID)).thenReturn(압구정로데오역);

        // when & then
        assertThatThrownBy(() -> favoriteService.createFavorite(LoginMember.from(홍길동_이메일), AddFavoriteRequest.of(논현역_ID, 압구정로데오역_ID)))
                .isInstanceOf(UnconnectedFindPathStationsException.class);
    }

    void 즐겨찾기_추가_검증(List<FavoriteDto> 즐겨찾기, int 즐겨찾기_수, Station 시작역, Station 종료역) {
        assertThat(즐겨찾기).hasSize(즐겨찾기_수);
        assertTrue(즐겨찾기.stream()
                .anyMatch(favoriteDto ->
                        favoriteDto.getStartStation().equals(StationDto.from(시작역))
                                && favoriteDto.getEndStation().equals(StationDto.from(종료역))
                ));
    }

}
