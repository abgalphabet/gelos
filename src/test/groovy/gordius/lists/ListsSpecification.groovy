package gordius.lists

import spock.lang.Specification
import spock.lang.Unroll

import static gordius.lists.ListExtension.lastN
import static gordius.lists.WorkingWithLists.last
import static gordius.lists.WorkingWithLists.nth
import static gordius.lists.WorkingWithLists.reverse
import static gordius.lists.WorkingWithLists.size
import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Created by Arthur on 28/11/14.
 */
class ListsSpecification extends Specification {

    @Unroll
    "P01 (*) Find the last element of #list"(List list, int element) {
/*
        Example:
        scala> last(List(1, 1, 2, 3, 5, 8))
        res0: Int = 8
*/
        expect:
        last(list) == element
        list.last() == element
        list[-1] == element

        where:
        list               || element
        [1, 1, 2, 3, 5, 8] || 8
        [1, 1, 2, 3, 8, 5] || 5
        [2]                || 2
    }

    @Unroll
    "P01 (*) Find the last element of empty list would throw exception"(Class exception, Closure lastAndExpectException) {
/*
        Example:
        scala> last(List(1, 1, 2, 3, 5, 8))
        res0: Int = 8
*/
        when:
        lastAndExpectException()

        then:
        thrown(exception)

        where:
        exception                      | lastAndExpectException
        NoSuchElementException         | { last([]) }
        NoSuchElementException         | { [].last() }
        ArrayIndexOutOfBoundsException | { [][-1] }

    }

    @Unroll
    "P02 (*) Find the last but one element of #list"(List list, int element) {
/*
        Example:
        scala> penultimate(List(1, 1, 2, 3, 5, 8))
        res0: Int = 5
*/
        expect:
        lastN(list, 1) == element
        list.lastN(1) == element
        list[-2] == element

        where:
        list               || element
        [1, 1, 2, 3, 5, 8] || 5
        [1, 1, 2, 3, 5]    || 3
    }

    @Unroll
    "P02 (*) Find the last but #lastN element of #list would throw exception"(List list, int lastN) {
        /*
        Example:
        scala> penultimate(List(1, 1, 2, 3, 5, 8))
        res0: Int = 5
        */
        when:
        list.lastN(lastN)

        then:
        thrown(NoSuchElementException)

        where:
        list      | lastN
        []        | 0
        []        | 1
        [1]       | 1
        [1, 2, 3] | 4
    }

    @Unroll
    "P03 (*) Find the #kth element of #list"(List list, int k, int element) {
        /*
        By convention, the first element in the list is element 0.
        Example:

        scala> nth(2, List(1, 1, 2, 3, 5, 8))
        res0: Int = 2
        */
        expect:
        nth(list, k) == element
        list.asImmutable().nth(k) == element

        where:
        list               | k || element
        [1, 1, 2, 3, 5, 8] | 2 || 2
        [1, 1, 2, 3, 5, 8] | 0 || 1
        [1, 1, 2, 3, 5, 8] | 4 || 5

    }

    @Unroll
    "P04 (*) Find the number of elements of #list"(List list, int length) {
        /*
           Example:
           scala> length(List(1, 1, 2, 3, 5, 8))
           res0: Int = 6
        */

        expect:
        size(list) == length
        list.inject(0) { int sum, def item -> sum+1 } == length

        where:
        list               || length
        [1, 1, 2, 3, 5, 8] || 6
        [1, 1, 2, 3, 5]    || 5
        [1]                || 1
        []                 || 0
    }

    @Unroll
    "P05 (*) Reverse #list"(List list, List reversed) {
        /*
        Example:
        scala> reverse(List(1, 1, 2, 3, 5, 8))
        res0: List[Int] = List(8, 5, 3, 2, 1, 1)
        */

        expect:
        reverse(list) == reversed
        list.asImmutable().reverse() == reversed

        where:
        list               || reversed
        [1, 1, 2, 3, 5, 8] || [8, 5, 3, 2, 1, 1]
        [1, 1, 2, 3, 5]    || [5, 3, 2, 1, 1]
        [1]                || [1]
        []                 || []
    }

