import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Yvonne on 2016-03-14.
 */
public class PieDay {
    //people, pie, min
    private static Integer[][][] cache = new Integer[250][250][250];

    /*
     * an example might help: pi(9,4,1)
     *   pies = 9, people = 4, min = 1
     *   for this the answer is calculated as follows:
     *       the first person could take 1 piece leaving us the task of finding the ways
     *                with now 8 pieces (9-1), 3 people and a min of 1 (= pi(8,3,1))
     *       the first person could take 2 pieces leaving us the task of finding the ways
     *                with now 7 pieces (9-2), 3 people and a min of 2 (= pi(7,3,2))
     *       the first person could NOT take 3 pieces (or more), because that would leave 6
     *                pieces for 3 people and since the first person took 3, they
     *                would have to take at least 3 themselves and that's 9 not 6 pieces!
     *                in general, the most the first person person can take is
     *                           pies/people (eg 9/4 = 2)
     *
     */
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);

        System.out.println("Pieces of pies");
        int pie = in.nextInt();
        System.out.println("No. of people");
        int people = in.nextInt();

        System.out.println("Solution total: " + pi(people, pie, 1));

    }

    private static int pi(int people, int pie, int min) {
        if (people > pie ) {
            return 0;
        } else if (people == pie || people == 1) {
            return 1;
        } else {
            //look in the cache first
            if (cache[people][pie][min] != null) {
                return cache[people][pie][min];
            } else {
                int solutions = 0;
                for (int i = min; i < (pie / people)+1; i++) {
                    //one less people and i less pies
                    solutions += pi(people - 1, pie - i, i);
                }
                cache[people][pie][min] = solutions;
                return solutions;
            }
        }

    }
}
