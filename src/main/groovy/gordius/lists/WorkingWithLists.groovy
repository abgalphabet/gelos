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
        def _lastN
        _lastN = { List ls, final int i ->
            if (i == 0) ls.head()
            else _lastN.trampoline(ls.tail(), i-1)
        }.trampoline()

        if (list) _lastN(list, size(list) - n - 1)
        else throw new NoSuchElementException()
    }

    @TailRecursive
    static nth(List list, final int n) {
        if (n == 0) list.head()
        else nth(list.tail(), n-1)
    }

    static size(List list) {
        def _size
        _size = { List ls, int count ->
            if (ls.empty) count
            else _size.trampoline(ls.tail(), count+1)
        }.trampoline()

        _size(list, 0)
    }

    static reverse(List list) {
        def _reverse
        _reverse = { List ls, Closure state ->
            if (ls.empty) state([])
            else _reverse.trampoline(ls.tail(), { it << ls.head() } >> state )
        }.trampoline()

        _reverse(list, {it})
    }
}
