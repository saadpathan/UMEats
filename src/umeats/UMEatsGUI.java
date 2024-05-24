package umeats;

/**
 *
 * @author Hp
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class UMEatsGUI extends JFrame {

    private static final Map<String, Map<String, Integer>> meals = new HashMap<>();
    private static int servedMeals = 0;
    private JTextField quantityField;
    private JComboBox<String> areaCombo, blockCombo;
    private JTextArea statsArea;
    private final String[] areas = {"KK1", "KK2", "KK3", "KK4", "KK5", "KK6", "KK7", "KK8", "KK9", "KK10", "KK11", "KK12", "KK13"};
    private final String[] blocks = {"Block A", "Block B", "Block C", "Block D", "Block E"};

    public UMEatsGUI() {
        super("UMEats Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2));

        areaCombo = new JComboBox<>(areas);
        blockCombo = new JComboBox<>(blocks);

        JButton addButton = new JButton("Add Meal");
        JButton takeButton = new JButton("Take Meal");

        quantityField = new JTextField();
        statsArea = new JTextArea(10, 40);
        statsArea.setEditable(false);

        controlPanel.add(new JLabel("Select Area:"));
        controlPanel.add(areaCombo);
        controlPanel.add(new JLabel("Select Block:"));
        controlPanel.add(blockCombo);
        controlPanel.add(new JLabel("Quantity:"));
        controlPanel.add(quantityField);
        controlPanel.add(addButton);
        controlPanel.add(takeButton);

        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(statsArea), BorderLayout.CENTER);

        addButton.addActionListener(e -> addMeal());
        takeButton.addActionListener(e -> takeMeal());

        setVisible(true);
    }

    private void addMeal() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity < 1) {
                throw new NumberFormatException("Quantity must be positive");
            }
            updateMealCount(areaCombo.getSelectedItem().toString(), blockCombo.getSelectedItem().toString(), quantity);
            displayStatistics("Meal added successfully.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void takeMeal() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity < 1 || quantity > 4) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please select between 1 and 4.", "Quantity Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String area = areaCombo.getSelectedItem().toString();
            String block = blockCombo.getSelectedItem().toString();
            int availableMeals = meals.getOrDefault(area, new HashMap<>()).getOrDefault(block, 0);
            if (availableMeals < quantity) {
                JOptionPane.showMessageDialog(this, "Not enough meals available in " + block + ".", "Availability Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            updateMealCount(area, block, -quantity);
            servedMeals += quantity;
            displayStatistics("Your meal is ready.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMealCount(String area, String block, int quantity) {
        meals.putIfAbsent(area, new HashMap<>());
        meals.get(area).merge(block, quantity, Integer::sum);
    }

    private void displayStatistics(String message) {
        statsArea.setText(message + "\n");
        statsArea.append("Available Meals: " + meals.values().stream().mapToInt(a -> a.values().stream().mapToInt(Integer::intValue).sum()).sum() + "\n");
        statsArea.append("Served Meals: " + servedMeals + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UMEatsGUI::new);
    }
}
