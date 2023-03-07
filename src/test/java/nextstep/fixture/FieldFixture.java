package nextstep.fixture;


public enum FieldFixture {
    식별자_아이디("id"),
    데이터_생성_결과_로케이션("Location"),

    역_아이디("stationId"),
    역_이름("name"),

    노선_이름("name"),
    노선_색깔("color"),
    노선_상행_종점역_ID("upStationId"),
    노선_하행_종점역_ID("downStationId"),
    노선_간_거리("distance"),
    노선_내_역_목록("stations"),
    노선_내_역_아이디("stations.id"),
    노선_내_역_이름("stations.name"),

    경로_내_역_목록("stations"),
    경로_내_역_아이디_목록("stations.id"),
    경로_조회_출발지_아이디("source"),
    경로_조회_도착지_아이디("target"),

    회원_이메일("email"),
    회원_비밀번호("password"),
    회원_나이("age"),
    회원_권한_목록("roles"),

    Access_Token("accessToken"),
    권한_증서("code"),

    즐겨찾기_출발지_아이디("source"),
    즐겨찾기_도착지_아이디("target"),
    즐겨찾기_목록_전체(""),
    즐겨찾기_목록_내_첫번째_식별자_아이디("[0].id"),
    즐겨찾기_목록_내_첫번째_출발지_이름("[0].source.name"),
    즐겨찾기_목록_내_첫번째_도착지_이름("[0].target.name"),
    ;

    private final String value;

    FieldFixture(String value) {
        this.value = value;
    }

    public String 필드명() {
        return value;
    }
}
