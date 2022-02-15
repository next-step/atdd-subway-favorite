package nextstep.subway.unit;

import nextstep.member.application.FavoriteService;
import nextstep.member.application.dto.FavoriteRequest;
import nextstep.member.application.dto.FavoriteResponse;
import nextstep.member.domain.Favorite;
import nextstep.member.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static nextstep.subway.unit.auth.AuthFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

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

    @Test
    void findFavorites() {
        // given
        Station 신림역 = stationRepository.save(new Station("신림역"));
        Station 서초역 = stationRepository.save(new Station("서초역"));
        favoriteService.createFavorite(memberId, new FavoriteRequest(source, target));
        favoriteService.createFavorite(memberId, new FavoriteRequest(신림역.getId(), 서초역.getId()));

        // when
        List<FavoriteResponse> responses = favoriteService.findFavorites(memberId);

        // then
        List<String> sources = responses.stream().map(f -> f.getSource().getName()).collect(Collectors.toList());
        List<String> targets = responses.stream().map(f -> f.getTarget().getName()).collect(Collectors.toList());
        assertThat(sources).containsOnly(강남역.getName(), 신림역.getName());
        assertThat(targets).containsOnly(양재역.getName(), 서초역.getName());
    }

    @Test
    void deleteFavorite() {
        // given
        FavoriteRequest createRequest = new FavoriteRequest(source, target);
        FavoriteResponse response = favoriteService.createFavorite(memberId, createRequest);

        // when
        favoriteService.deleteFavorite(memberId, response.getId());

        // then
        Optional<Favorite> favorite = favoriteRepository.findById(response.getId());
        assertThat(favorite).isEmpty();
    }

    @Test
    void deleteFavoriteNotMine() {
        // given
        Member anotherMember = memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        FavoriteRequest createRequest = new FavoriteRequest(source, target);
        FavoriteResponse response = favoriteService.createFavorite(memberId, createRequest);

        // when
        Throwable thrown = catchThrowable(() -> { favoriteService.deleteFavorite(anotherMember.getId(), response.getId()); });

        // then
        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }
}