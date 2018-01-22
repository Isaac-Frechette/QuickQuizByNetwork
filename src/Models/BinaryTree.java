package Models;

public class BinaryTree {

    public Node root;

    public void addNode(int key, String name) {

		// Create a new Node and initialize it
        Node newNode = new Node(key, name);

		// If there is no root this becomes root
        if (root == null) {

            root = newNode;

        } else {

			// Set root as the Node we will start
            // with as we traverse the tree
            Node focusNode = root;

			// Future parent for our new Node
            Node parent;

            while (true) {

				// root is the top parent so we start
                // there
                parent = focusNode;

				// Check if the new node should go on
                // the left side of the parent node
                if (key < focusNode.questionNo) {

					// Switch focus to the left child
                    focusNode = focusNode.leftChild;

					// If the left child has no children
                    if (focusNode == null) {

						// then place the new node on the left of it
                        parent.leftChild = newNode;
                        return; // All Done

                    }

                } else { // If we get here put the node on the right

                    focusNode = focusNode.rightChild;

					// If the right child has no children
                    if (focusNode == null) {

						// then place the new node on the right of it
                        parent.rightChild = newNode;
                        return; // All Done

                    }

                }

            }
        }

    }

	// All nodes are visited in ascending order
    // Recursion is used to go to one node and
    // then go to its child nodes and so forth
    public String inOrderTraverseTree(Node focusNode) {
        String s = "";
        if (focusNode != null) {

			// Traverse the left node
            s += inOrderTraverseTree(focusNode.leftChild);

			// Visit the currently focused on node
            s += (focusNode.getQuestionNo() + "-");
            s += (focusNode.getTopic() + "-");
            s += (Integer.toString(focusNode.getCorrectCount()) + "Students, ");

			// Traverse the right node
            s += inOrderTraverseTree(focusNode.rightChild);

        }
        return s;
    }

    public String preorderTraverseTree(Node focusNode) {
        String s = "";
        if (focusNode != null) {

            s += (focusNode.getQuestionNo() + "-");
            s += (focusNode.getTopic() + "-");
            s += (Integer.toString(focusNode.getCorrectCount()) + "Students, ");

            s += preorderTraverseTree(focusNode.leftChild);
            s += preorderTraverseTree(focusNode.rightChild);

        }
        return s;
    }

    public String postOrderTraverseTree(Node focusNode) {
        String s = "";
        if (focusNode != null) {

            s += postOrderTraverseTree(focusNode.leftChild);
            s += postOrderTraverseTree(focusNode.rightChild);

            s += (focusNode.getQuestionNo() + "-");
            s += (focusNode.getTopic() + "-");
            s += (Integer.toString(focusNode.getCorrectCount()) + "Students, ");

        }
        return s;
    }

    public Node findNode(int key) {

		// Start at the top of the tree
        Node focusNode = root;

		// While we haven't found the Node
        // keep looking
        while (focusNode.questionNo != key) {

			// If we should search to the left
            if (key < focusNode.questionNo) {

				// Shift the focus Node to the left child
                focusNode = focusNode.leftChild;

            } else {

				// Shift the focus Node to the right child
                focusNode = focusNode.rightChild;

            }

			// The node wasn't found
            if (focusNode == null) {
                return null;
            }

        }

        return focusNode;

    }
}


