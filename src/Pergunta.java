/**
 * Classe que representa uma pergunta do jogo do milhão
 * Todas as perguntas são sobre Tecnologia da Informação (TI)
 */
public class Pergunta {
    private String enunciado;
    private String[] alternativas;
    private int respostaCorreta; // 0, 1, 2 ou 3
    private int nivel; // 1 a 15 (15 níveis do jogo do milhão)
    private double premio; // Prêmio em reais para este nível
    
    public Pergunta(String enunciado, String[] alternativas, int respostaCorreta, int nivel, double premio) {
        this.enunciado = enunciado;
        this.alternativas = alternativas;
        this.respostaCorreta = respostaCorreta;
        this.nivel = nivel;
        this.premio = premio;
    }
    
    public String getEnunciado() {
        return enunciado;
    }
    
    public String[] getAlternativas() {
        return alternativas;
    }
    
    public int getRespostaCorreta() {
        return respostaCorreta;
    }
    
    public int getNivel() {
        return nivel;
    }
    
    public double getPremio() {
        return premio;
    }
    
    /**
     * Verifica se a resposta fornecida está correta
     */
    public boolean verificarResposta(int resposta) {
        return resposta == respostaCorreta;
    }
    
    /**
     * Retorna a pergunta formatada para envio ao cliente
     */
    public String formatarParaEnvio() {
        StringBuilder sb = new StringBuilder();
        sb.append(enunciado).append("|");
        for (int i = 0; i < alternativas.length; i++) {
            sb.append(alternativas[i]);
            if (i < alternativas.length - 1) {
                sb.append("|");
            }
        }
        sb.append("|").append(nivel).append("|").append(premio);
        return sb.toString();
    }
}

