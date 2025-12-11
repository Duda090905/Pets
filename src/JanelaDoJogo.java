package src; 

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class JanelaDoJogo extends JFrame {

    // --- LISTA DE PAÍSES (Seus arquivos de imagem devem ter ESSES nomes) ---
    private final String[] nomesPaises = {
        "Brasil", "EUA", "França", "Japão", "Austrália",
        "Alemanha", "Itália", "Canadá", "Argentina", "Espanha",
        "Reino Unido", "China", "México", "Portugal", "Rússia",
        "África do Sul", "Egito", "Índia", "Arábia Saudita", "Tailandia",
        "Uruguai", "Holanda", "Bélgica", "Grécia", "Turquia"
    };

    private int vidasCachorro = 3;
    private int vidasGato = 3;
    private boolean turnoCachorro = true;
    private boolean faseSetup = true;
    private int itensColocados = 0; 
    
    private ArrayList<Pais> mapaCachorro;
    private ArrayList<Pais> mapaGato;
    
    private JPanel painelTabuleiro;
    private JLabel labelStatus;
    private JLabel labelVidas;

    // Ícones internos
    private ImageIcon iconPeixePodre;
    private ImageIcon iconOsso; 

    public JanelaDoJogo() {
        super("Pet Wars: Mapa Mundi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(PetWars.LARGURA, PetWars.ALTURA); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Carrega ícones de ação
        iconPeixePodre = gerarIconePeixeInternal();
        iconOsso = tentarCarregarOuGerarOsso("osso.png");

        inicializarJogo();
        setVisible(true);
    }

    // --- A MÁGICA DAS IMAGENS ACONTECE AQUI ---
    private ImageIcon carregarImagemPais(String nomePais) {
        // O código tenta achar .png, .jpg ou .jpeg
        String[] extensoes = {".png", ".jpg", ".jpeg"};
        
        for (String ext : extensoes) {
            // Tenta achar na pasta src (onde ficam os códigos)
            File arquivo = new File("src/" + nomePais + ext); 
            
            // Se não achar na src, tenta na pasta raiz do projeto
            if (!arquivo.exists()) {
                arquivo = new File(nomePais + ext); 
            }
            
            // Se achou o arquivo, carrega e redimensiona
            if (arquivo.exists()) {
                try {
                    BufferedImage img = ImageIO.read(arquivo);
                    // Redimensiona para ficar bonito no quadrado (140 de largura x 90 de altura)
                    Image imgRedimensionada = img.getScaledInstance(140, 90, Image.SCALE_SMOOTH);
                    return new ImageIcon(imgRedimensionada);
                } catch (Exception e) {
                    System.out.println("Erro ao ler imagem: " + nomePais);
                }
            }
        }
        return null; // Se não achar nada, retorna nulo (e o código usa texto)
    }

    private ImageIcon tentarCarregarOuGerarOsso(String nomeArquivo) {
        try {
            File arquivo = new File("src/" + nomeArquivo);
            if (!arquivo.exists()) arquivo = new File(nomeArquivo);

            if (arquivo.exists()) {
                BufferedImage img = ImageIO.read(arquivo);
                Image imgRedimensionada = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                return new ImageIcon(imgRedimensionada);
            }
        } catch (Exception e) {}
        return gerarIconeOssoInternal(); 
    }

    // --- Ícones de desenho (caso não tenha imagem de osso/peixe) ---
    private ImageIcon gerarIconePeixeInternal() {
        BufferedImage img = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(100, 140, 50)); 
        g2.fillOval(10, 15, 35, 30); 
        g2.dispose(); 
        return new ImageIcon(img);
    }

    private ImageIcon gerarIconeOssoInternal() {
        BufferedImage img = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(new Color(245, 245, 220)); 
        g2.fillRect(15, 20, 30, 20);
        g2.dispose();
        return new ImageIcon(img);
    }

    private void inicializarJogo() {
        mapaCachorro = criarMapa();
        mapaGato = criarMapa();
        
        JPanel painelTopo = new JPanel(new GridLayout(2, 1));
        painelTopo.setBackground(new Color(230, 230, 250)); 
        
        labelStatus = new JLabel("CONFIGURAÇÃO", SwingConstants.CENTER);
        labelStatus.setFont(new Font("Arial", Font.BOLD, 22));
        labelStatus.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
        
        labelVidas = new JLabel("Vidas Cão: 3 | Vidas Gato: 3", SwingConstants.CENTER);
        labelVidas.setFont(new Font("Arial", Font.PLAIN, 18));
        
        painelTopo.add(labelStatus);
        painelTopo.add(labelVidas);
        add(painelTopo, BorderLayout.NORTH);

        painelTabuleiro = new JPanel();
        painelTabuleiro.setLayout(new GridLayout(5, 5, 8, 8));
        painelTabuleiro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painelTabuleiro.setBackground(Color.WHITE);
        add(painelTabuleiro, BorderLayout.CENTER);
        
        JButton btnVoltar = new JButton("VOLTAR AO MENU");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVoltar.setBackground(new Color(200, 50, 50));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.addActionListener(e -> {
            this.dispose();
            new PetWars(); 
        });
        add(btnVoltar, BorderLayout.SOUTH);

        iniciarFaseSetup("Cachorro");
    }

    private ArrayList<Pais> criarMapa() {
        ArrayList<Pais> mapa = new ArrayList<>();
        for (String nome : nomesPaises) {
            mapa.add(new Pais(nome));
        }
        return mapa;
    }

    private void atualizarTabuleiro(ArrayList<Pais> mapaAlvo, boolean modoEspiao) {
        painelTabuleiro.removeAll();
        
        for (Pais p : mapaAlvo) {
            JButton btn = new JButton();
            btn.setFocusPainted(false);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setFont(new Font("Arial", Font.BOLD, 10));
            
            p.botao = btn;

            // Tenta carregar imagem do país
            ImageIcon imagemPais = carregarImagemPais(p.nome);

            if (modoEspiao) {
                if (p.conteudo != Conteudo.VAZIO) {
                    definirEstiloRevelado(btn, p.conteudo, p.nome);
                } else {
                    configurarBotaoNaoRevelado(btn, p.nome, imagemPais);
                }
            } else {
                if (p.revelado) {
                    btn.setEnabled(false);
                    definirEstiloRevelado(btn, p.conteudo, p.nome);
                } else {
                    configurarBotaoNaoRevelado(btn, p.nome, imagemPais);
                }
            }
            
            btn.addActionListener(e -> processarClique(p));
            painelTabuleiro.add(btn);
        }
        painelTabuleiro.revalidate();
        painelTabuleiro.repaint();
    }

    private void configurarBotaoNaoRevelado(JButton btn, String nome, ImageIcon img) {
        if (img != null) {
            btn.setIcon(img);
            btn.setText(""); 
            btn.setBackground(Color.WHITE);
        } else {
            // Fallback se não tiver imagem: mostra texto
            btn.setIcon(null);
            btn.setText("<html><center>" + nome + "</center></html>");
            btn.setBackground(new Color(176, 224, 230));
        }
    }

    private void definirEstiloRevelado(JButton btn, Conteudo conteudo, String nomePais) {
        btn.setIcon(null); 
        switch (conteudo) {
            case ARMADILHA: 
                btn.setIcon(iconPeixePodre);
                btn.setBackground(new Color(200, 100, 100)); 
                btn.setToolTipText("Armadilha em " + nomePais);
                break;
            case DONO: 
                btn.setText("DONO");
                btn.setBackground(Color.BLACK); 
                btn.setForeground(Color.WHITE); 
                break;
            case PREMIO: 
                btn.setIcon(iconOsso);
                btn.setBackground(new Color(255, 215, 0)); 
                btn.setToolTipText("Prêmio em " + nomePais);
                break;
            default: 
                btn.setText("X"); 
                btn.setBackground(Color.LIGHT_GRAY); 
                btn.setForeground(Color.DARK_GRAY);
        }
    }

    private void processarClique(Pais p) {
        if (faseSetup) {
            logicaSetup(p);
        } else {
            logicaAtaque(p);
        }
    }

    private void logicaSetup(Pais p) {
        if (p.conteudo != Conteudo.VAZIO) {
            JOptionPane.showMessageDialog(this, "Já tem algo aqui!");
            return;
        }
        if (itensColocados < 3) {
            p.conteudo = Conteudo.ARMADILHA;
        } else if (itensColocados == 3) {
            p.conteudo = Conteudo.DONO;
        } else if (itensColocados == 4) {
            p.conteudo = Conteudo.PREMIO;
        }
        itensColocados++;
        atualizarTabuleiro(turnoCachorro ? mapaCachorro : mapaGato, true);

        String jogadorAtual = turnoCachorro ? "Cachorro" : "Gato";
        
        if (itensColocados < 3) {
            labelStatus.setText(jogadorAtual + ": Esconda mais " + (3 - itensColocados) + " armadilha(s).");
        } else if (itensColocados == 3) {
            labelStatus.setText("AGORA: Escolha onde o DONO está.");
            JOptionPane.showMessageDialog(this, "Armadilhas postas!\nAgora escolha o país onde o DONO está.");
        } else if (itensColocados == 4) {
            labelStatus.setText("POR FIM: Esconda o PRÊMIO!");
        } else if (itensColocados == 5) {
            JOptionPane.showMessageDialog(this, "Mochila do " + jogadorAtual + " pronta!\nClique OK para passar a vez.");
            if (turnoCachorro) {
                turnoCachorro = false;
                itensColocados = 0;
                iniciarFaseSetup("Gato");
            } else {
                faseSetup = false;
                turnoCachorro = true;
                JOptionPane.showMessageDialog(this, "Passaportes carimbados! \nQUE COMECE A VIAGEM!");
                iniciarTurno();
            }
        }
    }

    private void iniciarFaseSetup(String jogador) {
        labelStatus.setText(jogador + ": Escolha 3 países para Armadilhas");
        labelStatus.setForeground(jogador.equals("Cachorro") ? new Color(139, 69, 19) : Color.BLUE);
        atualizarTabuleiro(jogador.equals("Cachorro") ? mapaCachorro : mapaGato, true);
    }

    private void iniciarTurno() {
        String atacante = turnoCachorro ? "Cachorro" : "Gato";
        String defensor = turnoCachorro ? "Gato" : "Cachorro";
        
        labelStatus.setText("Vez do " + atacante + " procurar no mapa do " + defensor + "!");
        labelStatus.setForeground(turnoCachorro ? new Color(139, 69, 19) : Color.BLUE);

        ArrayList<Pais> alvo = turnoCachorro ? mapaGato : mapaCachorro;
        atualizarTabuleiro(alvo, false);
    }

    private void logicaAtaque(Pais p) {
        p.revelado = true;
        switch (p.conteudo) {
            case VAZIO: 
                JOptionPane.showMessageDialog(this, "Nada em " + p.nome + "..."); 
                break;
            case ARMADILHA:
                JOptionPane.showMessageDialog(this, "Problema na alfândega em " + p.nome + "!\nPerdeu 1 vida.", "Armadilha!", JOptionPane.INFORMATION_MESSAGE, iconPeixePodre);
                if (turnoCachorro) vidasCachorro--; else vidasGato--;
                break;
            case DONO:
                JOptionPane.showMessageDialog(this, "OPS! Encontrou o dono em " + p.nome + "!\nFim da viagem para você!");
                gameOver(!turnoCachorro);
                return;
            case PREMIO:
                ImageIcon iconeVitoria = turnoCachorro ? iconOsso : null; 
                JOptionPane.showMessageDialog(this, "VITÓRIA! Achou o item em " + p.nome + "!", "Achou!", JOptionPane.INFORMATION_MESSAGE, iconeVitoria);
                gameOver(turnoCachorro);
                return;
        }
        atualizarVidas();
        if (vidasCachorro <= 0 || vidasGato <= 0) {
            gameOver(vidasCachorro > 0);
        } else {
            turnoCachorro = !turnoCachorro;
            iniciarTurno();
        }
    }

    private void atualizarVidas() {
        labelVidas.setText("Vidas Cão: " + vidasCachorro + " | Vidas Gato: " + vidasGato);
    }

    private void gameOver(boolean cachorroGanhou) {
        String msg = cachorroGanhou ? "O CACHORRO DOMINOU O MUNDO!" : "O GATO DOMINOU O MUNDO!";
        ImageIcon iconeFinal = cachorroGanhou ? iconOsso : null;
        JOptionPane.showMessageDialog(this, msg, "Fim de Jogo", JOptionPane.INFORMATION_MESSAGE, iconeFinal);
        this.dispose(); 
        new PetWars(); 
    }
}