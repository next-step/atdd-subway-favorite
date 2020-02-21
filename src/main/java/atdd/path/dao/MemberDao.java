package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Member;
import atdd.path.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberDao {

	private final JdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	@Autowired
	public void setDataSource(final DataSource dataSource) {
		this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
			.withTableName("MEMBER")
			.usingGeneratedKeyColumns("ID");
	}

	public MemberDao(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Member save(final Member member) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("ID", member.getId());
		parameters.put("EMAIL", member.getEmail());
		parameters.put("NAME", member.getName());
		parameters.put("PASSWORD", member.getPassword());

		long memberId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
		return findById(memberId).orElseThrow(NoDataException::new);
	}

	public Optional<Member> findById(final long id) {
		String sql = "SELECT id, email, name, password " +
			"FROM MEMBER M " +
			"WHERE M.id = ?";

		Member member;
		try {
			member = jdbcTemplate.queryForObject(sql, new Object[]{id}, ((rs, rowNum) ->
				new Member(
					rs.getLong("id"),
					rs.getString("email"),
					rs.getString("name"),
					rs.getString("password")
				)
			));
		} catch (EmptyResultDataAccessException e) {
			throw new MemberNotFoundException();
		}
		return Optional.ofNullable(member);
	}

	public void deleteById(final Long id) {
		jdbcTemplate.update("delete from MEMBER where id = ?", id);
	}

	public Optional<Member> findByEmail(final String email) {
		String sql = "SELECT id, email, name, password " +
			"FROM MEMBER M " +
			"WHERE M.email = ?";

		Member member;
		try {
			member = jdbcTemplate.queryForObject(sql, new Object[]{email}, ((rs, rowNum) ->
				new Member(
					rs.getLong("id"),
					rs.getString("email"),
					rs.getString("name"),
					rs.getString("password")
				)
			));
		} catch (EmptyResultDataAccessException e) {
			throw new MemberNotFoundException();
		}
		return Optional.ofNullable(member);
	}
}