package io.github.madmike200590.aoc.day4;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

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

public class PassportChecker {

	private static final Logger LOGGER = LoggerFactory.getLogger(PassportChecker.class);

	@Predicate(name = "file_line_no")
	public static Set<List<ConstantTerm<String>>> numberedLinesFromFile(String path) {
		try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
			int lineNo = 0;
			String line;
			Set<List<ConstantTerm<String>>> retVal = new HashSet<>();
			while ((line = br.readLine()) != null) {
				retVal.add(List.of(ConstantTerm.getInstance(line), ConstantTerm.getInstance(Integer.toString(lineNo++))));
			}
			return retVal;
		} catch (IOException ex) {
			LOGGER.warn("Error reading file", ex);
			return Collections.emptySet();
		}
	}

	@Predicate(name = "string_parse_integer")
	public static Set<List<ConstantTerm<Integer>>> stringParseInteger(String str) {
		return Collections.singleton(Collections.singletonList(ConstantTerm.getInstance(Integer.valueOf(str))));
	}

	@Predicate(name = "string_is_empty")
	public static boolean isStringEmpty(String str) {
		return str.isEmpty();
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

	@Predicate(name = "array_length")
	public static <T extends Comparable<T>> Set<List<ConstantTerm<Integer>>> lengthOfList(List<T> lst) {
		return Collections.singleton(Collections.singletonList(ConstantTerm.getInstance(lst.size())));
	}

	// Args are asp files
	public static void main(String[] args) throws IOException {
		// Instantiate the ASP solver
		Alpha solver = new Alpha();
		// Collect external predicate definitions, i.e. register mapped java methods
		Map<String, PredicateInterpretation> externalPredicates = new HashMap<>(Externals.getStandardLibraryExternals());
		externalPredicates.putAll(Externals.scan(PassportChecker.class));
		// Parse given input files with respect to collected predicate definitions
		InputProgram program = solver.readProgramFiles(false, externalPredicates, Arrays.asList(args));

		java.util.function.Predicate<at.ac.tuwien.kr.alpha.common.Predicate> filter = (p) -> {
			return p.getName().equals("cnt_valid");
		};

		Stream<AnswerSet> answerSets = solver.solve(program, filter);

		AnswerSetFormatter<String> answerFmt = new SimpleAnswerSetFormatter("\n");
		System.out.println(answerFmt.format(answerSets.findFirst().get()));
	}

}
