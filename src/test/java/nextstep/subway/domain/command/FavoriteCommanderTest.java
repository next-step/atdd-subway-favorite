package nextstep.subway.domain.command;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import nextstep.subway.domain.entity.favorite.Favorite;
import nextstep.subway.domain.entity.station.Station;
import nextstep.subway.domain.exception.SubwayDomainException;
import nextstep.subway.domain.exception.SubwayDomainExceptionType;
import nextstep.subway.domain.repository.FavoriteRepository;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.repository.StationRepository;
import nextstep.subway.domain.testing.customizer.InitStationCustomizer;
import nextstep.subway.fixtures.LineFixture;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;

class FavoriteCommanderTest extends BaseTestSetup {
    @Autowired
    private FavoriteCommander sut;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Nested
    @DisplayName("createFavorite")
    class CreateFavorite {

        @ParameterizedTest
        @AutoSource
        @Customization(InitStationCustomizer.class)
        public void sut_throws_if_not_found_sourceStation(
                Station targetStation,
                Long memberId
        ) {

            // given
            stationRepository.save(targetStation);
            FavoriteCommand.CreateFavorite command = new FavoriteCommand.CreateFavorite(memberId, 222L, targetStation.getId());

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.createFavorite(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }

        @ParameterizedTest
        @AutoSource
        @Customization(InitStationCustomizer.class)
        public void sut_throws_if_not_found_targetStation(
                Station sourceStation,
                Long memberId
        ) {

            // given
            stationRepository.save(sourceStation);
            FavoriteCommand.CreateFavorite command = new FavoriteCommand.CreateFavorite(memberId, sourceStation.getId(), 222L);

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.createFavorite(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_STATION);
        }

        @ParameterizedTest
        @AutoSource
        @Customization(InitStationCustomizer.class)
        public void sut_throws_if_path_not_found(
                Long memberId,
                Station sourceStation,
                Station targetStation,
                List<Station> otherStations
        ) {

            // given
            stationRepository.saveAll(List.of(sourceStation, targetStation));
            stationRepository.saveAll(otherStations);
            lineRepository.saveAll(List.of(
                    LineFixture.prepareConnectedLine(sourceStation, otherStations.get(0)),
                    LineFixture.prepareConnectedLine(targetStation, otherStations.get(1))
            ));

            FavoriteCommand.CreateFavorite command = new FavoriteCommand.CreateFavorite(memberId, sourceStation.getId(), targetStation.getId());

            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.createFavorite(command));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.SOURCE_TARGET_NOT_CONNECTED);
        }

        @ParameterizedTest
        @AutoSource
        @Customization(InitStationCustomizer.class)
        public void sut_creates_favorite(
                Long memberId,
                Station sourceStation,
                Station targetStation,
                List<Station> allStations
        ) {
            // given
            allStations.add(0, sourceStation);
            allStations.add(targetStation);
            stationRepository.saveAll(allStations);
            lineRepository.save(LineFixture.prepareConnectedLine(allStations.toArray(new Station[0])));

            FavoriteCommand.CreateFavorite command = new FavoriteCommand.CreateFavorite(memberId, sourceStation.getId(), targetStation.getId());

            // when
            Long id = sut.createFavorite(command);

            // then
            transactionTemplate.execute(status -> {
                Favorite actual = favoriteRepository.findByIdOrThrow(id);
                assertAll("assert created favorite",
                        () -> assertThat(actual.getMemberId()).isEqualTo(command.getMemberId()),
                        () -> assertThat(actual.getSourceStationId()).isEqualTo(command.getSource()),
                        () -> assertThat(actual.getTargetStationId()).isEqualTo(command.getTarget())
                );
                return null;
            });
        }
    }
}