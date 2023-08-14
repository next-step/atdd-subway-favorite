package nextstep.member.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.member.domain.Favorite;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.Test;

class FavoriteTest {
    private final Member memberA = new Member(1L, "test1@test.com", "1234", 1);
    private final Member memberB = new Member(2L, "test2@test.com", "1234", 1);
    private final Favorite favorite = new Favorite(1L, 1L, 2L);

    @Test
    void isCreatedMember() {
        boolean value = favorite.isCreatedMember(memberB);
        assertThat(value).isFalse();

        boolean value1 = favorite.isCreatedMember(memberA);
        assertThat(value1).isTrue();
    }
}
