package atdd.favorite.application.dto;

public class FavoritePathRequestView {
    private Long id;
    private String email;
    private Long startId;
    private Long endId;

    public FavoritePathRequestView() {
    }

    public FavoritePathRequestView(String email, Long startId, Long endId) {
        this.email = email;
        this.startId = startId;
        this.endId = endId;
    }

    public FavoritePathRequestView(Long id, String email, Long startId, Long endId) {
        this.id = id;
        this.email = email;
        this.startId = startId;
        this.endId = endId;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Long getStartId() {
        return startId;
    }

    public Long getEndId() {
        return endId;
    }
}
