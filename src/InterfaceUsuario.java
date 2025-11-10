/**
 * Classe responsável pela interface textual do cliente
 */
public class InterfaceUsuario {
    
    /**
     * Mostra uma mensagem simples
     */
    public void mostrarMensagem(String mensagem) {
        System.out.println("\n" + mensagem);
    }
    
    /**
     * Mostra uma mensagem de erro
     */
    public void mostrarErro(String erro) {
        System.out.println("\n[ERRO] " + erro);
    }
    
    /**
     * Mostra uma pergunta formatada
     */
    public void mostrarPergunta(String enunciado, String[] alternativas, int nivel, double premio) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("NÍVEL " + nivel + " - PRÊMIO: R$ " + String.format("%.2f", premio));
        System.out.println("=".repeat(60));
        System.out.println("\n" + enunciado);
        System.out.println();
        
        for (int i = 0; i < alternativas.length; i++) {
            System.out.println("  [" + i + "] " + alternativas[i]);
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.print("Digite sua resposta (0-3) ou 'parar' para garantir o prêmio: ");
    }
    
    /**
     * Mostra o resultado de uma resposta
     */
    public void mostrarResultado(boolean acertou, double premio, int nivel) {
        System.out.println("\n" + "=".repeat(60));
        if (acertou) {
            System.out.println("✓ RESPOSTA CORRETA!");
            System.out.println("Prêmio atual: R$ " + String.format("%.2f", premio));
            System.out.println("Nível alcançado: " + nivel);
        } else {
            System.out.println("✗ RESPOSTA ERRADA!");
            System.out.println("Você foi eliminado!");
            System.out.println("Prêmio final: R$ 0,00");
        }
        System.out.println("=".repeat(60));
    }
    
    /**
     * Mostra estatísticas dos jogadores
     */
    public void mostrarEstatisticas(String estatisticas) {
        if (estatisticas == null || estatisticas.isEmpty()) {
            return;
        }
        
        System.out.println("\n" + "-".repeat(60));
        System.out.println("PLACAR ATUAL:");
        System.out.println("-".repeat(60));
        
        String[] jogadores = estatisticas.split(";");
        for (String jogadorInfo : jogadores) {
            if (jogadorInfo.isEmpty()) continue;
            
            String[] partes = jogadorInfo.split("\\|");
            if (partes.length >= 4) {
                String nome = partes[0];
                int nivel = Integer.parseInt(partes[1]);
                double premio = Double.parseDouble(partes[2]);
                String status = partes[3];
                
                System.out.printf("%-20s | Nível: %2d | Prêmio: R$ %10.2f | %s%n",
                    nome, nivel, premio, status);
            }
        }
        System.out.println("-".repeat(60));
    }
    
    /**
     * Mostra mensagem de fim de jogo
     */
    public void mostrarFimJogo(String vencedor, double premio) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           JOGO FINALIZADO!");
        System.out.println("=".repeat(60));
        System.out.println("VENCEDOR: " + vencedor);
        System.out.println("PRÊMIO FINAL: R$ " + String.format("%.2f", premio));
        System.out.println("=".repeat(60));
    }
    
    /**
     * Mostra mensagem de chat
     */
    public void mostrarChat(String remetente, String texto) {
        System.out.println("\n[CHAT] " + remetente + ": " + texto);
    }
}

