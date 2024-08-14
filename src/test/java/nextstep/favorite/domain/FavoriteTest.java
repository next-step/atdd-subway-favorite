package nextstep.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.favorite.domain.Favorite.Builder;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 도메인")
class FavoriteTest {

    private Favorite favorite;
    private Member user;
    private Member admin;

    @BeforeEach
    void setUp() {
        favorite = new Builder().id(1L).sourceStationId(1L).targetStationId(2L).memberId(1L).build();
        admin = new Member(1L, "email@email.com", "admin", 20);
        user = new Member(2L, "user@email.com", "user", 30);
    }

    @Test
    void isMember_멤버번호가_다를때() {
        assertThat(favorite.isMember(user)).isFalse();
    }

    @Test
    void isMember_멤버번호가_같을때() {
        assertThat(favorite.isMember(admin)).isTrue();
    }
}