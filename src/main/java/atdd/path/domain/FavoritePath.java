package atdd.path.domain;

import java.util.Objects;

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

    public Long getSourceStationId() {
        return sourceStation.getId();
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public Long getTargetStationId() {
        return targetStation.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoritePath favoritePath = (FavoritePath) o;
        return Objects.equals(id, favoritePath.id) &&
                Objects.equals(member, favoritePath.member) &&
                Objects.equals(sourceStation, favoritePath.sourceStation) &&
                Objects.equals(targetStation, favoritePath.targetStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, sourceStation, targetStation);
    }

}