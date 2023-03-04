package nextstep;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class ProdDataLoader implements DataLoader {

    @Override
    public void loadData() {
    }
}
