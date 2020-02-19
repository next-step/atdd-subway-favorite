package atdd.path.domain;

public class FavoritePath {

    private Long id;
    private Member member;
    private Station sourceStation;
    private Station targetStation;

    public FavoritePath(Member member, Station sourceStation, Station targetStation) {
        this(null, member, sourceStation, targetStation);
    }

    public FavoritePath(Long id, Member member, Station sourceStation, Station targetStation) {
        this.id = id;
        this.member = member;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

}
