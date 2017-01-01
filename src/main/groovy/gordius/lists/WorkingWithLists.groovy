package gordius.lists

import groovy.transform.TailRecursive

/**
 * Created by arthur on 31/12/2016.
 */
class WorkingWithLists {
    static last(List list) {
        def _last
        _last = { List ls ->
            if (ls == [ls.head()]) ls.head()
            else _last.trampoline(ls.tail())
        }.trampoline()

        if (list) _last(list)
        else throw new NoSuchElementException()
    }

    static lastN(List list, final int n) {
        _lastN(list, n, 0).last()
    }

    private static _lastN(List list, final int n, final int count) {
        int newCount = count + 1

        if (list == [list.head()]) {
            if (newCount <= n) throw new NoSuchElementException()
            else [newCount, list.head()]
        }
        else {
            def (size, item) = _lastN(list.tail(), n, newCount)
            if (n == size - newCount) [size, list.head()]
            else [size, item]
        }

    }
}
