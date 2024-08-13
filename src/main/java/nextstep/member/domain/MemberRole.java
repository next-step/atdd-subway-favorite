package nextstep.member.domain;

import nextstep.security.domain.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {

  MEMBER;


  @Override
  public String getAuthority() {
    return this.name();
  }

}
