package obects;

import java.util.*;

public class State {
    private HashMap<State, Set<Character>> moves;
    private HashMap<State, Set<Character>> backMoves;
    private int id;
    private static int idGenerator = 0;
    private int name;

    public State() {
        moves = new HashMap<State, Set<Character>>();
        backMoves = new HashMap<State, Set<Character>>();
        id = idGenerator++;
    }

    public Set<State> getNextStates(){
        Set<State> result = new HashSet<State>();
        result.addAll(this.moves.keySet());
        return result;
    }
    public void setName(int name) {
        this.name = name;
    }

    public void addState(State other, Character ch) {
        if (!moves.containsKey(other)) {
            Set<Character> temp = new HashSet<Character>();
            moves.put(other, temp);
            other.backMoves.put(this, temp);
        }

        moves.get(other).add(ch);
    }

    public void addBackState(State other, Character ch) {
        if (!other.moves.containsKey(this)) {
            Set<Character> temp = new HashSet<Character>();
            other.moves.put(this, temp);
            backMoves.put(other, temp);
        }
        backMoves.get(other).add(ch);
    }

    public void addState(State other, Set<Character> chars) {
        if (!moves.containsKey(other)) {
            Set<Character> temp = new HashSet<Character>();
            moves.put(other, temp);
            other.backMoves.put(this, temp);
        }

        moves.get(other).addAll(chars);
    }

    public void addBackState(State other, Set<Character> chars) {
        if (!other.moves.containsKey(this)) {
            Set<Character> temp = new HashSet<Character>();
            other.moves.put(this, temp);
            backMoves.put(other, temp);
        }
        backMoves.get(other).addAll(chars);
    }

    public Set<Character> removeState(State other) {
        Set<Character> returnSet = moves.get(other);
        moves.remove(other);
        other.backMoves.remove(this);
        return returnSet;
    }

    public Set<Character> removeBackState(State other) {
        Set<Character> returnSet = backMoves.get(other);
        backMoves.remove(other);
        other.moves.remove(this);
        return returnSet;
    }

    public int getId() {
        return id;
    }

    public HashMap<State, Set<Character>> getBackMoves() {
        return backMoves;
    }

    public HashMap<State, Set<Character>> getMoves() {
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof State)) return false;
        State other = (State) o;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id);
    }

    public int countMoves() {
        int count = 0;
        for (State state : moves.keySet()) {
            count += moves.get(state).size();
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
        ArrayList<State> sortedKeySet = new ArrayList<State>();
        sortedKeySet.addAll(moves.keySet());
        Collections.sort(sortedKeySet, (State n1, State n2) -> n1.getName() - n2.getName());
        for (State state : sortedKeySet) {
            ArrayList<Character> sortedMoves = new ArrayList<Character>();
            sortedMoves.addAll(moves.get(state));
            Collections.sort(sortedMoves);
            for (Character ch : moves.get(state)) {
                builder.append(" " + ch + " " + state.name);
            }
        }
        return builder.toString();
    }
}

