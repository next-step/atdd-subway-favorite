package nextstep.subway.auth.ui.converter;

import nextstep.subway.auth.domain.AuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationConverter {
    /*
    * request -> AuthenticationToken
    * 세션) 요청객체의 파라미터맵을 통해 받기
    * 토큰) 요청객체를 TokenRequest 객체로 변환해서 받기
    * */
    AuthenticationToken convert(HttpServletRequest request);
}
