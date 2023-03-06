package nextstep.subway.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "source_id")
    private Station sourceStation;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "target_id")
    private Station targetStation;

    @Builder
    private Favorite(final Member member, final Station sourceStation, final Station targetStation) {
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Favorite)) {
            return false;
        }
        Favorite favorite = (Favorite) o;
        return Objects.equals(getId(), favorite.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
