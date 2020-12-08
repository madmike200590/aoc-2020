package io.github.madmike200590.aoc;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import at.ac.tuwien.kr.alpha.api.Alpha;
import at.ac.tuwien.kr.alpha.api.externals.Externals;
import at.ac.tuwien.kr.alpha.common.AnswerSet;
import at.ac.tuwien.kr.alpha.common.AnswerSetFormatter;
import at.ac.tuwien.kr.alpha.common.SimpleAnswerSetFormatter;
import at.ac.tuwien.kr.alpha.common.fixedinterpretations.PredicateInterpretation;
import at.ac.tuwien.kr.alpha.common.program.InputProgram;
import io.github.madmike200590.aoc.aspsupport.AspFiles;
import io.github.madmike200590.aoc.aspsupport.AspLists;
import io.github.madmike200590.aoc.aspsupport.AspStrings;

public class Launcher {

	private static final Predicate<at.ac.tuwien.kr.alpha.common.Predicate> RESULT_FILTER = (p) -> {
		return p.getName().equals("result");
	};

	// Args are asp files
	public static void main(String[] args) throws IOException {
		// Instantiate the ASP solver
		Alpha solver = new Alpha();
		// Collect external predicate definitions, i.e. register mapped java methods
		Map<String, PredicateInterpretation> externalPredicates = new HashMap<>(Externals.getStandardLibraryExternals());
		externalPredicates.putAll(Externals.scan(AspFiles.class));
		externalPredicates.putAll(Externals.scan(AspLists.class));
		externalPredicates.putAll(Externals.scan(AspStrings.class));
		// Parse given input files with respect to collected predicate definitions
		InputProgram program = solver.readProgramFiles(false, externalPredicates, Arrays.asList(args));

		boolean debug = false;
		Predicate<at.ac.tuwien.kr.alpha.common.Predicate> filter = debug ? (p) -> true : RESULT_FILTER;

		Stream<AnswerSet> answerSets = solver.solve(program, filter);

		AnswerSetFormatter<String> answerFmt = new SimpleAnswerSetFormatter("\n");
		System.out.println(answerFmt.format(answerSets.findFirst().get()));
	}

}
