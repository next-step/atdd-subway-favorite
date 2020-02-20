package atdd.path.application;

import atdd.path.application.dto.AccessTokenResponseView;
import atdd.path.application.dto.CreateAccessTokenRequestView;
import atdd.path.dao.MemberDao;
import atdd.path.domain.Member;
import atdd.path.exception.MemberNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

	private MemberDao memberDao;
	private JwtTokenProvider jwtTokenProvider;

	public LoginService(final MemberDao memberDao, final JwtTokenProvider jwtTokenProvider) {
		this.memberDao = memberDao;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Transactional
	public AccessTokenResponseView createToken(final CreateAccessTokenRequestView request) {
		String email = request.getEmail();
		Member persistMember = memberDao.findByEmail(email).orElseThrow(MemberNotFoundException::new);
		String token = jwtTokenProvider.createToken(persistMember.getEmail());
		return AccessTokenResponseView.of(token);
	}
}
