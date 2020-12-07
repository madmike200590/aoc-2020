package io.github.madmike200590.aoc.aspsupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This is pretty much a dummy, we only need a List type implementing Comparable in order to be able to use our lists as
 * ASP terms.
 */
public class AlphaAspList<T> implements List<T>, Comparable<AlphaAspList<T>> {

	private final List<T> delegate;

	AlphaAspList(List<T> lst) {
		this.delegate = Collections.unmodifiableList(new ArrayList<>(lst));

	}

	@SafeVarargs
	public static <T> AlphaAspList<T> of(T... items) {
		List<T> lst = new ArrayList<>();
		for (T item : items) {
			lst.add(item);
		}
		return new AlphaAspList<T>(lst);
	}

	@Override
	public int compareTo(AlphaAspList<T> o) {
		return this.delegate.equals(o) ? 0 : this.delegate.size() - o.size();
	}

	@Override
	public int size() {
		return this.delegate.size();
	}

	@Override
	public boolean isEmpty() {
		return this.delegate.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.delegate.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return this.delegate.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.delegate.toArray();
	}

	@Override
	public <ARRT> ARRT[] toArray(ARRT[] a) {
		return this.delegate.toArray(a);
	}

	@Override
	public boolean add(T e) {
		return this.delegate.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.delegate.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.delegate.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return this.delegate.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return this.delegate.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.delegate.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.delegate.retainAll(c);
	}

	@Override
	public void clear() {
		this.delegate.clear();
	}

	@Override
	public T get(int index) {
		return this.delegate.get(index);
	}

	@Override
	public T set(int index, T element) {
		return this.delegate.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		this.delegate.add(index, element);
	}

	@Override
	public T remove(int index) {
		return this.delegate.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return this.delegate.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.delegate.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return this.delegate.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return this.delegate.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return this.delegate.subList(fromIndex, toIndex);
	}

	@Override
	public boolean equals(Object o) {
		return this.delegate.equals(o);
	}

}
