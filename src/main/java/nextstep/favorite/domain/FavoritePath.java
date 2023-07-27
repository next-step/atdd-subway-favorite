package nextstep.favorite.domain;

import lombok.*;
import nextstep.member.domain.Member;

import javax.persistence.*;

@Getter
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoritePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column
    private Long sourceId;

    @Column
    private Long targetId;

    @Builder
    public FavoritePath(Member member, Long sourceId, Long targetId) {
        this.member = member;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }
}
