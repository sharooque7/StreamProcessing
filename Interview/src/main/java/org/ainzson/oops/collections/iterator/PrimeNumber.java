package org.ainzson.oops.collections.iterator;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrimeNumber implements Iterable<Integer> {
    private final List<Integer> primes ;

    public PrimeNumber(int... numbers) {
        this.primes = this.checkPrime(numbers);
    }

    private List<Integer> checkPrime(int[] nums) {
        List<Integer> primes = new ArrayList<>();
        for (int i: nums) {
            int count = 0;
            for(int j = 1; j<= i ; j++) {
                if (i % j == 0) {
                    count++;
                }
            }
            if(count == 2) {
                primes.add(i);
            }
        }
        return primes;
    }

    public void displayPrimes() {
        for (int i: primes){
            System.out.println(i);
        }
    }

    @Override
    @NonNull
    public Iterator<Integer> iterator() {
        return new PrimeNumberIterator(primes);
    }
}
