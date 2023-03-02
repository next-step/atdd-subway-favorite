package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Favorite;

public class FavoriteResponse {

    private Long id;

    private StationResponse source;

    private StationResponse target;

    public Long getId() {
        return id;
    }


    public StationResponse getSource() {
        return source;
    }

    public StationResponse getTarget() {
        return target;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static FavoriteResponse from(Favorite favorite){
        FavoriteResponse response = new FavoriteResponse();
        response.id = favorite.getId();
        response.source = new StationResponse(favorite.getSource().getId(), favorite.getSource().getName());
        response.target = new StationResponse(favorite.getTarget().getId(), favorite.getTarget().getName());

        return response;
    }
}
