package nextstep.subway.domain.entity;

import autoparams.AutoSource;
import nextstep.subway.domain.command.FavoriteCommand;
import nextstep.subway.domain.entity.favorite.Favorite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteTest {
    @DisplayName("create")
    @Nested
    class Create {
        @ParameterizedTest
        @AutoSource
        public void sut_returns_new_favorite(FavoriteCommand.CreateFavorite command) {
            // when
            Favorite actual = Favorite.create(command);

            // then
            assertAll("assert create",
                    () -> assertThat(actual.getMemberId()).isEqualTo(command.getMemberId()),
                    () -> assertThat(actual.getSourceStationId()).isEqualTo(command.getSource()),
                    () -> assertThat(actual.getTargetStationId()).isEqualTo(command.getTarget())
            );
        }
    }
}
