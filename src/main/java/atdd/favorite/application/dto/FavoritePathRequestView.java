package atdd.favorite.application.dto;

public class FavoritePathRequestView {
    private Long id;
    private String email;
    private Long startId;
    private Long endId;

    public FavoritePathRequestView() {
    }

    public FavoritePathRequestView(Long id) {
        this(id, "", 0L, 0L);
    }

    public FavoritePathRequestView(String email) {
        this(0L, email, 0L, 0L);
    }

    public FavoritePathRequestView(Long id, String email) {
        this(id, email, 0L, 0L);
    }

    public FavoritePathRequestView(Long startId, Long endId) {
        this(0L, "", startId, endId);
    }

    public FavoritePathRequestView(String email, Long startId, Long endId) {
        this(0L, email, startId, endId);
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

    public void insertId(Long id) {
        this.id = id;
    }

    public void insertEmail(String email) {
        this.email = email;
    }
}
