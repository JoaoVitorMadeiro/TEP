import java.util.*;

/**
 * Classe que gerencia o estado global do jogo
 */
public class EstadoJogo {
    private List<Jogador> jogadores;
    private Map<Jogador, Pergunta> perguntasAtuais;
    private BancoPerguntas bancoPerguntas;
    private boolean jogoIniciado;
    private boolean jogoFinalizado;
    private Jogador vencedor;
    
    public EstadoJogo() {
        this.jogadores = new ArrayList<>();
        this.perguntasAtuais = new HashMap<>();
        this.bancoPerguntas = new BancoPerguntas();
        this.jogoIniciado = false;
        this.jogoFinalizado = false;
        this.vencedor = null;
    }
    
    /**
     * Adiciona um jogador ao jogo
     */
    public synchronized void adicionarJogador(Jogador jogador) {
        jogadores.add(jogador);
    }
    
    /**
     * Remove um jogador do jogo
     */
    public synchronized void removerJogador(Jogador jogador) {
        jogadores.remove(jogador);
        perguntasAtuais.remove(jogador);
    }
    
    /**
     * Obtém lista de jogadores
     */
    public List<Jogador> getJogadores() {
        return new ArrayList<>(jogadores);
    }
    
    /**
     * Inicia o jogo
     */
    public synchronized void iniciarJogo() {
        if (jogadores.size() < 2) {
            throw new IllegalStateException("É necessário pelo menos 2 jogadores para iniciar");
        }
        this.jogoIniciado = true;
        this.jogoFinalizado = false;
    }
    
    public boolean isJogoIniciado() {
        return jogoIniciado;
    }
    
    public boolean isJogoFinalizado() {
        return jogoFinalizado;
    }
    
    /**
     * Obtém uma pergunta para um jogador baseado no seu nível atual
     */
    public Pergunta obterPerguntaParaJogador(Jogador jogador) {
        int proximoNivel = jogador.getNivelAtual() + 1;
        Pergunta pergunta = bancoPerguntas.obterPergunta(proximoNivel);
        perguntasAtuais.put(jogador, pergunta);
        return pergunta;
    }
    
    /**
     * Processa a resposta de um jogador
     */
    public synchronized boolean processarResposta(Jogador jogador, int resposta) {
        Pergunta pergunta = perguntasAtuais.get(jogador);
        if (pergunta == null) {
            return false;
        }
        
        boolean acertou = pergunta.verificarResposta(resposta);
        
        if (acertou) {
            jogador.avancarNivel();
            jogador.setPremioAtual(pergunta.getPremio());
            perguntasAtuais.remove(jogador);
            
            // Verifica se ganhou o jogo (chegou ao nível 15)
            if (jogador.getNivelAtual() >= 15) {
                finalizarJogo(jogador);
            }
        } else {
            jogador.setEliminado(true);
            jogador.setPremioAtual(0); // Perde tudo ao errar
            perguntasAtuais.remove(jogador);
        }
        
        verificarFimJogo();
        return acertou;
    }
    
    /**
     * Processa a decisão de parar de um jogador
     */
    public synchronized void processarParada(Jogador jogador) {
        jogador.setParou(true);
        perguntasAtuais.remove(jogador);
        verificarFimJogo();
    }
    
    /**
     * Verifica se o jogo deve terminar
     */
    private void verificarFimJogo() {
        long jogadoresAtivos = jogadores.stream()
            .filter(Jogador::estaNoJogo)
            .count();
        
        if (jogadoresAtivos == 0) {
            finalizarJogo(null);
        } else if (jogadoresAtivos == 1 && !jogoFinalizado) {
            Jogador ultimoJogador = jogadores.stream()
                .filter(Jogador::estaNoJogo)
                .findFirst()
                .orElse(null);
            if (ultimoJogador != null) {
                finalizarJogo(ultimoJogador);
            }
        }
    }
    
    /**
     * Finaliza o jogo
     */
    private void finalizarJogo(Jogador vencedor) {
        this.jogoFinalizado = true;
        this.vencedor = vencedor;
    }
    
    public Jogador getVencedor() {
        return vencedor;
    }
    
    /**
     * Obtém estatísticas do jogo para envio aos clientes
     */
    public String obterEstatisticas() {
        StringBuilder sb = new StringBuilder();
        for (Jogador j : jogadores) {
            sb.append(j.getNome()).append("|")
              .append(j.getNivelAtual()).append("|")
              .append(j.getPremioAtual()).append("|")
              .append(j.isEliminado() ? "ELIMINADO" : (j.isParou() ? "PAROU" : "ATIVO"))
              .append(";");
        }
        return sb.toString();
    }
}

