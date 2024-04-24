import java.util.*;
import java.util.Date;
import java.sql.Time;

import java.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReservationSystem reservationSystem = new ReservationSystem();
            reservationSystem.showMainMenuGUI();
        });
    }
}

class ReservationSystem {
    private List<Restaurant> restaurants;
    private JFrame frame;
    private JTextField customerNameField, phoneNumberField;
    private JTextArea outputArea;
    private Restaurant selectedRestaurant;
    private String selectedDate, selectedTime;
    private int selectedNumberOfPeople;
    private CSVManager csvManager;
    private boolean continueMakingReservations;

    public void showMainMenuGUI() {
        frame = new JFrame("Restaurant Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
    
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        // Add components to panel
        JButton makeReservationButton = new JButton("Make new reservation");
        makeReservationButton.addActionListener(e -> showRestaurantSelectionGUI());
        panel.add(makeReservationButton);
    
        JButton viewReservationButton = new JButton("View reservation");
        viewReservationButton.addActionListener(e -> showViewReservationGUI());
        panel.add(viewReservationButton);
    
        JButton modifyReservationButton = new JButton("Modify reservation");
        modifyReservationButton.addActionListener(e -> showModifyReservationGUI());
        panel.add(modifyReservationButton);
    
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        JButton cancelReservationButton = new JButton("Cancel reservation");
    cancelReservationButton.addActionListener(e -> showCancelReservationGUI());
    panel.add(cancelReservationButton);

    }

    public void showCancelReservationGUI() {
        frame.getContentPane().removeAll();
    
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        // Add components to panel
        panel.add(new JLabel("Enter phone number:"));
        phoneNumberField = new JTextField();
        panel.add(phoneNumberField);
    
        JButton button = new JButton("Cancel Reservation");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phoneNumber = phoneNumberField.getText();
                List<Reservation> reservations = csvManager.readReservations();
                boolean reservationExists = reservations.stream()
                        .anyMatch(reservation -> reservation.getPhoneNumber().equals(phoneNumber));
                if (reservationExists) {
                    csvManager.deleteReservation(phoneNumber);
                    outputArea.setText("Reservation cancelled successfully.");
                } else {
                    outputArea.setText("No reservation found with the entered phone number.");
                }
            }
        });
        panel.add(button);
    
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        panel.add(outputArea);
    
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
    

    public void showViewReservationGUI() {
        frame.getContentPane().removeAll();
    
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        // Add components to panel
        panel.add(new JLabel("Enter phone number:"));
        phoneNumberField = new JTextField();
        panel.add(phoneNumberField);
    
        JButton button = new JButton("View Reservation");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewReservation();
            }
        });
        panel.add(button);
    
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        panel.add(outputArea);
    
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
    
    private void viewReservation() {
        // Get user input
        String phoneNumber = phoneNumberField.getText();
    
        // Read reservations from CSV file
        List<Reservation> reservations = csvManager.readReservations();
    
        // Search for the reservation with the entered phone number
        for (Reservation reservation : reservations) {
            if (reservation.getPhoneNumber().equals(phoneNumber)) {
                // Display the reservation details
                outputArea.setText("Reservation details:\n");
                outputArea.append("Customer: " + reservation.getCustomerName() + "\n");
                outputArea.append("Phone: " + reservation.getPhoneNumber() + "\n");
                outputArea.append("Date: " + reservation.getDate() + "\n");
                outputArea.append("Time: " + reservation.getTime() + "\n");
                outputArea.append("Number of People: " + reservation.getNumberOfPeople() + "\n");
                outputArea.append("Table: " + reservation.getTable().getTableNumber() + "\n");
                return;
            }
        }
    
        // If no reservation is found with the entered phone number
        outputArea.setText("No reservation found with the entered phone number.");
    }
    
    
    public void showModifyReservationGUI() {
        frame.getContentPane().removeAll();
    
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        // Add components to panel
        panel.add(new JLabel("Enter phone number:"));
        phoneNumberField = new JTextField();
        panel.add(phoneNumberField);
    
        JButton button = new JButton("Modify Reservation");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phoneNumber = phoneNumberField.getText();
                List<Reservation> reservations = csvManager.readReservations();
                boolean reservationExists = reservations.stream()
                        .anyMatch(reservation -> reservation.getPhoneNumber().equals(phoneNumber));
                if (reservationExists) {
                    csvManager.deleteReservation(phoneNumber);
                    showRestaurantSelectionGUI();
                } else {
                    outputArea.setText("No reservation found with the entered phone number.");
                }
            }
        });
        panel.add(button);
    
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        panel.add(outputArea);
    
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
       

    public ReservationSystem() {
        this.restaurants = new ArrayList<>();
        // Create and add restaurants to the list
        this.restaurants.add(new Restaurant("Restaurant A"));
        this.restaurants.add(new Restaurant("Restaurant B"));
        this.restaurants.add(new Restaurant("Restaurant C"));
        this.restaurants.add(new Restaurant("Restaurant D"));
        this.csvManager = new CSVManager("reservations.csv");
        this.continueMakingReservations = true;
    }

    public void showRestaurantSelectionGUI() {
        frame = new JFrame("Restaurant Reservation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add components to panel
        panel.add(new JLabel("Select a restaurant:"));

        for (Restaurant restaurant : restaurants) {
            JButton button = new JButton(restaurant.getName());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedRestaurant = restaurant;
                    showDateSelectionGUI();
                }
            });
            panel.add(button);
        }

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void showDateSelectionGUI() {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add components to panel
        panel.add(new JLabel("Select from the available dates:"));

        for (int i = 1; i <= 4; i++) {
            JButton button = new JButton("2024-04-" + (i < 10 ? "0" : "") + i);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedDate = button.getText();
                    showTimeSelectionGUI();
                }
            });
            panel.add(button);
        }

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void showTimeSelectionGUI() {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add components to panel
        panel.add(new JLabel("Select from the available timeslots:"));

        for (int i = 9; i <= 23; i++) {
            JButton button = new JButton((i < 10 ? "0" : "") + i + ":00:00");
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedTime = button.getText();
                    showNumberOfPeopleSelectionGUI();
                }
            });
            panel.add(button);
        }

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void showNumberOfPeopleSelectionGUI() {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add components to panel
        panel.add(new JLabel("Select the number of people:"));

        for (int i = 1; i <= 8; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedNumberOfPeople = Integer.parseInt(button.getText());
                    showReservationInputGUI();
                }
            });
            panel.add(button);
        }

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void showReservationInputGUI() {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Add components to panel
        panel.add(new JLabel("Enter customer name:"));
        customerNameField = new JTextField();
        panel.add(customerNameField);

        panel.add(new JLabel("Enter phone number:"));
        phoneNumberField = new JTextField();
        panel.add(phoneNumberField);

        JButton button = new JButton("Make Reservation");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeReservation();
            }
        });
        panel.add(button);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        panel.add(outputArea);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void makeReservation() {
    // Get user input
    String customerName = customerNameField.getText();
    String phoneNumber = phoneNumberField.getText();

    // If the selected restaurant is found, proceed with making a reservation
    if (selectedRestaurant != null) {
        // Parse the date and time
        Date selectedDate = null;
        try {
            selectedDate = java.sql.Date.valueOf(this.selectedDate);
        } catch (IllegalArgumentException e) {
            outputArea.setText("Invalid date format. Reservation creation failed.");
            return;
        }

        Time selectedTime = null;
        try {
            selectedTime = java.sql.Time.valueOf(this.selectedTime);
        } catch (IllegalArgumentException e) {
            outputArea.setText("Invalid time format. Reservation creation failed.");
            return;
        }

        // Make the reservation
        Reservation reservation = selectedRestaurant.makeReservation(customerName, phoneNumber, selectedDate,
            selectedTime, selectedNumberOfPeople);
        // Display the reservation details
        if (reservation != null) {
            outputArea.setText("Reservation created successfully:\n");
            outputArea.append("Customer: " + reservation.getCustomerName() + "\n");
            outputArea.append("Phone: " + reservation.getPhoneNumber() + "\n");
            outputArea.append("Date: " + reservation.getDate() + "\n");
            outputArea.append("Time: " + reservation.getTime() + "\n");
            outputArea.append("Number of People: " + reservation.getNumberOfPeople() + "\n");
            outputArea.append("Table: " + reservation.getTable().getTableNumber() + "\n");
            outputArea.append("Restaurant: " + selectedRestaurant.getName() + "\n");
            // Save the reservation to CSV file
            csvManager.saveReservation(reservation, selectedRestaurant.getName());
            // Reset the selected restaurant, date, time, and number of people
            selectedRestaurant = null;
            selectedDate = null;
            selectedTime = null;
            selectedNumberOfPeople = 0;
            // Ask the user if they want to make another reservation
            askForAnotherReservation();
        } else {
            outputArea.setText("No available tables for the specified date, time, and number of people. Please try again with different details.");
        showRestaurantSelectionGUI();            
        }
    } else {
        outputArea.setText("Invalid restaurant name. Reservation creation failed.");
    }
}

    public void askForAnotherReservation() {
        // Ask the user if they want to make another reservation
        int response = JOptionPane.showConfirmDialog(frame, "Do you want to make another reservation?",
                "Reservation System", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            // Continue making reservations
            this.continueMakingReservations = true;
            showRestaurantSelectionGUI();
        } else {
            // Exit the program
            System.exit(0);
        }
    }
}

