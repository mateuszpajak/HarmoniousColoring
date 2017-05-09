import java.util.*;
import java.util.stream.Collectors;

public class ForwardChecking {

    private Node[][] graph;
    private List<Integer> colors;
    private List listOfConstraints;
    private int assignments;
    private int dimension;
    private final Graph graphMethods = new Graph();
    private Integer[][] numberOfElements;

    public ForwardChecking(int dimension) {
        this.dimension = dimension;
        graph = new Node[dimension][dimension];
        colors = new LinkedList<>();
        listOfConstraints = new LinkedList();
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
                graph[i][j].setPositionX(i).setPositionY(j);
                LinkedList<Integer> listOfColors = new LinkedList<>();
                colors.forEach(item -> listOfColors.add(item));
                graph[i][j].setPossibleColors(listOfColors);
            }
        }
    }

    private boolean ifListContains(String constraint) {
        boolean[] result = new boolean[1];
        listOfConstraints.forEach(item -> {
            if (item.equals(constraint))
                result[0] = true;
        });
        return result[0];
    }

    public void forwardCheckingHeuristic1() {
        forwardCheckingHeuristic1(graph, graph[0][0]);
    }

    public void forwardCheckingHeuristic2() {
        forwardCheckingHeuristic2(graph, graph[0][0]);
    }

    public void forwardCheckingAlgorithm() {
        forwardCheckingAlgorithm(graph, graph[0][0]);
    }

    private List<Integer> getPossibleColors(Node[][] graph, Node node) {
        List<Integer> possibleColors = new LinkedList<>();
        for (int i = 0; i < (dimension % 2 == 0 ? (2 * dimension) : (2 * dimension + 1)); i++) {
            possibleColors.add(i + 1);
        }

        int i = node.getPositionX();
        int j = node.getPositionY();
        if (i > 0 && graph[i - 1][j].getColor() != 0) {
            possibleColors.remove(new Integer(graph[i - 1][j].getColor()));
        }
        if (j > 0 && graph[i][j - 1].getColor() != 0) {
            possibleColors.remove(new Integer(graph[i][j - 1].getColor()));
        }
        if (i < graph.length - 1 && graph[i + 1][j].getColor() != 0) {
            possibleColors.remove(new Integer(graph[i + 1][j].getColor()));
        }
        if (j < graph[0].length - 1 && graph[i][j + 1].getColor() != 0) {
            possibleColors.remove(new Integer(graph[i][j + 1].getColor()));
        }

        setConstraints();

        for (int x = 0; x < possibleColors.size(); x++) {
            if (i > 0) {
                String possibleColor = possibleColors.get(x) + "-" + graph[i - 1][j].getColor();
                if (ifListContains(possibleColor))
                    possibleColors.remove(x--);
            }
        }
        for (int x = 0; x < possibleColors.size(); x++) {
            if (j > 0) {
                String possibleColor = possibleColors.get(x) + "-" + graph[i][j - 1].getColor();
                if (ifListContains(possibleColor))
                    possibleColors.remove(x--);
            }
        }

        return possibleColors;
    }

    public boolean forwardCheckingAlgorithm(Node[][] graph, Node node) {
        if (graphMethods.isFilled(graph)) {
            return true;
        }
        if (node.getColor() == 0) {
            node.setPossibleColors(getPossibleColors(graph, node));
            for (int i = 0; i < node.getPossibleColors().size(); i++) {
                node.setColor(node.getPossibleColors().get(i));
                assignments++;

                if (graphMethods.isFilled(graph))
                    return true;
                else {
                    forwardCheckingAlgorithm(graph, graphMethods.getNext(graph, node));
                }
                if (graphMethods.isFilled(graph))
                    return true;
            }
            node.setColor(0);
            return false;
        }
        return false;
    }

    public boolean forwardCheckingHeuristic1(Node[][] graph, Node node) {
        if (graphMethods.isFilled(graph)) {
            return true;
        }

        if (node.getColor() == 0) {
            node.setPossibleColors(getPossibleColors(graph, node));
            for (int i = 0; i < node.getPossibleColors().size(); i++) {
                if (node.getColor() == 0) {
                    numberOfElements[0][node.getPositionX()]++;
                    numberOfElements[1][node.getPositionY()]++;
                }

                node.setColor(node.getPossibleColors().get(i));
                assignments++;

                if (graphMethods.isFilled(graph))
                    return true;
                else {
                    forwardCheckingHeuristic1(graph, graphMethods.getMostLimitedNode(graph, numberOfElements));
                }
                if (graphMethods.isFilled(graph))
                    return true;
            }
            if (node.getColor() != 0) {
                numberOfElements[0][node.getPositionX()] = numberOfElements[0][node.getPositionX()] == 0 ?
                        0 : numberOfElements[0][node.getPositionX()] - 1;
                numberOfElements[1][node.getPositionY()] = numberOfElements[1][node.getPositionY()] == 0 ?
                        0 : numberOfElements[1][node.getPositionY()] - 1;
            }
            node.setColor(0);
            return false;
        }
        return false;
    }

    private boolean forwardCheckingHeuristic2(Node[][] graph, Node node) {
        if (graphMethods.isFilled(graph)) {
            return true;
        }

        if (node.getColor() == 0) {
            node.setPossibleColors(getSortedPossibleColors(graph, node));
            for (int i = 0; i < node.getPossibleColors().size(); i++) {
                node.setColor(node.getPossibleColors().get(i));
                assignments++;
                if (graphMethods.isFilled(graph))
                    return true;
                else {
                    forwardCheckingHeuristic2(graph, graphMethods.getNext(graph, node));
                }
                if (graphMethods.isFilled(graph))
                    return true;
            }
            node.setColor(0);
            return false;
        }

        return false;
    }

    private List<Integer> getSortedPossibleColors(Node[][] graph, Node node) {
        List<Integer> result = new LinkedList<>();
        HashMap<Integer, Integer> countedColors = new HashMap<>();

        colors.forEach(item -> countedColors.put(item, 0));

        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j].getColor() != 0)
                    countedColors.replace(graph[i][j].getColor(), countedColors.get(graph[i][j].getColor()) + 1);
            }
        }
        List<Integer> possibleColors = getPossibleColors(graph, node);

        HashMap<Integer, Integer> countedPossibleColors = new HashMap<>();
        countedColors.forEach((key, value) -> {
            if (possibleColors.contains(key))
                countedPossibleColors.put(key, value);
        });

        HashMap<Integer, Integer> sortedMap =
                countedPossibleColors.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));

        sortedMap.forEach((key, value) -> result.add(key));

        return result;
    }


    private void setConstraints() {
        listOfConstraints = new LinkedList();

        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j].getColor() != 0) {
                    if (j > 0) {
                        listOfConstraints.add(graph[i][j].getColor() + "-" + graph[i][j - 1].getColor());
                        listOfConstraints.add(graph[i][j - 1].getColor() + "-" + graph[i][j].getColor());
                    }
                    if (i > 0) {
                        listOfConstraints.add(graph[i][j].getColor() + "-" + graph[i - 1][j].getColor());
                        listOfConstraints.add(graph[i - 1][j].getColor() + "-" + graph[i][j].getColor());
                    }
                    if (j < graph[0].length - 1) {
                        listOfConstraints.add(graph[i][j].getColor() + "-" + graph[i][j + 1].getColor());
                        listOfConstraints.add(graph[i][j + 1].getColor() + "-" + graph[i][j].getColor());
                    }
                    if (i < graph.length - 1) {
                        listOfConstraints.add(graph[i][j].getColor() + "-" + graph[i + 1][j].getColor());
                        listOfConstraints.add(graph[i + 1][j].getColor() + "-" + graph[i][j].getColor());
                    }
                }
            }
        }
    }

    public Node[][] getGraph() {
        return graph;
    }

    public ForwardChecking setGraph(Node[][] graph) {
        this.graph = graph;
        return this;
    }

    public List<Integer> getColors() {
        return colors;
    }

    public ForwardChecking setColors(List<Integer> colors) {
        this.colors = colors;
        return this;
    }

    public int getAssignments() {
        return assignments;
    }

    public ForwardChecking setAssignments(int assignments) {
        this.assignments = assignments;
        return this;
    }

    public static void main(String[] args) {
        ForwardChecking forwardChecking = new ForwardChecking(5);
        long startTime = System.currentTimeMillis();
        forwardChecking.forwardCheckingAlgorithm();
        //forwardChecking.forwardCheckingHeuristic1();
        //forwardChecking.forwardCheckingHeuristic2();
        long endTime = System.currentTimeMillis();
        Node[][] graph = forwardChecking.getGraph();
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                System.out.print(graph[i][j].getColor() + " \t");
            }
            System.out.println();
        }
        System.out.println("\nAssignments: " + forwardChecking.getAssignments() + "\nElapsed time: " + (endTime - startTime));
    }
}
