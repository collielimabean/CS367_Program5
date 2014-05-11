///////////////////////////////////////////////////////////////////////////////
// Main Class File:  LoadBalancerMain.java
// File:             SimpleHashMap.java
// Semester:         CS367 Spring 2014
//
// Author:           Allen Hung <athung2@wisc.edu>
// CS Login:         ahung
// Lecturer's Name:  Professor Jim Skrentny
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
// Pair Partner:     William Jen <wjen@wisc.edu>
// CS Login:         jen
// Lecturer's Name:  Professor Jim Skrentny
//////////////////////////// 80 columns wide ///////////////////////////////////

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements a generic map based on hash tables using chained
 * buckets for collision resolution.
 */
@SuppressWarnings("unchecked")
public class SimpleHashMap<K, V>
{
    
    /**
     * A map entry (key-value pair).
     * 
     * @param K
     *            Key value for the hash tag
     * @param V
     *            Value or data attached to the hash identifier
     */
    public static class Entry<K, V>
    {
        private K key;
        private V value;

        /**
         * Constructs the map entry with the specified key and value.
         * 
         * @param K
         *            Key value for the hash tag
         * @param V
         *            Value or data attached to the hash identifier
         */
        public Entry(K key, V value)
        {
            this.key = key;
            this.value = value;
        }

        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         */
        public K getKey()
        {
            return key;
        }

        /**
         * Returns the value corresponding to this entry.
         *
         * @return the value corresponding to this entry
         */
        public V getValue()
        {
            return value;
        }

        /**
         * Replaces the value corresponding to this entry with the specified
         * value.
         *
         * @param value
         *            new value to be stored in this entry
         * @return old value corresponding to the entry
         */
        public V setValue(V value)
        {
            V old = this.value;
            this.value = value;

            return old;
        }
    }

    /**
     * Integer array holding all possible (prime) capacities under 2^32.
     */
    private static final int[] CAPACITY_SIZES = { 11, 23, 47, 97, 197, 397,
            797, 1597, 3203, 6421, 12853, 25717, 51437, 102877, 205759, 411527,
            823117, 1646237, 3292489, 6584983, 13169977, 26339969, 52679969,
            105359939, 210719881, 421439783, 842879579, 1685759167 };

    private static final double MAX_LOAD_FACTOR = 0.75;
    private static final int INITIAL_CAPACITY = CAPACITY_SIZES[0];

    private int numItems;
    private int capacity_index;
    private int capacity;
    private double loadFactor;

    private List<Entry<K, V>>[] map;
    private List<Entry<K, V>> shadow;

