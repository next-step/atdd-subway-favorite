package nextstep.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.member.Member;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class FavoritePath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "source_id")
    @ManyToOne
    private Station source;

    @JoinColumn(name = "target_id")
    @ManyToOne
    private Station target;

    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    public FavoritePath(Station source, Station target, Member member) {
        this.source = source;
        this.target = target;
        this.member = member;
    }
}
