# Arquitetura e Protocolo de Comunicação

## Arquitetura Cliente-Servidor

### Visão Geral

O jogo utiliza uma arquitetura cliente-servidor onde:

- **Servidor**: É a autoridade central do jogo. Valida todas as jogadas, gerencia o estado global e notifica os clientes sobre mudanças.
- **Cliente**: Interface para o jogador interagir com o servidor. Envia comandos e recebe atualizações do estado do jogo.

### Diagrama de Arquitetura

```
┌─────────────┐         ┌─────────────┐         ┌─────────────┐
│   Cliente 1 │────────▶│             │◀────────│   Cliente 2 │
│             │         │   Servidor  │         │             │
└─────────────┘         │             │         └─────────────┘
                        │  - Estado   │
┌─────────────┐         │  - Validação│         ┌─────────────┐
│   Cliente 3 │────────▶│  - Perguntas│◀────────│   Cliente N │
│             │         │             │         │             │
└─────────────┘         └─────────────┘         └─────────────┘
```

## Protocolo de Comunicação

### Escolha do Protocolo: TCP

**Justificativa:**

1. **Confiabilidade**: TCP garante a entrega ordenada de todas as mensagens, essencial para comandos críticos do jogo como respostas e validações.

2. **Conexão Persistente**: Permite comunicação bidirecional eficiente durante toda a partida, sem necessidade de reestabelecer conexão a cada mensagem.

3. **Sincronização de Estado**: Ideal para jogos que precisam manter estado sincronizado entre servidor e múltiplos clientes simultaneamente.

4. **Ordem das Mensagens**: Garante que as mensagens cheguem na ordem correta, evitando inconsistências no estado do jogo.

5. **Controle de Fluxo**: TCP gerencia automaticamente o controle de fluxo, evitando sobrecarga do servidor.

**Por que não UDP?**

- UDP não garante entrega das mensagens
- Mensagens podem chegar fora de ordem
- Não há controle de fluxo automático
- Perda de mensagens críticas poderia quebrar a lógica do jogo

### Formato das Mensagens

Todas as mensagens seguem o padrão: `COMANDO|parametro1|parametro2|...`

O caractere `|` (pipe) é usado como delimitador entre o comando e os parâmetros.

### Comandos do Cliente

#### CONECTAR
**Formato:** `CONECTAR|nome`

**Descrição:** Conecta o jogador ao servidor com o nome especificado.

**Exemplo:** `CONECTAR|João`

**Resposta do Servidor:** `CONECTADO|Bem-vindo ao Jogo do Milhão - TI! Aguardando outros jogadores...`

---

#### RESPOSTA
**Formato:** `RESPOSTA|numero`

**Descrição:** Envia a resposta do jogador para a pergunta atual (0, 1, 2 ou 3).

**Exemplo:** `RESPOSTA|2`

**Resposta do Servidor:** `RESULTADO|ACERTOU|50000.0|5` ou `RESULTADO|ERROU|0.0|4`

---

#### PARAR
**Formato:** `PARAR`

**Descrição:** Jogador decide parar e garantir o prêmio atual.

**Exemplo:** `PARAR`

**Resposta do Servidor:** `RESULTADO|ACERTOU|100000.0|6`

---

#### CHAT
**Formato:** `CHAT|mensagem`

**Descrição:** Envia mensagem no chat para todos os jogadores.

**Exemplo:** `CHAT|Boa sorte a todos!`

**Resposta do Servidor:** `MENSAGEM|João|Boa sorte a todos!` (broadcast para todos)

---

### Comandos do Servidor

#### CONECTADO
**Formato:** `CONECTADO|mensagem`

**Descrição:** Confirma conexão bem-sucedida do jogador.

**Exemplo:** `CONECTADO|Bem-vindo ao Jogo do Milhão - TI! Aguardando outros jogadores...`

---

#### ERRO
**Formato:** `ERRO|mensagem`

**Descrição:** Indica um erro ocorrido.

**Exemplo:** `ERRO|Você não está conectado`

---

#### PERGUNTA
**Formato:** `PERGUNTA|enunciado|alt1|alt2|alt3|alt4|nivel|premio`

**Descrição:** Envia uma pergunta para o jogador.

**Exemplo:** `PERGUNTA|O que significa HTML?|HyperText Markup Language|High Tech Modern Language|Home Tool Markup Language|Hyperlink Text Markup Language|1|1000.0`

---

#### RESULTADO
**Formato:** `RESULTADO|acertou|premio|nivel`

**Descrição:** Informa o resultado da resposta do jogador.

**Exemplo:** `RESULTADO|ACERTOU|50000.0|5` ou `RESULTADO|ERROU|0.0|4`

---

