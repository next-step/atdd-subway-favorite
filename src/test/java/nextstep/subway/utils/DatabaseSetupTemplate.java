package nextstep.subway.utils;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class DatabaseSetupTemplate {
    protected JdbcTemplate jdbcTemplate;

    public DatabaseSetupTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final void setUpDatabase() {
        disableReferentialIntegrity();
        truncateTables();
        resetAutoIncrement();
        insertInitialData();
        enableReferentialIntegrity();
    }

    protected void disableReferentialIntegrity() {
        //외래키 비활성화
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
    }

    protected abstract void truncateTables();

    protected abstract void resetAutoIncrement();

    protected abstract void insertInitialData();

    protected void enableReferentialIntegrity() {
        //외래키 활성화
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
