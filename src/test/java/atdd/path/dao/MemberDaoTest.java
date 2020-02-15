package atdd.path.dao;

import atdd.path.application.exception.NoDataException;
import atdd.path.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @DisplayName("회원 정보를 저장해야 한다")
    @Test
    void mustSave() {
        Member savedMember = memberDao.save(TEST_MEMBER);

        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo(TEST_MEMBER_EMAIL);
        assertThat(savedMember.getName()).isEqualTo(TEST_MEMBER_NAME);
        assertThat(savedMember.getPassword()).isEqualTo(TEST_MEMBER_PASSWORD);
    }

    @DisplayName("회원 아이디로 회원 정보를 삭제해야 한다")
    @Test
    void mustDeleteById() {
        Member savedMember = memberDao.save(TEST_MEMBER);

        memberDao.deleteById(savedMember.getId());

        assertThrows(
                NoDataException.class,
                () -> memberDao.deleteById(savedMember.getId())
        );
    }

}