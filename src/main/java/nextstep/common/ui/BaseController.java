package nextstep.common.ui;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {
    private static final String PRINCIPAL = "principal";

    protected String getPrincipal(HttpServletRequest request) {
        return (String) request.getAttribute(PRINCIPAL);
    }
}
