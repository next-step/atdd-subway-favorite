package atdd.path.util;

public class MaskingUtils {

	public static String getMasked(String value) {
		int length = value.length();
		return "*".repeat(length);
	}
}
