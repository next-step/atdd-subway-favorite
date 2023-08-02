package nextstep.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_EXIST_MEMBER("회원이 존재하지 않습니다."),
    NOT_EXIST_STATION("지하철역이 존재하지 않습니다."),
    NOT_EXIST_LINE("지하철 노선이 존재하지 않습니다."),

    LOGIN_ERROR("아이디 또는 비밀번호를 확인해주세요."),
    NOT_VALID_BEARER_GRANT_TYPE("인증 타입이 Bearer 타입이 아닙니다."),

    INVALID_DISTANCE("기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없다."),
    ALREADY_REGISTERED_SECTION("이미 등록되어있는 노선입니다."),
    UNREGISTERED_STATION("지하철 노선에 등록되어 있지 않은 역입니다."),
    STAND_ALONE_LINE_SECTION("지하철 노선에 구간이 1개만 존재할 경우 삭제할 수 없습니다."),

    SAME_DEPARTURE_AND_ARRIVAL_STATIONS("출발역과 도착역이 같을 수 없습니다."),
    UNLINKED_DEPARTURE_AND_ARRIVAL_STATIONS("출발역과 도착역이 연결되어 있지 않습니다.");

    private String message;

}
