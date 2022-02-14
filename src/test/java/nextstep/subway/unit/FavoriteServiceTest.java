package nextstep.subway.unit;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static nextstep.subway.unit.auth.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FavoriteServiceTest {

    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    MemberRepository memberRepository;
    FavoriteService favoriteService;

    Member member;
    Long memberId;
    Station 강남역;
    Station 양재역;
    Long source;
    Long target;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);

        member = memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        memberId = member.getId();
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        source = 강남역.getId();
        target = 양재역.getId();
    }

    @Test
    void createFavorite() {
        // given
        FavoriteRequest createRequest = new FavoriteRequest(source, target);

        // when
        FavoriteResponse response = favoriteService.createFavorite(memberId, createRequest);

        // then
        assertThat(response.getSource().getName()).isEqualTo(강남역.getName());
        assertThat(response.getTarget().getName()).isEqualTo(양재역.getName());
    }
}