import java.util.*;
import java.util.stream.Collectors;

public class Backtracking {

    private Node[][] graph;
    private List<Integer> colors;
    private List listOfConstraints;
    private int assignments;
    private Integer[][] numberOfElements;
    private final Graph graphMethods = new Graph();

    public Backtracking() {
    }

    public Backtracking(int dimension) {
        graph = new Node[dimension][dimension];
        colors = new ArrayList<>();
        listOfConstraints = new ArrayList();
        numberOfElements = new Integer[2][dimension];
        assignments = 0;

        for (int i = 0; i < numberOfElements.length; i++) {
            for (int j = 0; j < numberOfElements[i].length; j++) {
                numberOfElements[i][j] = 0;
            }
        }

        for (int i = 0; i < (dimension % 2 == 0 ? (2 * dimension) : (2 * dimension + 1)); i++) {
            colors.add(i + 1);
        }

        int id = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                graph[i][j] = new Node(id++);
                ArrayList<Integer> listOfColors = new ArrayList<>();
                colors.forEach(item -> listOfColors.add(item));
                graph[i][j].setPositionX(i).setPositionY(j);
                graph[i][j].setPossibleColors(listOfColors);
            }
        }
    }

    public Node[][] getGraph() {
        return graph;
    }

    public Backtracking setGraph(Node[][] graph) {
        this.graph = graph;
        return this;
    }

    public List<Integer> getColors() {
        return colors;
    }

    public Backtracking setColors(List<Integer> colors) {
        this.colors = colors;
        return this;
    }

    public List getListOfConstraints() {
        return listOfConstraints;
    }

    public Backtracking setListOfConstraints(List listOfConstraints) {
        this.listOfConstraints = listOfConstraints;
        return this;
    }

    public int getAssignments() {
        return assignments;
    }

    public Backtracking setAssignments(int assignments) {
        this.assignments = assignments;
        return this;
    }

    public void backtracking() {
        backtrackingAlgorithm(graph, graph[0][0]);
    }

    public void backtrackingHeuristic1() {
        backtrackingHeuristic1(graph, graph[0][0]);
    }

    public void backtrackingHeuristic2() {
        backtrackingHeuristic2(graph, graph[0][0]);
    }

    private void decrementNumberOfElements(Node node) {

        if (node.getColor() != 0) {
            numberOfElements[0][node.getPositionX()] = numberOfElements[0][node.getPositionX()] == 0 ?
                    0 : numberOfElements[0][node.getPositionX()] - 1;
            numberOfElements[1][node.getPositionY()] = numberOfElements[1][node.getPositionY()] == 0 ?
                    0 : numberOfElements[1][node.getPositionY()] - 1;
        }

    }

    private boolean backtrackingAlgorithm(Node[][] graph, Node node) {
        if (graphMethods.isFilled(graph)) {
            return true;
        }

        for (int i = 0; i < colors.size(); i++) {
            if (graphMethods.isFilled(graph) && checkConstraints(graph, node)) {
                return true;
            } else {
                node.setColor(colors.get(i));
                assignments++;
                if (checkConstraints(graph, node)) {
                    if (backtrackingAlgorithm(graph, graphMethods.getNext(graph, node)))
                        return true;
                    else if (i == colors.size() - 1) {
                        node.setColor(0);
                        return false;
                    }
                } else if (i == colors.size() - 1) {
                    node.setColor(0);
                    return false;
                }
            }
        }
        return false;
    }

    private boolean backtrackingHeuristic1(Node[][] graph, Node node) {
        if (graphMethods.isFilled(graph)) {
            return true;
        }

        for (int i = 0; i < colors.size(); i++) {
            if (graphMethods.isFilled(graph) && checkConstraints(graph, node)) {
                return true;
            } else {
                if (node.getColor() == 0) {
                    numberOfElements[0][node.getPositionX()]++;
                    numberOfElements[1][node.getPositionY()]++;
                }
                node.setColor(colors.get(i));
                assignments++;

                if (checkConstraints(graph, node)) {
                    if (backtrackingHeuristic1(graph, graphMethods.getMostLimitedNode(graph, numberOfElements)))
                        return true;
                    else if (i == colors.size() - 1) {
                        decrementNumberOfElements(node);
                        node.setColor(0);
                        return false;
                    }
                } else if (i == colors.size() - 1) {
                    decrementNumberOfElements(node);
                    node.setColor(0);
                    return false;
                }
            }
        }
        return false;
    }

    private boolean backtrackingHeuristic2(Node[][] graph, Node node) {
        if (graphMethods.isFilled(graph)) {
            return true;
        }

        List<Integer> sortedColors = getSortedColors(graph);
        for (int i = 0; i < sortedColors.size(); i++) {
            if (graphMethods.isFilled(graph) && checkConstraints(graph, node)) {
                return true;
            } else {
                node.setColor(sortedColors.get(i));
                assignments++;
                if (checkConstraints(graph, node)) {
                    if (backtrackingHeuristic2(graph, graphMethods.getNext(graph, node)))
                        return true;
                    else if (i == sortedColors.size() - 1) {
                        node.setColor(0);
                        return false;
                    }
                } else if (i == sortedColors.size() - 1) {
                    node.setColor(0);
                    return false;
                }
            }
        }
        return false;
    }

    private List<Integer> getSortedColors(Node[][] graph) {
        List<Integer> result = new LinkedList<>();
        HashMap<Integer, Integer> countedColors = new HashMap<>();

        colors.forEach(item -> countedColors.put(item, 0));

        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j].getColor() != 0)
                    countedColors.replace(graph[i][j].getColor(), countedColors.get(graph[i][j].getColor()) + 1);
            }
        }

        HashMap<Integer, Integer> sortedMap =
                countedColors.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));

        sortedMap.forEach((key, value) -> result.add(key));

        return result;
    }

    private boolean checkConstraints(Node[][] graph, Node node) {

        Integer color = node.getColor();

        Node left = graphMethods.getLeft(graph, node);
        Node upper = graphMethods.getUpper(graph, node);

        boolean leftConstraint = left != null ? left.getColor() != color : true;
        boolean upperConstraint = upper != null ? upper.getColor() != color : true;
        boolean pairConstraint = false;
        if (leftConstraint && upperConstraint) {
            ArrayList<Node> listOfNodes = new ArrayList<>();
            if (left != null)
                listOfNodes.add(left);
            if (upper != null)
                listOfNodes.add(upper);
            pairConstraint = checkPairConstraints(listOfNodes, color, node);
        }
        return leftConstraint && upperConstraint && pairConstraint;
    }

    private boolean checkPairConstraints(ArrayList<Node> listOfNodes, Integer color, Node node) {
        boolean result = true;

        listOfConstraints = new ArrayList();
        for (int x = 0; x < graph.length; x++) {
            for (int y = 0; y < graph[x].length; y++) {
                if (graph[x][y].getColor() != 0 && !graph[x][y].equals(node)) {
                    if (y > 0) {
                        String pairFirst = graph[x][y].getColor() + "-" + graph[x][y - 1].getColor();
                        listOfConstraints.add(pairFirst);
                        String pairFirst2 = graph[x][y - 1].getColor() + "-" + graph[x][y].getColor();
                        listOfConstraints.add(pairFirst2);
                    }
                    if (x > 0) {
                        String pairFirst = graph[x - 1][y].getColor() + "-" + graph[x][y].getColor();
                        listOfConstraints.add(pairFirst);
                        String pairFirst2 = graph[x][y].getColor() + "-" + graph[x - 1][y].getColor();
                        listOfConstraints.add(pairFirst2);
                    }
                }
            }
        }

        for (int i = 0; i < listOfNodes.size(); i++) {
            String pair = listOfNodes.get(i).getColor() + "-" + color;
            if (listContains(pair))
                result = false;
        }
        return result;
    }

    private boolean listContains(String constraint) {
        boolean[] result = new boolean[1];
        listOfConstraints.forEach(item -> {
            if (item.equals(constraint))
                result[0] = true;
        });

        return result[0];
    }

    public static void main(String[] args) {
        Backtracking backtracking = new Backtracking(5);
        long startTime = System.currentTimeMillis();
        backtracking.backtracking();
        //backtracking.backtrackingHeuristic1();
        //backtracking.backtrackingHeuristic2();
        long endTime = System.currentTimeMillis();
        Node[][] g = backtracking.getGraph();
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[i].length; j++) {
                System.out.print(g[i][j].getColor() + " \t");
            }
            System.out.println();
        }
        System.out.println("\nAssignments: " + backtracking.getAssignments() + "\nElapsed time: " + (endTime - startTime));
    }
}
