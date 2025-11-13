import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utilitário para criar e gerenciar bots do Jogo do Milhão - TI
 * Permite criar múltiplos bots com diferentes estratégias
 */
public class CriadorBot {
    
    private List<ClienteBot> bots;
    
    public CriadorBot() {
        this.bots = new ArrayList<>();
    }
    
    /**
     * Cria um bot com nome e estratégia especificados
     */
    public ClienteBot criarBot(String nome, ClienteBot.EstrategiaBot estrategia) {
        ClienteBot bot = new ClienteBot(nome, estrategia);
        bots.add(bot);
        return bot;
    }
    
    /**
     * Cria um bot com nome aleatório
     */
    public ClienteBot criarBotAleatorio(ClienteBot.EstrategiaBot estrategia) {
        String nome = "Bot_" + System.currentTimeMillis() + "_" + bots.size();
        return criarBot(nome, estrategia);
    }
    
    /**
     * Conecta todos os bots criados ao servidor
     */
    public void conectarTodos() {
        System.out.println("Conectando " + bots.size() + " bot(s) ao servidor...\n");
        
        for (ClienteBot bot : bots) {
            if (bot.conectar()) {
                System.out.println("✓ Bot conectado com sucesso");
                // Aguarda um pouco entre conexões
                aguardar(500);
            } else {
                System.out.println("✗ Falha ao conectar bot");
            }
        }
        
        System.out.println("\nTodos os bots foram conectados!");
    }
    
    /**
     * Desconecta todos os bots
     */
    public void desconectarTodos() {
        for (ClienteBot bot : bots) {
            bot.desconectar();
        }
    }
    
    /**
     * Aguarda um tempo em milissegundos
     */
    private void aguardar(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Método principal - Interface de linha de comando
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CriadorBot criador = new CriadorBot();
        
        System.out.println("========================================");
        System.out.println("  CRIADOR DE BOTS - JOGO DO MILHÃO");
        System.out.println("========================================");
        System.out.println();
        
        // Solicita número de bots
        System.out.print("Quantos bots deseja criar? (1-10): ");
        int numBots = 1;
        try {
            numBots = Integer.parseInt(scanner.nextLine().trim());
            if (numBots < 1 || numBots > 10) {
                System.out.println("Número inválido. Criando 1 bot.");
                numBots = 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Criando 1 bot.");
        }
        
        System.out.println();
        System.out.println("Estratégias disponíveis:");
        System.out.println("  1. ALEATORIA - Responde aleatoriamente");
        System.out.println("  2. CONSERVADORA - Para cedo para garantir prêmio");
        System.out.println("  3. AGRESSIVA - Tenta ir até o fim");
        System.out.println();
        
        // Cria os bots
        for (int i = 0; i < numBots; i++) {
            System.out.println("--- Bot " + (i + 1) + " ---");
            
            // Solicita nome do bot
            System.out.print("Nome do bot (ou Enter para gerar automático): ");
            String nome = scanner.nextLine().trim();
            if (nome.isEmpty()) {
                nome = "Bot_" + (i + 1);
            }
            
            // Solicita estratégia
            System.out.print("Estratégia (1-3): ");
            ClienteBot.EstrategiaBot estrategia = ClienteBot.EstrategiaBot.ALEATORIA;
            try {
                int opcao = Integer.parseInt(scanner.nextLine().trim());
                switch (opcao) {
                    case 1:
                        estrategia = ClienteBot.EstrategiaBot.ALEATORIA;
                        break;
                    case 2:
                        estrategia = ClienteBot.EstrategiaBot.CONSERVADORA;
                        break;
                    case 3:
                        estrategia = ClienteBot.EstrategiaBot.AGRESSIVA;
                        break;
                    default:
                        System.out.println("Opção inválida. Usando ALEATORIA.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Usando ALEATORIA.");
            }
            
            criador.criarBot(nome, estrategia);
            System.out.println("✓ Bot '" + nome + "' criado com estratégia " + estrategia);
            System.out.println();
        }
        
        // Conecta todos os bots
        System.out.println("========================================");
        criador.conectarTodos();
        System.out.println("========================================");
        System.out.println();
        System.out.println("Bots estão jogando...");
        System.out.println("Pressione Enter para encerrar todos os bots.");
        
        scanner.nextLine();
        
        criador.desconectarTodos();
        scanner.close();
        
        System.out.println("\nTodos os bots foram desconectados. Encerrando...");
    }
}
