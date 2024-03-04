package nextstep.common;

import java.lang.reflect.Field;

public class ReflectionUtils {
	public static void setId(Object object, Long id) {
		try {
			Field idField = object.getClass().getDeclaredField("id");
			idField.setAccessible(true);
			idField.set(object, id);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
