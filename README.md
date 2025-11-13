# Jogo do Milhão - TI (Tecnologia da Informação)

## Descrição

Jogo multiplayer em rede baseado no "Jogo do Milhão" do Silvio Santos, com perguntas exclusivamente sobre Tecnologia da Informação (TI). Múltiplos jogadores podem se conectar a um servidor central e competir respondendo perguntas sobre programação, redes, banco de dados e outras áreas de TI.

## Arquitetura

### Cliente-Servidor
- **Servidor**: Gerencia o estado do jogo, valida jogadas, distribui perguntas e coordena os jogadores
- **Cliente**: Interface para o jogador interagir com o servidor

### Protocolo de Comunicação

O jogo utiliza **sockets TCP** para comunicação entre cliente e servidor.

#### Justificativa da Escolha TCP

Escolhemos TCP (Transmission Control Protocol) pelos seguintes motivos:

1. **Confiabilidade**: TCP garante a entrega ordenada de todas as mensagens, essencial para comandos críticos do jogo
2. **Conexão Persistente**: Permite comunicação bidirecional eficiente durante toda a partida
3. **Sincronização de Estado**: Ideal para jogos que precisam manter estado sincronizado entre servidor e clientes
4. **Ordem das Mensagens**: Garante que as mensagens cheguem na ordem correta, evitando inconsistências no jogo

#### Formato das Mensagens

Todas as mensagens seguem o formato: `COMANDO|parametros`

**Comandos do Cliente:**
- `CONECTAR|nome` - Conecta o jogador ao servidor
- `RESPOSTA|numero` - Envia resposta (0-3)
- `PARAR` - Jogador decide parar e garantir prêmio
- `CHAT|mensagem` - Envia mensagem no chat

**Comandos do Servidor:**
- `CONECTADO|mensagem` - Confirma conexão
- `ERRO|mensagem` - Indica erro
- `PERGUNTA|enunciado|alt1|alt2|alt3|alt4|nivel|premio` - Envia pergunta
- `RESULTADO|acertou|premio|nivel` - Resultado da resposta
- `ESTATISTICAS|jogador1|nivel1|premio1|status1;...` - Estatísticas dos jogadores
- `JOGO_INICIADO` - Indica início do jogo
- `JOGO_FINALIZADO|vencedor|premio` - Indica fim do jogo
- `MENSAGEM|remetente|texto` - Mensagem de chat

## Requisitos

- Java JDK 8 ou superior
- Sistema operacional: Windows, Linux ou macOS

## Compilação

### Compilar o Servidor

```bash
cd src
javac Servidor.java
```

### Compilar o Cliente

```bash
cd src
javac Cliente.java
```

### Compilar Todos os Arquivos

```bash
cd src
javac *.java
```

## Execução

### 1. Iniciar o Servidor

Primeiro, inicie o servidor na porta 12345:

```bash
cd src
java Servidor
```

Você verá a mensagem:
```
========================================
  SERVIDOR DO JOGO DO MILHÃO - TI
========================================
Servidor iniciado na porta 12345
Aguardando conexões de clientes...
```

### 2. Iniciar os Clientes

Em terminais separados, execute o cliente:

```bash
cd src
java Cliente
```

**Importante**: É necessário pelo menos **2 clientes** conectados para iniciar o jogo.

### 3. Jogar

1. Digite seu nome quando solicitado
2. Aguarde outros jogadores se conectarem
3. Quando houver pelo menos 2 jogadores, o jogo inicia automaticamente
4. Responda as perguntas digitando 0, 1, 2 ou 3
5. Você pode parar a qualquer momento digitando `parar` para garantir seu prêmio atual
6. Use `/chat <mensagem>` para enviar mensagens aos outros jogadores

## Estrutura do Projeto

```
TEP/
├── src/
│   ├── Servidor.java          # Servidor principal
│   ├── GerenciadorCliente.java # Gerencia comunicação com cada cliente
│   ├── EstadoJogo.java        # Gerencia estado global do jogo
│   ├── Cliente.java           # Cliente principal
│   ├── ClienteBot.java        # Cliente bot (jogador automático)
│   ├── CriadorBot.java        # Utilitário para criar múltiplos bots
│   ├── InterfaceUsuario.java  # Interface textual do cliente
│   ├── Jogador.java           # Representa um jogador
│   ├── Pergunta.java          # Representa uma pergunta
│   ├── BancoPerguntas.java    # Banco de perguntas sobre TI
│   └── Protocolo.java         # Protocolo de comunicação
└── README.md                   # Este arquivo
```

