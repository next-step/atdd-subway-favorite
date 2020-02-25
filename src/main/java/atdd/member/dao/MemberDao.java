package atdd.member.dao;

import atdd.member.domain.Member;
import atdd.path.application.exception.NoDataException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    private static final String EMAIL = "EMAIL";
    private static final String PASSWORD = "PASSWORD";
    private static final String MEMBER = "MEMBER";
    private static final String ID = "ID";
    private static final String NAME = "NAME";

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(MEMBER)
            .usingGeneratedKeyColumns(ID);
    }

    public Member save(Member member) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(EMAIL, member.getEmail());
        parameters.put(NAME, member.getName());
        parameters.put(PASSWORD, member.getPassword());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(id);
    }

    public Member findById(Long id) {
        Map<String, Object> result = jdbcTemplate.queryForMap(
            "select id , email, name from member where id = ?", id);
        return new Member((long) result.get(ID), (String) result.get(EMAIL), (String) result.get(NAME));
    }

    public Member findByEmail(String email) {
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(
                "select id, email, name, password from member where email = ?", email);
            return new Member((long) result.get(ID), (String) result.get(EMAIL), (String) result.get(NAME),
                (String) result.get(PASSWORD));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteById(Long id) {
        Member member = Optional.ofNullable(findById(id)).orElseThrow(NoDataException::new);
        jdbcTemplate.update("delete from member where id =?", member.getId());
    }


}
