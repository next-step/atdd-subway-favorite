package nextstep.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class TestDataLoaderBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DataLoader dataLoader;

    /*
        ContextRefreshedEvent : ApplicationContext가 초기화되거나 새로 고쳐질 때 (refresh) 트리거 됨
        - Test ApplicationContext가 Dirty해질 경우에만 이 트리거가 탈 것 같음 -> beforeEach()를 통해 loadData()를 진행
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Acceptence 클래스의 beforeEach와 중복이 됨..
//        dataLoader.loadData();
    }
}
