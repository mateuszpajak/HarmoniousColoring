public class Graph {

    public boolean isFilled(Node[][] graph) {
        boolean isFilled = true;
        boolean isEnd = true;
        int i = 0;
        do {
            if (i == graph.length) {
                isEnd = false;
            } else {
                for (int j = 0; j < graph[i].length; j++) {
                    if (graph[i][j].getColor() == 0) {
                        isFilled = false;
                        isEnd = false;
                    }
                }
            }
            i++;
        } while (isEnd);

        return isFilled;
    }

    public Node getNext(Node[][] graph, Node node) {
        Node result = null;

        int i = node.getId() / (graph[0].length);
        int j = Math.abs(node.getId() - i * graph[0].length);

        if (j < graph[0].length - 1) {
            result = graph[i][j + 1];
        } else if (i < graph.length - 1) {
            result = graph[i + 1][0];
        }

        return result;
    }

    public Node getLeft(Node[][] graph, Node node) {
        Node result = null;

        int i = node.getId() / (graph[0].length);
        int j = Math.abs(node.getId() - i * graph[0].length);

        if (j > 0) {
            result = graph[i][j - 1];
        }

        return result;
    }

    public Node getRight(Node[][] graph, Node node) {
        Node result = null;

        int i = node.getId() / (graph[0].length);
        int j = Math.abs(node.getId() - i * graph[0].length);

        if (j < graph[0].length - 1) {
            result = graph[i][j + 1];
        }

        return result;
    }

    public Node getUpper(Node[][] graph, Node node) {
        Node result = null;

        int i = node.getId() / (graph[0].length);
        int j = Math.abs(node.getId() - i * graph[0].length);

        if (i > 0) {
            result = graph[i - 1][j];
        }

        return result;
    }

    public Node getBottom(Node[][] graph, Node node) {
        Node result = null;

        int i = node.getId() / (graph[0].length);
        int j = Math.abs(node.getId() - i * graph[0].length);

        if (i < graph.length - 1) {
            result = graph[i + 1][j];
        }

        return result;
    }


    public Node getPrevious(Node[][] graph, Node node) {
        Node result = null;

        int i = node.getId() / (graph[0].length);
        int j = Math.abs(node.getId() - i * graph[0].length);

        if (j > 0) {
            result = graph[i][j - 1];
        } else if (i > 0) {
            result = graph[i - 0][graph[0].length];
        }

        return result;
    }

    public Node getMostLimitedNode(Node[][] graph, Integer[][] numberOfElements) {
        Node result = null;

        int positionX = 0;
        int positionY = 0;
        int max = 0;
        for (int i = 0; i < numberOfElements.length; i++) {
            for (int j = 0; j < numberOfElements[i].length; j++) {
                if (max < numberOfElements[i][j] &&
                        numberOfElements[i][j] < graph.length) {
                    max = numberOfElements[i][j];
                    positionX = i;
                    positionY = j;
                }
            }
        }

        boolean found = false;
        if (positionX == 0) {
            for (int i = 0; i < graph[0].length; i++) {
                if (graph[positionY][i].getColor() == 0 && !found) {
                    result = graph[positionY][i];
                    found = true;
                }
            }
        } else {
            for (int i = 0; i < graph[0].length; i++) {
                if (graph[i][positionY].getColor() == 0 && !found) {
                    result = graph[i][positionY];
                    found = true;
                }
            }
        }
        return result;
    }

    public static void printGraph(Node[][] graph) {
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                System.out.print(graph[i][j].toString() + " \t");
            }
            System.out.println();
        }
    }

}
