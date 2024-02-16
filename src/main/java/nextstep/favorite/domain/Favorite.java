package nextstep.favorite.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.exception.UnauthorizedDeletionException;
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

    private Long memberId;

    public static Favorite of(Station sourceStation, Station targetStation, Long memberId) {
        return Favorite.builder()
                .sourceStation(sourceStation)
                .targetStation(targetStation)
                .memberId(memberId)
                .build();
    }

    public void checkOwnershipBeforeDeletion(Long requester) {
        if (!this.memberId.equals(requester)) {
            throw new UnauthorizedDeletionException();
        }
    }
}
