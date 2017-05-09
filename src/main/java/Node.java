import java.util.LinkedList;
import java.util.List;

public class Node {
    private int id;
    private int color;
    private int positionX;
    private int positionY;
    private List<Integer> possibleColors = new LinkedList<>();

    public Node() {
    }

    public Node(int id) {
        this.id = id;
    }

    public Node(int id, int color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public Node setId(int id) {
        this.id = id;
        return this;
    }

    public int getColor() {
        return color;
    }

    public Node setColor(int color) {
        this.color = color;
        return this;
    }

    public List<Integer> getPossibleColors() {
        return possibleColors;
    }

    public Node setPossibleColors(List<Integer> possibleColors) {
        this.possibleColors = possibleColors;
        return this;
    }

    public int getPositionX() {
        return positionX;
    }

    public Node setPositionX(int positionX) {
        this.positionX = positionX;
        return this;
    }

    public int getPositionY() {
        return positionY;
    }

    public Node setPositionY(int positionY) {
        this.positionY = positionY;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;
        return color == node.color;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + color;
        return result;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", color=" + color +
                '}';
    }
}
