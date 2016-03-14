import java.util.Scanner;

/**
 * Created by Yvonne on 2016-03-14.
 */
public class NutrientsTree {
    private static final char TREE_START_TOKEN = '(';
    private static final char TREE_END_TOKEN = ')';

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        TreeNode root = createTreeNode(in.nextLine()); //convert String to tree
        int growth = in.nextInt(); //growth agent

        // find the maximum amount of transportable nutrients using
        // i = 0 .. growth units of growth agent on root (essentially
        // solving the problem)
        optimize(root, growth);

        System.out.println(root.maxNutrients[growth]);
    }

    /**
     * form a tree
     * @param s
     * @return
     */
    public static TreeNode createTreeNode(String s) {
        s = s.trim();
        // leaf node
        if (!s.startsWith(String.valueOf(TREE_START_TOKEN)))
            //is a leaf
            return new TreeNode(Integer.parseInt(s));
        else {
            //sub tree node
            //remove the brackets, where we are left with the two children
            s = s.substring(1, s.length() - 1).trim();

            // split into two parts
            // the first part is either a number or
            // a node that ends with a ")"
            // i is the index of the space where it defines where the two children are separated
            int i;
            //if the left child is a tree node
            if (s.startsWith(String.valueOf(TREE_START_TOKEN))) {
                // find corresponding ")" by counting
                int count = 1;
                i = 1;
                while (count > 0) {
                    //another subtree
                    if (s.charAt(i) == TREE_START_TOKEN)
                        count++;
                    else if (s.charAt(i) == TREE_END_TOKEN)
                        count--;
                    i++;
                }
            } else
                //left child is a leaf, find the first
                i = s.indexOf(" ");

            // create the left and right subtrees
            return new TreeNode(createTreeNode(s.substring(0, i)), createTreeNode(s.substring(i + 1)));
        }
    }


    /**
     *
     * The main method is a recursive method which calculates and stores
     * the max nutrients a node can return, given a growth agent amount.
     * The method stores the max nutrients for ALL growth
     * agents from 0 to the given amount.
     *
     * For both sides of the node, we calculate and save the max for each growth agent amount.
     *
     * @param node
     * @param growth
     */
    public static void optimize(TreeNode node, int growth) {
        // trivial solution for leaf nodes
        if (node.left == null) {
            TreeNode leaf = node;
            leaf.maxNutrients = new int[growth + 1];
            for (int i = 0; i <= growth; i++)
                leaf.maxNutrients[i] = leaf.value + i;
        } else {
            int max, tmp;

            // calculate the maxNutrients of the left node
            optimize(node.left, growth);

            //actual calculation part
            //calculate the weighted nutrients the left node can carry given the growth value given the growth agent value
            int[] optL = new int[growth + 1];
            for (int i = 0; i <= growth; i++) {
                max = 0;
                //the growth agent value granted to the children can not be larger than the parent.
                for (int j = 0; j <= i; j++) {
                    tmp = Math.min((1 + j) * (1 + j), node.left.maxNutrients[i - j]);
                    if (tmp > max)
                        max = tmp;
                }
                optL[i] = max;
            }

            //calculate the maxNutrients of the right node
            optimize(node.right, growth);

            // optimize right subtree with consideration for right branch/edge thickness
            int[] optR = new int[growth + 1];
            for (int i = 0; i <= growth; i++) {
                max = 0;
                for (int j = 0; j <= i; j++) {
                    tmp = Math.min((1 + j) * (1 + j), node.right.maxNutrients[i - j]);
                    if (tmp > max)
                        max = tmp;
                }
                optR[i] = max;
            }

            // for the "root" node of this recursion, find the optimized solution for each growth agents value.
            node.maxNutrients = new int[growth + 1];
            //find the l
            for (int i = 0; i <= growth; i++) {
                max = 0;
                for (int j = 0; j <= i; j++) {
                    tmp = optL[j] + optR[i - j];
                    if (tmp > max)
                        max = tmp;
                }
                node.maxNutrients[i] = max;
            }
        }
    }
}


class TreeNode {
    //an array that iterates through the growth agents value, each item is the each max nutrients that can be transported for that particular growth agent.
    public int[] maxNutrients;
    public int value;
    public TreeNode left, right;

    // for a non-leaf node
    public TreeNode(TreeNode l, TreeNode r) {
        value = 0;
        left = l;
        right = r;
    }

    // for a leaf
    public TreeNode(int v) {
        value = v;
        left = null;
        right = null;
    }
}
