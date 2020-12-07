package io.github.madmike200590.aoc.aspsupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.tuwien.kr.alpha.api.externals.Predicate;
import at.ac.tuwien.kr.alpha.common.terms.ConstantTerm;

public final class AspFiles {

	private static final Logger LOGGER = LoggerFactory.getLogger(AspFiles.class);

	private AspFiles() {

	}

	@Predicate(name = "file_line")
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

}
