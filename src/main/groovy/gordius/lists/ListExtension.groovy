package gordius.lists
/**
 * Created by Arthur on 28/11/14.
 */
class ListExtension {
    static lastN(List self, int n = 0) {
        WorkingWithLists.lastN(self, n)
    }

    static nth(List self, int n) {
        WorkingWithLists.nth(self, n)
    }

    static isPalindrome(List self) {
        WorkingWithLists.isPalindrome(self)
    }

    static compress(List self) {
        WorkingWithLists.compress(self)
    }

    static pack(List self) {
        WorkingWithLists.pack(self)
    }

    static encode(List self) {
        WorkingWithLists.encode(self)
    }

    static encodeModified(List self) {
        WorkingWithLists.encodeModified(self)
    }

    static decode(List self) {
        WorkingWithLists.decode(self)
    }

    static encodeDirect(List self) {
        WorkingWithLists.encodeDirect(self)
    }

    static duplicate(List self) {
        WorkingWithLists.duplicate(self)
    }

    static duplicateN(List self, int n) {
        WorkingWithLists.duplicateN(self, n)
    }

    static splitN(List self, int n) {
        WorkingWithLists.split(self, n)
    }

    static slice(List self, int from, int to) {
        WorkingWithLists.slice(self, from, to)
    }

    static rotate(List self, int n) {
        WorkingWithLists.rotate(self, n)
    }

    static insertAt(List self, def o, int i) {
        WorkingWithLists.insertAt(self, o, i)
    }

    static randomSelect(List self, int n) {
        WorkingWithLists.randomSelect(self, n)
    }

    static randomPermute(List self) {
        WorkingWithLists.randomPermute(self)
    }


    static group3(List self, List grouping = [2,3,4]) {
        WorkingWithLists.group3(self, grouping)
    }

    static lsort(List lists) {
        WorkingWithLists.lsort(lists)
    }

    static lsortFreq(List lists) {
        WorkingWithLists.lsortFreq(lists)
    }

}
