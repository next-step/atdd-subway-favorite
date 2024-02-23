package nextstep.core.favorite.domain;

import nextstep.core.member.domain.Member;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member member;

    protected Favorite() {
    }

    public Favorite(Member member) {
        this.member = member;
    }
}
