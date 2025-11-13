import java.io.*;
import java.net.*;
import java.util.Random;

/**
 * Bot automatizado para jogar o Jogo do Milhão - TI
 * Conecta-se ao servidor e joga automaticamente usando diferentes estratégias
 */
public class ClienteBot {
    private static final String HOST = "localhost";
    private static final int PORTA = 12345;
    
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter saida;
    private boolean conectado;
    private String nomeBot;
    private EstrategiaBot estrategia;
    private Random random;
    private boolean jogoAtivo;
    private int nivelAtual;
    private double premioAtual;
    
    /**
     * Estratégias disponíveis para o bot
     */
    public enum EstrategiaBot {
        ALEATORIA,      // Responde aleatoriamente
        CONSERVADORA,   // Para cedo para garantir prêmio
        AGRESSIVA       // Tenta ir até o fim
    }
    
    public ClienteBot(String nome, EstrategiaBot estrategia) {
        this.nomeBot = nome;
        this.estrategia = estrategia;
        this.conectado = false;
        this.random = new Random();
        this.jogoAtivo = false;
        this.nivelAtual = 0;
        this.premioAtual = 0;
    }
    
    /**
     * Conecta ao servidor
     */
    public boolean conectar() {
        try {
            socket = new Socket(HOST, PORTA);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            saida = new PrintWriter(socket.getOutputStream(), true);
            conectado = true;
            
            // Envia comando de conexão
            saida.println(Protocolo.criarMensagemConectar(nomeBot));
            log("Conectado ao servidor");
            
            // Inicia thread para receber mensagens
            Thread threadRecebimento = new Thread(this::receberMensagens);
            threadRecebimento.setDaemon(true);
            threadRecebimento.start();
            
            return true;
            
        } catch (ConnectException e) {
            log("ERRO: Não foi possível conectar ao servidor");
            return false;
        } catch (IOException e) {
            log("ERRO ao conectar: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Thread que recebe mensagens do servidor
     */
    private void receberMensagens() {
        try {
            String mensagem;
            while (conectado && (mensagem = entrada.readLine()) != null) {
                processarMensagem(mensagem);
            }
        } catch (IOException e) {
            if (conectado) {
                log("Conexão perdida com o servidor");
                desconectar();
            }
        }
    }
    
    /**
     * Processa mensagens recebidas do servidor
     */
    private void processarMensagem(String mensagem) {
        String[] partes = Protocolo.parsearMensagem(mensagem);
        
        if (partes.length == 0) {
            return;
        }
        
        String comando = partes[0];
        
        switch (comando) {
            case Protocolo.CONECTADO:
                if (partes.length >= 2) {
                    log("Servidor: " + partes[1]);
                }
                break;
                
            case Protocolo.ERRO:
                if (partes.length >= 2) {
                    log("ERRO: " + partes[1]);
                }
                break;
                
            case Protocolo.JOGO_INICIADO:
                log("=== JOGO INICIADO ===");
                jogoAtivo = true;
                break;
                
            case Protocolo.PERGUNTA:
                if (partes.length >= 8) {
                    String enunciado = partes[1];
                    int nivel = Integer.parseInt(partes[6]);
                    double premio = Double.parseDouble(partes[7]);
                    
                    log("\n--- Nível " + nivel + " (R$ " + String.format("%.2f", premio) + ") ---");
                    log("Pergunta: " + enunciado);
                    
                    // Aguarda um pouco antes de responder (simula pensamento)
                    aguardar(1000, 3000);
                    
                    // Decide se para ou responde
                    if (deveParar(nivel, premio)) {
                        log("Decisão: PARAR e garantir R$ " + String.format("%.2f", premioAtual));
                        enviarParada();
                    } else {
                        int resposta = escolherResposta();
                        log("Resposta escolhida: " + resposta);
                        enviarResposta(resposta);
                    }
                }
                break;
                
            case Protocolo.RESULTADO:
                if (partes.length >= 4) {
                    boolean acertou = partes[1].equals("ACERTOU");
                    double premio = Double.parseDouble(partes[2]);
                    int nivel = Integer.parseInt(partes[3]);
                    
                    if (acertou) {
                        nivelAtual = nivel;
                        premioAtual = premio;
                        log("✓ ACERTOU! Nível: " + nivel + " | Prêmio: R$ " + String.format("%.2f", premio));
                    } else {
                        log("✗ ERROU! Eliminado. Prêmio final: R$ " + String.format("%.2f", premio));
                        jogoAtivo = false;
                    }
                }
                break;
                
            case Protocolo.JOGO_FINALIZADO:
                if (partes.length >= 3) {
                    String vencedor = partes[1];
                    double premio = Double.parseDouble(partes[2]);
                    log("\n=== JOGO FINALIZADO ===");
                    log("Vencedor: " + vencedor);
                    log("Prêmio: R$ " + String.format("%.2f", premio));
                    jogoAtivo = false;
                }
                break;
                
            case Protocolo.ESTATISTICAS:
                // Bot não precisa processar estatísticas detalhadamente
                break;
                
            case Protocolo.MENSAGEM:
                // Bot não processa mensagens de chat
                break;
        }
    }
    
    /**
     * Decide se o bot deve parar baseado na estratégia
     */
    private boolean deveParar(int nivel, double premio) {
        switch (estrategia) {
            case CONSERVADORA:
                // Para após nível 5 (R$ 50.000) com 70% de chance
                if (nivel > 5 && random.nextDouble() < 0.7) {
                    return true;
                }
                // Para após nível 7 (R$ 300.000) com 90% de chance
                if (nivel > 7 && random.nextDouble() < 0.9) {
                    return true;
                }
                return false;
                
            case AGRESSIVA:
                // Só para após nível 10 (R$ 1.500.000) com 30% de chance
                if (nivel > 10 && random.nextDouble() < 0.3) {
                    return true;
                }
                return false;
                
            case ALEATORIA:
            default:
                // 10% de chance de parar em qualquer nível após o 3
                return nivel > 3 && random.nextDouble() < 0.1;
        }
    }
    
    /**
     * Escolhe uma resposta aleatória (0-3)
     */
    private int escolherResposta() {
        return random.nextInt(4);
    }
    
    /**
     * Aguarda um tempo aleatório entre min e max milissegundos
     */
    private void aguardar(int min, int max) {
        try {
            int tempo = min + random.nextInt(max - min);
            Thread.sleep(tempo);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Envia resposta do bot
     */
    private void enviarResposta(int resposta) {
        if (conectado && saida != null) {
            saida.println(Protocolo.criarMensagemResposta(resposta));
        }
    }
    
    /**
     * Envia decisão de parar
     */
    private void enviarParada() {
        if (conectado && saida != null) {
            saida.println(Protocolo.criarMensagemParar());
        }
    }
    
    /**
     * Desconecta do servidor
     */
    public void desconectar() {
        conectado = false;
        try {
            if (entrada != null) entrada.close();
            if (saida != null) saida.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            log("Erro ao fechar conexão: " + e.getMessage());
        }
    }
    
    /**
     * Log de mensagens do bot
     */
    private void log(String mensagem) {
        System.out.println("[BOT " + nomeBot + "] " + mensagem);
    }
    
    public boolean isConectado() {
        return conectado;
    }
    
    public boolean isJogoAtivo() {
        return jogoAtivo;
    }
    
    /**
     * Método principal para testar o bot
     */
    public static void main(String[] args) {
        String nome = "Bot_" + System.currentTimeMillis();
        EstrategiaBot estrategia = EstrategiaBot.ALEATORIA;
        
        // Permite passar nome e estratégia como argumentos
        if (args.length > 0) {
            nome = args[0];
        }
        if (args.length > 1) {
            try {
                estrategia = EstrategiaBot.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Estratégia inválida. Usando ALEATORIA.");
            }
        }
        
        System.out.println("========================================");
        System.out.println("  BOT DO JOGO DO MILHÃO - TI");
        System.out.println("========================================");
        System.out.println("Nome: " + nome);
        System.out.println("Estratégia: " + estrategia);
        System.out.println("========================================\n");
        
        ClienteBot bot = new ClienteBot(nome, estrategia);
        
        if (!bot.conectar()) {
            System.out.println("Não foi possível conectar ao servidor.");
            return;
        }
        
        // Mantém o bot rodando até o jogo terminar
        while (bot.isConectado()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("\n[BOT " + nome + "] Desconectado do servidor.");
    }
}
