package nextstep.subway.unit;

import nextstep.subway.application.DtoFactory;
import nextstep.subway.application.FavouriteService;
import nextstep.subway.application.dto.FavouriteResponse;
import nextstep.subway.domain.favourite.Favourite;
import nextstep.subway.domain.favourite.FavouriteRepository;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.member.Member;
import nextstep.subway.domain.member.MemberRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.utils.exception.FavouriteNotFoundException;
import nextstep.utils.exception.FavouriteNotMineException;
import nextstep.utils.exception.PathNotLinkedException;
import nextstep.utils.exception.StationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.application.DtoFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class FavouriteServiceTest {
    @Autowired
    private FavouriteService favouriteService;
    @Autowired
    private FavouriteRepository favouriteRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;

    private Member 사용자;
    private Station 강남역;
    private Station 역삼역;
    private Line 이호선;

    @BeforeEach
    void init() {
        사용자 = new Member("test@email.com", "1234", 26);
        memberRepository.save(사용자);

        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        stationRepository.save(강남역);
        stationRepository.save(역삼역);

        이호선 = new Line("2호선", "green");
        이호선.addSection(강남역, 역삼역, 13);
        lineRepository.save(이호선);
    }

    @Test
    @DisplayName("즐겨찾기 등록 요청을 처리한다.")
    void doFavourite() {
        // when
        Long id = favouriteService.add(사용자.getId(), createFavouriteRequest(강남역.getId(), 역삼역.getId()));

        // then
        Favourite favourite = favouriteRepository.findById(id).orElseThrow(FavouriteNotFoundException::new);
        assertThat(favourite.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회 요청을 처리한다.")
    void showFavourites() {
        //given
        favouriteService.add(사용자.getId(), createFavouriteRequest(강남역.getId(), 역삼역.getId()));

        // when
        List<FavouriteResponse> favouriteResponseList = favouriteService.findAll(사용자.getId());

        // then
        assertThat(favouriteResponseList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("즐겨찾기 취소 요청을 처리한다.")
    void deleteFavourite() {
        //given
        Long id = favouriteService.add(사용자.getId(), createFavouriteRequest(강남역.getId(), 역삼역.getId()));

        // when
        favouriteService.delete(사용자.getId(), id);

        // then
        List<FavouriteResponse> favouriteResponseList = favouriteService.findAll(사용자.getId());
        assertThat(favouriteResponseList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("내 선호경로인지 검증한다.")
    void validateDeleteFavourite() {
        // given
        Member 새로운_사용자 = memberRepository.save(new Member("test2@email.com", "1234", 24));
        Long 새로운_사용자_선호경로Id =
                favouriteService.add(새로운_사용자.getId(), createFavouriteRequest(강남역.getId(), 역삼역.getId()));

        // when/then
        assertThatThrownBy(() -> favouriteService.delete(사용자.getId(), 새로운_사용자_선호경로Id))
                .isInstanceOf(FavouriteNotMineException.class);
    }

    @Test
    @DisplayName("없는 선호 경로라면, 예외를 발생한다.")
    void validateDeleteFavourite2() {
        // when/then
        assertThatThrownBy(() -> favouriteService.delete(사용자.getId(), 9192312L))
                .isInstanceOf(FavouriteNotFoundException.class);
    }

    @Test
    @DisplayName("없는 역이라면, 예외를 발생한다.")
    void validateCreateFavourite() {
        // when/then
        assertThatThrownBy(() -> favouriteService.add(사용자.getId(), createFavouriteRequest(강남역.getId(), 1231421L)))
                .isInstanceOf(StationNotFoundException.class);
    }

    @Test
    @DisplayName("이어지지 못하는 두 역이면, 예외를 발생한다.")
    void validateCreateFavourite2() {
        // given
        Station 용산역 = new Station("용산역");
        Station 운정역 = new Station("운정역");
        stationRepository.save(용산역);
        stationRepository.save(운정역);

        Line 경의중앙선 = new Line("경의중앙선", "blue");
        경의중앙선.addSection(용산역, 운정역, 13);
        lineRepository.save(경의중앙선);

        // when/then
        assertThatThrownBy(() -> favouriteService.add(사용자.getId(), createFavouriteRequest(강남역.getId(), 용산역.getId())))
                .isInstanceOf(PathNotLinkedException.class);
    }
}
