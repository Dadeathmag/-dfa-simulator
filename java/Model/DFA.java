package Model;
import java.util.*;
import javax.swing.SwingUtilities;

import GUI.DFAPanel;

public class DFA {
    private List<State> states;
    private List<Transition> transitions;
    private State startState;
    private Set<State> acceptStates;

    // construtor to create a dfa model
    public DFA() {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        acceptStates = new HashSet<>();
        startState = null;
    }

    /* 
    public DFA(int x) {
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        acceptStates = new HashSet<>();
        startState = null;
        initialize();
    }
    */

    public void addState(State s) {
        states.add(s);
        if (s.isStart()) startState = s;
        if (s.isAccept()) acceptStates.add(s);
    }

    public void removeState(State s) {
        states.remove(s);
        transitions.removeIf(t -> t.getFrom() == s || t.getTo() == s);
        acceptStates.remove(s);
        if (startState == s) startState = null;
    }

    public void addTransition(Transition t) {
        transitions.add(t);
    }

    public void removeTransition(Transition t) {
        transitions.remove(t);
    }

    public List<State> getStates() { return states; }
    public List<Transition> getTransitions() { return transitions; }
    public State getStartState() { return startState; }
    public Set<State> getAcceptStates() { return acceptStates; }

    public void setStartState(State s) {
        if (startState != null) startState.setStart(false);
        startState = s;
        if (s != null) s.setStart(true);
    }

    public void setAcceptState(State s, boolean isAccept) {
        s.setAccept(isAccept);
        if (isAccept) acceptStates.add(s);
        else acceptStates.remove(s);
    }

    // Simulate DFA on input string, return true if accepted
    public boolean simulate(String input, DFAPanel panel) {
        if (startState == null) return false;
        State current = startState;
        updateSimState(panel, current);
        sleep(500); // simulate delay

        for (char c : input.toCharArray()) {
            State next = null;
            for (Transition t : transitions) {
                if (t.getFrom() == current && t.getSymbols().contains(c)) {
                    next = t.getTo();
                    break;
                }
            }
            if (next == null) return false;
            updateSimState(panel, next);
            sleep(500);
            current = next;
        }

        updateSimState(panel, null); // reset after simulation
        return acceptStates.contains(current);
    }

    //funtion to run updation of current state in another thred to avoid freezing
    private void updateSimState(DFAPanel panel, State state) {
        SwingUtilities.invokeLater(() -> panel.setCurrentSimState(state));
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }


    public void initialize(){
        states = new ArrayList<>();
        transitions = new ArrayList<>();
        acceptStates = new HashSet<>();
        startState = null;
    }

    //overloading init to make a hardcoded dfa model (binary MOD 3=0)
    public void initialize(int x){
        State q0=new State(0, 170, 260);
        q0.setStart(true);
        q0.setAccept(true);
        State q1=new State(1, 400, 260);
        State q2=new State(2, 640, 260);
        this.addState(q0);
        this.addState(q1);
        this.addState(q2);
        Transition t0=new Transition(q0, q0,new HashSet<>(Arrays.asList('0')));
        Transition t1=new Transition(q0, q1,new HashSet<>(Arrays.asList('1')));
        Transition t2=new Transition(q1, q2,new HashSet<>(Arrays.asList('0')));
        Transition t3=new Transition(q2, q2,new HashSet<>(Arrays.asList('1')));
        Transition t4=new Transition(q2, q1,new HashSet<>(Arrays.asList('0')));
        Transition t5=new Transition(q1, q0,new HashSet<>(Arrays.asList('1')));
        this.addTransition(t0);
        this.addTransition(t1);
        this.addTransition(t2);
        this.addTransition(t3);
        this.addTransition(t4);
        this.addTransition(t5);
    }
} 