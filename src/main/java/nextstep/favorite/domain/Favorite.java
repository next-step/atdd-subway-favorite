package nextstep.favorite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.member.domain.Member;
import nextstep.station.Station;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Station sourceStation;

    @ManyToOne
    private Station targetStation;

    @ManyToOne
    private Member member;

    public static Favorite of(Station sourceStation, Station targetStation, Member member) {
        return Favorite.builder()
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .member(member)
                .build();
    }
}
