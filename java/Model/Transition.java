package Model;
import java.util.*;

public class Transition {
    private State from;
    private State to;
    private Set<Character> symbols;

    public Transition(State from, State to, Set<Character> symbols) {
        this.from = from;
        this.to = to;
        this.symbols = symbols;
    }

    public State getFrom() { return from; }
    public State getTo() { return to; }
    public Set<Character> getSymbols() { return symbols; }
    
} 