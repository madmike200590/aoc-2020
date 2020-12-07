package io.github.madmike200590.aoc.day3;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import at.ac.tuwien.kr.alpha.api.Alpha;
import at.ac.tuwien.kr.alpha.api.externals.Externals;
import at.ac.tuwien.kr.alpha.common.AnswerSet;
import at.ac.tuwien.kr.alpha.common.atoms.Atom;
import at.ac.tuwien.kr.alpha.common.fixedinterpretations.PredicateInterpretation;
import at.ac.tuwien.kr.alpha.common.program.InputProgram;
import at.ac.tuwien.kr.alpha.common.terms.ConstantTerm;

public class TobboganNavigator {

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
