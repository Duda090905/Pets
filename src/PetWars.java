package src; // <--- ESSA LINHA É OBRIGATÓRIA NO SEU CASO

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PetWars extends JFrame {

    public static final int LARGURA = 1100;
    public static final int ALTURA = 850;

    public PetWars() {
        super("Pet Wars - Edição Mundial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(LARGURA, ALTURA); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelFundo = new JPanel();
        painelFundo.setBackground(new Color(30, 144, 255)); 
        painelFundo.setLayout(new BoxLayout(painelFundo, BoxLayout.Y_AXIS));
        painelFundo.setBorder(new EmptyBorder(100, 50, 50, 50)); 

        JLabel titulo = new JLabel("PET WARS: MUNDIAL");
        titulo.setFont(new Font("Impact", Font.BOLD, 80)); 
        titulo.setForeground(Color.YELLOW);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Cão vs Gato: A Corrida pelo Mundo");
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 24));
        subtitulo.setForeground(Color.WHITE);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnIniciar = criarBotaoMenu("VIAJAR (INICIAR)");
        JButton btnSair = criarBotaoMenu("FICAR EM CASA (SAIR)");

        btnIniciar.addActionListener(e -> {
            this.dispose(); 
            new JanelaDoJogo(); 
        });

        btnSair.addActionListener(e -> System.exit(0));

        painelFundo.add(titulo);
        painelFundo.add(subtitulo);
        painelFundo.add(Box.createRigidArea(new Dimension(0, 100))); 
        painelFundo.add(btnIniciar);
        painelFundo.add(Box.createRigidArea(new Dimension(0, 30))); 
        painelFundo.add(btnSair);

        add(painelFundo);
        setVisible(true);
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 24));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(350, 70));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.DARK_GRAY);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new PetWars());
    }
}