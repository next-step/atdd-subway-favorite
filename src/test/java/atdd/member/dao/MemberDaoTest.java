package atdd.member.dao;

import atdd.member.dao.MemberDao;
import atdd.member.domain.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static atdd.path.TestConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class MemberDaoTest
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private MemberDao memberDao;
    private Member persistMember;

    @BeforeEach
    void setUp()
    {
        memberDao = new MemberDao(jdbcTemplate);
        memberDao.setDataSource(dataSource);
        persistMember = saveMember();
    }

    private Member saveMember()
    {
        Member member = new Member(MEMBER_EMAIL, MEMBER_NAME, MEMBER_PASSWORD);
        return memberDao.save(member);
    }

    @DisplayName("회원 저장")
    @Test
    public void save()
    {
        // then
        assertThat(persistMember.getId()).isNotNull();
        assertThat(persistMember.getEmail()).isEqualTo(MEMBER_EMAIL);
        assertThat(persistMember.getName()).isEqualTo(MEMBER_NAME);
    }

    @DisplayName("ID로 Member 조회")
    @Test
    public void findById()
    {
        // when
        Member memberById = memberDao.findById(persistMember.getId());

        // then
        assertThat(memberById.getId()).isNotNull();
        assertThat(memberById.getEmail()).isEqualTo(MEMBER_EMAIL);
        assertThat(memberById.getName()).isEqualTo(MEMBER_NAME);
    }

    @DisplayName("회원 삭제")
    @Test
    public void deleteById()
    {
        // when
        memberDao.deleteById(persistMember.getId());

        // then
        Assertions.assertThrows(
                EmptyResultDataAccessException.class,
                () -> memberDao.findById(persistMember.getId())
        );
    }

    @DisplayName("Email, Password로 조회")
    @Test
    public void findByEmailAndPassword()
    {
        // when
        Member memberByEmailAndPassword
                = memberDao.findByEmailAndPassword(persistMember.getEmail(), persistMember.getPassword());

        // then
        assertThat(memberByEmailAndPassword.getId()).isNotNull();
        assertThat(memberByEmailAndPassword.getEmail()).isEqualTo(MEMBER_EMAIL);
        assertThat(memberByEmailAndPassword.getName()).isEqualTo(MEMBER_NAME);
    }

    @DisplayName("Email로 조회")
    @Test
    public void findByEmail()
    {
        // when
        Member memberByEmail = memberDao.findByEmail(persistMember.getEmail());

        // then
        assertThat(memberByEmail.getId()).isNotNull();
        assertThat(memberByEmail.getName()).isEqualTo(MEMBER_NAME);
    }
}
