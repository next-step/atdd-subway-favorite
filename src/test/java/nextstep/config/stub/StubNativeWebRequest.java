package nextstep.config.stub;

import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.servlet.http.HttpServletRequest;

public class StubNativeWebRequest extends DispatcherServletWebRequest {

    public StubNativeWebRequest(HttpServletRequest request) {
        super(request);
    }

}
