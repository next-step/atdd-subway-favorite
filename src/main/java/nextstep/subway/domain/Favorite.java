package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.domain.Member;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station source;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station target;

    public Favorite(Member member, Station source, Station target) {
        this.member = member;
        this.source = source;
        this.target = target;
    }

    public static Favorite of(Member member, Station source, Station target) {
        return new Favorite(member, source, target);
    }

    public void validateMyFavorite(Long memberId) {
        if (memberId.equals(memberId)) {
            return;
        }

        throw new IllegalArgumentException("자신의 즐겨찾기만 조회가능 합니다");
    }
}
