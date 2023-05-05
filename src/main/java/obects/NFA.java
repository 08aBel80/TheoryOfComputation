package obects;

import java.util.ArrayList;
import java.util.Set;

public class NFA {
    private Node startNode;
    private ArrayList<Node> acceptNodes;
    private boolean isNamed = false;
    private ArrayList<Node> names;
    private int numMoves;

    public NFA(Node startNode, ArrayList<Node> acceptNodes) {
        reformatStartNode(startNode);
        this.startNode = startNode;
        this.acceptNodes = acceptNodes;

    }


    private void reformatStartNode(Node startNode) {
        if (!startNode.getMoves().containsKey(startNode)) return;

        Node temp = new Node();
        Set<Character> charSet = startNode.removeNode(startNode);
        startNode.addNode(temp, charSet);

        for (Node node : startNode.getMoves().keySet()) {
            temp.addNode(node, startNode.getMoves().get(node));
        }
    }

    public Node getStartNode() {
        return startNode;
    }

    public ArrayList<Node> getAcceptNodes() {
        return acceptNodes;
    }

    public void concat(NFA other) {
        isNamed = false;
        if (other.acceptNodes.size() == 0) return;
        if (this.acceptNodes.size() == 0) {
            this.startNode = other.startNode;
            this.acceptNodes = other.acceptNodes;
            return;
        }


        for (Node otherSecondNode : other.startNode.getMoves().keySet()) {
            Set<Character> charSet = otherSecondNode.removeBackNode(other.startNode);
            for (Node endNode : acceptNodes) {
                endNode.addNode(otherSecondNode, charSet);
            }
        }
        if (other.acceptNodes.contains(other.startNode)) {
            other.acceptNodes.remove(other.startNode);
            this.acceptNodes.addAll(other.acceptNodes);
        } else {
            this.acceptNodes = other.acceptNodes;
        }

    }

    public void union(NFA other) {
        isNamed = false;
        if (other.acceptNodes.size() == 0) return;
        if (this.acceptNodes.size() == 0) {
            this.startNode = other.startNode;
            this.acceptNodes = other.acceptNodes;
            return;
        }
        for (Node otherSecondNode : other.startNode.getMoves().keySet()) {
            this.startNode.addNode(otherSecondNode, otherSecondNode.removeBackNode(other.startNode));
        }
        if (other.acceptNodes.contains(other.startNode)) {
            other.acceptNodes.remove(other.startNode);
            this.acceptNodes.add(startNode);
        }
        this.acceptNodes.addAll(other.acceptNodes);
    }

    public void star() {
        isNamed = false;
        for (Node acceptNode : this.acceptNodes) {
            for (Node secondNode : startNode.getMoves().keySet()) {
                acceptNode.addNode(secondNode, startNode.getMoves().get(secondNode));
            }
        }
    }

    public ArrayList<Node> getNames() {
        if (isNamed) {
            return names;
        }
        names = new ArrayList<Node>();
        numMoves = 0;
        recursiveNaming(startNode);
        isNamed = true;
        return names;
    }

    public int countNodes() {
        return getNames().size();
    }

    public int countMoves() {
        if (isNamed) {
            return numMoves;
        }
        getNames();
        return numMoves;
    }

    private void recursiveNaming(Node curNode) {
        curNode.setName(names.size());
        names.add(curNode);
        for (Node node : curNode.getMoves().keySet()) {
            if (!names.contains(node)) recursiveNaming(node);
            numMoves += curNode.getMoves().get(node).size();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(countNodes() + " " + acceptNodes.size() + " " + countMoves() + "\n");
        for (Node node : acceptNodes) {
            builder.append(node.getName() + " ");
        }
        builder.deleteCharAt(builder.length() - 1);
        for (Node node : getNames()) builder.append( "\n" + node.toString() );
        return builder.toString();
    }
}
