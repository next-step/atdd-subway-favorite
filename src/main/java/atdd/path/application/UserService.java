package atdd.path.application;

import atdd.path.application.dto.UserResponseView;
import atdd.path.application.exception.InvalidJwtAuthenticationException;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import atdd.path.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

	private MemberDao memberDao;
	private JwtTokenProvider jwtTokenProvider;

	public UserService(final MemberDao memberDao, final JwtTokenProvider jwtTokenProvider) {
		this.memberDao = memberDao;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	public UserResponseView retrieveMyInfo(final HttpServletRequest req) {
		String token = jwtTokenProvider.resolveToken(req);
		if (!jwtTokenProvider.validateToken(token)) {
			throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
		}
		String email = jwtTokenProvider.getUserEmail(token);
		Member member = memberDao.findByEmail(email).orElseThrow(MemberNotFoundException::new);
		return UserResponseView.of(member);
	}
}
