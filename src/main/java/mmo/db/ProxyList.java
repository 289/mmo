package mmo.db;

import java.util.*;

/**
 * @author Jin Shuai
 */
public class ProxyList<E> implements List<E> {
    final DbEntity parent;
    final List<E> wrapped;
    final String propName;

    public ProxyList(DbEntity parent, List<E> wrapped, String propName) {
        this.parent = parent;
        this.wrapped = wrapped;
        this.propName = propName;
    }

    @Override
    public boolean add(E e) {
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            addLogSucc = transaction.addLog(new TransLog() {
                int addIndex = wrapped.size();
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    wrapped.remove(addIndex);
                }
            });
        }
        boolean ret;
        if(addLogSucc){
            parent.removePropDirty(propName);
            ret = wrapped.add(e);
        } else {
            ret = wrapped.add(e);
            parent.markPropDirty(propName);
        }
        return ret;
    }

    @Override
    public void add(int index, E element) {
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            addLogSucc = transaction.addLog(new TransLog() {
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    wrapped.remove(index);
                }
            });
        }
        if(addLogSucc){
            parent.removePropDirty(propName);
            wrapped.add(index, element);
        } else {
            wrapped.add(index, element);
            parent.markPropDirty(propName);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            int addSize = c.size();
            addLogSucc = transaction.addLog(new TransLog() {
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    for (int i = 0; i < addSize; i++) {
                        wrapped.remove(wrapped.size() - 1);
                    }
                }
            });
        }
        boolean ret;
        if(addLogSucc){
            parent.removePropDirty(propName);
            ret = wrapped.addAll(c);
        } else {
            ret = wrapped.addAll(c);
            parent.markPropDirty(propName);
        }
        return ret;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            int addSize = c.size();
            addLogSucc = transaction.addLog(new TransLog() {
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    for (int i = index; i < index + addSize ; i++) {
                        wrapped.remove(i);
                    }
                }
            });
        }
        boolean ret;
        if(addLogSucc){
            parent.removePropDirty(propName);
            ret = wrapped.addAll(index, c);
        } else {
            ret = wrapped.addAll(index, c);
            parent.markPropDirty(propName);
        }
        return ret;
    }

    @Override
    public void clear() {
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            List<E> copy = new ArrayList<>(wrapped);
            addLogSucc = transaction.addLog(new TransLog() {
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    wrapped.addAll(copy);
                }
            });
        }
        if(addLogSucc){
            parent.removePropDirty(propName);
            wrapped.clear();
        } else {
            wrapped.clear();
            parent.markPropDirty(propName);
        }
    }

    @Override
    public E remove(int index) {
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            E removed = get(index);
            addLogSucc = transaction.addLog(new TransLog() {
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    wrapped.add(index, removed);
                }
            });
        }
        E ret;
        if(addLogSucc){
            parent.removePropDirty(propName);
            ret = wrapped.remove(index);
        } else {
            ret = wrapped.remove(index);
            parent.markPropDirty(propName);
        }
        return ret;
    }

    @Override
    public boolean remove(Object o) {
        for (int index = 0; index < wrapped.size(); index++){
            E curr = wrapped.get(index);
            if ((o == null && curr == null) || o.equals(curr)) {
                remove(index);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            List<E> old = new ArrayList<>(wrapped);
            addLogSucc = transaction.addLog(new TransLog() {
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    wrapped.clear();
                    wrapped.addAll(old);
                }
            });
        }
        boolean ret;
        if(addLogSucc){
            parent.removePropDirty(propName);
            ret = wrapped.removeAll(c);
        } else {
            ret = wrapped.removeAll(c);
            parent.markPropDirty(propName);
        }
        return ret;
    }

    @Override
    public E set(int index, E element) {
        Transaction transaction = Transaction.current();
        boolean addLogSucc = false;
        if(transaction != null){
            E removed = get(index);
            addLogSucc = transaction.addLog(new TransLog() {
                @Override
                public void commit() {
                    parent.markPropDirty(propName);
                }

                @Override
                public void rollback() {
                    wrapped.set(index, removed);
                }
            });
        }
        E ret;
        if(addLogSucc){
            parent.removePropDirty(propName);
            ret = wrapped.set(index, element);
        } else {
            ret = wrapped.set(index, element);
            parent.markPropDirty(propName);
        }
        return ret;
    }

    //////////////////////////////下面是只读方法///////////////////////////////////////////////////////////////////

    @Override
    public boolean contains(Object o) {
        return wrapped.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return wrapped.containsAll(c);
    }

    @Override
    public boolean equals(Object obj) {
        return wrapped.equals(obj);
    }

    @Override
    public E get(int index) {
        return wrapped.get(index);
    }

    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }

    @Override
    public int indexOf(Object o) {
        return wrapped.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    private class WrapIt implements Iterator<E> {
        private Iterator<E> it = wrapped.iterator();
        private E current;

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public E next() {
            return current = it.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class WrapListIt implements ListIterator<E> {
        private ListIterator<E> it;
        private E current;

        WrapListIt() {
            it = wrapped.listIterator();
        }

        WrapListIt(int index) {
            it = wrapped.listIterator(index);
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return it.hasPrevious();
        }

        @Override
        public E next() {
            return current = it.next();
        }

        @Override
        public int nextIndex() {
            return it.nextIndex();
        }

        @Override
        public E previous() {
            return current = it.previous();
        }

        @Override
        public int previousIndex() {
            return it.previousIndex();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ProxyList<E>.WrapIt();
    }

    @Override
    public int lastIndexOf(Object o) {
        return wrapped.lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ProxyList.WrapListIt();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ProxyList.WrapListIt(index);
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        // 通过 WrapIt 实现修改操作拦截。
        boolean modified = false;
        Iterator<E> e = iterator();
        while (e.hasNext()) {
            if (!c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return wrapped.toArray();
    }

    @Override
    public <X> X[] toArray(X[] a) {
        return wrapped.toArray(a);
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }

}
