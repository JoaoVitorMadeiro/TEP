import java.io.*;
import java.net.Socket;

/**
 * Classe que gerencia a comunicação com um cliente específico
 * Cada cliente conectado tem uma thread dedicada
 */
public class GerenciadorCliente extends Thread {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter saida;
    private Servidor servidor;
    private Jogador jogador;
    private boolean conectado;
    
    public GerenciadorCliente(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
        this.conectado = true;
        
        try {
            this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.saida = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Erro ao criar streams de comunicação: " + e.getMessage());
            conectado = false;
        }
    }
    
    @Override
    public void run() {
        try {
            // Aguarda conexão do cliente
            String mensagem;
            while (conectado && (mensagem = entrada.readLine()) != null) {
                processarMensagem(mensagem);
            }
        } catch (IOException e) {
            System.err.println("Erro na comunicação com cliente: " + e.getMessage());
        } finally {
            desconectar();
        }
    }
    
    /**
     * Processa mensagens recebidas do cliente
     */
    private void processarMensagem(String mensagem) {
        String[] partes = Protocolo.parsearMensagem(mensagem);
        
        if (partes.length == 0) {
            return;
        }
        
        String comando = partes[0];
        
        switch (comando) {
            case Protocolo.CONECTAR:
                if (partes.length >= 2) {
                    String nome = partes[1];
                    conectarJogador(nome);
                }
                break;
                
            case Protocolo.RESPOSTA:
                if (partes.length >= 2) {
                    try {
                        int resposta = Integer.parseInt(partes[1]);
                        processarResposta(resposta);
                    } catch (NumberFormatException e) {
                        enviarMensagem(Protocolo.criarMensagemErro("Resposta inválida"));
                    }
                }
                break;
                
            case Protocolo.PARAR:
                processarParada();
                break;
                
            case Protocolo.CHAT:
                if (partes.length >= 2) {
                    String textoChat = partes[1];
                    processarChat(textoChat);
                }
                break;
                
            default:
                enviarMensagem(Protocolo.criarMensagemErro("Comando desconhecido: " + comando));
        }
    }
    
    /**
     * Conecta um jogador ao servidor
     */
    private void conectarJogador(String nome) {
        if (jogador != null) {
            enviarMensagem(Protocolo.criarMensagemErro("Já está conectado"));
            return;
        }
        
        jogador = new Jogador(nome, socket);
        servidor.adicionarJogador(jogador, this);
        
        enviarMensagem(Protocolo.criarMensagemConectado(
            "Bem-vindo ao Jogo do Milhão - TI! Aguardando outros jogadores..."
        ));
        
        // Verifica se pode iniciar o jogo
        servidor.verificarInicioJogo();
    }
    
    /**
     * Processa resposta de um jogador
     */
    private void processarResposta(int resposta) {
        if (jogador == null) {
            enviarMensagem(Protocolo.criarMensagemErro("Você não está conectado"));
            return;
        }
        
        if (!servidor.isJogoIniciado()) {
            enviarMensagem(Protocolo.criarMensagemErro("O jogo ainda não começou"));
            return;
        }
        
        if (!jogador.estaNoJogo()) {
            enviarMensagem(Protocolo.criarMensagemErro("Você não está mais no jogo"));
            return;
        }
        
        boolean acertou = servidor.processarResposta(jogador, resposta);
        double premio = jogador.getPremioAtual();
        int nivel = jogador.getNivelAtual();
        
        enviarMensagem(Protocolo.criarMensagemResultado(acertou, premio, nivel));
        
        // Envia estatísticas atualizadas
        servidor.broadcastEstatisticas();
        
        // Se acertou, envia próxima pergunta
        if (acertou && jogador.estaNoJogo()) {
            Pergunta proximaPergunta = servidor.obterPerguntaParaJogador(jogador);
            if (proximaPergunta != null) {
                enviarMensagem(Protocolo.criarMensagemPergunta(proximaPergunta));
            }
        }
    }
    
    /**
     * Processa decisão de parar do jogador
     */
    private void processarParada() {
        if (jogador == null) {
            enviarMensagem(Protocolo.criarMensagemErro("Você não está conectado"));
            return;
        }
        
        if (!servidor.isJogoIniciado()) {
            enviarMensagem(Protocolo.criarMensagemErro("O jogo ainda não começou"));
            return;
        }
        
        if (!jogador.estaNoJogo()) {
            enviarMensagem(Protocolo.criarMensagemErro("Você não está mais no jogo"));
            return;
        }
        
        servidor.processarParada(jogador);
        enviarMensagem(Protocolo.criarMensagemResultado(true, jogador.getPremioAtual(), jogador.getNivelAtual()));
        servidor.broadcastEstatisticas();
    }
    
    /**
     * Processa mensagem de chat
     */
    private void processarChat(String texto) {
        if (jogador == null) {
            return;
        }
        
        servidor.broadcastChat(jogador.getNome(), texto);
    }
    
    /**
     * Envia mensagem para o cliente
     */
    public void enviarMensagem(String mensagem) {
        if (saida != null && conectado) {
            saida.println(mensagem);
        }
    }
    
    /**
     * Desconecta o cliente
     */
    public void desconectar() {
        conectado = false;
        
        if (jogador != null) {
            servidor.removerJogador(jogador, this);
        }
        
        try {
            if (entrada != null) entrada.close();
            if (saida != null) saida.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
        
        System.out.println("Cliente desconectado: " + (jogador != null ? jogador.getNome() : "Desconhecido"));
    }
    
    public Jogador getJogador() {
        return jogador;
    }
    
    public boolean isConectado() {
        return conectado;
    }
}

