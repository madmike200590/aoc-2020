package io.github.madmike200590.aoc.day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.kr.alpha.api.Alpha;
import at.ac.tuwien.kr.alpha.api.externals.Externals;
import at.ac.tuwien.kr.alpha.api.externals.Predicate;
import at.ac.tuwien.kr.alpha.common.AnswerSet;
import at.ac.tuwien.kr.alpha.common.AnswerSetFormatter;
import at.ac.tuwien.kr.alpha.common.SimpleAnswerSetFormatter;
import at.ac.tuwien.kr.alpha.common.fixedinterpretations.PredicateInterpretation;
import at.ac.tuwien.kr.alpha.common.program.InputProgram;
import at.ac.tuwien.kr.alpha.common.terms.ConstantTerm;
import io.github.madmike200590.aoc.AlphaAspList;

public class PasswordPolicyChecker {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordPolicyChecker.class);

	@at.ac.tuwien.kr.alpha.api.externals.Predicate(name = "file_line")
	public static Set<List<ConstantTerm<String>>> linesInFile(String path) {
		try {
			return Files.lines(Paths.get(path))
					.map((line) -> Collections.singletonList(ConstantTerm.getInstance(line)))
					.collect(Collectors.toSet());
		} catch (IOException ex) {
			LOGGER.warn("IOException accessing path " + path, ex);
			return Collections.emptySet();
		}
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

	@Predicate(name = "array_idx_value")
	public static <T extends Comparable<T>> Set<List<ConstantTerm<T>>> getValueAt(List<T> lst, int idx) {
		if (idx >= lst.size()) {
			return Collections.emptySet();
		}
		return Collections.singleton(Collections.singletonList(ConstantTerm.getInstance(lst.get(idx))));
	}

	// Args are asp files
	public static void main(String[] args) throws IOException {
		// Instantiate the ASP solver
		Alpha solver = new Alpha();
		// Collect external predicate definitions, i.e. register mapped java methods
		Map<String, PredicateInterpretation> externalPredicates = new HashMap<>(Externals.getStandardLibraryExternals());
		externalPredicates.putAll(Externals.scan(PasswordPolicyChecker.class));
		// Parse given input files with respect to collected predicate definitions
		InputProgram program = solver.readProgramFiles(false, externalPredicates, Arrays.asList(args));

		java.util.function.Predicate<at.ac.tuwien.kr.alpha.common.Predicate> filter = (p) -> {
			return p.getName().equals("valid_passwords")
					|| p.getName().equals("satisfies_policy");
		};

		Stream<AnswerSet> answerSets = solver.solve(program, filter);

		AnswerSetFormatter<String> answerFmt = new SimpleAnswerSetFormatter("\n");
		System.out.println(answerFmt.format(answerSets.findFirst().get()));
	}

}