    @Unroll
    "P06 (*) Find out whether #list is a palindrome"(List list, boolean isPalindrome) {
        /*
        Example:
        scala> isPalindrome(List(1, 2, 3, 2, 1))
        res0: Boolean = true
        */

        expect:
        list.asImmutable().isPalindrome() == isPalindrome

        where:
        list               | isPalindrome
        [1, 2, 3, 3, 2, 1] | true
        [1, 2, 3, 2, 1]    | true
        [1, 1, 2, 3, 5]    | false
        [1]                | true
        []                 | true
    }

    @Unroll
    "P07 (**) Flatten a nested list structure of #list"(List list, List flattened) {
        /*
        Example:
        scala> flatten(List(List(1, 1), 2, List(3, List(5, 8))))
        res0: List[Any] = List(1, 1, 2, 3, 5, 8)
        */

        expect:
        list.asImmutable().flatten() == flattened

        where:
        list                      | flattened
        []                        | []
        [1, 1, 3, 5, 8]           | [1, 1, 3, 5, 8]
        [[1, 1], [3, [5, 8]]]     | [1, 1, 3, 5, 8]
        [[1, [2, [3, [4, [5]]]]]] | [1, 2, 3, 4, 5]
    }

    @Unroll
    "P08 (**) Eliminate consecutive duplicates of #list elements"(List list, List compressed) {
        /*
        P08 (**) Eliminate consecutive duplicates of list elements.
        If a list contains repeated elements they should be replaced with a single copy of the element. The order of the elements should not be changed.
        Example:

        scala> compress(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
        res0: List[Symbol] = List('a, 'b, 'c, 'a, 'd, 'e)
        */

        expect:
        list.asImmutable().uniqueEx() == compressed

        where:
        list                                                                   | compressed
        ['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e'] | ['a', 'b', 'c', 'd', 'e']
        ['a', 'b', 'c']                                                        | ['a', 'b', 'c']
        []                                                                     | []
    }

    @Unroll
    "P09 (**) Pack consecutive duplicates of #list elements into sublists"(List list, List packed) {
        /*
        P09 (**) Pack consecutive duplicates of list elements into sublists.
        If a list contains repeated elements they should be placed in separate sublists.
        Example:

        scala> pack(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
        res0: List[List[Symbol]] = List(List('a, 'a, 'a, 'a), List('b), List('c, 'c), List('a, 'a), List('d), List('e, 'e, 'e, 'e))
         */

        expect:
        list.asImmutable().pack() == packed

        where:
        list                                                                   | packed
        ['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e'] | [['a', 'a', 'a', 'a'], ['b'], ['c', 'c'], ['a', 'a'], ['d'], ['e', 'e', 'e', 'e']]
        ['a', 'b', 'c']                                                        | [['a'], ['b'], ['c']]
        []                                                                     | []
    }

    @Unroll
    "P10 (*) Run-length encoding of #list"(List list, List encoded) {
        /*
        P10 (*) Run-length encoding of a list.
        Use the result of problem P09 to implement the so-called run-length encoding data compression method. Consecutive duplicates of elements are encoded as tuples (N, E) where N is the number of duplicates of the element E.
        Example:

        scala> encode(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
        res0: List[(Int, Symbol)] = List((4,'a), (1,'b), (2,'c), (2,'a), (1,'d), (4,'e))
         */
        expect:
        list.asImmutable().encode() == encoded

        where:
        list                                                                   | encoded
        ['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e'] | [[4, 'a'], [1, 'b'], [2, 'c'], [2, 'a'], [1, 'd'], [4, 'e']]
        ['a', 'a']                                                             | [[2, 'a']]
        []                                                                     | []
    }

