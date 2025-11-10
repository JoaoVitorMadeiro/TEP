/**
 * Classe que define o protocolo de comunicação entre cliente e servidor
 * 
 * Formato das mensagens:
 * - COMANDO|parametros
 * 
 * Comandos do Cliente:
 * - CONECTAR|nome
 * - RESPOSTA|numero (0-3)
 * - PARAR
 * - CHAT|mensagem
 * 
 * Comandos do Servidor:
 * - CONECTADO|mensagem
 * - ERRO|mensagem
 * - PERGUNTA|enunciado|alt1|alt2|alt3|alt4|nivel|premio
 * - RESULTADO|acertou|premio|nivel
 * - ESTATISTICAS|jogador1|nivel1|premio1|status1;jogador2|nivel2|premio2|status2;...
 * - JOGO_INICIADO
 * - JOGO_FINALIZADO|vencedor|premio
 * - MENSAGEM|remetente|texto
 */
public class Protocolo {
    
    // Comandos do Cliente
    public static final String CONECTAR = "CONECTAR";
    public static final String RESPOSTA = "RESPOSTA";
    public static final String PARAR = "PARAR";
    public static final String CHAT = "CHAT";
    
    // Comandos do Servidor
    public static final String CONECTADO = "CONECTADO";
    public static final String ERRO = "ERRO";
    public static final String PERGUNTA = "PERGUNTA";
    public static final String RESULTADO = "RESULTADO";
    public static final String ESTATISTICAS = "ESTATISTICAS";
    public static final String JOGO_INICIADO = "JOGO_INICIADO";
    public static final String JOGO_FINALIZADO = "JOGO_FINALIZADO";
    public static final String MENSAGEM = "MENSAGEM";
    
    /**
     * Cria mensagem de conexão do cliente
     */
    public static String criarMensagemConectar(String nome) {
        return CONECTAR + "|" + nome;
    }
    
    /**
     * Cria mensagem de resposta do cliente
     */
    public static String criarMensagemResposta(int resposta) {
        return RESPOSTA + "|" + resposta;
    }
    
    /**
     * Cria mensagem de parada do cliente
     */
    public static String criarMensagemParar() {
        return PARAR;
    }
    
    /**
     * Cria mensagem de chat do cliente
     */
    public static String criarMensagemChat(String mensagem) {
        return CHAT + "|" + mensagem;
    }
    
    /**
     * Cria mensagem de conexão bem-sucedida do servidor
     */
    public static String criarMensagemConectado(String mensagem) {
        return CONECTADO + "|" + mensagem;
    }
    
    /**
     * Cria mensagem de erro do servidor
     */
    public static String criarMensagemErro(String mensagem) {
        return ERRO + "|" + mensagem;
    }
    
    /**
     * Cria mensagem de pergunta do servidor
     */
    public static String criarMensagemPergunta(Pergunta pergunta) {
        return PERGUNTA + "|" + pergunta.formatarParaEnvio();
    }
    
    /**
     * Cria mensagem de resultado do servidor
     */
    public static String criarMensagemResultado(boolean acertou, double premio, int nivel) {
        return RESULTADO + "|" + (acertou ? "ACERTOU" : "ERROU") + "|" + premio + "|" + nivel;
    }
    
    /**
     * Cria mensagem de estatísticas do servidor
     */
    public static String criarMensagemEstatisticas(String estatisticas) {
        return ESTATISTICAS + "|" + estatisticas;
    }
    
    /**
     * Cria mensagem de jogo iniciado do servidor
     */
    public static String criarMensagemJogoIniciado() {
        return JOGO_INICIADO;
    }
    
    /**
     * Cria mensagem de jogo finalizado do servidor
     */
    public static String criarMensagemJogoFinalizado(String vencedor, double premio) {
        return JOGO_FINALIZADO + "|" + vencedor + "|" + premio;
    }
    
    /**
     * Cria mensagem de chat do servidor
     */
    public static String criarMensagemChat(String remetente, String texto) {
        return MENSAGEM + "|" + remetente + "|" + texto;
    }
    
    /**
     * Parseia uma mensagem recebida
     */
    public static String[] parsearMensagem(String mensagem) {
        return mensagem.split("\\|", -1);
    }
}

