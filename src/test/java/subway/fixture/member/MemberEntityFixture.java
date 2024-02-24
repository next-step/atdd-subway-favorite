package subway.fixture.member;

import subway.member.Member;

public class MemberEntityFixture {
	public static Member 멤버_생성() {
		return new Member("admin@admin.com", "password", 20);
	}
}
