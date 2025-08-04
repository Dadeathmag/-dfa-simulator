package GUI;
import javax.swing.*;

import Model.DFA;

import java.awt.*;

public class InitDialog extends JDialog {
    public InitDialog(JFrame parent, DFA dfa, DFAPanel panel) {
        super(parent, "Initialize DFA", true);
        setLayout(new BorderLayout(10, 10));

        // Top label
        JLabel label = new JLabel("DFA model which only accepts binary numbers divisble by 3");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(label, BorderLayout.NORTH);

        // Buttons
        JButton hardcodedBtn = new JButton("Use Hardcoded DFA");
        JButton manualBtn = new JButton("Create DFA Manually");

        
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelButtons.add(hardcodedBtn);
        panelButtons.add(manualBtn);
        add(panelButtons, BorderLayout.CENTER);

        // Button actions
        hardcodedBtn.addActionListener(e -> {
            dfa.getStates().clear();
            dfa.getTransitions().clear();
            dfa.initialize(1);
            panel.stateIdCounter=3;
            panel.repaint();
            dispose();
        });

        manualBtn.addActionListener(e -> {
            dfa.getStates().clear();
            dfa.getTransitions().clear();
            dfa.initialize();
            panel.stateIdCounter=0;
            panel.repaint();
            dispose();
        });

        setSize(400, 150);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
