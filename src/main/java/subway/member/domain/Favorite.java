package subway.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.station.domain.Station;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Station sourceStation;

    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Station targetStation;
}
