package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@ExtendWith(SpringExtension.class)
class FavoriteTest {

    private static final String MAIL = "a@b.com";
    private static final String PASS = "1234abcd";
    private static final Integer AGE = 80;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long memberId;
    private Long favoriteId;

    @BeforeEach
    void setUp() {
        final Member member = new Member(MAIL, PASS, AGE);
        memberRepository.save(member);
        memberId = member.getId();

        final Favorite favorite = new Favorite(1L, 3L, member);
        favoriteId = favoriteRepository.save(favorite).getId();
    }

    @DisplayName("member와의 관계가 잘 맺어졌는지 테스트해본다.")
    @Test
    void relation() {
        final Optional<Member> maybeMember = memberRepository.findById(memberId);
        final Optional<Favorite> maybeFavorite = favoriteRepository.findById(favoriteId);

        assertThat(maybeMember.isEmpty()).isFalse();
        assertThat(maybeFavorite.isEmpty()).isFalse();

        final Member member = maybeMember.get();
        final Favorite favorite = maybeFavorite.get();

        assertThat(favorite.isOwnedBy(member)).isTrue();
    }
}