package atdd.path.dao;

import atdd.path.domain.Member;
import atdd.path.exception.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class MemberDaoTest {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSource dataSource;

	private MemberDao memberDao;

	@BeforeEach
	void setUp() {
		memberDao = new MemberDao(jdbcTemplate);
		memberDao.setDataSource(dataSource);
	}

	@DisplayName("회원 객체를 저장")
	@Test
	void save() {
		// given
		Member member = new Member(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);

		// when
		Member persistMember = memberDao.save(member);

		// then
		assertThat(persistMember.getEmail()).isEqualTo(MEMBER_EMAIL);
		assertThat(persistMember.getName()).isEqualTo(MEMBER_NAME);
	}

	@DisplayName("id로 Member 조회")
	@Test
	void findById() {
		// given
		Member member = new Member(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);
		Member persistMember = memberDao.save(member);

		// when
		Optional<Member> byId = memberDao.findById(persistMember.getId());

		// then
		assertThat(byId.get().getEmail()).isEqualTo(MEMBER_EMAIL);
		assertThat(byId.get().getName()).isEqualTo(MEMBER_NAME);
	}

	@DisplayName("id로 Member 삭제")
	@Test
	void deleteById() {
		// given
		Member member = new Member(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);
		Member persistMember = memberDao.save(member);

		// when
		memberDao.deleteById(persistMember.getId());

		// then
		assertThatThrownBy(() -> memberDao.findById(persistMember.getId()))
			.isInstanceOf(MemberNotFoundException.class);
	}

	@DisplayName("email로 Member 조회")
	@Test
	void findByEmail() {
		// given
		Member member = new Member(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);
		Member persistMember = memberDao.save(member);

		// when
		Optional<Member> memberOptional = memberDao.findByEmail(persistMember.getEmail());

		// then
		assertThat(memberOptional.get().getEmail()).isEqualTo(MEMBER_EMAIL);
		assertThat(memberOptional.get().getName()).isEqualTo(MEMBER_NAME);
	}
}