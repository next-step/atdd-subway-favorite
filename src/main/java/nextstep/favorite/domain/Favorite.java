package nextstep.favorite.domain;

import nextstep.exception.SubwayIllegalArgumentException;
import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite() {
    }

    public Favorite(Member member, Station source, Station target) {
        validateStation(source, target);
        validateEqualStartAndDestination(source, target);
        this.member = member;
        this.source = source;
        this.target = target;
    }

    private void validateEqualStartAndDestination(Station source, Station target) {
        if (source == target) {
            throw new SubwayIllegalArgumentException("출발역과 도착역이 같을 수 없습니다.");
        }
    }

    private void validateStation(Station source, Station target) {
        if (source == null || target == null) {
            throw new SubwayIllegalArgumentException("출발역과, 도착역 둘다 입력해줘야 합니다.");
        }
    }


    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
