package nextstep.favorite.unit;

import nextstep.favorite.application.FavoriteService;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.member.application.dto.MemberRequest;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.application.LineService;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class FavoriteServiceTest {
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FavoriteService favoriteService;
    private Station 강남역;
    private Station 선릉역;
    private Station 삼성역;
    private Long 강남역Id;
    private Long 선릉역Id;
    private Long 삼성역Id;
    private Line 강남_선릉_노선;
    private Member 사용자;

    @BeforeEach
    void init() {
        강남역 = new Station("강남역");
        선릉역 = new Station("선릉역");
        삼성역 = new Station("삼성역");
        강남_선릉_노선 = new Line("노선", "red", 강남역, 선릉역, 10);
        사용자 = new Member("apvmffkdls@gmail.com", "1234", 30);

        강남역Id = stationRepository.save(강남역).getId();
        선릉역Id = stationRepository.save(선릉역).getId();
        삼성역Id = stationRepository.save(삼성역).getId();
    }

    @DisplayName("즐겨찾기 등록 시, 등록된 회원이 아니면 예외가 발생한다.")
    @Test
    void createFavorite_invalid_notFound_member() {
        // given
        lineRepository.save(강남_선릉_노선);
        memberRepository.save(사용자);
        final FavoriteRequest favoriteRequest = new FavoriteRequest(강남역Id, 선릉역Id);
        final LoginMember loginMember = new LoginMember("존재하지않는사용자이메일.com");

        // when
        // then
        assertThatThrownBy(() -> { favoriteService.createFavorite(loginMember, favoriteRequest); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록된 회원이 아닙니다.");
    }

    @DisplayName("즐겨찾기 등록 시, 비정상적인 경로 입니다.")
    @Test
    void createFavorite_invalid_path() {
        // given
        memberRepository.save(사용자);
        final FavoriteRequest favoriteRequest = new FavoriteRequest(강남역Id, 선릉역Id);
        final LoginMember loginMember = new LoginMember(사용자.getEmail());

        // when
        // then
        assertThatThrownBy(() -> { favoriteService.createFavorite(loginMember, favoriteRequest); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("그래프에 존재하지 않는 정점입니다.");
    }

}
