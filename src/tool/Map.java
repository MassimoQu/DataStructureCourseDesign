package tool;

public interface Map<K,V> {
	int size();
	boolean containsKey(Object key);
	V get(Object key);
	V put(K key,V value);
	V remove(Object key);
}
