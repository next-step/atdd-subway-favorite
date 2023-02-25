package nextstep.subway.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Favorite(final Station source, final Station target, final Member member) {
        this.source = source;
        this.target = target;
        this.member = member;

        member.addFavorite(this);
    }
}
