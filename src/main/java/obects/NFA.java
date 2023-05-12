package obects;

import java.util.*;

public class NFA {
    private State startState;
    private Set<State> acceptStates;
    private boolean isNamed = false;
    private ArrayList<State> names;
    private int numMoves;

    public NFA(State startState) {
        reformatStartState(startState);
        this.startState = startState;
        acceptStates = new HashSet<State>();
    }

    public NFA(State startState, State endState) {
        this(startState);
        addAcceptState(endState);
    }

    public NFA(State startState, ArrayList<State> acceptStates) {
        this(startState);
        addAcceptStates(acceptStates);
    }

    public NFA(String regex) {
        NFA regexToNfa = analyzeRegex(regex);
        this.startState = regexToNfa.startState;
        this.acceptStates = regexToNfa.acceptStates;
    }

    public void addAcceptState(State state) {
        acceptStates.add(state);
    }

    public void addAcceptStates(ArrayList<State> states) {
        acceptStates.addAll(states);
    }

    public boolean simulate(String str) {
        return recursiveSimulate(str, startState, 0);
    }

    private boolean recursiveSimulate(String str, State currentState, int index) {
        if (index == str.length()) return acceptStates.contains(currentState);

        boolean result = false;
        char ch = str.charAt(index);

        for (State nextState : currentState.getNextStates()) {
            if (currentState.getMoves().get(nextState).contains(ch)) {
                result = result || recursiveSimulate(str, nextState, index + 1);
            }
        }

        return result;
    }

    private NFA analyzeRegex(String regex) {
        Stack<NFA> nfaStack = new Stack<NFA>();
        Stack<Character> operations = new Stack<Character>();

        for (int i = 0; i < regex.length(); i++) {
            char ch = regex.charAt(i);
            if (i > 0) {
                char prev = regex.charAt(i - 1);
                if ((prev != '(' && prev != '|') && (ch != '*' && ch != '|' && ch != ')')) {
                    while (!operations.empty() && operations.peek() != '|' && operations.peek() != '(') {
                        evaluate(operations, nfaStack);
                    }
                    operations.push('c');
                }
            }
            if (isSymbol(ch)) {
                nfaStack.push(oneCharNFA(ch));
            } else if (ch == '*') {
                nfaStack.peek().star();
            } else if (ch == '(') {
                operations.push(ch);
            } else if (i > 0) {
                char prev = regex.charAt(i - 1);
                if (ch == ')' && prev == '(') {
                    nfaStack.push(emptyStringNFA());
                    operations.pop();
                } else {
                    while (!operations.empty() && operations.peek() != '(') {
                        evaluate(operations, nfaStack);
                    }
                    if (ch == '|') {
                        operations.push(ch);
                    }
                    if (ch == ')') {
                        operations.pop();
                    }
                }
            }
        }
        while (!operations.empty()) {
            evaluate(operations, nfaStack);
        }
        return nfaStack.pop();
    }

    private static void evaluate(Stack<Character> operations, Stack<NFA> nfaStack) {
        char operator = operations.pop();
        switch (operator) {
            case 'c':
                NFA nfa2 = nfaStack.pop();
                NFA nfa1 = nfaStack.pop();
                nfa1.concat(nfa2);
                nfaStack.push(nfa1);
                break;
            case '|':
                nfa2 = nfaStack.pop();
                nfa1 = nfaStack.pop();
                nfa1.union(nfa2);
                nfaStack.push(nfa1);
                break;
        }

    }

    public static NFA oneCharNFA(char c) {
        State startState = new State();
        State acceptState = new State();
        startState.addState(acceptState, c);

        return new NFA(startState, acceptState);
    }

    public static NFA emptyStringNFA() {
        State startState = new State();

        return new NFA(startState, startState);
    }

    public static NFA emptyNFA() {
        State startState = new State();

        return new NFA(startState);
    }

    public static boolean isSymbol(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'X') ||
                (c >= '0' && c <= '9');
    }

    private void reformatStartState(State startState) {
        if (!startState.getMoves().containsKey(startState)) return;

        State temp = new State();
        Set<Character> charSet = startState.removeState(startState);
        startState.addState(temp, charSet);

        for (State state : startState.getNextStates()) {
            temp.addState(state, startState.getMoves().get(state));
        }
    }

    public State getStartState() {
        return startState;
    }

    public Set<State> getAcceptStates() {
        return acceptStates;
    }

    public void concat(NFA other) {
        isNamed = false;
        if (other.acceptStates.size() == 0) return;
        if (this.acceptStates.size() == 0) {
            this.startState = other.startState;
            this.acceptStates = other.acceptStates;
            return;
        }


        for (State otherSecondState : other.startState.getNextStates()) {
            Set<Character> charSet = otherSecondState.removeBackState(other.startState);
            for (State endState : acceptStates) {
                endState.addState(otherSecondState, charSet);
            }
        }
        if (other.acceptStates.contains(other.startState)) {
            other.acceptStates.remove(other.startState);
            this.acceptStates.addAll(other.acceptStates);
        } else {
            this.acceptStates = other.acceptStates;
        }

    }

    public void union(NFA other) {
        isNamed = false;
        if (other.acceptStates.size() == 0) return;
        if (this.acceptStates.size() == 0) {
            this.startState = other.startState;
            this.acceptStates = other.acceptStates;
            return;
        }
        for (State otherSecondState : other.startState.getNextStates()) {
            this.startState.addState(otherSecondState, otherSecondState.removeBackState(other.startState));
        }
        if (other.acceptStates.contains(other.startState)) {
            other.acceptStates.remove(other.startState);
            this.acceptStates.add(startState);
        }
        this.acceptStates.addAll(other.acceptStates);
    }

    public void star() {
        isNamed = false;
        for (State acceptState : this.acceptStates) {
            for (State secondState : startState.getNextStates()) {
                acceptState.addState(secondState, startState.getMoves().get(secondState));
            }
        }
        acceptStates.add(startState);
    }

    public ArrayList<State> getNames() {
        if (isNamed) {
            return names;
        }
        names = new ArrayList<State>();
        numMoves = 0;
        recursiveNaming(startState);
        isNamed = true;
        return names;
    }

    public int countStates() {
        return getNames().size();
    }

    public int countMoves() {
        if (isNamed) {
            return numMoves;
        }
        getNames();
        return numMoves;
    }

    private void recursiveNaming(State curState) {
        curState.setName(names.size());
        names.add(curState);
        for (State state : curState.getNextStates()) {
            if (!names.contains(state)) recursiveNaming(state);
            numMoves += curState.getMoves().get(state).size();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(countStates() + " " + acceptStates.size() + " " + countMoves() + "\n");
        ArrayList<State> acceptList = new ArrayList<State>();
        acceptList.addAll(acceptStates);
        Collections.sort(acceptList, (State n1, State n2) -> n1.getName() - n2.getName());
        for (State state : acceptList) {
            builder.append(state.getName() + " ");
        }
        builder.deleteCharAt(builder.length() - 1);
        for (State state : getNames()) builder.append("\n" + state.toString());
        return builder.toString();
    }
}
