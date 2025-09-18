package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.StdRandom;


public class TestOnLargeAmount {
    @Test
    public void randmizedTest() {
        SimpleDeque<Integer> correct = new SimpleDeque<>();
        LinkedListDeque<Integer> underTest = new LinkedListDeque<>();

        int N = 1000;
        for (int i = 0; i < N; i++) {
            int operation = StdRandom.uniform(0, 8);
            switch (operation) {
                case 0: {
                    int randVal = StdRandom.uniform(0, 100);
                    correct.addLast(randVal);
                    underTest.addLast(randVal);
//                    System.out.println("addLast(" + randVal + ")");
                    break;
                }
                case 1: {
                    int randVal = StdRandom.uniform(0, 100);
                    correct.addFirst(randVal);
                    underTest.addFirst(randVal);
//                    System.out.println("addLast(" + randVal + ")");
                    break;
                }
                case 2: {
                    boolean res = correct.isEmpty();
//                    if (res) {
//                        System.out.println("Empty");
//                    } else {
//                        System.out.println("Not empty");
//                    }
                    assertEquals(res, underTest.isEmpty());
                    break;
                }
                case 3: {
                    int size = correct.size();
//                    System.out.println("size=" + size);
                    assertEquals(size, underTest.size());
                    break;
                }
                case 4: {
                    Integer first = correct.removeFirst();
//                    System.out.println("remove first: " + first);
                    assertEquals(first, underTest.removeFirst());
                    break;
                }
                case 5: {
                    Integer last = correct.removeLast();
//                    System.out.println("remove last: " + last);
                    assertEquals(last, underTest.removeLast());
                    break;
                }
                case 6: {
                    int randIdx = StdRandom.uniform(0, correct.size() + 1);
                    Integer item = correct.get(randIdx);
//                    System.out.println("get position " + randIdx + ": " + item);
                    Integer item2 = underTest.get(randIdx);
                    assertEquals(item, item2);
                    break;
                }
                default:
                    break;
            }
        }
    }
}
