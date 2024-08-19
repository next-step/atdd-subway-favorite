package nextstep.utils;

import nextstep.line.acceptance.LineAcceptanceTest;
import nextstep.path.acceptance.PathAcceptanceTest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Profile("test")
@Service
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(entity -> entity.getJavaType().getAnnotation(Entity.class) != null)
                .map(entity -> entity.getName())
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute(Object testClass) {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            truncateTables(testClass, tableName);
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void truncateTables(Object testClass, String tableName) {
        if (notTruncateAble(testClass, tableName)) return;
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
    }

    private static boolean notTruncateAble(Object testClass, String tableName) {
        if (testClass.equals(LineAcceptanceTest.class) && tableName.equals("Station")) {
            return true;
        }
        if (testClass.equals(PathAcceptanceTest.class) && tableName.equals("Station")) {
            return true;
        }
        return false;
    }
}
