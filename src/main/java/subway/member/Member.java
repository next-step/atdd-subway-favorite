package subway.member;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import subway.favorite.Favorite;
import subway.favorite.Favorites;

@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Integer age;

	@Embedded
	private Favorites favorites = new Favorites();

	protected Member() {
	}

	public Member(String email, String password, Integer age) {
		this.email = email;
		this.password = password;
		this.age = age;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public Integer getAge() {
		return age;
	}

	public Favorites getFavorites() {
		return favorites;
	}

	public List<Favorite> getFavoriteList() {
		return getFavorites().getFavoriteList();
	}

	public void update(Member member) {
		this.email = member.email;
		this.password = member.password;
		this.age = member.age;
	}

	public boolean checkPassword(String password) {
		return Objects.equals(this.password, password);
	}

	public void addFavorite(Favorite favorite) {
		getFavorites().addFavorite(favorite);
		favorite.setMember(this);
	}

	public void remove(Favorite favorite) {
		getFavoriteList().remove(favorite);
	}
}
