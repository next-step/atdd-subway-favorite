package nextstep.config;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .httpBasic().disable()
            .csrf().disable()
            .cors().and()
            .authorizeRequests()
            .antMatchers("/members/me").authenticated()
            .anyRequest().permitAll()
            .and()
            .addFilterBefore(new JwtFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
