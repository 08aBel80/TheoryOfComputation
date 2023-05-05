package obects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Node {
    private HashMap<Node, Set<Character>> moves;
    private HashMap<Node, Set<Character>> backMoves;
    private int id;
    private static int idGenerator = 0;
    private int name;

    public Node() {
        moves = new HashMap<Node, Set<Character>>();
        backMoves = new HashMap<Node, Set<Character>>();
        id = idGenerator++;
    }

    public void setName(int name) {
        this.name = name;
    }

    public void addNode(Node other, Character ch) {
        if (!moves.containsKey(other)) {
            Set<Character> temp = new HashSet<Character>();
            moves.put(other, temp);
            other.backMoves.put(this, temp);
        }

        moves.get(other).add(ch);
    }

    public void addBackNode(Node other, Character ch) {
        if (!other.moves.containsKey(this)) {
            Set<Character> temp = new HashSet<Character>();
            other.moves.put(this, temp);
            backMoves.put(other, temp);
        }
        backMoves.get(other).add(ch);
    }

    public void addNode(Node other, Set<Character> chars) {
        if (!moves.containsKey(other)) {
            Set<Character> temp = new HashSet<Character>();
            moves.put(other, temp);
            other.backMoves.put(this, temp);
        }

        moves.get(other).addAll(chars);
    }

    public void addBackNode(Node other, Set<Character> chars) {
        if (!other.moves.containsKey(this)) {
            Set<Character> temp = new HashSet<Character>();
            other.moves.put(this, temp);
            backMoves.put(other, temp);
        }
        backMoves.get(other).addAll(chars);
    }

    public Set<Character> removeNode(Node other) {
        Set<Character> returnSet = moves.get(other);
        moves.remove(other);
        other.backMoves.remove(this);
        return returnSet;
    }

    public Set<Character> removeBackNode(Node other) {
        Set<Character> returnSet = backMoves.get(other);
        backMoves.remove(other);
        other.moves.remove(this);
        return returnSet;
    }

    public int getId() {
        return id;
    }

    public HashMap<Node, Set<Character>> getBackMoves() {
        return backMoves;
    }

    public HashMap<Node, Set<Character>> getMoves() {
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Node)) return false;
        Node other = (Node) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }

    public int countMoves() {
        int count = 0;
        for (Node node : moves.keySet()) {
            count += moves.get(node).size();
        }
        return count;
    }

    public int getName() {
        return name;
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(countMoves());
        for (Node node : moves.keySet()) {
            for (Character ch : moves.get(node)) {
                builder.append(" " + ch + " " + node.name);
            }
        }
        return builder.toString();
    }
}

