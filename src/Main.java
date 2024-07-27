import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Main {
    private FileWriter csvWriter;
    private JFrame frame;
    private JTextArea viewOrdersTextArea;
    private boolean orderPlaced = false;

    public Main() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use a JPanel as the content pane for the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image image = new ImageIcon("D:\\1.jpg").getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);

        Label l = new Label("                                          ICS");

        Button OrderButton = new Button("Order Page");
        Button ViewOrdersButton = new Button("View Orders Page");
        OrderButton.setBounds(70, 290, 150, 30);
        ViewOrdersButton.setBounds(230, 290, 150, 30);

        OrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrderPage();
            }
        });

        ViewOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (orderPlaced) {
                    ViewOrdersPage();
                } else {
                    JOptionPane.showMessageDialog(frame, "No orders placed yet. Place an order first.");
                }
            }
        });

        backgroundPanel.add(l);
        backgroundPanel.add(OrderButton);
        backgroundPanel.add(ViewOrdersButton);

        frame.setContentPane(backgroundPanel);
        frame.setSize(400, 400);
        frame.setTitle("Transport Management System");
        frame.setLayout(null);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    // Close the CSV writer when the window is closing
                    if (csvWriter != null) {
                        csvWriter.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
    }

    private void OrderPage() {
        frame.getContentPane().removeAll();
        Label l1 = new Label("Customer Name                     : ");
        TextField t1 = new TextField();
        Label l2 = new Label("Phone No         : ");
        TextField t2 = new TextField();
        Label l3 = new Label("Address     : ");
        TextField fromAddress = new TextField();
        Label l4 = new Label("From Date       : ");
        TextField toAddress = new TextField();
        Label l5 = new Label("Vehicle Type : ");
        Choice c1 = new Choice();
        Label l6 = new Label("To Date        :");
        TextField t6 = new TextField();
        Button b = new Button("Place Order");
        Button backButton = new Button("Back");

        try {
            csvWriter = new FileWriter("OrderDetails.csv", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data = "Details :" + ("\nName : " + t1.getText()) + ("\nPhone No: " + t2.getText())
                        + ("\nFrom Address : " + fromAddress.getText()) + ("\nTo Address : " + toAddress.getText())
                        + ("\nDate : " + t6.getText()) + ("\nVehicle Type : " + c1.getSelectedItem());

                JOptionPane.showMessageDialog(frame, data);

                // Add order details to CSV
                addOrderToCSV(t1.getText(), t2.getText(), fromAddress.getText(), toAddress.getText(), t6.getText(), c1.getSelectedItem());

                // Optional: Show confirmation dialog
                JOptionPane.showMessageDialog(frame, "Order details added to CSV!");

                // Set orderPlaced to true
                orderPlaced = true;

                // Go back to the main page
                MainPage();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Go back to the main page
                MainPage();
            }
        });

        c1.add("Bus");
        c1.add("Car");
        c1.add("Two Wheeler");
        c1.add("Truck");

        l1.setBounds(20, 100, 120, 30);
        t1.setBounds(140, 105, 190, 20);

        l2.setBounds(20, 130, 120, 30);
        t2.setBounds(140, 135, 190, 20);

        l3.setBounds(20, 160, 120, 30);
        fromAddress.setBounds(140, 165, 190, 20);

        l4.setBounds(20, 190, 120, 30);
        toAddress.setBounds(140, 195, 190, 20);

        l6.setBounds(20, 220, 120, 30);
        t6.setBounds(140, 225, 190, 20);

        l5.setBounds(20, 250, 120, 30);
        c1.setBounds(140, 255, 105, 30);

        b.setBounds(70, 290, 110, 30);
        backButton.setBounds(200, 290, 110, 30);

        frame.getContentPane().add(l1);
        frame.getContentPane().add(t1);
        frame.getContentPane().add(l2);
        frame.getContentPane().add(t2);
        frame.getContentPane().add(l3);
        frame.getContentPane().add(fromAddress);
        frame.getContentPane().add(l4);
        frame.getContentPane().add(toAddress);
        frame.getContentPane().add(l6);
        frame.getContentPane().add(t6);
        frame.getContentPane().add(l5);
        frame.getContentPane().add(c1);
        frame.getContentPane().add(b);
        frame.getContentPane().add(backButton);

        frame.revalidate();
        frame.repaint();
    }

    private void ViewOrdersPage() {
        frame.getContentPane().removeAll();
        Button exportToExcelButton = new Button("Export to Excel");
        Button deleteAllButton = new Button("Delete All");
        Button backButton = new Button("Back");

        exportToExcelButton.setBounds(20, 260, 110, 30);
        deleteAllButton.setBounds(150, 260, 110, 30);
        backButton.setBounds(280, 260, 110, 30);

        viewOrdersTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(viewOrdersTextArea);
        scrollPane.setBounds(20, 40, 300, 150);

        exportToExcelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Call a method to export to Excel or CSV only if orders are available
                if (orderPlaced) {
                    exportToCSV();
                } else {
                    JOptionPane.showMessageDialog(frame, "No orders to export. Place an order first.");
                }
            }
        });

        deleteAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Delete all orders from the JTextArea
                viewOrdersTextArea.setText("");

                // Clear the CSV file
                clearCSVFile("OrderDetails.csv");

                // Update orderPlaced to false
                orderPlaced = false;
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Go back to the main page
                MainPage();
            }
        });

        // Display the orders in JTextArea
        displayOrders();

        frame.getContentPane().add(scrollPane);
        frame.getContentPane().add(exportToExcelButton);
        frame.getContentPane().add(deleteAllButton);
        frame.getContentPane().add(backButton);

        frame.revalidate();
        frame.repaint();
    }

    private void MainPage() {
        frame.getContentPane().removeAll();
        Label l = new Label("                                          ICS");

        Button switchToOrderButton = new Button("Order Page");
        Button switchToViewOrdersButton = new Button("View Orders Page");
        switchToOrderButton.setBounds(70, 290, 150, 30);
        switchToViewOrdersButton.setBounds(230, 290, 150, 30);

        switchToOrderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                OrderPage();
            }
        });

        switchToViewOrdersButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (orderPlaced) {
                    ViewOrdersPage();
                } else {
                    JOptionPane.showMessageDialog(frame, "No orders placed yet. Place an order first.");
                }
            }
        });

        frame.getContentPane().add(l);
        frame.getContentPane().add(switchToOrderButton);
        frame.getContentPane().add(switchToViewOrdersButton);

        frame.revalidate();
        frame.repaint();
    }

    private void addOrderToCSV(String name, String phone, String fromAddress, String toAddress, String date, String receiverPlace) {
        try {
            csvWriter.append(name)
                    .append(",")
                    .append(phone)
                    .append(",")
                    .append(fromAddress)
                    .append(",")
                    .append(toAddress)
                    .append(",")
                    .append(date)
                    .append(",")
                    .append(receiverPlace)
                    .append("\n");
            csvWriter.flush();
            orderPlaced = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exportToCSV() {
        try {
            FileWriter exportWriter = new FileWriter("ExportedOrders.csv");
            List<String> orders = readOrdersFromFile("OrderDetails.csv");
            for (String order : orders) {
                exportWriter.write(order + "\n");
            }
            exportWriter.close();
            JOptionPane.showMessageDialog(frame, "Data exported to CSV/Excel successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayOrders() {
        List<String> orders = readOrdersFromFile("OrderDetails.csv");
        for (String order : orders) {
            viewOrdersTextArea.append(order + "\n");
        }
    }

    private List<String> readOrdersFromFile(String fileName) {
        List<String> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                orders.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private void clearCSVFile(String fileName) {
        try {
            FileWriter clearWriter = new FileWriter(fileName);
            clearWriter.write(""); // Overwrite the file with an empty string
            clearWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
