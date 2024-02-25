package nextstep.favorite.domain;

import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite(Member member, Station source, Station target, Collection<Section> sections) {
        CreateFavoriteValidator.validate(source, target, sections);

        this.member = member;
        this.source = source;
        this.target = target;
    }
}
