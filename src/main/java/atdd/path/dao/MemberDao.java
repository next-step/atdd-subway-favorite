package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("ID");
    }

    public Member save(Member member) {
        final Map<String, String> params = Map.ofEntries(
                Map.entry("email", member.getEmail()),
                Map.entry("name", member.getName()),
                Map.entry("password", member.getPassword())
        );

        final Long memberId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return findById(memberId).orElseThrow(NoDataException::new);
    }

    public void deleteById(Long memberId) {
        final Member findMember = findById(memberId).orElseThrow(NoDataException::new);
        jdbcTemplate.update("delete from member where id = ?", findMember.getId());
    }

    private Optional<Member> findById(Long memberId) {
        final List<Member> results = jdbcTemplate.query(
                "select id, email, name, password from member m where m.id = ?",
                new Object[]{memberId},
                mapper);

        return ofNullable(CollectionUtils.isEmpty(results) ? null : results.get(0));
    }

    private static RowMapper<Member> mapper = (rs, rowNum) -> new Member(
            rs.getLong("ID"),
            rs.getString("EMAIL"),
            rs.getString("NAME"),
            rs.getString("PASSWORD")
    );

}
