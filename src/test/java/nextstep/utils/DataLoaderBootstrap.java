package nextstep.utils;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class DataLoaderBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final DataLoader dataLoader;

    public DataLoaderBootstrap(final DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        dataLoader.loadData();
    }
}
