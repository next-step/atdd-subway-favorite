package nextstep.subway.domain.query;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import nextstep.subway.domain.entity.favorite.Favorite;
import nextstep.subway.domain.entity.station.Station;
import nextstep.subway.domain.exception.SubwayDomainException;
import nextstep.subway.domain.exception.SubwayDomainExceptionType;
import nextstep.subway.domain.repository.FavoriteRepository;
import nextstep.subway.domain.repository.StationRepository;
import nextstep.subway.domain.testing.customizer.InitStationCustomizer;
import nextstep.subway.domain.view.FavoriteView;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class FavoriteReaderTest extends BaseTestSetup {

    @Autowired
    private FavoriteReader sut;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    @Nested
    @DisplayName("getById")
    class GetById {
        @ParameterizedTest
        @AutoSource
        @Customization(InitStationCustomizer.class)
        public void sut_returns_favorite(
                Long memberId,
                Station source,
                Station target
        ) {
            // given
            stationRepository.saveAll(List.of(source, target));
            Favorite favorite = favoriteRepository.save(new Favorite(memberId, source.getId(), target.getId()));

            // when
            FavoriteView.Main actual = sut.getOneById(favorite.getId());

            // then
            assertThat(actual.getId()).isEqualTo(favorite.getId());
            assertThat(actual.getSource().getId()).isEqualTo(source.getId());
            assertThat(actual.getTarget().getId()).isEqualTo(target.getId());
        }

        @ParameterizedTest
        @AutoSource
        public void sut_throws_error_if_not_found_favorite() {
            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.getOneById(123123L));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_FAVORITE);
        }
    }

    @Nested
    @DisplayName("getFavoritesByMemberId")
    class GetFavoritesByMemberId {
        @ParameterizedTest
        @AutoSource
        @Customization(InitStationCustomizer.class)
        public void sut_returns_favorites(
                Station station1,
                Station station2,
                Station station3,
                Long memberId
        ) {
            // given
            stationRepository.saveAll(List.of(station1, station2, station3));
            Favorite favorite = favoriteRepository.save(new Favorite(memberId, station1.getId(), station2.getId()));
            Favorite favorite2 = favoriteRepository.save(new Favorite(memberId, station2.getId(), station3.getId()));
            favoriteRepository.save(new Favorite(memberId + 12, station1.getId(), station3.getId()));

            // when
            List<FavoriteView.Main> actual = sut.getFavoritesByMemberId(memberId);

            // then
            List<Long> favoriteIds = actual.stream().map(FavoriteView.Main::getId).collect(Collectors.toList());
            assertThat(favoriteIds).containsExactly(favorite.getId(), favorite2.getId());
        }
    }
}
