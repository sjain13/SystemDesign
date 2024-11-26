package code.java;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.function.Function;

public class BloomFilter<T>{
    private final BitSet bitSet;
    private final int bitSetSize;
    private final int numberofHashFunctions;
    private final Function<T, Integer>[] hashFunctions;

    public BloomFilter(int bitSetSize, int numberofHashFunctions, Function<T,Integer>... hashFunctions){
        this.bitSetSize = bitSetSize;
        this.numberofHashFunctions = numberofHashFunctions;
        this.hashFunctions = hashFunctions;
        this.bitSet = new BitSet(bitSetSize);
    }

    // Add an element to the Bloom Filter
    public void add(T element){
        for(Function<T, Integer> hashFunction : hashFunctions){
            int hash = Math.abs(hashFunction.apply(element)) % bitSetSize;
            bitSet.set(hash);
        }
    }

    // Check if an element might exist in the Bloom Filter
    public boolean mightContain(T element) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = Math.abs(hashFunction.apply(element)) % bitSetSize;
            if (!bitSet.get(hash)) {
                return false; // Definitely not in the set
            }
        }
        return true; // Might be in the set
    }

    // Main method for testing
    public static void main(String[] args) {
        // Define hash functions for Strings
        Function<String, Integer> hash1 = s -> s.hashCode();
        Function<String, Integer> hash2 = s -> s.hashCode() * 31;
        Function<String, Integer> hash3 = s -> {
            int hash = 0;
            for (byte b : s.getBytes(StandardCharsets.UTF_8)) {
                hash = (hash * 31) + b;
            }
            return hash;
        };

        // Create a Bloom Filter with 3 hash functions and a bit set of size 100
        @SuppressWarnings("unchecked")
        BloomFilter<String> bloomFilter = new BloomFilter<>(100, 3, hash1, hash2, hash3);

        // Add elements to the Bloom Filter
        bloomFilter.add("apple");
        bloomFilter.add("banana");
        bloomFilter.add("cherry");

        // Test elements
        System.out.println("Might contain 'apple': " + bloomFilter.mightContain("apple")); // True
        System.out.println("Might contain 'banana': " + bloomFilter.mightContain("banana")); // True
        System.out.println("Might contain 'grape': " + bloomFilter.mightContain("grape")); // False (or true, as a false positive)

    }
}