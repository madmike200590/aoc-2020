package io.github.madmike200590.aoc.aspsupport;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import at.ac.tuwien.kr.alpha.api.externals.Predicate;
import at.ac.tuwien.kr.alpha.common.terms.ConstantTerm;

public final class AspLists {

	private AspLists() {
		
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
	
}
