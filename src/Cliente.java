import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Cliente do Jogo do Milhão - TI
 * Conecta-se ao servidor e permite interação do jogador
 */
public class Cliente {
    private static final String HOST = "localhost";
    private static final int PORTA = 12345;
    
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter saida;
    private boolean conectado;
    private String nomeJogador;
    private InterfaceUsuario interfaceUsuario;
    
    public Cliente() {
        this.conectado = false;
        this.interfaceUsuario = new InterfaceUsuario();
    }
    
    /**
     * Conecta ao servidor
     */
    public boolean conectar(String nome) {
        try {
            socket = new Socket(HOST, PORTA);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            saida = new PrintWriter(socket.getOutputStream(), true);
            conectado = true;
            nomeJogador = nome;
            
            // Envia comando de conexão
            saida.println(Protocolo.criarMensagemConectar(nome));
            
            // Inicia thread para receber mensagens
            Thread threadRecebimento = new Thread(this::receberMensagens);
            threadRecebimento.setDaemon(true);
            threadRecebimento.start();
            
            return true;
            
        } catch (ConnectException e) {
            interfaceUsuario.mostrarErro("Não foi possível conectar ao servidor. Verifique se o servidor está rodando.");
            return false;
        } catch (IOException e) {
            interfaceUsuario.mostrarErro("Erro ao conectar: " + e.getMessage());
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
                interfaceUsuario.mostrarErro("Conexão perdida com o servidor.");
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
                    interfaceUsuario.mostrarMensagem(partes[1]);
                }
                break;
                
            case Protocolo.ERRO:
                if (partes.length >= 2) {
                    interfaceUsuario.mostrarErro(partes[1]);
                }
                break;
                
            case Protocolo.JOGO_INICIADO:
                interfaceUsuario.mostrarMensagem("\n=== JOGO INICIADO! ===");
                break;
                
            case Protocolo.PERGUNTA:
                if (partes.length >= 7) {
                    String enunciado = partes[1];
                    String[] alternativas = {
                        partes[2], partes[3], partes[4], partes[5]
                    };
                    int nivel = Integer.parseInt(partes[6]);
                    double premio = Double.parseDouble(partes[7]);
                    
                    interfaceUsuario.mostrarPergunta(enunciado, alternativas, nivel, premio);
                }
                break;
                
            case Protocolo.RESULTADO:
                if (partes.length >= 4) {
                    boolean acertou = partes[1].equals("ACERTOU");
                    double premio = Double.parseDouble(partes[2]);
                    int nivel = Integer.parseInt(partes[3]);
                    
                    interfaceUsuario.mostrarResultado(acertou, premio, nivel);
                }
                break;
                
            case Protocolo.ESTATISTICAS:
                if (partes.length >= 2) {
                    interfaceUsuario.mostrarEstatisticas(partes[1]);
                }
                break;
                
            case Protocolo.JOGO_FINALIZADO:
                if (partes.length >= 3) {
                    String vencedor = partes[1];
                    double premio = Double.parseDouble(partes[2]);
                    interfaceUsuario.mostrarFimJogo(vencedor, premio);
                }
                break;
                
            case Protocolo.MENSAGEM:
                if (partes.length >= 3) {
                    String remetente = partes[1];
                    String texto = partes[2];
                    interfaceUsuario.mostrarChat(remetente, texto);
                }
                break;
        }
    }
    
    /**
     * Envia resposta do jogador
     */
    public void enviarResposta(int resposta) {
        if (conectado && saida != null) {
            saida.println(Protocolo.criarMensagemResposta(resposta));
        }
    }
    
    /**
     * Envia decisão de parar
     */
    public void enviarParada() {
        if (conectado && saida != null) {
            saida.println(Protocolo.criarMensagemParar());
        }
    }
    
    /**
     * Envia mensagem de chat
     */
    public void enviarChat(String mensagem) {
        if (conectado && saida != null) {
            saida.println(Protocolo.criarMensagemChat(mensagem));
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
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
    
    public boolean isConectado() {
        return conectado;
    }
    
    public String getNomeJogador() {
        return nomeJogador;
    }
    
    /**
     * Método principal do cliente
     */
    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        Scanner scanner = new Scanner(System.in);
        
        // Solicita nome do jogador
        System.out.println("========================================");
        System.out.println("  JOGO DO MILHÃO - TI");
        System.out.println("========================================");
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();
        
        // Conecta ao servidor
        if (!cliente.conectar(nome)) {
            System.out.println("Não foi possível conectar ao servidor.");
            scanner.close();
            return;
        }
        
        // Loop principal de interação
        while (cliente.isConectado()) {
            System.out.print("\n> ");
            String comando = scanner.nextLine().trim();
            
            if (comando.isEmpty()) {
                continue;
            }
            
            // Comandos especiais
            if (comando.equalsIgnoreCase("sair") || comando.equalsIgnoreCase("quit")) {
                cliente.desconectar();
                break;
            } else if (comando.equalsIgnoreCase("parar")) {
                cliente.enviarParada();
            } else if (comando.startsWith("/chat ")) {
                String mensagem = comando.substring(6);
                cliente.enviarChat(mensagem);
            } else if (comando.matches("^[0-3]$")) {
                // Resposta numérica (0-3)
                int resposta = Integer.parseInt(comando);
                cliente.enviarResposta(resposta);
            } else {
                System.out.println("Comandos disponíveis:");
                System.out.println("  0-3: Responder pergunta (0, 1, 2 ou 3)");
                System.out.println("  parar: Parar e garantir prêmio atual");
                System.out.println("  /chat <mensagem>: Enviar mensagem no chat");
                System.out.println("  sair: Desconectar do servidor");
            }
        }
        
        scanner.close();
        System.out.println("\nDesconectado do servidor. Até logo!");
    }
}

