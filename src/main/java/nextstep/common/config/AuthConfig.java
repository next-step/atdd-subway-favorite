package nextstep.common.config;

import java.util.List;
import nextstep.auth.application.JwtTokenProvider;
import nextstep.auth.application.service.UserDetailService;
import nextstep.auth.application.ui.AuthenticationPrincipalArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailService userDetailService;

    public AuthConfig(JwtTokenProvider jwtTokenProvider, UserDetailService userDetailService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailService = userDetailService;
    }


    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new AuthenticationPrincipalArgumentResolver(jwtTokenProvider, userDetailService));
    }
}
