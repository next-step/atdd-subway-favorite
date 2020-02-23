package atdd.favorite.application.dto;

public class CreateFavoritePathRequestView {
    private String userEmail;
    private Long startStationId;
    private Long endStationId;

    public String getUserEmail() {
        return userEmail;
    }

    public Long getStartStationId() {
        return startStationId;
    }

    public Long getEndStationId() {
        return endStationId;
    }

    public CreateFavoritePathRequestView(String userEmail, Long startStationId, Long endStationId) {
        this.userEmail = userEmail;
        this.startStationId = startStationId;
        this.endStationId = endStationId;
    }
}
