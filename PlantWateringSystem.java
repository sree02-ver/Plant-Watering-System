package PlantWateringApp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Plant {
    private String name;
    private int soilMoistureLevel;
    private int wateringThreshold;

    public Plant(String name, int wateringThreshold) {
        this.name = name;
        this.soilMoistureLevel = 50; // initial soil moisture level
        this.wateringThreshold = wateringThreshold;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public int getSoilMoistureLevel() {
        return soilMoistureLevel;
    }

    public int getWateringThreshold() {
        return wateringThreshold;
    }

    public boolean needsWatering() {
        return soilMoistureLevel < wateringThreshold;
    }

    public void water() {
        soilMoistureLevel = 100;
    }
}

class WateringSystem {
    private List<Plant> plants;
    private int waterLevel;
    private int wateringInterval;
    private JTextArea logTextArea;

    public WateringSystem(int waterLevel, int wateringInterval, JTextArea logTextArea) {
        this.plants = new ArrayList<>();
        this.waterLevel = waterLevel;
        this.wateringInterval = wateringInterval;
        this.logTextArea = logTextArea;
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void checkPlants() {
        for (Plant plant : plants) {
            if (plant.needsWatering()) {
                waterPlant(plant);
            }
        }
    }

    public void waterPlant(Plant plant) {
        if (waterLevel >= plant.getWateringThreshold()) {
            plant.water();
            waterLevel -= plant.getWateringThreshold();
            logTextArea.append("Watered " + plant.getName() + "\n");
        } else {
            logTextArea.append("Not enough water to water " + plant.getName() + "\n");
        }
    }

    public void simulate() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            checkPlants();
            waterLevel += random.nextInt(10); // simulate random water input
            logTextArea.append("Water level: " + waterLevel + "\n");
            try {
                Thread.sleep(wateringInterval * 1000); // simulate watering interval
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class PlantWateringSystem extends JFrame {
    private JPanel mainPanel;
    private JPanel inputPanel;
    private JPanel plantListPanel;
    private JLabel waterLevelLabel;
    private JLabel plantsLabel;
    private JTextField waterLevelField;
    private JTextField plantsField;
    private JButton addButton;
    private JButton simulateButton;
    private JTextArea logTextArea;
    private WateringSystem wateringSystem;

    public PlantWateringSystem() {
        setTitle("Plant Watering System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        plantListPanel = new JPanel(new BorderLayout());
        plantListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        waterLevelLabel = new JLabel("Water Level:");
        plantsLabel = new JLabel("Plants:");
        waterLevelField = new JTextField(10);
        plantsField = new JTextField(10);
        addButton = new JButton("Add Plant");
        simulateButton = new JButton("Simulate");
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);

        inputPanel.add(waterLevelLabel);
        inputPanel.add(waterLevelField);
        inputPanel.add(plantsLabel);
        inputPanel.add(plantsField);
        inputPanel.add(addButton);
        inputPanel.add(simulateButton);

        JScrollPane scrollPane = new JScrollPane(logTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log"));

        plantListPanel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plantName = plantsField.getText();
                int wateringThreshold;
                try {
                    wateringThreshold = Integer.parseInt(waterLevelField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number for the watering threshold.");
                    return;
                }
                Plant plant = new Plant(plantName, wateringThreshold);
                wateringSystem.addPlant(plant);
                plantsField.setText("");
                waterLevelField.setText("");
                logTextArea.append("Added plant: " + plant.getName() + "\n");
            }
        });

        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logTextArea.setText("");
                wateringSystem.simulate();
            }
        });

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(plantListPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);

        wateringSystem = new WateringSystem(100, 5, logTextArea); // Set initial water level and watering interval
    }
}
