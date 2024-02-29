package nextstep.common.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();

    HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000); // HttpClient 총 연결 시간을 10초로 설정

    @Bean
    public WebClient webClient() {
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY); // GET 요청의 파라미터 셋팅을 하기 위한 URI 템플릿의 인코딩을 위한 설정
        return WebClient.builder()
                .uriBuilderFactory(factory)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)) // in-memory buffer 값이 256KB
                .clientConnector(new ReactorClientHttpConnector(httpClient)) // 256KB 보다 큰 HTTP 메시지를 처리 시도 -> Throw Exception
                .build();
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-pool")
                .maxConnections(100)                    // connection pool의 갯수
                .pendingAcquireTimeout(Duration.ofMillis(0)) //커넥션 풀에서 커넥션을 얻기 위해 기다리는 최대 시간
                .pendingAcquireMaxCount(-1)             //커넥션 풀에서 커넥션을 가져오는 시도 횟수 (-1: no limit)
                .maxIdleTime(Duration.ofMillis(1000L))        //커넥션 풀에서 idle 상태의 커넥션을 유지하는 시간
                .build();
    }
}