#### ESTATISTICAS
**Formato:** `ESTATISTICAS|jogador1|nivel1|premio1|status1;jogador2|nivel2|premio2|status2;...`

**Descrição:** Envia estatísticas de todos os jogadores.

**Exemplo:** `ESTATISTICAS|João|5|50000.0|ATIVO;Maria|4|30000.0|ATIVO;Pedro|3|0.0|ELIMINADO`

**Status possíveis:**
- `ATIVO`: Jogador ainda no jogo
- `ELIMINADO`: Jogador errou e foi eliminado
- `PAROU`: Jogador decidiu parar

---

#### JOGO_INICIADO
**Formato:** `JOGO_INICIADO`

**Descrição:** Indica que o jogo foi iniciado.

---

#### JOGO_FINALIZADO
**Formato:** `JOGO_FINALIZADO|vencedor|premio`

**Descrição:** Indica que o jogo foi finalizado e informa o vencedor.

**Exemplo:** `JOGO_FINALIZADO|João|1000000.0`

---

#### MENSAGEM
**Formato:** `MENSAGEM|remetente|texto`

**Descrição:** Mensagem de chat de um jogador.

**Exemplo:** `MENSAGEM|João|Boa sorte a todos!`

---

## Diagrama de Sequência

### Conexão e Início do Jogo

```
Cliente 1          Servidor          Cliente 2
   │                  │                  │
   │──CONECTAR|João──▶│                  │
   │◀──CONECTADO──────│                  │
   │                  │                  │
   │                  │◀──CONECTAR|Maria│
   │                  │──CONECTADO──────▶│
   │                  │                  │
   │                  │ (2+ jogadores)   │
   │◀──JOGO_INICIADO──│──JOGO_INICIADO──▶│
   │◀──PERGUNTA───────│──PERGUNTA───────▶│
```

### Resposta e Validação

```
Cliente              Servidor          Outros Clientes
   │                    │                    │
   │──RESPOSTA|2───────▶│                    │
   │                    │ (valida resposta)  │
   │                    │                    │
   │◀──RESULTADO────────│                    │
   │◀──ESTATISTICAS─────│──ESTATISTICAS─────▶│
   │                    │                    │
   │◀──PERGUNTA─────────│                    │
   │                    │                    │
```

### Fim do Jogo

```
Cliente              Servidor          Outros Clientes
   │                    │                    │
   │──RESPOSTA|1───────▶│                    │
   │                    │ (último jogador)   │
   │                    │                    │
   │◀──RESULTADO────────│                    │
   │◀──JOGO_FINALIZADO──│──JOGO_FINALIZADO──▶│
   │                    │                    │
```

## Gerenciamento de Estado

### Estado do Jogo

O servidor mantém um estado global do jogo que inclui:

- Lista de jogadores conectados
- Nível atual de cada jogador
- Prêmio atual de cada jogador
- Status de cada jogador (ATIVO, ELIMINADO, PAROU)
- Pergunta atual de cada jogador
- Status do jogo (INICIADO, FINALIZADO)

### Sincronização

- O servidor é a única fonte de verdade
- Todas as validações são feitas no servidor
- Mudanças de estado são broadcastadas para todos os clientes
- Cada cliente mantém apenas uma visão local do estado recebido

## Tratamento de Desconexões

### Desconexão de Cliente

1. Servidor detecta perda de conexão (IOException)
2. Remove jogador do estado do jogo
3. Notifica outros jogadores (atualiza estatísticas)
4. Verifica se o jogo deve continuar

### Desconexão do Servidor

1. Clientes detectam perda de conexão
2. Exibem mensagem de erro
3. Permitem que o jogador tente reconectar

## Threads e Concorrência

### Servidor

- **Thread Principal**: Aceita novas conexões
- **Thread por Cliente**: Cada cliente tem uma thread dedicada (GerenciadorCliente)
- **Sincronização**: Uso de `synchronized` para proteger estado compartilhado

### Cliente

- **Thread Principal**: Loop de entrada do usuário
- **Thread de Recebimento**: Recebe mensagens do servidor (daemon thread)

## Segurança e Validação

### Validações no Servidor

1. **Conexão**: Verifica se jogador está conectado antes de processar comandos
2. **Jogo Iniciado**: Verifica se o jogo foi iniciado antes de processar jogadas
3. **Jogador Ativo**: Verifica se jogador ainda está no jogo
4. **Respostas**: Valida que a resposta está no intervalo válido (0-3)
5. **Estado**: Valida estado antes de processar ações

### Tratamento de Erros

- Mensagens malformadas são ignoradas
- Erros são comunicados ao cliente via mensagem `ERRO`
- Exceções são capturadas e logadas no servidor
- Clientes desconectados são removidos graciosamente

