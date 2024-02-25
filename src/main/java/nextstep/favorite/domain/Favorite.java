package nextstep.favorite.domain;

import nextstep.favorite.domain.exception.FavoriteBadRequestException;
import nextstep.favorite.domain.exception.FavoriteMemberException;
import nextstep.member.domain.Member;
import nextstep.station.domain.Station;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(indexes = {
        @Index(name = "favorite_source_target_member_unique", 
                columnList = "source_station_id, target_station_id, member_id",
                unique = true
        )
})
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JoinColumn(name = "source_station_id")
    private Station sourceStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "target_station_id")
    private Station targetStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action=OnDeleteAction.CASCADE)
    @JoinColumn(name = "member_id")
    private Member member;

    private Favorite(Station sourceStation, Station targetStation, Member member) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
        this.member = member;
    }

    protected Favorite() {
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

    public static Favorite create(Station source, Station target, Member member) {
        if (source.equals(target)) {
            throw new FavoriteBadRequestException("출발역과 도착역은 같을 수 없습니다.");
        }

        return new Favorite(source, target, member);
    }

    public void validateCommandingMember(String commandingMemberEmail) {
        if (!commandingMemberEmail.equals(member.getEmail())) {
            throw new FavoriteMemberException();
        }
    }
}
