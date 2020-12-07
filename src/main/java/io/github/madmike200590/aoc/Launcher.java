package io.github.madmike200590.aoc;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import at.ac.tuwien.kr.alpha.api.Alpha;
import at.ac.tuwien.kr.alpha.api.externals.Externals;
import at.ac.tuwien.kr.alpha.common.AnswerSet;
import at.ac.tuwien.kr.alpha.common.AnswerSetFormatter;
import at.ac.tuwien.kr.alpha.common.SimpleAnswerSetFormatter;
import at.ac.tuwien.kr.alpha.common.fixedinterpretations.PredicateInterpretation;
import at.ac.tuwien.kr.alpha.common.program.InputProgram;
import io.github.madmike200590.aoc.day4.PassportChecker;

public class Launcher {
	
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
