package nextstep.subway.setup;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseCleaner {
    public static void clean(ApplicationContext applicationContext) {
        EntityManager entityManager = applicationContext.getBean(EntityManager.class);
        TransactionTemplate transactionTemplate = applicationContext.getBean(TransactionTemplate.class);

        transactionTemplate.execute((status) -> {
            entityManager.clear();
            truncateAll(entityManager);
            return null;
        });
    }

    @SuppressWarnings("unchecked")
    private static List<String> getTableNames(EntityManager entityManager) {
        List<Object[]> tableNamesQueryResult = entityManager.createNativeQuery("SHOW TABLES").getResultList();
        return tableNamesQueryResult
                .stream()
                .map((row) -> String.valueOf(row[0]))
                .collect(Collectors.toList());
    }

    public static void truncateAll(EntityManager entityManager) {
        List<String> tableNames = getTableNames(entityManager);

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