    @Unroll
    "P11 (*) Modified run-length encoding of #list"(List list, List encodeModified) {
        /*
        P11 (*) Modified run-length encoding.
        Modify the result of problem P10 in such a way that if an element has no duplicates it is simply copied into the result list.asImmutable(). Only elements with duplicates are transferred as (N, E) terms.
        Example:

        scala> encodeModified(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
        res0: List[Any] = List((4,'a), 'b, (2,'c), (2,'a), 'd, (4,'e))
         */

        expect:
        list.asImmutable().encodeModified() == encodeModified

        where:
        list                                                                   | encodeModified
        ['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e'] | [[4, 'a'], 'b', [2, 'c'], [2, 'a'], 'd', [4, 'e']]
        ['a', 'b', 'c']                                                        | ['a', 'b', 'c']
        []                                                                     | []

    }

    @Unroll
    "P12 (**) Decode a run-length encoded #list"(List list, List decoded) {
        /*
        P12 (**) Decode a run-length encoded list.asImmutable().
        Given a run-length code list generated as specified in problem P10, construct its uncompressed version.
        Example:

        scala> decode(List((4, 'a), (1, 'b), (2, 'c), (2, 'a), (1, 'd), (4, 'e)))
        res0: List[Symbol] = List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e)
         */

        expect:
        list.asImmutable().decode() == decoded

        where:
        list                                                         | decoded
        [[4, 'a'], [1, 'b'], [2, 'c'], [2, 'a'], [1, 'd'], [4, 'e']] | ['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e']
        [[4, 'a'], 'b', [2, 'c'], [2, 'a'], 'd', [4, 'e']]           | ['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e']
        []                                                           | []

    }

    @Unroll
    "P13 (**) Run-length encoding of #list (direct solution)"(List list, List encoded) {
        /*
        P13 (**) Run-length encoding of a list (direct solution).
        Implement the so-called run-length encoding data compression method directly. I.e. don't use other methods you've written (like P09's pack); do all the work directly.
        Example:

        scala> encodeDirect(List('a, 'a, 'a, 'a, 'b, 'c, 'c, 'a, 'a, 'd, 'e, 'e, 'e, 'e))
        res0: List[(Int, Symbol)] = List((4,'a), (1,'b), (2,'c), (2,'a), (1,'d), (4,'e))
         */

        expect:
        list.asImmutable().encodeDirect() == encoded

        where:
        list                                                                   | encoded
        ['a', 'a', 'a', 'a', 'b', 'c', 'c', 'a', 'a', 'd', 'e', 'e', 'e', 'e'] | [[4, 'a'], [1, 'b'], [2, 'c'], [2, 'a'], [1, 'd'], [4, 'e']]
        ['a', 'b', 'c']                                                        | [[1, 'a'], [1, 'b'], [1, 'c']]
        []                                                                     | []
    }

    @Unroll
    "P14 (*) Duplicate the elements of #list"(List list, List duplicate) {
        /*
        P14 (*) Duplicate the elements of a list.
        Example:
        scala> duplicate(List('a, 'b, 'c, 'c, 'd))
        res0: List[Symbol] = List('a, 'a, 'b, 'b, 'c, 'c, 'c, 'c, 'd, 'd)
         */

        expect:
        list.asImmutable().duplicate() == duplicate

        where:
        list                           | duplicate
        ['a', 'b', 'c', 'c', 'd', 'e'] | ['a', 'a', 'b', 'b', 'c', 'c', 'c', 'c', 'd', 'd', 'e', 'e']
        []                             | []
    }

