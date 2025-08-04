import javax.swing.*;

import GUI.DFAPanel;
import GUI.InitDialog;
import Model.DFA;

import java.awt.*;
import java.awt.event.*;

public class DFASimulator {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DFA dfa = new DFA();
            DFAPanel panel = new DFAPanel(dfa);

            JFrame frame = new JFrame("DFA Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(panel, BorderLayout.CENTER);

            JPanel controlPanel = new JPanel();

            JButton initButton = new JButton("Initialize DFA");
            initButton.addActionListener(e -> {
                new InitDialog(frame, dfa, panel).setVisible(true);
            });

            JTextField inputField = new JTextField(20);
            JButton simulateBtn = new JButton("Simulate");
            JLabel resultLabel = new JLabel("");
            controlPanel.add(new JLabel("Input: "));
            controlPanel.add(initButton);
            controlPanel.add(inputField);
            controlPanel.add(simulateBtn);
            controlPanel.add(resultLabel);
            frame.add(controlPanel, BorderLayout.SOUTH);

            ActionListener simulateAction = e -> {
                String input = inputField.getText();
                simulateBtn.setEnabled(false);
                inputField.setEnabled(false);
                resultLabel.setText("Simulating...");
                resultLabel.setForeground(Color.BLACK);
                new Thread(() -> {
                    boolean accepted = dfa.simulate(input, panel);
                    SwingUtilities.invokeLater(() -> {
                        resultLabel.setText(accepted ? "Accepted" : "Rejected");
                        resultLabel.setForeground(accepted ? Color.GREEN : Color.RED);
                        simulateBtn.setEnabled(true);
                        inputField.setEnabled(true);
                    });
                }).start();
            };

            inputField.addActionListener(simulateAction);  // Trigger on Enter key
            simulateBtn.addActionListener(simulateAction); // Trigger on button click

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
