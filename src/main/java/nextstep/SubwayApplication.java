package nextstep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SubwayApplication {
    private static DataLoaderBootstrap dataLoaderBootstrap;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication();
        application.addListeners(dataLoaderBootstrap);
        application.run(SubwayApplication.class, args);
    }

}