    @Unroll
    "P15 (**) Duplicate the elements of #list a given number of times"(List list, int n, List duplicate) {
        /*
        P15 (**) Duplicate the elements of a list a given number of times.
        Example:
        scala> duplicateN(3, List('a, 'b, 'c, 'c, 'd))
        res0: List[Symbol] = List('a, 'a, 'a, 'b, 'b, 'b, 'c, 'c, 'c, 'c, 'c, 'c, 'd, 'd, 'd)
         */

        expect:
        list.asImmutable().duplicateN(n) == duplicate

        where:
        list                           | n | duplicate
        ['a', 'b', 'c', 'c', 'd', 'e'] | 1 | ['a', 'b', 'c', 'c', 'd', 'e']
        ['a', 'b', 'c', 'c', 'd', 'e'] | 2 | ['a', 'a', 'b', 'b', 'c', 'c', 'c', 'c', 'd', 'd', 'e', 'e']
        ['a', 'b', 'c', 'c', 'd', 'e'] | 3 | ['a', 'a', 'a', 'b', 'b', 'b', 'c', 'c', 'c', 'c', 'c', 'c', 'd', 'd', 'd', 'e', 'e', 'e']
    }

    @Unroll
    "P16 (**) Drop every Nth element from #list"(List list, List dropped) {
        /*
        P16 (**) Drop every Nth element from a list.
        Example:
        scala> drop(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        res0: List[Symbol] = List('a, 'b, 'd, 'e, 'g, 'h, 'j, 'k)
         */

        expect:
        list.asImmutable().dropEx(3) == dropped

        where:
        list                                                    | dropped
        ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'] | ['a', 'b', 'd', 'e', 'g', 'h', 'j', 'k']
        ['a', 'b', 'c']                                         | ['a', 'b']
        ['a', 'b']                                              | ['a', 'b']
        []                                                      | []
    }

    @Unroll
    "P17 (*) Split #list into two parts"(List list, List split) {
        /*
        P17 (*) Split a list into two parts.
        The length of the first part is given. Use a Tuple for your result.
        Example:

        scala> split(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        res0: (List[Symbol], List[Symbol]) = (List('a, 'b, 'c),List('d, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
         */
        expect:
        list.asImmutable().splitEx(3) == split

        where:
        list                                                    | split
        ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'] | [['a', 'b', 'c'], ['d', 'e', 'f', 'g', 'h', 'i', 'j', 'k']]
        ['a', 'b', 'c']                                         | [['a', 'b', 'c'], []]
        ['a', 'b']                                              | [['a', 'b'], []]
        []                                                      | [[], []]
    }

    @Unroll
    "P18 (**) Extract a slice from #list"(List list, List slice) {
        /*
        P18 (**) Extract a slice from a list.
        Given two indices, I and K, the slice is the list containing the elements from and including the Ith element up to but not including the Kth element of the original list.asImmutable(). Start counting the elements with 0.
        Example:

        scala> slice(3, 7, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        res0: List[Symbol] = List('d, 'e, 'f, 'g)
         */
        expect:
        list.asImmutable().slice(3, 7) == slice

        where:
        list                                                    | slice
        ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'] | ['d', 'e', 'f', 'g']
        ['a', 'b', 'c', 'd']                                    | ['d']
        ['a', 'b', 'c']                                         | []
        []                                                      | []
    }

