package io.github.madmike200590.aoc.aspsupport;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import at.ac.tuwien.kr.alpha.api.externals.Predicate;
import at.ac.tuwien.kr.alpha.common.terms.ConstantTerm;

public final class AspStrings {

	private AspStrings() {

	}

	@Predicate(name = "string_char_at")
	public static Set<List<ConstantTerm<String>>> getCharacterAt(String str, int idx) {
		String retVal = Character.toString(str.charAt(idx));
		return Collections.singleton(Collections.singletonList(ConstantTerm.getInstance(retVal)));
	}

	// &string_substring_occurences[PASSWD, CHARSEQ](ACT_OCCURS)
	@Predicate(name = "string_substring_occurences")
	public static Set<List<ConstantTerm<Integer>>> calculateOccurences(String str, String pattern) {
		int occurences = StringUtils.countMatches(str, pattern);
		return Collections.singleton(Collections.singletonList(ConstantTerm.getInstance(occurences)));
	}

	@Predicate(name = "string_parse_integer")
	public static Set<List<ConstantTerm<Integer>>> stringParseInteger(String str) {
		return Collections.singleton(Collections.singletonList(ConstantTerm.getInstance(Integer.valueOf(str))));
	}

	@Predicate(name = "string_trim")
	public static Set<List<ConstantTerm<String>>> trimString(String str) {
		return Collections.singleton(Collections.singletonList(ConstantTerm.getInstance(str.trim())));
	}

	@Predicate(name = "string_split")
	public static Set<List<ConstantTerm<AlphaAspList<String>>>> splitString(String str, String regex) {
		// LOGGER.info("Split: {} by {}", str, regex);
		return Collections.singleton(
				Collections.singletonList(
						ConstantTerm.getInstance(AlphaAspList.of(str.split(regex)))));
	}

	@Predicate(name = "string_is_empty")
	public static boolean isStringEmpty(String str) {
		return str.isEmpty();
	}

	@Predicate(name = "string_substring")
	public static Set<List<ConstantTerm<String>>> substringOfString(String str, int startIdx, int endIdx) {
		return Collections.singleton(Collections.singletonList(ConstantTerm.getInstance(str.substring(startIdx, endIdx))));
	}

	@Predicate(name = "str_x_xs")
	public static <T extends Comparable<T>> Set<List<ConstantTerm<String>>> stringHeadRemainder(String str) {
		List<ConstantTerm<String>> xXs = null;
		if (str.isEmpty()) {
			return Collections.emptySet();
		} else if (str.length() == 1) {
			xXs = List.of(ConstantTerm.getInstance(str), ConstantTerm.getInstance(""));
		} else {
			xXs = List.of(ConstantTerm.getInstance(str.substring(0, 1)), ConstantTerm.getInstance(str.substring(1, str.length())));
		}
		return Collections.singleton(xXs);
	}

}
