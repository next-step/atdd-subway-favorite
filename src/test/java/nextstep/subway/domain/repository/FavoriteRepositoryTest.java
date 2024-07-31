package nextstep.subway.domain.repository;

import autoparams.AutoSource;
import nextstep.subway.domain.entity.favorite.Favorite;
import nextstep.subway.domain.exception.SubwayDomainException;
import nextstep.subway.domain.exception.SubwayDomainExceptionType;
import nextstep.util.BaseTestSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class FavoriteRepositoryTest  extends BaseTestSetup {
    @Autowired
    private FavoriteRepository sut;

    @Nested
    @DisplayName("findByIdOrThrow")
    class FindByIdOrThrow {
        @ParameterizedTest
        @AutoSource
        public void sut_return_favorite(Favorite favorite) {
            // given
            sut.save(favorite);

            transactionTemplate.execute(status -> {
                // when
                Favorite actual = sut.findByIdOrThrow(favorite.getId());

                // then
                assertThat(actual).usingRecursiveComparison().isEqualTo(favorite);

                return null;
            });
        }

        @Test
        public void sut_throws_if_not_found_favorite() {
            // when
            SubwayDomainException actual = (SubwayDomainException) catchThrowable(() -> sut.findByIdOrThrow(123123L));

            // then
            assertThat(actual.getExceptionType()).isEqualTo(SubwayDomainExceptionType.NOT_FOUND_FAVORITE);
        }
    }

    @Nested
    @DisplayName("findAllByMemberId")
    class FindAllByMemberId {
        public Favorite changeFavoriteMemberId(Favorite favorite, Long memberId) {
            try {
                Field memberIdField = favorite.getClass().getDeclaredField("memberId");
                memberIdField.setAccessible(true);
                memberIdField.set(favorite, memberId);
                return favorite;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @ParameterizedTest
        @AutoSource
        public void sut_return_favorite(Long memberId, List<Favorite> favorites) {
            // given
            List<Favorite> expected = favorites.stream().map((f) -> changeFavoriteMemberId(f, memberId)).collect(Collectors.toList());
            sut.saveAll(expected);

            transactionTemplate.execute(status -> {
                // when
                List<Favorite> actual = sut.findAllByMemberId(memberId);

                // then
                assertThat(actual).usingRecursiveComparison().isEqualTo(expected);

                return null;
            });
        }

        @Test
        public void sut_returns_empty_if_not_existed() {
            // when
            List<Favorite> actual = sut.findAllByMemberId(123L);

            // then
            assertThat(actual.size()).isEqualTo(0);
        }
    }
}