import java.util.*;

/**
 * Banco de perguntas sobre TI para o jogo do milhão
 * Contém perguntas de diferentes níveis de dificuldade
 */
public class BancoPerguntas {
    private static final double[] PREMIOS = {
        1000, 5000, 10000, 30000, 50000,
        100000, 300000, 500000, 1000000, 1500000,
        2000000, 3000000, 5000000, 10000000, 1000000
    };
    
    private List<List<Pergunta>> perguntasPorNivel;
    private Random random;
    
    public BancoPerguntas() {
        this.perguntasPorNivel = new ArrayList<>();
        this.random = new Random();
        inicializarPerguntas();
    }
    
    /**
     * Inicializa todas as perguntas organizadas por nível
     */
    private void inicializarPerguntas() {
        // Nível 1 - R$ 1.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que significa a sigla 'HTML'?", 
                new String[]{"HyperText Markup Language", "High Tech Modern Language", "Home Tool Markup Language", "Hyperlink Text Markup Language"}, 
                0, 1, PREMIOS[0]),
            new Pergunta("Qual é a linguagem de programação mais usada para desenvolvimento web frontend?", 
                new String[]{"JavaScript", "Python", "Java", "C++"}, 
                0, 1, PREMIOS[0]),
            new Pergunta("O que é um 'bug' em programação?", 
                new String[]{"Um erro no código", "Um recurso novo", "Um tipo de variável", "Um método de teste"}, 
                0, 1, PREMIOS[0])
        ));
        
        // Nível 2 - R$ 5.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que significa 'HTTP'?", 
                new String[]{"HyperText Transfer Protocol", "High Tech Transfer Protocol", "Home Tool Transfer Protocol", "Hyperlink Text Transfer Protocol"}, 
                0, 2, PREMIOS[1]),
            new Pergunta("Qual estrutura de dados segue o princípio LIFO (Last In, First Out)?", 
                new String[]{"Pilha (Stack)", "Fila (Queue)", "Lista (List)", "Árvore (Tree)"}, 
                0, 2, PREMIOS[1]),
            new Pergunta("O que é um 'loop' em programação?", 
                new String[]{"Uma estrutura de repetição", "Um tipo de variável", "Um método de ordenação", "Um erro comum"}, 
                0, 2, PREMIOS[1])
        ));
        
        // Nível 3 - R$ 10.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'CSS'?", 
                new String[]{"Cascading Style Sheets", "Computer Style Sheets", "Creative Style Sheets", "Colorful Style Sheets"}, 
                0, 3, PREMIOS[2]),
            new Pergunta("Qual é o protocolo padrão para envio de emails?", 
                new String[]{"SMTP", "HTTP", "FTP", "TCP"}, 
                0, 3, PREMIOS[2]),
            new Pergunta("O que significa 'API'?", 
                new String[]{"Application Programming Interface", "Advanced Programming Interface", "Application Program Integration", "Advanced Program Integration"}, 
                0, 3, PREMIOS[2])
        ));
        
        // Nível 4 - R$ 30.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'SQL'?", 
                new String[]{"Structured Query Language", "Simple Query Language", "Standard Query Language", "System Query Language"}, 
                0, 4, PREMIOS[3]),
            new Pergunta("Qual é a complexidade de tempo do algoritmo de ordenação QuickSort no pior caso?", 
                new String[]{"O(n²)", "O(n log n)", "O(n)", "O(log n)"}, 
                0, 4, PREMIOS[3]),
            new Pergunta("O que é 'REST' em desenvolvimento web?", 
                new String[]{"Representational State Transfer", "Remote State Transfer", "Resource State Transfer", "Representative State Transfer"}, 
                0, 4, PREMIOS[3])
        ));
        
        // Nível 5 - R$ 50.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'OOP'?", 
                new String[]{"Object-Oriented Programming", "Online Object Programming", "Optimized Object Programming", "Organized Object Programming"}, 
                0, 5, PREMIOS[4]),
            new Pergunta("Qual é a porta padrão para o protocolo HTTPS?", 
                new String[]{"443", "80", "8080", "21"}, 
                0, 5, PREMIOS[4]),
            new Pergunta("O que é 'Git'?", 
                new String[]{"Sistema de controle de versão", "Linguagem de programação", "Banco de dados", "Framework web"}, 
                0, 5, PREMIOS[4])
        ));
        
        // Nível 6 - R$ 100.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'DNS'?", 
                new String[]{"Domain Name System", "Data Name System", "Digital Name System", "Dynamic Name System"}, 
                0, 6, PREMIOS[5]),
            new Pergunta("Qual é a complexidade de espaço do algoritmo de ordenação MergeSort?", 
                new String[]{"O(n)", "O(1)", "O(n log n)", "O(log n)"}, 
                0, 6, PREMIOS[5]),
            new Pergunta("O que é 'JSON'?", 
                new String[]{"JavaScript Object Notation", "Java Object Notation", "JavaScript Online Notation", "Java Online Notation"}, 
                0, 6, PREMIOS[5])
        ));
        
        // Nível 7 - R$ 300.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'MVC'?", 
                new String[]{"Model-View-Controller", "Model-View-Component", "Method-View-Controller", "Model-Variable-Controller"}, 
                0, 7, PREMIOS[6]),
            new Pergunta("Qual é a diferença entre '===' e '==' em JavaScript?", 
                new String[]{"===' compara valor e tipo, '==' compara apenas valor", "Não há diferença", "===' compara apenas valor, '==' compara valor e tipo", "Ambos comparam apenas valor"}, 
                0, 7, PREMIOS[6]),
            new Pergunta("O que é 'AJAX'?", 
                new String[]{"Asynchronous JavaScript and XML", "Advanced JavaScript and XML", "Asynchronous Java and XML", "Advanced Java and XML"}, 
                0, 7, PREMIOS[6])
        ));
        
        // Nível 8 - R$ 500.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'SOLID' em programação orientada a objetos?", 
                new String[]{"Conjunto de princípios de design", "Linguagem de programação", "Framework", "Banco de dados"}, 
                0, 8, PREMIOS[7]),
            new Pergunta("Qual é a complexidade de tempo do algoritmo de busca binária?", 
                new String[]{"O(log n)", "O(n)", "O(n log n)", "O(1)"}, 
                0, 8, PREMIOS[7]),
            new Pergunta("O que é 'Docker'?", 
                new String[]{"Plataforma de containerização", "Linguagem de programação", "Banco de dados", "Framework web"}, 
                0, 8, PREMIOS[7])
        ));
        
        // Nível 9 - R$ 1.000.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'Microservices'?", 
                new String[]{"Arquitetura de software com serviços independentes", "Tipo de banco de dados", "Linguagem de programação", "Protocolo de rede"}, 
                0, 9, PREMIOS[8]),
            new Pergunta("O que é 'CI/CD'?", 
                new String[]{"Continuous Integration/Continuous Deployment", "Computer Integration/Computer Deployment", "Code Integration/Code Deployment", "Continuous Integration/Code Deployment"}, 
                0, 9, PREMIOS[8]),
            new Pergunta("O que é 'NoSQL'?", 
                new String[]{"Banco de dados não relacional", "Linguagem de consulta", "Protocolo de rede", "Framework web"}, 
                0, 9, PREMIOS[8])
        ));
        
        // Nível 10 - R$ 1.500.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'Kubernetes'?", 
                new String[]{"Sistema de orquestração de containers", "Linguagem de programação", "Banco de dados", "Framework web"}, 
                0, 10, PREMIOS[9]),
            new Pergunta("O que é 'GraphQL'?", 
                new String[]{"Linguagem de consulta para APIs", "Banco de dados", "Linguagem de programação", "Protocolo de rede"}, 
                0, 10, PREMIOS[9]),
            new Pergunta("O que é 'Blockchain'?", 
                new String[]{"Tecnologia de registro distribuído", "Linguagem de programação", "Banco de dados relacional", "Protocolo HTTP"}, 
                0, 10, PREMIOS[9])
        ));
        
        // Nível 11 - R$ 2.000.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'Machine Learning'?", 
                new String[]{"Subcampo da inteligência artificial", "Linguagem de programação", "Banco de dados", "Protocolo de rede"}, 
                0, 11, PREMIOS[10]),
            new Pergunta("O que é 'DevOps'?", 
                new String[]{"Cultura que integra desenvolvimento e operações", "Linguagem de programação", "Framework", "Banco de dados"}, 
                0, 11, PREMIOS[10]),
            new Pergunta("O que é 'Big Data'?", 
                new String[]{"Grandes volumes de dados", "Linguagem de programação", "Protocolo de rede", "Framework web"}, 
                0, 11, PREMIOS[10])
        ));
        
        // Nível 12 - R$ 3.000.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'Cloud Computing'?", 
                new String[]{"Entrega de serviços de computação pela internet", "Linguagem de programação", "Banco de dados", "Protocolo de rede"}, 
                0, 12, PREMIOS[11]),
            new Pergunta("O que é 'IaC' (Infrastructure as Code)?", 
                new String[]{"Gerenciamento de infraestrutura através de código", "Linguagem de programação", "Banco de dados", "Framework"}, 
                0, 12, PREMIOS[11]),
            new Pergunta("O que é 'Serverless'?", 
                new String[]{"Modelo de execução sem gerenciar servidores", "Sem servidor físico", "Linguagem de programação", "Protocolo de rede"}, 
                0, 12, PREMIOS[11])
        ));
        
        // Nível 13 - R$ 5.000.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'Quantum Computing'?", 
                new String[]{"Computação usando mecânica quântica", "Tipo de banco de dados", "Linguagem de programação", "Protocolo de rede"}, 
                0, 13, PREMIOS[12]),
            new Pergunta("O que é 'Edge Computing'?", 
                new String[]{"Processamento próximo à fonte de dados", "Tipo de servidor", "Linguagem de programação", "Banco de dados"}, 
                0, 13, PREMIOS[12]),
            new Pergunta("O que é 'Zero Trust Security'?", 
                new String[]{"Modelo de segurança que não confia em nada por padrão", "Tipo de criptografia", "Protocolo de rede", "Framework"}, 
                0, 13, PREMIOS[12])
        ));
        
        // Nível 14 - R$ 10.000.000
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'Neural Network'?", 
                new String[]{"Rede de neurônios artificiais para aprendizado", "Tipo de conexão de rede", "Protocolo de comunicação", "Banco de dados"}, 
                0, 14, PREMIOS[13]),
            new Pergunta("O que é 'Distributed Systems'?", 
                new String[]{"Sistemas com componentes em múltiplas máquinas", "Tipo de banco de dados", "Linguagem de programação", "Framework"}, 
                0, 14, PREMIOS[13]),
            new Pergunta("O que é 'CAP Theorem'?", 
                new String[]{"Teorema sobre consistência, disponibilidade e partição", "Teorema sobre criptografia", "Teorema sobre algoritmos", "Teorema sobre redes"}, 
                0, 14, PREMIOS[13])
        ));
        
        // Nível 15 - R$ 1.000.000 (pergunta final)
        perguntasPorNivel.add(Arrays.asList(
            new Pergunta("O que é 'Event Sourcing'?", 
                new String[]{"Padrão de armazenamento de eventos como fonte da verdade", "Tipo de banco de dados", "Linguagem de programação", "Protocolo de rede"}, 
                0, 15, PREMIOS[14]),
            new Pergunta("O que é 'CQRS' (Command Query Responsibility Segregation)?", 
                new String[]{"Padrão que separa leitura e escrita", "Tipo de banco de dados", "Linguagem de programação", "Framework"}, 
                0, 15, PREMIOS[14]),
            new Pergunta("O que é 'Reactive Programming'?", 
                new String[]{"Paradigma de programação assíncrona orientada a fluxos", "Linguagem de programação", "Banco de dados", "Protocolo de rede"}, 
                0, 15, PREMIOS[14])
        ));
    }
    
    /**
     * Obtém uma pergunta aleatória do nível especificado
     */
    public Pergunta obterPergunta(int nivel) {
        if (nivel < 1 || nivel > 15) {
            return null;
        }
        
        List<Pergunta> perguntasNivel = perguntasPorNivel.get(nivel - 1);
        int indiceAleatorio = random.nextInt(perguntasNivel.size());
        return perguntasNivel.get(indiceAleatorio);
    }
    
    /**
     * Retorna o prêmio garantido para um nível
     */
    public static double getPremioGarantido(int nivel) {
        if (nivel < 1 || nivel > 15) {
            return 0;
        }
        return PREMIOS[nivel - 1];
    }
}

