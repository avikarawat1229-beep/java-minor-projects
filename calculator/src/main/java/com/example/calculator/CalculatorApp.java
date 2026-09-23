package com.example.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorApp extends JFrame implements ActionListener {

    private JTextField firstNumber;
    private JTextField secondNumber;
    private JLabel resultLabel;

    private JButton addButton;
    private JButton subtractButton;
    private JButton multiplyButton;
    private JButton divideButton;
    private JButton clearButton;
    private JButton exitButton;

    public CalculatorApp() {

        setTitle("My Calculator");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2, 10, 10));

        panel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel firstLabel = new JLabel("First Number:");
        JLabel secondLabel = new JLabel("Second Number:");

        firstNumber = new JTextField();
        secondNumber = new JTextField();

        addButton = new JButton("ADD");
        subtractButton = new JButton("SUBTRACT");
        multiplyButton = new JButton("MULTIPLY");
        divideButton = new JButton("DIVIDE");
        clearButton = new JButton("CLEAR");
        exitButton = new JButton("EXIT");

        resultLabel = new JLabel("Result: ");

        addButton.addActionListener(this);
        subtractButton.addActionListener(this);
        multiplyButton.addActionListener(this);
        divideButton.addActionListener(this);
        clearButton.addActionListener(this);
        exitButton.addActionListener(this);

        panel.add(firstLabel);
        panel.add(firstNumber);

        panel.add(secondLabel);
        panel.add(secondNumber);

        panel.add(addButton);
        panel.add(subtractButton);

        panel.add(multiplyButton);
        panel.add(divideButton);

        panel.add(clearButton);
        panel.add(exitButton);

        panel.add(new JLabel(""));
        panel.add(resultLabel);

        add(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == exitButton) {
            System.exit(0);
        }

        if (e.getSource() == clearButton) {
            firstNumber.setText("");
            secondNumber.setText("");
            resultLabel.setText("Result: ");
            return;
        }

        try {

            double num1 = Double.parseDouble(firstNumber.getText());
            double num2 = Double.parseDouble(secondNumber.getText());

            double result = 0;

            if (e.getSource() == addButton) {
                result = num1 + num2;
            }
            else if (e.getSource() == subtractButton) {
                result = num1 - num2;
            }
            else if (e.getSource() == multiplyButton) {
                result = num1 * num2;
            }
            else if (e.getSource() == divideButton) {

                if (num2 == 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Cannot divide by zero."
                    );
                    return;
                }

                result = num1 / num2;
            }

            resultLabel.setText("Result: " + result);

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numbers."
            );
        }
    }

    public static void main(String[] args) {
        new CalculatorApp();
    }
}