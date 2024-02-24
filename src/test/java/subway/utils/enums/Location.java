package subway.utils.enums;

import java.util.function.Function;

import org.springframework.web.util.UriComponentsBuilder;

public enum Location {
	LINES("/lines", path -> UriComponentsBuilder.fromPath("/lines/").path(path)),
	STATIONS("/stations", path -> UriComponentsBuilder.fromPath("/stations/").path(path)),
	PATHS("/paths", path -> UriComponentsBuilder.fromPath("/paths/").path(path)),
	FAVORITES("/favorites", path -> UriComponentsBuilder.fromPath("/favorites/").path(path)),
	MEMBERS("/members", path -> UriComponentsBuilder.fromPath("/members/").path(path)),
	TOKENS("/login/token", path -> UriComponentsBuilder.fromPath("/login/token/").path(path));

	private final String uri;
	private final Function<String, UriComponentsBuilder> expression;

	Location(String uri, Function<String, UriComponentsBuilder> expression) {
		this.uri = uri;
		this.expression = expression;
	}

	public String path() {
		return uri;
	}

	public UriComponentsBuilder path(Object path) {
		return expression.apply(String.valueOf(path));
	}
}
