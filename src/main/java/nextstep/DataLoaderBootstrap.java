package nextstep;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@RequiredArgsConstructor
public class DataLoaderBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final DataLoader dataLoader;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        dataLoader.loadData();
    }
}