    @Unroll
    "P19 (**) Rotate #list #n places to the left"(List list, int n, List rotated) {
        /*
        P19 (**) Rotate a list N places to the left.
        Examples:
        scala> rotate(3, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        res0: List[Symbol] = List('d, 'e, 'f, 'g, 'h, 'i, 'j, 'k, 'a, 'b, 'c)

        scala> rotate(-2, List('a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i, 'j, 'k))
        res1: List[Symbol] = List('j, 'k, 'a, 'b, 'c, 'd, 'e, 'f, 'g, 'h, 'i)
         */
        expect:
        list.asImmutable().rotate(3)

        where:
        list                                                    | n  | rotated
        ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'] | 3  | ['d', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'a', 'b', 'c']
        ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'] | 0  | ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k']
        ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k'] | -2 | ['j', 'k', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i']
        ['a']                                                   | 1  | ['a']
        ['a']                                                   | 3  | ['a']
        []                                                      | 0  | []
    }

    @Unroll
    "P20 (*) Remove the #ith element from #list"(List list, int i, List removed) {
        /*
        P20 (*) Remove the Kth element from a list.
        Return the list and the removed element in a Tuple. Elements are numbered from 0.
        Example:

        scala> removeAt(1, List('a, 'b, 'c, 'd))
        res0: (List[Symbol], Symbol) = (List('a, 'c, 'd),'b)
         */
        expect:
        list.asImmutable().removeAt(i) == removed

        where:
        list                 | i | removed
        ['a', 'b', 'c', 'd'] | 1 | [['a', 'c', 'd'], 'b']
        ['a', 'b', 'c', 'd'] | 0 | [['b', 'c', 'd'], 'a']
        ['a', 'b', 'c', 'd'] | 5 | ['a', 'b', 'c', 'd']
        []                   | 0 | []

    }

    @Unroll
    "P21 (*) Insert an element at a #i position into #list"(List list, int i, List inserted) {
        /*
        P21 (*) Insert an element at a given position into a list.
        Example:
        scala> insertAt('new, 1, List('a, 'b, 'c, 'd))
        res0: List[Symbol] = List('a, 'new, 'b, 'c, 'd)
         */
        expect:
        list.asImmutable().insertAt('n', i) == inserted

        where:
        list                 | i | inserted
        ['a', 'b', 'c', 'd'] | 1 | ['a', 'n', 'b', 'c', 'd']
        ['a', 'b', 'c', 'd'] | 0 | ['n', 'a', 'b', 'c', 'd']
        ['a', 'b', 'c', 'd'] | 4 | ['a', 'b', 'c', 'd', 'n']
        ['a', 'b', 'c', 'd'] | 5 | ['a', 'b', 'c', 'd', 'n'] //TODO should throw exception
        []                   | 0 | ['n']

    }

    @Unroll
    "P22 (*) Create a list containing all integers within a given #range"(Range range, List expanded) {
        /*
        Example:
        scala> range(4, 9)
        res0: List[Int] = List(4, 5, 6, 7, 8, 9)
         */
        expect:
        range == expanded

        where:
        range | expanded
        4..9  | [4, 5, 6, 7, 8, 9]
        9..4  | [9, 8, 7, 6, 5, 4]
        0..0  | [0]
        0..-2 | [0, -1, -2]
    }

    @Unroll
    "P23 (**) Extract a #n number of randomly selected elements from #list"(List list, int n) {
        /*
        P23 (**) Extract a given number of randomly selected elements from a list.
        Example:
        scala> randomSelect(3, List('a, 'b, 'c, 'd, 'f, 'g, 'h))
        res0: List[Symbol] = List('e, 'd, 'a)
        Hint: Use the solution to problem P20
         */
        when:
        def select = list.asImmutable().randomSelect(n)

        then:
        expect select, hasSize(n)
        expect list, hasItems(*select)

        where:
        list                                     | n
        ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'] | 3
        ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'] | 8

    }

    @Unroll
    "P23 (**) Extract a #n number of randomly selected elements from #list would throw exception with improper size"(List list, int n) {
        /*
        P23 (**) Extract a given number of randomly selected elements from a list.
        Example:
        scala> randomSelect(3, List('a, 'b, 'c, 'd, 'f, 'g, 'h))
        res0: List[Symbol] = List('e, 'd, 'a)
        Hint: Use the solution to problem P20
         */
        when:
        list.asImmutable().randomSelect(n)

        then:
        thrown(IllegalArgumentException)

        where:
        list  | n
        ['a'] | 3
        []    | 0

    }

    @Unroll
    "P24 (*) Lotto: Draw N different random numbers from the set 1..#m"(int n, int m) {
        /*
        P24 (*) Lotto: Draw N different random numbers from the set 1..M.
        Example:
        scala> lotto(6, 49)
        res0: List[Int] = List(23, 1, 17, 33, 21, 37)
         */
        when:
        def result = List.lotto(n, m)

        then:
        expect result, hasSize(n)
        expect result, hasItems(lessThan(m))

        where:
        n | m
        3 | 49
        8 | 30
    }

    @Unroll
    "P25 (*) Generate a random permutation of the elements of #list"(List list) {
        /*
        P25 (*) Generate a random permutation of the elements of a list.
        Hint: Use the solution of problem P23.
        Example:

        scala> randomPermute(List('a, 'b, 'c, 'd, 'e, 'f))
        res0: List[Symbol] = List('b, 'a, 'd, 'c, 'e, 'f)
         */
        when:
        def result = list.asImmutable().randomPermute()

        then:
        expect result, containsInAnyOrder(*list)

        where:
        list << [['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h']]

    }

    @Unroll
    "P26 (**) Generate the combinations of #n distinct objects chosen from the N elements of #list"(List list, int n, int expectedNoOfCombinations) {
        /*
        P26 (**) Generate the combinations of K distinct objects chosen from the N elements of a list.
        In how many ways can a committee of 3 be chosen from a group of 12 people? We all know that there are C(12,3) = 220 possibilities (C(N,K) denotes the well-known binomial coefficient). For pure mathematicians, this result may be great. But we want to really generate all the possibilities.
        Example:

        scala> combinations(3, List('a, 'b, 'c, 'd, 'e, 'f))
        res0: List[List[Symbol]] = List(List('a, 'b, 'c), List('a, 'b, 'd), List('a, 'b, 'e), ...
                 */

        expect:
        that list.asImmutable().combinations(n), hasSize(expectedNoOfCombinations)
        that list.asImmutable().combinations2(n), hasSize(expectedNoOfCombinations)

        where:
        list                           | n | expectedNoOfCombinations
        ['a', 'b', 'c', 'd', 'e', 'f'] | 3 | 20
        ['a', 'b', 'c', 'd', 'e', 'f'] | 4 | 15
        ['a', 'b', 'c', 'd', 'e', 'f'] | 5 | 6
        ['a', 'b', 'c', 'd', 'e', 'f'] | 6 | 1
        ['a', 'b', 'c', 'd', 'e', 'f'] | 0 | 0
        []                             | 6 | 0
        []                             | 0 | 0

    }

    @Unroll
    "P27 (**) Group the elements of #list into disjoint subsets of #n"(List list, int n, List grouping, int expectedNoOfGroups, int expectedNoOfGroupNs) {
        /*
        P27 (**) Group the elements of a set into disjoint subsets.
        a) In how many ways can a group of 9 people work in 3 disjoint subgroups of 2, 3 and 4 persons? Write a function that generates all the possibilities.
        Example:

        scala> group3(List("Aldo", "Beat", "Carla", "David", "Evi", "Flip", "Gary", "Hugo", "Ida"))
        res0: List[List[List[String]]] = List(List(List(Aldo, Beat), List(Carla, David, Evi), List(Flip, Gary, Hugo, Ida)), ...
        b) Generalize the above predicate in a way that we can specify a list of group sizes and the predicate will return a list of groups.

        Example:

        scala> group(List(2, 2, 5), List("Aldo", "Beat", "Carla", "David", "Evi", "Flip", "Gary", "Hugo", "Ida"))
        res0: List[List[List[String]]] = List(List(List(Aldo, Beat), List(Carla, David), List(Evi, Flip, Gary, Hugo, Ida)), ...
        Note that we do not want permutations of the group members; i.e. ((Aldo, Beat), ...) is the same solution as ((Beat, Aldo), ...). However, we make a difference between ((Aldo, Beat), (Carla, David), ...) and ((Carla, David), (Aldo, Beat), ...).

        You may find more about this combinatorial problem in a good book on discrete mathematics under the term "multinomial coefficients".
         */
        expect:
        that list.asImmutable().group(n), hasSize(expectedNoOfGroups)
        that list.asImmutable().groupN(grouping), hasSize(expectedNoOfGroupNs)

        where:
        list                           | n | grouping  | expectedNoOfGroups | expectedNoOfGroupNs
        []                             | 0 | [2, 2, 2] | 0                  | 0
        ['a', 'b', 'c']                | 0 | [2, 2, 2] | 0                  | 0
        ['a', 'b', 'c']                | 5 | [2, 2, 2] | 0                  | 0
        ['a', 'b', 'c']                | 1 | [2, 2, 2] | 1                  | 0
        ['a', 'b', 'c']                | 3 | [2, 2, 2] | 1                  | 0
        ['a', 'b', 'c', 'd']           | 3 | [1, 3]    | 6                  | 4
        ['a', 'b', 'c', 'd']           | 3 | [3, 1]    | 6                  | 4
        ['a', 'b', 'c', 'd']           | 3 | [2, 2]    | 6                  | 3
        ['a', 'b', 'c', 'd', 'e', 'f'] | 3 | [2, 2, 2] | 90                 | 15

    }

    @Unroll
    "P28 (**) Sorting a list of #lists according to length of sublists"(List lists, List expected, List expected2) {
        /*
        P28 (**) Sorting a list of lists according to length of sublists.
        a) We suppose that a list contains elements that are lists themselves. The objective is to sort the elements of the list according to their length. E.g. short lists first, longer lists later, or vice versa.
        Example:

        scala> lsort(List(List('a, 'b, 'c), List('d, 'e), List('f, 'g, 'h), List('d, 'e), List('i, 'j, 'k, 'l), List('m, 'n), List('o)))
        res0: List[List[Symbol]] = List(List('o), List('d, 'e), List('d, 'e), List('m, 'n), List('a, 'b, 'c), List('f, 'g, 'h), List('i, 'j, 'k, 'l))
        b) Again, we suppose that a list contains elements that are lists themselves. But this time the objective is to sort the elements according to their length frequency; i.e. in the default, sorting is done ascendingly, lists with rare lengths are placed, others with a more frequent length come later.

        Example:

        scala> lsortFreq(List(List('a, 'b, 'c), List('d, 'e), List('f, 'g, 'h), List('d, 'e), List('i, 'j, 'k, 'l), List('m, 'n), List('o)))
        res1: List[List[Symbol]] = List(List('i, 'j, 'k, 'l), List('o), List('a, 'b, 'c), List('f, 'g, 'h), List('d, 'e), List('d, 'e), List('m, 'n))
        Note that in the above example, the first two lists in the result have length 4 and 1 and both lengths appear just once. The third and fourth lists have length 3 and there are two list of this length. Finally, the last three lists have length 2. This is the most frequent length.
         */
        expect:
        lists.asImmutable().lsort() == expected
        lists.asImmutable().lsortFreq() == expected2

        where:
        lists                                                             | expected                                                          | expected2
        []                                                                | []                                                                | []
        [[]]                                                              | [[]]                                                              | [[]]
        [['a', 'b', 'c']]                                                 | [['a', 'b', 'c']]                                                 | [['a', 'b', 'c']]
        [['a'], ['c'], ['b']]                                             | [['a'], ['c'], ['b']]                                             | [['a'], ['c'], ['b']]
        [['b', 'c'], ['c'], ['f', 'g', 'h'], ['i', 'j', 'k'], ['d', 'e']] | [['c'], ['b', 'c'], ['d', 'e'], ['f', 'g', 'h'], ['i', 'j', 'k']] | [['c'], ['b', 'c'], ['d', 'e'], ['f', 'g', 'h'], ['i', 'j', 'k']]
        [['f', 'g', 'h'], ['b', 'c'], ['c'], ['i', 'j', 'k'], ['d', 'e']] | [['c'], ['b', 'c'], ['d', 'e'], ['f', 'g', 'h'], ['i', 'j', 'k']] | [['c'], ['f', 'g', 'h'], ['i', 'j', 'k'], ['b', 'c'], ['d', 'e']]

    }
}