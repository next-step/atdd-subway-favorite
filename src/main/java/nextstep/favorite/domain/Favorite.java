package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.station.domain.Station;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sourceStationId")
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetStationId")
    private Station targetStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    public Favorite() {
    }

    public Favorite(Station sourceStation, Station targetStation, Member member) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.member = member;
    }

    public Favorite(Long id, Station sourceStation, Station targetStation, Member member) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(id, favorite.id) && Objects.equals(sourceStation, favorite.sourceStation) && Objects.equals(targetStation, favorite.targetStation) && Objects.equals(member, favorite.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceStation, targetStation, member);
    }
}
