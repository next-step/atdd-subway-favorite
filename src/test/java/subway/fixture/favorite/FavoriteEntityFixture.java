package subway.fixture.favorite;

import subway.favorite.Favorite;
import subway.member.Member;
import subway.station.Station;

public class FavoriteEntityFixture {
	private static final Member 멤버 = new Member("admin@admin.com", "password", 20);

	public static Favorite 즐겨찾기_생성() {
		Station 불광역 = new Station("불광역");
		Station 양재역 = new Station("양재역");
		return new Favorite(멤버, 불광역, 양재역);
	}

	public static Favorite 즐겨찾기_생성(Station sourceStation, Station targetStation) {
		return new Favorite(멤버, sourceStation, targetStation);
	}
}

