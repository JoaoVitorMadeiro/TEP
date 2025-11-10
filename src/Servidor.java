import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Servidor principal do Jogo do Milhão - TI
 * Gerencia múltiplos clientes simultaneamente usando sockets TCP
 * 
 * Justificativa da escolha TCP:
 * - Garante entrega ordenada das mensagens
 * - Confiável para comandos críticos do jogo
 * - Conexão persistente permite comunicação bidirecional eficiente
 * - Ideal para jogos que precisam de sincronização de estado
 */
public class Servidor {
    private static final int PORTA = 12345;
    private ServerSocket serverSocket;
    private EstadoJogo estadoJogo;
    private Map<Jogador, GerenciadorCliente> clientes;
    private boolean servidorRodando;
    
    public Servidor() {
        this.estadoJogo = new EstadoJogo();
        this.clientes = new HashMap<>();
        this.servidorRodando = false;
    }
    
    /**
     * Inicia o servidor
     */
    public void iniciar() {
        try {
            serverSocket = new ServerSocket(PORTA);
            servidorRodando = true;
            
            System.out.println("========================================");
            System.out.println("  SERVIDOR DO JOGO DO MILHÃO - TI");
            System.out.println("========================================");
            System.out.println("Servidor iniciado na porta " + PORTA);
            System.out.println("Aguardando conexões de clientes...");
            System.out.println("Pressione Ctrl+C para encerrar o servidor");
            System.out.println("========================================\n");
            
            // Loop principal: aceita conexões de clientes
            while (servidorRodando) {
                try {
                    Socket clienteSocket = serverSocket.accept();
                    System.out.println("Nova conexão recebida de: " + clienteSocket.getInetAddress());
                    
                    // Cria um gerenciador para o novo cliente
                    GerenciadorCliente gerenciador = new GerenciadorCliente(clienteSocket, this);
                    gerenciador.start();
                    
                } catch (SocketException e) {
                    if (servidorRodando) {
                        System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Erro ao iniciar servidor: " + e.getMessage());
            e.printStackTrace();
        } finally {
            encerrar();
        }
    }
    
    /**
     * Adiciona um jogador ao jogo
     */
    public synchronized void adicionarJogador(Jogador jogador, GerenciadorCliente gerenciador) {
        estadoJogo.adicionarJogador(jogador);
        clientes.put(jogador, gerenciador);
        System.out.println("Jogador conectado: " + jogador.getNome() + 
                         " (Total: " + estadoJogo.getJogadores().size() + ")");
    }
    
    /**
     * Remove um jogador do jogo
     */
    public synchronized void removerJogador(Jogador jogador, GerenciadorCliente gerenciador) {
        estadoJogo.removerJogador(jogador);
        clientes.remove(jogador);
        System.out.println("Jogador desconectado: " + jogador.getNome() + 
                         " (Total: " + estadoJogo.getJogadores().size() + ")");
    }
    
    /**
     * Verifica se pode iniciar o jogo (mínimo 2 jogadores)
     */
    public synchronized void verificarInicioJogo() {
        if (!estadoJogo.isJogoIniciado() && estadoJogo.getJogadores().size() >= 2) {
            iniciarJogo();
        }
    }
    
    /**
     * Inicia o jogo
     */
    private void iniciarJogo() {
        estadoJogo.iniciarJogo();
        System.out.println("\n=== JOGO INICIADO ===");
        System.out.println("Jogadores: " + estadoJogo.getJogadores().size());
        
        // Notifica todos os clientes
        broadcast(Protocolo.criarMensagemJogoIniciado());
        
        // Envia primeira pergunta para cada jogador
        for (Jogador jogador : estadoJogo.getJogadores()) {
            Pergunta pergunta = estadoJogo.obterPerguntaParaJogador(jogador);
            if (pergunta != null) {
                GerenciadorCliente gerenciador = clientes.get(jogador);
                if (gerenciador != null) {
                    gerenciador.enviarMensagem(Protocolo.criarMensagemPergunta(pergunta));
                }
            }
        }
        
        broadcastEstatisticas();
    }
    
    /**
     * Processa resposta de um jogador
     */
    public synchronized boolean processarResposta(Jogador jogador, int resposta) {
        boolean acertou = estadoJogo.processarResposta(jogador, resposta);
        
        if (estadoJogo.isJogoFinalizado()) {
            Jogador vencedor = estadoJogo.getVencedor();
            if (vencedor != null) {
                System.out.println("\n=== JOGO FINALIZADO ===");
                System.out.println("VENCEDOR: " + vencedor.getNome());
                System.out.println("PRÊMIO: R$ " + String.format("%.2f", vencedor.getPremioAtual()));
                
                broadcast(Protocolo.criarMensagemJogoFinalizado(
                    vencedor.getNome(), 
                    vencedor.getPremioAtual()
                ));
            }
        }
        
        return acertou;
    }
    
    /**
     * Processa decisão de parar de um jogador
     */
    public synchronized void processarParada(Jogador jogador) {
        estadoJogo.processarParada(jogador);
        System.out.println("Jogador " + jogador.getNome() + " parou com R$ " + 
                         String.format("%.2f", jogador.getPremioAtual()));
        
        if (estadoJogo.isJogoFinalizado()) {
            Jogador vencedor = estadoJogo.getVencedor();
            if (vencedor != null) {
                System.out.println("\n=== JOGO FINALIZADO ===");
                System.out.println("VENCEDOR: " + vencedor.getNome());
                System.out.println("PRÊMIO: R$ " + String.format("%.2f", vencedor.getPremioAtual()));
                
                broadcast(Protocolo.criarMensagemJogoFinalizado(
                    vencedor.getNome(), 
                    vencedor.getPremioAtual()
                ));
            }
        }
    }
    
    /**
     * Obtém pergunta para um jogador
     */
    public Pergunta obterPerguntaParaJogador(Jogador jogador) {
        return estadoJogo.obterPerguntaParaJogador(jogador);
    }
    
    /**
     * Envia mensagem para todos os clientes
     */
    public synchronized void broadcast(String mensagem) {
        for (GerenciadorCliente gerenciador : clientes.values()) {
            if (gerenciador.isConectado()) {
                gerenciador.enviarMensagem(mensagem);
            }
        }
    }
    
    /**
     * Envia estatísticas para todos os clientes
     */
    public synchronized void broadcastEstatisticas() {
        String estatisticas = estadoJogo.obterEstatisticas();
        broadcast(Protocolo.criarMensagemEstatisticas(estatisticas));
    }
    
    /**
     * Envia mensagem de chat para todos os clientes
     */
    public synchronized void broadcastChat(String remetente, String texto) {
        String mensagem = Protocolo.criarMensagemChat(remetente, texto);
        broadcast(mensagem);
        System.out.println("[CHAT] " + remetente + ": " + texto);
    }
    
    /**
     * Verifica se o jogo está iniciado
     */
    public boolean isJogoIniciado() {
        return estadoJogo.isJogoIniciado();
    }
    
    /**
     * Encerra o servidor
     */
    public void encerrar() {
        servidorRodando = false;
        
        // Fecha todas as conexões
        for (GerenciadorCliente gerenciador : new ArrayList<>(clientes.values())) {
            gerenciador.desconectar();
        }
        
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Erro ao fechar servidor: " + e.getMessage());
        }
        
        System.out.println("\nServidor encerrado.");
    }
    
    /**
     * Método principal
     */
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        
        // Adiciona handler para encerramento gracioso
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nEncerrando servidor...");
            servidor.encerrar();
        }));
        
        servidor.iniciar();
    }
}

