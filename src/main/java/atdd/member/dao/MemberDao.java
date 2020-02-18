package atdd.member.dao;

import atdd.member.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberDao
{
    private final JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource)
    {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("MEMBER")
                .usingGeneratedKeyColumns("ID");
    }

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Member save(Member member)
    {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ID", member.getId());
        parameters.put("EMAIL", member.getEmail());
        parameters.put("NAME", member.getName());
        parameters.put("PASSWORD", member.getPassword());

        Long memberId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return findById(memberId);
    }

    public Member findById(long id)
    {
        String sql = "SELECT ID " +
                     "      ,EMAIL " +
                     "      ,NAME " +
                     "      ,PASSWORD " +
                     "  FROM MEMBER M " +
                     " WHERE M.ID = ?";

        Member member = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new Member(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password")
                ));
        return member;
    }

    public void deleteById(Long id)
    {
        String sql = "DELETE " +
                     "  FROM MEMBER" +
                     " WHERE ID = ?";
        jdbcTemplate.update(sql, id);
    }

    public Member findByEmailAndPassword(String email, String password)
    {
        String sql = "SELECT ID" +
                     "     , EMAIL" +
                     "     , NAME" +
                     "     , PASSWORD" +
                     "  FROM MEMBER" +
                     " WHERE EMAIL = ?" +
                     "   AND PASSWORD = ?";

        Member member = jdbcTemplate.queryForObject(sql, new Object[]{email, password}, (rs, rowNum) ->
                new Member(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password")
                ));
        return member;
    }

    public Member findByEmail(String email)
    {
        String sql = "SELECT ID" +
                     "     , EMAIL" +
                     "     , NAME" +
                     "     , PASSWORD" +
                     "  FROM MEMBER" +
                     " WHERE EMAIL = ?";

        Member member = jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) ->
                new Member(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("password")
                ));
        return member;
    }
}
