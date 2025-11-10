import java.net.Socket;

/**
 * Classe que representa um jogador conectado ao servidor
 */
public class Jogador {
    private String nome;
    private Socket socket;
    private int nivelAtual;
    private double premioAtual;
    private boolean eliminado;
    private boolean parou;
    
    public Jogador(String nome, Socket socket) {
        this.nome = nome;
        this.socket = socket;
        this.nivelAtual = 0;
        this.premioAtual = 0;
        this.eliminado = false;
        this.parou = false;
    }
    
    public String getNome() {
        return nome;
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public int getNivelAtual() {
        return nivelAtual;
    }
    
    public void setNivelAtual(int nivelAtual) {
        this.nivelAtual = nivelAtual;
    }
    
    public double getPremioAtual() {
        return premioAtual;
    }
    
    public void setPremioAtual(double premioAtual) {
        this.premioAtual = premioAtual;
    }
    
    public boolean isEliminado() {
        return eliminado;
    }
    
    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }
    
    public boolean isParou() {
        return parou;
    }
    
    public void setParou(boolean parou) {
        this.parou = parou;
    }
    
    /**
     * Avança para o próximo nível
     */
    public void avancarNivel() {
        this.nivelAtual++;
    }
    
    /**
     * Verifica se o jogador ainda está no jogo
     */
    public boolean estaNoJogo() {
        return !eliminado && !parou;
    }
}

