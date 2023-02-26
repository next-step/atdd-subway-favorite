package nextstep.member.domain;

import lombok.Builder;

import javax.persistence.*;

@Entity
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;
    private Long sourceId;
    private String sourceName;

    private Long targetId;
    private String targetName;

    protected Favorite() {
    }

    @Builder
    public Favorite(Member member, Long sourceId, String sourceName, Long targetId, String targetName) {
        this.member = member;
        this.sourceId = sourceId;
        this.sourceName = sourceName;
        this.targetId = targetId;
        this.targetName = targetName;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public Long getTargetId() {
        return targetId;
    }

    public String getTargetName() {
        return targetName;
    }
}
