package GUI;
import javax.swing.*;

import Model.DFA;
import Model.State;
import Model.Transition;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DFAPanel extends JPanel {
    private DFA dfa;
    private State dragState = null;
    private int dragOffsetX, dragOffsetY;
    private State transitionFrom = null;
    private int mouseX, mouseY;
    public int stateIdCounter = 0;
    private State currentSimState = null;


    //JLabel coords = new JLabel("");

    public DFAPanel(DFA dfa) {
        this.dfa = dfa;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));

        //this.add(coords);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                State s = getStateAt(e.getX(), e.getY());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (s == null) {
                        // Add new state
                        State newState = new State(stateIdCounter++, e.getX(), e.getY());
                        dfa.addState(newState);
                        repaint();
                    } else {
                        // Select state for dragging or transition
                        if (e.getClickCount() == 2) {
                            // Double click: start drawing transition
                            transitionFrom = s;
                        } else {
                            dragState = s;
                            dragOffsetX = e.getX() - s.getX();
                            dragOffsetY = e.getY() - s.getY();
                        }
                    }
                } else if (SwingUtilities.isRightMouseButton(e) && s != null) {
                    // Right click: show popup to set start/accept
                    showStatePopup(s, e.getX(), e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (dragState != null) {
                    dragState = null;
                }
                if (transitionFrom != null) {
                    State to = getStateAt(e.getX(), e.getY());
                    if (to != null) { // Allow self-loop
                        String symbol = JOptionPane.showInputDialog(DFAPanel.this, "Transition symbol(s):");
                        if (symbol != null && symbol.length() > 0) {
                            Set<Character> symbols = new HashSet<>();
                            for (char c : symbol.toCharArray()) symbols.add(c);
                            dfa.addTransition(new Transition(transitionFrom, to, symbols));
                        }
                    }
                    transitionFrom = null;
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (dragState != null) {
                    dragState.setX(e.getX() - dragOffsetX);
                    dragState.setY(e.getY() - dragOffsetY);
                    repaint();
                }
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }

            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }
        });
    }

    private State getStateAt(int x, int y) {
        for (State s : dfa.getStates()) {
            int dx = s.getX() - x;
            int dy = s.getY() - y;
            if (dx * dx + dy * dy <= 400) return s; // radius 20 grabable point from center
        }
        return null;
    }

    private void showStatePopup(State s, int x, int y) {
        JPopupMenu menu = new JPopupMenu();
        
        JMenuItem setStart = new JMenuItem(s.isStart() ? "Unset Start State" : "Set as Start State");
        setStart.addActionListener(e -> {
            dfa.setStartState(s,!s.isStart());
            repaint();
        });
        menu.add(setStart);

        JMenuItem toggleAccept = new JMenuItem(s.isAccept() ? "Unset Accept State" : "Set as Accept State");
        toggleAccept.addActionListener(e -> {
            dfa.setAcceptState(s, !s.isAccept());
            repaint();
        });
        menu.add(toggleAccept);

        JMenuItem remove = new JMenuItem("Remove State");
        remove.addActionListener(e -> {
            dfa.removeState(s);
            stateIdCounter--;
            repaint();
        });
        menu.add(remove);

        menu.show(this, x, y);
    }

    public void setCurrentSimState(State state) {
        this.currentSimState = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.DARK_GRAY);
        g2.drawString("X: " + mouseX + " Y: " + mouseY, 10, 15);
        //coords.setText("X:"+mouseX+" Y:"+mouseY);

        // Draw transitions
        for (Transition t : dfa.getTransitions()) {
            drawTransition(g2, t);
        }

        // Draw states
        for (State s : dfa.getStates()) {
            drawState(g2, s);
        }

        // Draw transition in progress
        if (transitionFrom != null) {
            drawTransitionInProgress(g2);
        }

        g2.dispose();
    }

    private void drawState(Graphics2D g2, State s) {
        int x = s.getX();
        int y = s.getY();

        if (s == currentSimState) {
            g2.setColor(Color.YELLOW);
        } else {
            g2.setColor(Color.WHITE);
        }
        g2.fillOval(x - 20, y - 20, 40, 40);
        g2.setColor(Color.BLACK);
        g2.drawOval(x - 20, y - 20, 40, 40);


        if (s.isAccept()) {
            g2.drawOval(x - 16, y - 16, 32, 32);
        }

        if (s.isStart()) {
            g2.setColor(Color.GREEN);
            int arrowX = x - 40;
            int arrowY = y;
            g2.drawLine(arrowX, arrowY, x - 20, y);
            g2.fillPolygon(
                new int[]{x - 20, x - 25, x - 25},
                new int[]{y, y - 5, y + 5},
                3
            );
        }

        g2.setColor(Color.BLACK);
        g2.drawString("q" + s.getId(), x - 8, y + 5);
    }


    private static final int STATE_RADIUS = 20;

    private void drawTransition(Graphics2D g2, Transition t) {
        State from = t.getFrom();
        State to = t.getTo();
        int x1 = from.getX(), y1 = from.getY();
        int x2 = to.getX(), y2 = to.getY();
        String symbolsStr = t.getSymbols().toString().replaceAll("[\\[\\]]", "");

        if (from == to) {
            drawSelfLoop(g2, from, symbolsStr);
        } else {
            g2.setColor(Color.BLACK);
            drawArrow(g2, x1, y1, x2, y2, symbolsStr);
        }
    }


    private void drawTransitionInProgress(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        g2.drawLine(transitionFrom.getX(), transitionFrom.getY(), mouseX, mouseY);
    }



    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2, String label) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double dx = x2 - x1;
        double dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy);
        double ux = dx / len;
        double uy = dy / len;

        // Perpendicular unit vector for spacing
        double px = -uy;
        double py = ux;

        // Stable hash-based offset
        int hash = (x1 * 31 + y1 * 37 + x2 * 41 + y2 * 43 + label.hashCode()) & 0x7FFFFFFF;
        int offset = (hash % 30) - 15;  // [-15, 15] pixel offset

        // Offset line start and end
        int startX = (int) (x1 + ux * STATE_RADIUS + px * offset);
        int startY = (int) (y1 + uy * STATE_RADIUS + py * offset);
        int endX = (int) (x2 - ux * STATE_RADIUS + px * offset);
        int endY = (int) (y2 - uy * STATE_RADIUS + py * offset);

        // Draw the line
        g2.setColor(Color.BLACK);
        g2.drawLine(startX, startY, endX, endY);

        // Arrowhead
        double angle = Math.atan2(endY - startY, endX - startX);
        int arrowLen = 10;
        int[] xPoints = {
            endX,
            endX - (int)(arrowLen * Math.cos(angle - Math.PI / 6)),
            endX - (int)(arrowLen * Math.cos(angle + Math.PI / 6))
        };
        int[] yPoints = {
            endY,
            endY - (int)(arrowLen * Math.sin(angle - Math.PI / 6)),
            endY - (int)(arrowLen * Math.sin(angle + Math.PI / 6))
        };
        g2.fillPolygon(xPoints, yPoints, 3);

        // Draw label
        int labelX = (startX + endX) / 2;
        int labelY = (startY + endY) / 2;
        g2.setColor(Color.BLUE);
        g2.drawString(label, labelX + 5, labelY - 5);

        g2.dispose();
    }


    private void drawSelfLoop(Graphics g, State s, String symbols) {
        int x = s.getX();
        int y = s.getY();

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));

        // Arc centered horizontally above the state
        int arcW = 40, arcH = 40;
        int arcX = x - arcW / 2;
        int arcY = y - arcH - 10; // gap above state
        g2.drawArc(arcX, arcY, arcW, arcH, 0, 270);

        // Arrowhead at end of arc
        int arrowX = x + arcW/2  ;
        int arrowY = y -25; // Slightly down from arc end
        g2.fillPolygon(
            new int[]{arrowX, arrowX - 7, arrowX + 4},    // X-coordinates
            new int[]{arrowY, arrowY - 6, arrowY - 10},  // Y-coordinates
            3
        );

        // Symbols label near top-right of the arc
        g2.setColor(Color.BLUE);
        g2.drawString(symbols, x + 15, y - arcH - 5);

        g2.dispose();
    }


} 
