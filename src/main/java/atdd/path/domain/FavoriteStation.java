package atdd.path.domain;

import java.util.Objects;

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

    public Long getMemberId() {
        return member.getId();
    }

    public Station getStation() {
        return station;
    }

    public Long getStationId() {
        return station.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteStation favoriteStation = (FavoriteStation) o;
        return Objects.equals(id, favoriteStation.id) &&
                Objects.equals(member, favoriteStation.member) &&
                Objects.equals(station, favoriteStation.station);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, station);
    }

}
