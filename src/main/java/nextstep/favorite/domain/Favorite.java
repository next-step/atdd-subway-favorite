package nextstep.favorite.domain;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Station;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE favorite SET deleted_at = CURRENT_TIMESTAMP where favorite_id = ?")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private Station startStation;

    @OneToOne(fetch = FetchType.LAZY)
    private Station endStation;

    @Column
    private LocalDateTime deletedAt;

    protected Favorite() {
    }

    private Favorite(Station startStation, Station endStation) {
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public static Favorite of(Station startStation, Station endStation) {
        return new Favorite(startStation, endStation);
    }

    public Long getFavoriteId() {
        return favoriteId;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public void setMember(Member member){
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(favoriteId, favorite.favoriteId) && Objects.equals(startStation, favorite.startStation) && Objects.equals(endStation, favorite.endStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(favoriteId, startStation, endStation);
    }

}
