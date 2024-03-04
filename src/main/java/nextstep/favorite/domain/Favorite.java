package nextstep.favorite.domain;

import nextstep.auth.AuthenticationException;
import nextstep.member.domain.Member;
import nextstep.station.Station;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sourceStationId")
    private Station sourceStation;

    @ManyToOne
    @JoinColumn(name = "targetStationId")
    private Station targetStation;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    public Favorite() {
    }

    public Favorite(Station sourceStation, Station targetStation, Member member) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.member = member;
    }

    public void validateDeletionByMember(Member member) {
        if (!this.member.equals(member)) {
            throw new AuthenticationException();
        }
    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public Member getMember() {
        return member;
    }

}
