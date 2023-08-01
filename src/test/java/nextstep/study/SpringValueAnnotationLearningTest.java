package nextstep.study;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootTest
public class SpringValueAnnotationLearningTest {

    @Autowired
    ApplicationContext applicationContext;

    @DisplayName("Spring에서 @Value로 값을 입력받으면 초기화 값을 덮어씌운다")
    @Test
    void value() {
        ValueTest valueTest = applicationContext.getBean(ValueTest.class);

        assertThat(valueTest.getValue()).isEqualTo("overwrite");
    }

    @DisplayName("Spring이 아니라 POJO로 호출하면 @Value 값이 들어가지 않는다")
    @Test
    void value_withoutSpring() {
        ValueTest valueTest = new ValueTest();

        assertThat(valueTest.getValue()).isEqualTo("test");
    }
}

@Component
class ValueTest implements BeanPostProcessor {

    // 필드 선언 전 호출
    {
        assertThat(this.value).isNull();
    }

    // @Value 사용시 final을 붙이니까 test에서 값이 변경되지 않았다.
    @Value("overwrite")
    private String value = "test";

    // 필드 선언 후 호출
    {
        assertThat(this.value).isEqualTo("test");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException {
        // 처음에 test 값이 들어가고 초기화 후 overwrite가 들어갈줄 알았는데 애초에 overwrite 값이 들어가고 있음
//        assertThat(this.value).isEqualTo("test");

        assertThat(this.value).isEqualTo("overwrite");

        return bean;
    }

    public String getValue() {
        return this.value;
    }
}

