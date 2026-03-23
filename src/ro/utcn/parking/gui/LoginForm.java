package ro.utcn.parking.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {


    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;


    private final Color BG_DARK = new Color(30, 39, 46);
    private final Color TEXT_WHITE = new Color(245, 246, 250);
    private final Color ACCENT_BLUE = new Color(52, 152, 219);

    public LoginForm() {
        setTitle("Login - Parking System");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());


        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(BG_DARK);
        headerPanel.setBorder(new EmptyBorder(30, 0, 20, 0));

        JLabel title = new JLabel("AUTHENTICATION");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setForeground(ACCENT_BLUE);
        headerPanel.add(title);

        add(headerPanel, BorderLayout.NORTH);


        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(BG_DARK);
        formPanel.setBorder(new EmptyBorder(20, 50, 20, 50));


        JLabel lblUser = new JLabel("Username");
        lblUser.setForeground(Color.GRAY);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);


        txtUser = new JTextField();
        styleInput(txtUser);


        JLabel lblPass = new JLabel("Password");
        lblPass.setForeground(Color.GRAY);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);


        txtPass = new JPasswordField();
        styleInput(txtPass);


        formPanel.add(lblUser);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtUser);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(lblPass);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(txtPass);

        add(formPanel, BorderLayout.CENTER);


        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(BG_DARK);
        footerPanel.setBorder(new EmptyBorder(0, 0, 40, 0));

        btnLogin = new JButton("Autentificare");
        btnLogin.setBackground(ACCENT_BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setPreferredSize(new Dimension(200, 40));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));


        btnLogin.addActionListener((ActionEvent e) -> {
            autentificare();
        });


        getRootPane().setDefaultButton(btnLogin);

        footerPanel.add(btnLogin);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void styleInput(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setBackground(new Color(60, 70, 80)); // Gri închis
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void autentificare() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());


        if (user.equalsIgnoreCase("admin") && pass.equals("1234")) {

            this.dispose();


            SwingUtilities.invokeLater(() -> new MainApp().setVisible(true));

        } else {
            JOptionPane.showMessageDialog(this, "Date incorecte!\nÎncearcă: admin / 1234", "Acces Refuzat", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}