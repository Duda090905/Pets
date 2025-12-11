package src;

import javax.swing.JButton;

public class Pais {
    String nome;
    Conteudo conteudo;
    boolean revelado;
    JButton botao;

    public Pais(String nome) {
        this.nome = nome;
        this.conteudo = Conteudo.VAZIO;
        this.revelado = false;
    }
}