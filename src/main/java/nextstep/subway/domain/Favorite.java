package nextstep.subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "favorite_source_id")
    private Station source;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "favorite_source_id")
    private Station target;
}
