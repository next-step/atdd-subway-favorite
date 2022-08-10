package nextstep.member.domain;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long sourceId;

    private Long targetId;

    public Favorite(Member member, Long sourceId, Long targetId) {
        this.member = member;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    public static Favorite of(Member member, Long sourceId, Long targetId) {
        return  new Favorite(member, sourceId, targetId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(member, favorite.member) && Objects.equals(sourceId, favorite.sourceId) && Objects.equals(targetId, favorite.targetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, sourceId, targetId);
    }

    public void validFavorite() {
        if (this.sourceId.equals(this.targetId)) {
            throw new IllegalArgumentException("등록할 경로가 잘못 되었습니다.");
        }
    }
}
