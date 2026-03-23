package ro.utcn.parking.gui;

import ro.utcn.parking.exceptions.NumarInmatriculareInvalidException;
import ro.utcn.parking.exceptions.TimpDepasitException;
import ro.utcn.parking.model.LocParcare;
import ro.utcn.parking.model.Masina;
import ro.utcn.parking.service.Abonament;
import ro.utcn.parking.service.ParcareManager;
import ro.utcn.parking.service.Taxabil;
import ro.utcn.parking.service.TaxareStandard;
import ro.utcn.parking.service.ParcareManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class MainApp extends JFrame {


    private ParcareManager sistemParcare;
    private ButonModern[] butoaneLocuri;
    private JLabel statusLabel;
    private JTextField searchField;

    private JTextArea logArea;


    private final Color BG_DARK = new Color(30, 39, 46);
    private final Color SIDEBAR_BG = new Color(72, 84, 96);
    private final Color TEXT_WHITE = new Color(245, 246, 250);
    private final Color GREEN_START = new Color(11, 232, 129);
    private final Color GREEN_END = new Color(5, 196, 107);
    private final Color RED_START = new Color(255, 94, 87);
    private final Color RED_END = new Color(255, 63, 52);

    public MainApp() {

        sistemParcare = new ParcareManager(20);
        sistemParcare.incarcaDatele();


        searchField = new JTextField(10);
        searchField.setBackground(new Color(45, 52, 54));
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { cauta(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { cauta(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { cauta(); }

            private void cauta() {
                String text = searchField.getText().trim().toUpperCase();
                for (int i = 0; i < butoaneLocuri.length; i++) {
                    LocParcare loc = sistemParcare.getLocuri().get(i);
                    butoaneLocuri[i].setBorder(null);

                    if (!text.isEmpty() && loc.isEsteOcupat()) {
                        String nrMasina = loc.getMasinaCurenta().getNumarInmatriculare();
                        if (nrMasina.contains(text)) {
                            butoaneLocuri[i].setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
                        }
                    }
                }
            }
        });
        setTitle("Sistem Parcare");
        setSize(1350, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);


        JPanel header = new JPanel();
        header.setBackground(BG_DARK);
        header.setBorder(new EmptyBorder(20, 0, 20, 0));

        header.add(new JLabel("<html><font color='white'>Caută Nr: </font></html>"));
        header.add(searchField);

        JLabel labelCeas = new JLabel();
        labelCeas.setForeground(Color.ORANGE);
        labelCeas.setFont(new Font("Consolas", Font.BOLD, 18));
        header.add(labelCeas);

        Timer timerCeas = new Timer(1000, e -> {
            labelCeas.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        });
        timerCeas.start();

        JLabel titlu = new JLabel("PARKING IULIUS MALL");
        titlu.setFont(new Font("Verdana", Font.BOLD, 32));
        titlu.setForeground(TEXT_WHITE);
        header.add(titlu);
        add(header, BorderLayout.NORTH);

        JPanel zonaDrag = new JPanel(new GridBagLayout());
        zonaDrag.setBackground(SIDEBAR_BG);
        zonaDrag.setPreferredSize(new Dimension(200, 0));
        zonaDrag.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        CarIcon draggableCar = new CarIcon();
        draggableCar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        draggableCar.setToolTipText("Trage masina pe un loc");

        draggableCar.setTransferHandler(new TransferHandler() {
            @Override
            public int getSourceActions(JComponent c) { return COPY; }
            @Override
            protected Transferable createTransferable(JComponent c) { return new StringSelection("MASINA_NOUA"); }
        });
        draggableCar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JComponent comp = (JComponent) e.getSource();
                comp.getTransferHandler().exportAsDrag(comp, e, TransferHandler.COPY);
            }
        });

        zonaDrag.add(draggableCar);
        add(zonaDrag, BorderLayout.WEST);


        JPanel zonaLog = new JPanel(new BorderLayout());
        zonaLog.setPreferredSize(new Dimension(300, 0));
        zonaLog.setBackground(new Color(20, 20, 20));
        zonaLog.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));

        JLabel logTitle = new JLabel("  JURNAL ACTIVITATE");
        logTitle.setForeground(Color.CYAN);
        logTitle.setFont(new Font("Consolas", Font.BOLD, 14));
        logTitle.setPreferredSize(new Dimension(0, 30));
        zonaLog.add(logTitle, BorderLayout.NORTH);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(20, 20, 20));
        logArea.setForeground(new Color(0, 255, 0));
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setMargin(new Insets(5, 5, 5, 5));

        JScrollPane scrollLog = new JScrollPane(logArea);
        scrollLog.setBorder(null);
        scrollLog.getVerticalScrollBar().setBackground(new Color(20, 20, 20));

        zonaLog.add(scrollLog, BorderLayout.CENTER);

        add(zonaLog, BorderLayout.EAST);


        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BG_DARK);
        mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel panouLocuri = new JPanel(new GridLayout(4, 5, 15, 15));
        panouLocuri.setBackground(BG_DARK);
        butoaneLocuri = new ButonModern[20];

        for (int i = 0; i < 20; i++) {
            int idLoc = i + 1;
            butoaneLocuri[i] = new ButonModern(idLoc);
            butoaneLocuri[i].setTransferHandler(new DropHandler(idLoc));
            int finalI = i;
            butoaneLocuri[i].addActionListener(e -> gestionareIesire(finalI + 1));
            panouLocuri.add(butoaneLocuri[i]);
        }

        mainContainer.add(panouLocuri, BorderLayout.CENTER);
        add(mainContainer, BorderLayout.CENTER);


        JPanel footer = new JPanel();
        footer.setBackground(BG_DARK);
        footer.setBorder(new EmptyBorder(10, 0, 10, 0));
        statusLabel = new JLabel(sistemParcare.genereazaRaport());
        statusLabel.setForeground(Color.CYAN);
        statusLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        footer.add(statusLabel);
        add(footer, BorderLayout.SOUTH);

        sincronizeazaInterfata();
        logEvent("Sistem initializat. Asteptare vehicule...");

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                sistemParcare.salveazaDatele();
            }
        });
    }

    private String genereazaTextChitanta(String nrMasina, String tipPlata, double cost, long minute) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dataAcum = LocalDateTime.now().format(dtf);

        return "==========================================\n" +
                "           IULIUS MALL PARKING            \n" +
                "          --- BON FISCAL ---              \n" +
                "==========================================\n" +
                " DATA: " + dataAcum + "\n" +
                " VEHICUL: " + nrMasina + "\n" +
                " DURATA: " + (minute / 60) + "h " + (minute % 60) + "min\n" +
                " TIP PLATA: " + tipPlata + "\n" +
                "------------------------------------------\n" +
                " TOTAL DE PLATA:    " + String.format("%.2f", cost) + " RON\n" +
                "==========================================\n" +
                "      VA MULTUMIM SI DRUM BUN!            \n" +
                "==========================================\n";
    }

    private void sincronizeazaInterfata() {
        for (int i = 0; i < butoaneLocuri.length; i++) {
            LocParcare loc = sistemParcare.getLocuri().get(i);
            if (loc.isEsteOcupat()) {
                String nr = loc.getMasinaCurenta().getNumarInmatriculare();
                butoaneLocuri[i].setOcupat(nr);
            }
        }
        updateGUI();
    }
    private void logEvent(String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        logArea.append("[" + time + "] " + message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void updateGUI() {
        statusLabel.setText(sistemParcare.genereazaRaport());
    }


    private class DropHandler extends TransferHandler {
        private int idLoc;

        public DropHandler(int idLoc) {
            this.idLoc = idLoc;
        }

        @Override
        public boolean canImport(TransferSupport support) {
            LocParcare loc = sistemParcare.getLocuri().get(idLoc - 1);
            return support.isDataFlavorSupported(DataFlavor.stringFlavor) && !loc.isEsteOcupat();
        }

        @Override
        public boolean importData(TransferSupport support) {
            try {
                String nr = JOptionPane.showInputDialog(MainApp.this, "Introdu numărul de înmatriculare:");

                if (nr == null) return false;

                String nrClean = nr.toUpperCase().trim().replaceAll("\\s+", "");


                Masina.valideazaNumar(nrClean);


                if (sistemParcare.existaMasina(nrClean)) {
                    JOptionPane.showMessageDialog(MainApp.this, "Vehiculul este deja parcat!", "Eroare", JOptionPane.ERROR_MESSAGE);
                    return false;
                }


                LocParcare loc = sistemParcare.getLocuri().get(idLoc - 1);
                Masina m = new Masina(nrClean, "Auto");
                loc.ocupeazaLoc(m);


                butoaneLocuri[idLoc - 1].setOcupat(nrClean);

                updateGUI();
                logEvent("INTRARE: " + nrClean + " la locul " + idLoc);
                return true;

            } catch (NumarInmatriculareInvalidException ex) {

                JOptionPane.showMessageDialog(MainApp.this, ex.getMessage(), "Format Incorect", JOptionPane.WARNING_MESSAGE);
                logEvent("(!) Format invalid încercat: " + ex.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    private void gestionareIesire(int idLoc) {
        LocParcare loc = sistemParcare.getLocuri().get(idLoc - 1);
        if (!loc.isEsteOcupat()) return;

        ButonModern btn = butoaneLocuri[idLoc - 1];
        String nrMasina = loc.getMasinaCurenta().getNumarInmatriculare();

        Object[] options = {"Plata Cash/Card", "Sunt Abonat"};
        int tipPlata = JOptionPane.showOptionDialog(this,
                "Alegeti metoda de validare pentru iesire:",
                "Check-out: " + nrMasina,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (tipPlata == -1) return;

        try {
            Taxabil strategiePlata;
            String tipLog = (tipPlata == 1) ? "ABONAMENT" : "STANDARD";
            strategiePlata = (tipPlata == 1) ? new Abonament("AB" + System.currentTimeMillis()) : new TaxareStandard();


            LocalDateTime oraIntrareLocala = loc.getOraIntrare();
            double cost = sistemParcare.iesireMasina(nrMasina, strategiePlata);


            if (oraIntrareLocala == null) {
                oraIntrareLocala = LocalDateTime.now().minusMinutes(60);
                logEvent("(!) ATENȚIE: Ora intrare lipsă pentru " + nrMasina + ". Folosesc default.");
            }
            long minute = java.time.Duration.between(oraIntrareLocala, LocalDateTime.now()).toMinutes();
            String textChitanta = genereazaTextChitanta(nrMasina, tipLog, cost, minute);


            JTextArea area = new JTextArea(textChitanta);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            area.setEditable(false);
            area.setBackground(new Color(245, 245, 245));

            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Chitanță Eliberată", JOptionPane.INFORMATION_MESSAGE);


            try (PrintWriter out = new PrintWriter("chitanta_" + nrMasina + ".txt")) {
                out.println(textChitanta);
            }

            logEvent("IESIRE: " + nrMasina + " | Chitanta salvata.");
            btn.setLiber();
            updateGUI();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Eroare: " + ex.getMessage());
            if (ex instanceof TimpDepasitException) {
                try { loc.elibereazaLoc(); } catch (Exception ignored) {}
                btn.setLiber();
                updateGUI();
                logEvent("ALERTA TIMP: " + nrMasina + " a depasit limita!");
            }
        }
    }


    class CarIcon extends JPanel {
        public CarIcon() { setPreferredSize(new Dimension(120, 80)); setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(231, 76, 60));
            g2.fillRoundRect(10, 30, 100, 35, 10, 10);
            g2.setColor(new Color(44, 62, 80));
            g2.fillRoundRect(25, 10, 70, 25, 5, 5);
            g2.setColor(Color.BLACK);
            g2.fillOval(20, 55, 22, 22); g2.fillOval(78, 55, 22, 22);
            g2.setColor(Color.GRAY);
            g2.fillOval(26, 61, 10, 10); g2.fillOval(84, 61, 10, 10);
        }
    }


    class ButonModern extends JButton {
        private int id; private boolean isOcupat = false; private boolean isHover = false;
        public ButonModern(int id) {
            this.id = id; setContentAreaFilled(false); setFocusPainted(false); setBorderPainted(false);
            setText("LOC " + id); setFont(new Font("Verdana", Font.BOLD, 14)); setForeground(Color.WHITE);
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { isHover = true; repaint(); }
                public void mouseExited(MouseEvent e) { isHover = false; repaint(); }
            });
        }
        public void setOcupat(String nr) {
            this.isOcupat = true; setText("<html><center>OCUPAT<br><font size='4'>" + nr + "</font></center></html>"); repaint(); }
        public void setLiber() { this.isOcupat = false; setText("LOC " + id); repaint(); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 50)); g2.fillRoundRect(5, 5, getWidth()-10, getHeight()-10, 20, 20);
            Color s = isOcupat ? RED_START : GREEN_START; Color e = isOcupat ? RED_END : GREEN_END;
            if(isHover) { s = s.brighter(); e = e.brighter(); }
            g2.setPaint(new GradientPaint(0, 0, s, 0, getHeight(), e));
            g2.fillRoundRect(0, 0, getWidth()-4, getHeight()-4, 25, 25);
            g2.setPaint(new Color(255, 255, 255, 30)); g2.fillRoundRect(0, 0, getWidth()-4, getHeight()/2, 25, 25);
            g2.dispose(); super.paintComponent(g);
        }
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new MainApp().setVisible(true)); }
}