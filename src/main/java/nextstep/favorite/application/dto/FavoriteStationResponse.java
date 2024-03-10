package nextstep.favorite.application.dto;

public class FavoriteStationResponse {

    private final Long id;
    private final String name;

    public FavoriteStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
