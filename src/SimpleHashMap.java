import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements a generic map based on hash tables using chained
 * buckets for collision resolution.
 *
 * <p>
 * A map is a data structure that creates a key-value mapping. Keys are unique
 * in the map. That is, there cannot be more than one value associated with a
 * same key. However, two keys can map to a same value.
 * </p>
 *
 * <p>
 * The <tt>SimpleHashMap</tt> class takes two generic parameters, <tt>K</tt> and
 * <tt>V</tt>, standing for the types of keys and values respectively. Items are
 * stored in a hash table. Hash values are computed from the <tt>hashCode()</tt>
 * method of the <tt>K</tt> type objects.
 * </p>
 *
 * <p>
 * The chained buckets are implemented using Java's <tt>LinkedList</tt> class.
 * When a hash table is created, its initial table size and maximum load factor
 * is set to <tt>11</tt> and <tt>0.75</tt>. The hash table can hold arbitrarily
 * many key-value pairs and resizes itself whenever it reaches its maximum load
 * factor.
 * </p>
 *
 * <p>
 * <tt>null</tt> values are not allowed in <tt>SimpleHashMap</tt> and a
 * NullPointerException is thrown if used. You can assume that <tt>equals()</tt>
 * and <tt>hashCode()</tt> on <tt>K</tt> are defined, and that, for two non-
 * <tt>null</tt> keys <tt>k1</tt> and <tt>k2</tt>, if <tt>k1.equals(k2)</tt>
 * then <tt>k1.hashCode() == k2.hashCode()</tt>. Do not assume that if the hash
 * codes are the same that the keys are equal since collisions are possible.
 * </p>
 */
@SuppressWarnings("unchecked")
public class SimpleHashMap<K, V>
{

    /**
     * A map entry (key-value pair).
     */
    public static class Entry<K, V>
    {
        private K key;
        private V value;

        /**
         * Constructs the map entry with the specified key and value.
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

    private static int[] CAPACITY_SIZES = { 11, 23, 47, 97, 197, 397, 797,
            1597, 3203, 6421, 12853, 25717, 51437, 102877, 205759, 411527,
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
     **/
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

        int index = indexOf(key);

        for (Entry<K, V> entry : map[index])
            if (entry.getKey().equals(key))
                return entry.getValue();

        return null;
    }

    /**
     * <p>
     * Associates the specified value with the specified key in this map.
     * Neither the key nor the value can be <tt>null</tt>. If the map previously
     * contained a mapping for the key, the old value is replaced.
     * </p>
     *
     * <p>
     * If the load factor of the hash table after the insertion would exceed the
     * maximum load factor <tt>0.75</tt>, then the resizing mechanism is
     * triggered. The size of the table should grow at least by a constant
     * factor in order to ensure the amortized constant complexity, but you are
     * free to decide the exact value of the new table size (e.g. whether to use
     * a prime or not). After that, all of the mappings are rehashed to the new
     * table.
     * </p>
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

        if (loadFactor >= MAX_LOAD_FACTOR)
            rehash();

        return put(key, value, false);
    }

    private V put(K key, V value, boolean rehashing)
    {
        V prevValue = null;

        List<Entry<K, V>> bucket = map[indexOf(key)];

        if (bucket == null)
        {
            bucket = new LinkedList<Entry<K, V>>();
            updateLoadFactor();

            map[indexOf(key)] = bucket;
        }

        Entry<K, V> newEntry = new Entry<K, V>(key, value);

        bucket.add(newEntry);
        numItems++;

        if (!rehashing)
            shadow.add(newEntry);

        return prevValue;
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
                shadow.remove(entry);
                
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
     * Returns a list of all the mappings contained in this map. This method
     * will iterate over the hash table and collect all the entries into a new
     * list. If the map is empty, return an empty list (not <tt>null</tt>). The
     * order of entries in the list can be arbitrary.
     *
     * @return a list of mappings in this map
     */
    public List<Entry<K, V>> entries()
    {
        return new ArrayList<Entry<K, V>>(shadow);
    }

    private int indexOf(Object key)
    {
        int index = key.hashCode() % capacity;

        return (index < 0) ? index + capacity : index;
    }

    private void updateLoadFactor()
    {
        loadFactor = ((double) numItems) / capacity;
    }

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
