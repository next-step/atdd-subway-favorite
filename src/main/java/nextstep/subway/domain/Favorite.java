package nextstep.subway.domain;

import nextstep.subway.domain.exceptions.CanNotDeleteFavoriteException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Station source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private Station target;

    public Favorite(Long memberId, Station source, Station target) {
        this.memberId = memberId;
        this.source = source;
        this.target = target;
    }

    public Favorite() {
    }

    public void delete(Long memberId) {
        if (!Objects.equals(this.memberId, memberId)) {
            throw new CanNotDeleteFavoriteException("다른 사람의 즐겨찾기를 삭제할 수 없습니다. memberId: " + memberId);
        }
    }

    public Long getId() {
        return id;
    }

    public Station getSource() {
        return source;
    }

    public Station getTarget() {
        return target;
    }
}
