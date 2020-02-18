package atdd.member.dao;


import static atdd.path.TestConstant.LINE_NAME;
import static atdd.path.TestConstant.TEST_LINE;
import static org.assertj.core.api.Assertions.assertThat;

import atdd.member.domain.Member;
import atdd.path.dao.EdgeDao;
import atdd.path.dao.LineDao;
import atdd.path.dao.StationDao;
import atdd.path.domain.Line;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class MemberDaoTest {

    private static final Member MEMBER = new Member("seok2@naver.com", "이재석" , "1234");
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

    @Test
    @DisplayName("회원 저장")
    public void save() {
        Member member = memberDao.save(MEMBER);
        assertThat(member.getId()).isNotNull();
        assertThat(member.getEmail()).isEqualTo(MEMBER.getEmail());
        assertThat(member.getName()).isEqualTo(MEMBER.getName());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void deleteById() {
        Member member = memberDao.save(MEMBER);
        memberDao.deleteById(member.getId());
    }
}