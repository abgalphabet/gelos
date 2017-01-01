package gordius.lists
/**
 * Created by Arthur on 28/11/14.
 */
class ListExtension {
    static def lastN(List self, int n = 0) {
        WorkingWithLists.lastN(self, n)
    }

    static def nth(List self, int n) {
        WorkingWithLists.nth(self, n)
    }

    static def isPalindrome(List self) {
        boolean isPalindrome = true
        for (int i = 0; i < self.size() / 2; i++) {
            isPalindrome = isPalindrome && self[i] == self[self.size() - 1 - i]
            if (!isPalindrome) break;
        }
        isPalindrome
    }

    static def pack(List self) {
        def packed = []
        for (int i = 0; i < self.size(); i++) {
            def elem = self[i]
            if (i > 0 && elem == packed.last().last()) {
                packed.last() << elem
            } else {
                packed << [elem]
            }
        }
        packed
    }

    static def encode(List self) {
        self.pack() collect { [it.size(), it.head()] }
    }

    static def encodeModified(List self) {
        self.encode() collect { it[0] > 1 ? [it[0], it[1]] : it[1] }
    }

    static def decode(List self) {
        self.collect {
            it instanceof List && it.size() == 2 ? [it[1]] * it[0] : it
        } flatten()
    }

    static def encodeDirect(List self) {
        if (self) {
            def encoded = [[1, self[0]]]
            for (int i = 1; i < self.size(); i++) {
                self[i] == encoded.last()[1] ? encoded.last()[0]++ : encoded << [1, self[i]]
            }
            encoded
        } else {
            []
        }
    }

    static def duplicate(List self) {
        self.collect { [it, it] } flatten()
    }

    static def duplicateN(List self, int n) {
        self.collect { [it] * n } flatten()
    }

    static def dropEx(List self, int n) {
        def list = []
        self.eachWithIndex { entry, i -> (i + 1) % n != 0 ? list << entry : null }
        list
    }

    static def splitEx(List self, int n) {
        def list = [[], []]
        self.eachWithIndex { entry, i -> (i < n ? list.first() : list.last()) << entry }
        list
    }

    static def slice(List self, int from, int to) {
        if (!self || self.size() < from) {
            return []
        }
        self.subList(from, self.size() < to ? self.size() : to)
    }

    static def rotate(List self, int n) {
        def i = n > 0 ? n : self.size() + n
        def split = self.splitEx(i)
        [split.last(), split.first()]
    }

    static def removeAt(List self, int i) {
        if (!self) []
        else if (i >= self.size()) self
        else {
            def (head, tail) = self.splitEx(i)
            [head + tail.tail(), tail.head()]
        }
    }

    static def insertAt(List self, def o, int i) {
        def split = self.splitEx(i)
        split.first() + [o] + split.last()
    }

    static def uniqueEx(List self) {
        def unique = [] as Set
        self.each { unique << it }
        unique as List
    }

    static def randomSelect(List self, int n) {
        if (!self || self.size() < n) throw new IllegalArgumentException("empty list or not enough items for selection");


        def random = new Random()
        def select = [], list = self
        (1..n).each {
            def temp = list.removeAt(random.nextInt(list.size()))
            list = temp.first()
            select << temp.last()
        }
        select
    }

    static def randomPermute(List self) {
        self.randomSelect(self.size())
    }

    static def combinations(List self, int n) {
        if (!self || n == 0) return []

        def sublistMapR = { List ls -> (0..ls.size() - 1).collect { i -> ls.removeAt(i) } }

        def combinationsR
        combinationsR = { List running, List list, int m ->
            if (m <= 0) return running

            def result = []
            sublistMapR(list).each { ls, o ->
                def nextRun = running.collect { it + o }
                result += combinationsR(nextRun, ls, m - 1)
            }
            result
        }

        def combinations = combinationsR([[]], self, n)
        combinations.collect { it as Set }.unique()
    }

    static def combinations2(List self, int n) {
        if (!self || n == 0) return []

        def combinationsR
        combinationsR = { List running, List list, int m ->
            if (list.size() == m) return [list]
            if (m == 0 || list.size() < m) return running

            def result = []
            list.each { def o ->
                def nextRun = running.collect { it + o }
                result += combinationsR(nextRun, list.tail(), m - 1)
            }
            result
        }


        def combinations = combinationsR([[]], self, n)
        combinations.collect { it as Set }.findAll { it.size() == n }.unique()
    }

    static def group(List list, int n) {
        def groupR
        groupR = { Set running, List ls, int k ->
            if (!ls) return []
            if (k == 1) return running.each { it << ls.toSet() }

            def groups = [] as Set
            (1..ls.size()).each { m ->
                ls.combinations(m).each { g ->
                    def nextRun = running.collect { [*(it.toList()), g] as Set } as Set
                    groups += groupR(nextRun, ls - g, k - 1)
                }
            }
            groups
        }

        def groups = groupR([new HashSet()] as Set, list, n)
        groups.collect { it.collect { it as List } as List }
    }

    static def groupN(List list, List grouping) {
        def groupR
        groupR = { Set running, List ls, List groupingR ->
            if (!ls || ls.size() < groupingR.sum()) return []
            if (groupingR.size() == 1) return running.each { it << ls.toSet() }

            def groups = [] as Set
            ls.combinations(groupingR.head()).each { g ->
                def nextRun = running.collect { [*(it.toList()), g] as Set } as Set
                groups += groupR(nextRun, ls - g, groupingR.tail())
            }
            groups
        }

        def groups = groupR([new HashSet()] as Set, list, grouping)
        groups.collect { it.collect { it as List } as List }
    }

    static def lsort(List lists) {
        if (!lists || lists.size() <= 1) return lists

        lists.collect { [it.size(), it] }.sort { it[0] }.collect { it[1] }
    }

    static def lsortFreq(List lists) {
        if (!lists || lists.size() <= 1) return lists

        def result = []
        lists.groupBy { it.size() }.sort { it.value.size() }.each { result += it.value }
        result
    }

}
