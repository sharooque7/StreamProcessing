package org.ainzson.oops.collections.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PrimeNumberIterator implements Iterator<Integer> {

    private List<Integer> primes;
    private int index = 0;
    private int size = 0;

    public PrimeNumberIterator(List<Integer> primes) {
        this.primes = primes;
        this.size = primes.size();
    }
    @Override
    public boolean hasNext() {
        return index < size;
    }

    @Override
    public Integer next() {
        if(!hasNext()) throw  new NoSuchElementException();
        System.out.println("next() called. Returning prime: " + primes.get(index));
        return primes.get(index++);
    }
}
