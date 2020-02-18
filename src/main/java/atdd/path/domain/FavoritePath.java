package atdd.path.domain;

public class FavoritePath {

    private Long id;
    private Station sourceStation;
    private Station targetStation;

    public FavoritePath(Long id, Station sourceStation, Station targetStation) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

}
