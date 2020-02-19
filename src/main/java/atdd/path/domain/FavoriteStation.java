package atdd.path.domain;

public class FavoriteStation {

    private Long id;
    private Member member;
    private Station station;

    public FavoriteStation(Member member, Station station) {
        this(null, member, station);
    }

    public FavoriteStation(Long id, Member member, Station station) {
        this.id = id;
        this.member = member;
        this.station = station;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Station getStation() {
        return station;
    }

}