## Funcionalidades

### Servidor
- ✅ Aceita múltiplas conexões simultâneas
- ✅ Gerencia estado do jogo (tabuleiro, placar, rodadas)
- ✅ Valida todas as jogadas
- ✅ Processa comandos dos clientes
- ✅ Lida com desconexões sem travar
- ✅ Sistema de chat entre jogadores
- ✅ Distribui perguntas por nível de dificuldade

### Cliente
- ✅ Conecta ao servidor
- ✅ Envia jogadas/comandos
- ✅ Recebe e exibe estado atual do jogo
- ✅ Interface textual intuitiva
- ✅ Sistema de chat

### Cliente Bot (Novo!)
- ✅ Jogador automatizado para testes
- ✅ Três estratégias de jogo:
  - **Aleatória**: Responde aleatoriamente
  - **Conservadora**: Para cedo para garantir prêmio
  - **Agressiva**: Tenta ir até o fim
- ✅ Criador de múltiplos bots simultâneos
- ✅ Ideal para testes e demonstrações

## Regras do Jogo

1. O jogo possui 15 níveis de dificuldade
2. Cada nível tem um prêmio associado:
   - Nível 1: R$ 1.000
   - Nível 2: R$ 5.000
   - Nível 3: R$ 10.000
   - Nível 4: R$ 30.000
   - Nível 5: R$ 50.000
   - Nível 6: R$ 100.000
   - Nível 7: R$ 300.000
   - Nível 8: R$ 500.000
   - Nível 9: R$ 1.000.000
   - Nível 10: R$ 1.500.000
   - Nível 11: R$ 2.000.000
   - Nível 12: R$ 3.000.000
   - Nível 13: R$ 5.000.000
   - Nível 14: R$ 10.000.000
   - Nível 15: R$ 1.000.000 (pergunta final)

3. Ao errar uma pergunta, o jogador é eliminado e perde todo o prêmio
4. O jogador pode parar a qualquer momento para garantir o prêmio atual
5. O último jogador restante vence o jogo
6. Todas as perguntas são sobre Tecnologia da Informação

## Testes

Para testar o jogo:

1. Abra um terminal e inicie o servidor
2. Abra pelo menos 2 terminais adicionais e inicie clientes
3. Conecte os clientes com nomes diferentes
4. O jogo iniciará automaticamente quando houver 2+ jogadores
5. Teste as funcionalidades:
   - Responder perguntas corretamente
   - Responder perguntas incorretamente (eliminação)
   - Parar e garantir prêmio
   - Chat entre jogadores
   - Desconexão de jogadores

### Testando com Bots

Para facilitar os testes, você pode usar bots automatizados:

#### Opção 1: Usando o Criador de Bots (Recomendado)

```bash
./executar_criador_bot.sh
```

O criador de bots permite:
- Criar múltiplos bots de uma vez (1-10)
- Escolher nome personalizado para cada bot
- Selecionar estratégia individual (Aleatória, Conservadora ou Agressiva)
- Gerenciar todos os bots de uma vez

#### Opção 2: Executando Bots Individualmente

```bash
# Bot com nome e estratégia personalizados
./executar_bot.sh NomeDoBot ALEATORIA

# Exemplos:
./executar_bot.sh Robozinho CONSERVADORA
./executar_bot.sh MegaBot AGRESSIVA
```

Estratégias disponíveis:
- `ALEATORIA` - Responde aleatoriamente, 10% chance de parar após nível 3
- `CONSERVADORA` - Para após nível 5 (70% chance) ou nível 7 (90% chance)
- `AGRESSIVA` - Tenta chegar longe, só para após nível 10 (30% chance)

#### Opção 3: Modo Programático

```bash
cd src
# Bot com nome automático
java ClienteBot

# Bot com nome e estratégia
java ClienteBot MeuBot CONSERVADORA
```

## Solução de Problemas

### Erro: "Não foi possível conectar ao servidor"
- Verifique se o servidor está rodando
- Verifique se a porta 12345 está disponível
- Verifique se o firewall não está bloqueando a conexão

### Erro: "Port already in use"
- Outro processo está usando a porta 12345
- Encerre o processo ou altere a porta no código

### Cliente não recebe mensagens
- Verifique a conexão de rede
- Verifique se o servidor está rodando
- Reinicie o cliente

## Autores

Desenvolvido para a disciplina de Programação de Computadores.

## Licença

Este projeto foi desenvolvido para fins educacionais.

