package gordius.lists

import groovy.transform.TailRecursive

import static java.lang.Math.abs

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
        _lastN = { List ls, int count, Closure state ->
            if (ls.empty) state(count, null)
            else _lastN.trampoline(ls.tail(), count + 1, state << { int size, def result ->
                if (result) [size, result]
                else (n == size - count - 1) ? [size, ls.head()] : [size, null]
            })
        }.trampoline()

        def result = _lastN(list, 0, { count, item -> [count, item] }).last()
        if (!result) throw new NoSuchElementException()

        result
    }

    @TailRecursive
    static nth(List list, final int n) {
        if (n == 0) list.head()
        else nth(list.tail(), n - 1)
    }

    static size(List list) {
        def _size
        _size = { List ls, int count ->
            if (ls.empty) count
            else _size.trampoline(ls.tail(), count + 1)
        }.trampoline()

        _size(list, 0)
    }

    static reverse(List list) {
        def _reverse
        _reverse = { List ls, Closure state ->
            if (ls.empty) state([])
            else _reverse.trampoline(ls.tail(), { it << ls.head() } >> state)
        }.trampoline()

        _reverse(list, { it })
    }

    static isPalindrome(List list) {
        list == reverse(list)
    }

    static flatten(List list) {
        def _flatten
        _flatten = { List ls, result ->
            if (ls.empty) result
            else if (ls.head() instanceof List) _flatten.trampoline(ls.tail(), result + _flatten(ls.head(), []))
            else _flatten.trampoline(ls.tail(), result << ls.head())
        }.trampoline()

        _flatten(list, [])
    }

    static compress(List list) {
        def _unique
        _unique = { List ls, result, last ->
            if (ls.empty) result
            else if (ls.head() == last) _unique.trampoline(ls.tail(), result, last)
            else _unique.trampoline(ls.tail(), result << ls.head(), ls.head())
        }.trampoline()

        _unique(list, [], null)
    }

    static pack(List list) {
        def _pack
        _pack = { List ls, List result, last ->
            if (ls.empty) result
            else if (ls.head() != last) _pack.trampoline(ls.tail(), result << [ls.head()], ls.head())
            else {
                result.last() << ls.head()
                _pack.trampoline(ls.tail(), result, last)
            }
        }.trampoline()

        _pack(list, [], null)
    }

    static encode(List list) {
        pack(list).collect { [size(it), it.head()] }
    }

    static encodeModified(List list) {
        encode(list) collect { it[0] > 1 ? [it[0], it[1]] : it[1] }
    }

    static decode(List list) {
        if (!list) list
        else flatten(list.collect { it instanceof List ? [it[1]] * it[0] : it })
    }

    static encodeDirect(List list) {
        def _encode
        _encode = { List ls, List result, last ->
            if (ls.empty) result
            else if (ls.head() != last) _encode.trampoline(ls.tail(), result << [1, ls.head()], ls.head())
            else {
                result.last()[0] += 1
                _encode.trampoline(ls.tail(), result, last)
            }
        }.trampoline()

        _encode(list, [], null)
    }

    static duplicate(List list) {
        duplicateN(list, 2)
    }

    static duplicateN(List list, int n) {
        list.collect { [it] * n } flatten()
    }

    static dropNth(List list, int n) {
        def result = []
        list.eachWithIndex { entry, i -> if ((i + 1) % n != 0) result << entry }
        result
    }

    static split(List list, int n) {
        def result = [[], []]
        list.eachWithIndex { entry, i -> (i < n ? result.first() : result.last()) << entry }
        result
    }

    static slice(List self, int from, int to) {
        Range range = from..<to
        def result = []
        self.eachWithIndex { def entry, int i ->
            if (range.containsWithinBounds(i)) result << entry
        }
        result
    }

    static rotate(List self, int n) {
        def i = n > 0 ? n : self.size() + n
        def split = self.splitN(i)
        split.last() + split.first()
    }

    static removeAt(List self, int i) {
        if (!self) []
        else {
            def (head, tail) = self.splitN(i)
            if (!tail) head
            else [head + tail.tail(), tail.head()]
        }
    }

    static insertAt(List self, def o, int i) {
        def split = self.splitN(i)
        split.first() + [o] + split.last()
    }

    static range(final int from, final int to) {
        final int step = to > from ? 1 : -1

        def _range
        _range = { List result, int current, int n ->
            if (n == 0) result
            else _range.trampoline(result << current, (current + 1 * step), n - 1)
        }.trampoline()

        if (from == to) [from]
        else _range([], from, abs(to - from) + 1)
    }

    static randomSelect(List self, int n) {
        if (!self) throw new IllegalArgumentException("empty list or not enough items for selection");

        def size = self.size()
        if (size < n) throw new IllegalArgumentException("empty list or not enough items for selection");

        def random = new Random()
        def list = self

        (1..n).collect {
            if (size == it) list.head()
            else {
                def ls = removeAt(list, random.nextInt(size - it))
                list = ls.first()
                ls.last()
            }
        }
    }

    static lotto(int n, int m) {
        randomSelect(range(1, m), n)
    }

    static randomPermute(List self) {
        randomSelect(self, self.size())
    }

    static combinations(List self, int n) {
        if (!self) return []

        def size = self.size()
        if (size == 0 || size < n || n == 0) return []

        def _combinations
        _combinations = { List ls, int k ->
            if (!ls || k == 0) return [[]]
            if (ls.size() == k) return [ls]

            _combinations(ls.tail(), k - 1).collect { [ls.head()] + it } + _combinations(ls.tail(), k)
        }

        _combinations(self, n)
    }

    static group3(List list, List grouping = [2, 3, 4]) {
        if (grouping.size() != 3 || list.size() != grouping.sum()) return []

        combinations(list, grouping[0])
                .collect { [it, list - it] }
                .collectMany { l ->
                   combinations(l.last(), grouping[1]).collect { l.init() + [it, l.last() - it] }
                }

    }

    static lsort(List<List> lists) {
        if (!lists || lists.size() <= 1) return lists

        lists.collect { [it.size(), it] }.sort { it[0] }.collect { it[1] }
    }

    static lsortFreq(List<List> lists) {
        if (!lists || lists.size() <= 1) return lists

        lists.groupBy { it.size() }.sort { it.value.size() }.collectMany { it.value }
    }

}
