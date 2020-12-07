package io.github.madmike200590.aoc.day3;

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
import at.ac.tuwien.kr.alpha.common.atoms.Atom;
import at.ac.tuwien.kr.alpha.common.fixedinterpretations.PredicateInterpretation;
import at.ac.tuwien.kr.alpha.common.program.InputProgram;
import at.ac.tuwien.kr.alpha.common.terms.ConstantTerm;

public class TobboganNavigator {

	private static final Logger LOGGER = LoggerFactory.getLogger(TobboganNavigator.class);

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

	@Predicate(name = "string_char_at")
	public static Set<List<ConstantTerm<String>>> getCharacterAt(String str, int idx) {
		String retVal = Character.toString(str.charAt(idx));
		return Collections.singleton(Collections.singletonList(ConstantTerm.getInstance(retVal)));
	}

	// Args are asp files
	public static void main(String[] args) throws IOException {
		// Instantiate the ASP solver
		Alpha solver = new Alpha();
		// Collect external predicate definitions, i.e. register mapped java methods
		Map<String, PredicateInterpretation> externalPredicates = new HashMap<>(Externals.getStandardLibraryExternals());
		externalPredicates.putAll(Externals.scan(TobboganNavigator.class));
		// Parse given input files with respect to collected predicate definitions
		InputProgram program = solver.readProgramFiles(false, externalPredicates, Arrays.asList(args));

//		java.util.function.Predicate<at.ac.tuwien.kr.alpha.common.Predicate> filter = (p) -> {
//			return p.getName().equals("hit_tree_at")
//					|| p.getName().equals("cnt_trees")
//					|| p.getName().equals("object_at");
//		};

		// Stream<AnswerSet> answerSets = solver.solve(program, filter);
		Stream<AnswerSet> answerSets = solver.solve(program);

		AnswerSet as = answerSets.findFirst().get();
		long result = 1;
		for (Atom atom : as.getPredicateInstances(at.ac.tuwien.kr.alpha.common.Predicate.getInstance("cnt_trees", 2))) {
			ConstantTerm<Integer> numTerm = (ConstantTerm<Integer>) atom.getTerms().get(1);
			long val = numTerm.getObject();
			result *= val;
		}
		System.out.println("Convoluted tree count: " + Long.toString(result));

//		PrintStream ps = new PrintStream(new File("/tmp/aoc_d3_p1.asp.out"));
//		AnswerSetFormatter<String> answerFmt = new SimpleAnswerSetFormatter("\n");
//		ps.println(answerFmt.format(answerSets.findFirst().get()));
//		ps.close();
	}

}
