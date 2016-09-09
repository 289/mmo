package mmo.db;

import java.util.*;

/**
 * @author Jin Shuai
 */
public class ProxyMap<K, V> implements Map<K, V> {
    final DbEntity parent;
    final Map<K, V> wrapped;
    final String propName;

    public ProxyMap(DbEntity parent, Map<K, V> wrapped, String propName) {
        this.parent = parent;
        this.wrapped = wrapped;
        this.propName = propName;
    }

    private void transPut(Map<? extends K, ? extends V> m){
        if (transPut0(m)) {
            parent.removePropDirty(propName);
            wrapped.putAll(m);
        } else {
            wrapped.putAll(m);
            parent.markPropDirty(propName);
        }
    }

    private V transPut(K k, V v){
        Map<K, V> add = new HashMap<>(3);
        add.put(k, v);
        V old = null;
        if (!transPut0(add)) {
            old = wrapped.put(k, v);
            parent.markPropDirty(propName);
        } else {
            parent.removePropDirty(propName);
            old = wrapped.put(k, v);
        }
        return old;
    }

    private boolean transPut0(Map<? extends K, ? extends V> m){
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            Set<K> newKeys = new HashSet<>(m.keySet());
            Map<K, V> old = new HashMap<K, V>();
            newKeys.stream().filter(k -> wrapped.containsKey(k)).forEach(k -> old.put(k, wrapped.get(k)));
            addLogSucc = transaction.addLog(new TransLog() {
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    newKeys.forEach(k -> wrapped.remove(k));
                    wrapped.putAll(old);
                }
            });
        }
        return addLogSucc;
    }

    private void transRemove(Set<K> keys){
        if (!transRemove0(keys)) {
            keys.forEach(k -> wrapped.remove(k));
            parent.markPropDirty(propName);
        } else {
            parent.removePropDirty(propName);
            keys.forEach(k -> wrapped.remove(k));
        }
    }

    private V transRemove(K key){
        Set<K> set = new HashSet<>();
        set.add(key);
        V ret = null;
        if (!transRemove0(set)) {
            ret = wrapped.remove(key);
            parent.markPropDirty(propName);
        } else {
            parent.removePropDirty(propName);
            ret = wrapped.remove(key);
        }
        return ret;
    }

    private boolean transRemove0(Set<K> keys){
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            Set<K> removeKeys = new HashSet<>(keys);
            Map<K, V> old = new HashMap<K, V>();
            removeKeys.stream().filter(k -> wrapped.containsKey(k)).forEach(k -> old.put(k, wrapped.get(k)));
            addLogSucc = transaction.addLog(new TransLog() {
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    wrapped.putAll(old);
                }
            });
        }
        return addLogSucc;
    }

    @Override
    public V put(K key, V value) {
        return transPut(key, value);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        transPut(m);
    }

    @Override
    public V remove(Object arg0) {
        return transRemove((K) arg0);
    }

    @Override
    public void clear() {
        transRemove(wrapped.keySet());
    }

    public int compare(Integer i1, Integer i2){
        return i1 == null ? (i2 == null ?  0 : -1) : (i2 == null ? 1 : Integer.compare(i1, i2));
    }

    @Override
    public boolean containsKey(Object key) {
        return wrapped.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return wrapped.containsValue(value);
    }

    abstract class WrapEntryIt<E> implements Iterator<E> {
        private final Iterator<Entry<K, V>> it;
        private WrapEntry current;

        WrapEntryIt() {
            this.it = wrapped.entrySet().iterator();
        }

        WrapEntryIt(Iterator<Entry<K, V>> it) {
            this.it = it;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        Entry<K, V> nextEntry() {
            return current = new WrapEntry(it.next());
        }
    }

    private Iterator<Entry<K, V>> newEntryIterator() {
        return new WrapEntryIt<Entry<K, V>>() {
            public Entry<K, V> next() {
                return nextEntry();
            }
        };
    }

    final class WrapEntry implements Map.Entry<K, V> {
        private Map.Entry<K, V> e;
        WrapEntry(Map.Entry<K, V> e) {
            this.e = e;
        }

        @Override
        public boolean equals(Object obj) {
            return e.equals(obj);
        }

        @Override
        public K getKey() {
            return e.getKey();
        }

        @Override
        public V getValue() {
            return e.getValue();
        }

        @Override
        public int hashCode() {
            return e.hashCode();
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }

    private final class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return ProxyMap.this.newEntryIterator();
        }

        @Override
        public boolean contains(Object o) {
            return ProxyMap.this.wrapped.entrySet().contains(o);
        }

        @Override
        public int size() {
            return ProxyMap.this.wrapped.entrySet().size();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            transRemove(wrapped.keySet());
        }
    }

    private EntrySet esview;

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return esview != null ? esview : (esview = new EntrySet());
    }

    @Override
    public V get(Object key) {
        return wrapped.get(key);
    }

    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return wrapped.equals(obj);
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    private Iterator<K> newKeyIterator() {
        return new WrapEntryIt<K>() {
            public K next() {
                return nextEntry().getKey();
            }
        };
    }

    private final class KeySet extends AbstractSet<K> {
        @Override
        public Iterator<K> iterator() {
            return ProxyMap.this.newKeyIterator();
        }

        @Override
        public int size() {
            return ProxyMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return ProxyMap.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            transRemove(wrapped.keySet());
        }
    }

    private KeySet ksview;

    @Override
    public Set<K> keySet() {
        return ksview != null ? ksview: (ksview = new KeySet());
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    private Iterator<V> newValueIterator() {
        return new WrapEntryIt<V>() {
            public V next() {
                return nextEntry().getValue();
            }
        };
    }

    private final class Values extends AbstractCollection<V> {
        @Override
        public Iterator<V> iterator() {
            return ProxyMap.this.newValueIterator();
        }

        @Override
        public int size() {
            return ProxyMap.this.size();
        }

        @Override
        public boolean contains(Object o) {
            return ProxyMap.this.containsValue(o);
        }

        @Override
        public void clear() {
            transRemove(wrapped.keySet());
        }
    }

    private Values vsview;

    @Override
    public Collection<V> values() {
        return vsview != null ? vsview: (vsview = new Values());
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }

}