class Restaurant {
    private String name;
    private List<Table> tables;
    private List<Reservation> reservations;

    public Restaurant(String name) {
        this.name = name;
        this.tables = new ArrayList<>();
        this.reservations = new ArrayList<>();
        // Create and add tables to the list with different sizes
        this.tables.add(new Table(1, 2)); // Table 1 can accommodate 2 people
        this.tables.add(new Table(2, 4)); // Table 2 can accommodate 4 people
        this.tables.add(new Table(3, 6)); // Table 3 can accommodate 6 people
        this.tables.add(new Table(4, 8)); // Table 4 can accommodate 8 people
    }

    public String getName() {
        return name;
    }

    public Reservation makeReservation(String customerName, String phoneNumber, Date date, Time time,
            int numberOfPeople) {
        Table availableTable = getAvailableTable(date, time, numberOfPeople);
        if (availableTable != null) {
            Reservation reservation = new Reservation(customerName, phoneNumber, date, time, numberOfPeople,
                    availableTable);
            reservations.add(reservation);
            return reservation;
        }
        return null;
    }

    private Table getAvailableTable(Date date, Time time, int numberOfPeople) {
        for (Table table : tables) {
            if (table.getMaxSize() < numberOfPeople) {
                continue; // Skip this table if it cannot accommodate the number of people
            }
            boolean isTableAvailable = true;
            for (Reservation reservation : reservations) {
                if (reservation.getTable().equals(table) && reservation.getDate().equals(date) && reservation.getTime().equals(time)) {
                    isTableAvailable = false;
                    break;
                }
            }
            if (isTableAvailable) {
                return table;
            }
        }
        return null; // Return null if no suitable table is available
    }
    public List<Reservation> getReservations() {
        return reservations;
    }
}

