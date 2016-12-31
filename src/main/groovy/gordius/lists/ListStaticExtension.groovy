package gordius.lists

/**
 * Created by Arthur on 28/11/14.
 */
public class ListStaticExtension {

    static def lotto(List type, int n, int m) {
        def result = []
        def random = new Random()
        (1..n).each {
            result << random.nextInt(m)
        }
        result
    }
}
