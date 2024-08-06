package nextstep.path.config;

import nextstep.path.service.DijkstraShortestPathService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FinderConfig {

    private static final String DIJKSTRA = "dijkstra";

    @Value("${finder}")
    private String finderImplementation;

    @Bean
    public DijkstraShortestPathService dijkstraShortestPathService() {
        if (DIJKSTRA.equalsIgnoreCase(finderImplementation)) {
            return new DijkstraShortestPathService();
        }

        throw new IllegalArgumentException("Unsupported finder");
    }
}

