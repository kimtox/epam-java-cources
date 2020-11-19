package com.epam.university.java.core.task039;

public class Node implements Comparable<Node> {
    Character value;
    int weight;
    Node left;
    Node right;

    /**
     * Node constructor.
     *
     * @param value  is the value.
     * @param weight is the weight.
     * @param left   is the left child.
     * @param right  is the right child.
     */
    public Node(Character value, int weight, Node left, Node right) {
        this.value = value;
        this.weight = weight;
        this.left = left;
        this.right = right;
    }

    /**
     * Node constructor.
     *
     * @param value  is the value.
     * @param weight is the weight.
     */

    public Node(Character value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    @Override
    public int compareTo(Node o) {
        return o.weight - weight;
    }

    /**
     * Method for getting the unique code for the Node with non-empty value.
     * @param c is the current Character.
     * @param path is the path to the sought Character.
     * @return path.
     */
    public String getNodeCode(Character c, String path) {
        if (c == this.value) {
            return path;
        } else {
            if (this.left != null) {
                String currentPath = left.getNodeCode(c, path + 0);
                if (currentPath != null) {
                    return currentPath;
                }
            }

            if (this.right != null) {
                String currentPath = right.getNodeCode(c, path + 1);
                if (currentPath != null) {
                    return currentPath;
                }
            }
        }
        return null;
    }
}