class Table {
    private int tableNumber;
    private int maxSize; // Maximum number of people this table can accommodate

    public Table(int tableNumber, int maxSize) {
        this.tableNumber = tableNumber;
        this.maxSize = maxSize;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public int getMaxSize() {
        return maxSize;
    }
}

class Reservation {
    private String customerName;
    private String phoneNumber;
    private Date date;
    private Time time;
    private int numberOfPeople;
    private Table table;

    public Reservation(String customerName, String phoneNumber, Date date, Time time, int numberOfPeople,
            Table table) {
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.time = time;
        this.numberOfPeople = numberOfPeople;
        this.table = table;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public Table getTable() {
        return table;
    }
}

class CSVManager {
    private String filePath;

    public CSVManager(String filePath) {
        this.filePath = filePath;
    }

    public void deleteReservation(String phoneNumber) {
        List<Reservation> reservations = readReservations();
        reservations.removeIf(reservation -> reservation.getPhoneNumber().equals(phoneNumber));
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(filePath))) {
            for (Reservation reservation : reservations) {
                writer.println(reservation.getCustomerName() + "," +
                        reservation.getPhoneNumber() + "," +
                        reservation.getDate() + "," +
                        reservation.getTime() + "," +
                        reservation.getNumberOfPeople() + "," +
                        reservation.getTable().getTableNumber());
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void saveReservation(Reservation reservation, String restaurantName) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(filePath, true))) {
            StringBuilder sb = new StringBuilder();
            /*sb.append("CustomerName");
            sb.append(',');
            sb.append("PhoneNumber");
            sb.append(',');
            sb.append("Date");
            sb.append(',');
            sb.append("Time");
            sb.append(',');
            sb.append("NumberOfPeople");
            sb.append(',');
            sb.append("TableNumber");
            sb.append(',');
            sb.append("RestaurantName");
            sb.append('\n');
            */

            sb.append(reservation.getCustomerName());
            sb.append(',');
            sb.append(reservation.getPhoneNumber());
            sb.append(',');
            sb.append(reservation.getDate());
            sb.append(',');
            sb.append(reservation.getTime());
            sb.append(',');
            sb.append(reservation.getNumberOfPeople());
            sb.append(',');
            sb.append(reservation.getTable().getTableNumber());
            sb.append(',');
            sb.append(restaurantName);
            sb.append('\n');

            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
   

    public List<Reservation> readReservations() {
        List<Reservation> reservations = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String customerName = data[0];
                String phoneNumber = data[1];
                Date date = java.sql.Date.valueOf(data[2]);
                Time time = java.sql.Time.valueOf(data[3]);
                int numberOfPeople = Integer.parseInt(data[4]);
                Table table = new Table(Integer.parseInt(data[5]), numberOfPeople);
                Reservation reservation = new Reservation(customerName, phoneNumber, date, time, numberOfPeople, table);
                reservations.add(reservation);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return reservations;
    }
}