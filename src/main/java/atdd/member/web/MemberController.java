package atdd.member.web;

import atdd.member.application.JwtTokenProvider;
import atdd.member.application.dto.CreateMemberRequestView;
import atdd.member.application.dto.MemberResponseView;
import atdd.member.application.exception.InvalidJwtAuthenticationException;
import atdd.member.dao.MemberDao;
import atdd.member.domain.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/members")
public class MemberController
{
    public static final String MEMBER_URL = "/members";
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public MemberController(MemberDao memberDao, JwtTokenProvider jwtTokenProvider)
    {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody CreateMemberRequestView view)
    {
        Member persistMember = memberDao.save(view.toMember());
        return ResponseEntity
                .created(URI.create(MEMBER_URL + "/" +persistMember.getId()))
                .body(MemberResponseView.of(persistMember));
    }

    @GetMapping("/{id}")
    public ResponseEntity retrieveMember(@PathVariable Long id)
    {
        try
        {
            Member persistMember = memberDao.findById(id);
            return ResponseEntity.ok().body(MemberResponseView.of(persistMember));
        }
        catch (EmptyResultDataAccessException e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMember(@PathVariable Long id)
    {
        memberDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity retrieveMember(HttpServletRequest req)
    {
        String extractEmail = extractEmail(req);
        Member persistMember = memberDao.findByEmail(extractEmail);
        return ResponseEntity.ok().body(MemberResponseView.of(persistMember));
    }

    public String extractEmail(HttpServletRequest req)
    {
        String token = jwtTokenProvider.resolveToken(req);
        if( StringUtils.isEmpty(token) || !jwtTokenProvider.validateToken(token) )
        {
            throw new InvalidJwtAuthenticationException("invalid token");
        }
        return jwtTokenProvider.getMemberEmail(token);
    }
}
