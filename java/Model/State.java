package Model;

public class State {
    private int id;                                 //state id
    private int x, y;                               //coordinates
    private boolean isStart;
    private boolean isAccept;

    //constructor
    public State(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.isStart = false;
        this.isAccept = false;
    }


    public int getId() { return id; }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public boolean isStart() { return isStart; }
    public boolean isAccept() { return isAccept; }

    public void setStart(boolean isStart) { this.isStart = isStart; }
    public void setAccept(boolean isAccept) { this.isAccept = isAccept; }
} 