    /**
     * Constructs an empty hash map with initial capacity <tt>11</tt> and
     * maximum load factor <tt>0.75</tt>.
     */
    public SimpleHashMap()
    {
        map = (LinkedList<Entry<K, V>>[]) new LinkedList<?>[INITIAL_CAPACITY];
        shadow = new ArrayList<Entry<K, V>>(INITIAL_CAPACITY);

        capacity = INITIAL_CAPACITY;
        capacity_index = 0;

        loadFactor = 0;
        numItems = 0;
    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key
     *            the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or <tt>null</tt>
     *         if this map contains no mapping for the key
     * @throws NullPointerException
     *             if the specified key is <tt>null</tt>
     */
    public V get(Object key)
    {
        if (key == null)
            throw new NullPointerException("Null key -> get");

        Entry<K, V> entry = getEntry(key);

        if (entry == null)
            return null;

        return entry.getValue();
    }

    /**
     * Get the Entry that has the specified key.
     * 
     * @param key
     *            Key to match
     * @return the Entry that holds the key and its value, null if not found
     */
    private Entry<K, V> getEntry(Object key)
    {
        int index = indexOf(key);

        if (map[index] == null)
            return null;

        for (Entry<K, V> entry : map[index])
            if (entry.getKey().equals(key))
                return entry;

        return null;
    }

    /**
     * Associates the specified value with the specified key in this map.
     *
     * @param key
     *            key with which the specified value is to be associated
     * @param value
     *            value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
     *         if there was no mapping for <tt>key</tt>.
     * @throws NullPointerException
     *             if the key or value is <tt>null</tt>
     */
    public V put(K key, V value)
    {
        if (key == null || value == null)
            throw new NullPointerException("Null key or value -> put");

        Entry<K, V> contains = getEntry(key);

        if (contains != null)
        {
            V prevValue = contains.getValue();
            contains.setValue(value);

            return prevValue;
        }

        if (loadFactor >= MAX_LOAD_FACTOR)
            rehash();

        return put(key, value, false);
    }

    /**
     * Helper method to place key into the map.
     * <p>
     * Differentiates between rehashing and regular modes, since rehashing uses
     * the shadow to rebuild the map, and thus the shadow cannot be modified
     * mid-rehashing.
     * </p>
     * 
     * @param key
     *            key to associate with specified value
     * @param value
     *            value to associate with specified key
     * @param rehashing
     *            true if in rehashing procedure, flase otherwise
     * @return the value that was previously associated with the key
     */
    private V put(K key, V value, boolean rehashing)
    {
        List<Entry<K, V>> bucket = map[indexOf(key)];

        if (bucket == null)
        {
            bucket = new LinkedList<Entry<K, V>>();
            map[indexOf(key)] = bucket;
        }

        Entry<K, V> newEntry = new Entry<K, V>(key, value);

        bucket.add(newEntry);
        numItems++;

        if (!rehashing)
            shadow.add(newEntry);

        updateLoadFactor();

        return null;
    }

    /**
     * Removes the mapping for the specified key from this map if present. This
     * method does nothing if the key is not in the hash table.
     *
     * @param key
     *            key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
     *         if there was no mapping for <tt>key</tt>.
     * @throws NullPointerException
     *             if key is <tt>null</tt>
     */
    public V remove(Object key)
    {
        if (key == null)
            throw new NullPointerException("Null key -> remove");

        V prevValue = null;

        List<Entry<K, V>> bucket = map[indexOf(key)];

        Iterator<Entry<K, V>> it_b = bucket.iterator();

        while (it_b.hasNext())
        {
            Entry<K, V> entry = it_b.next();

            if (entry.getKey().equals(key))
            {
                prevValue = entry.getValue();

                Iterator<Entry<K, V>> it_shadow = shadow.iterator();

                while (it_shadow.hasNext())
                {
                    Entry<K, V> shadow_entry = it_shadow.next();

                    if (shadow_entry.getKey().equals(key)
                            && shadow_entry.getValue().equals(prevValue))
                    {
                        it_shadow.remove();
                        break;
                    }
                }

                numItems--;

                it_b.remove();
                break;
            }
        }

        updateLoadFactor();

        return prevValue;
    }

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map
     */
    public int size()
    {
        return numItems;
    }

    /**
     * Returns a list of all the mappings contained in this map.
     *
     * @return a list of mappings in this map
     */
    public List<Entry<K, V>> entries()
    {
        return shadow;
    }

    /**
     * Returns the index where the object should be placed.
     * 
     * @return index where the object should be sent into the map.
     */
    private int indexOf(Object key)
    {
        int index = key.hashCode() % capacity;

        // add table size to index if index is negative
        return (index < 0) ? index + capacity : index;
    }

    /**
     * Updates the load factor.
     */
    private void updateLoadFactor()
    {
        loadFactor = ((double) numItems) / capacity;
    }

    /**
     * Rehashes the hashmap when the load factor exceeds the limit defined in
     * MAX_LOAD_FACTOR.
     *
     * @throws FullHashMapException
     *             if the hash map cannot expand further
     */
    private void rehash()
    {
        if (capacity_index == CAPACITY_SIZES.length - 1)
            throw new FullHashMapException(
                    "Max size of SimpleHashMap exceeded!");

        capacity_index++;
        capacity = CAPACITY_SIZES[capacity_index];

        map = (LinkedList<Entry<K, V>>[]) new LinkedList<?>[capacity];

        for (Entry<K, V> entry : shadow)
            put(entry.getKey(), entry.getValue(), true);
    }
}
