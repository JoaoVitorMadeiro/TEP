# Como Usar os Bots - Jogo do Milhão TI

## Introdução

O sistema de bots permite criar jogadores automáticos que participam do jogo sem intervenção humana. Isso é útil para:
- Testar o servidor com múltiplos jogadores
- Demonstrar o funcionamento do jogo
- Desenvolver e testar novas funcionalidades
- Simular partidas completas

## Executando Bots

### 1. Criador de Bots Interativo (Mais Fácil)

O jeito mais simples de criar e gerenciar bots:

```bash
./executar_criador_bot.sh
```

**Passo a passo:**

1. Execute o comando acima
2. Informe quantos bots deseja criar (1-10)
3. Para cada bot:
   - Digite um nome (ou pressione Enter para nome automático)
   - Escolha a estratégia:
     - 1: ALEATORIA
     - 2: CONSERVADORA
     - 3: AGRESSIVA
4. Os bots serão conectados automaticamente
5. Pressione Enter quando quiser desconectar todos os bots

**Exemplo de uso:**

```
$ ./executar_criador_bot.sh

========================================
  CRIADOR DE BOTS - JOGO DO MILHÃO
========================================

Quantos bots deseja criar? (1-10): 3

Estratégias disponíveis:
  1. ALEATORIA - Responde aleatoriamente
  2. CONSERVADORA - Para cedo para garantir prêmio
  3. AGRESSIVA - Tenta ir até o fim

--- Bot 1 ---
Nome do bot (ou Enter para gerar automático): Robozinho
Estratégia (1-3): 2
✓ Bot 'Robozinho' criado com estratégia CONSERVADORA

--- Bot 2 ---
Nome do bot (ou Enter para gerar automático): 
Estratégia (1-3): 1
✓ Bot 'Bot_2' criado com estratégia ALEATORIA

--- Bot 3 ---
Nome do bot (ou Enter para gerar automático): MegaBot
Estratégia (1-3): 3
✓ Bot 'MegaBot' criado com estratégia AGRESSIVA
```

### 2. Executando Bots Individualmente

Para ter mais controle, execute cada bot em um terminal separado:

```bash
./executar_bot.sh NomeDoBot ESTRATEGIA
```

**Exemplos:**

```bash
# Terminal 1
./executar_bot.sh Robozinho CONSERVADORA

# Terminal 2
./executar_bot.sh MegaBot AGRESSIVA

# Terminal 3
./executar_bot.sh RandomBot ALEATORIA
```

### 3. Modo Programático (Avançado)

Execute diretamente via Java:

```bash
cd src

# Bot com nome e estratégia automáticos
java ClienteBot

# Bot com nome específico e estratégia aleatória
java ClienteBot MeuBot ALEATORIA

# Bot com estratégia conservadora
java ClienteBot BotConservador CONSERVADORA

# Bot com estratégia agressiva
java ClienteBot BotAgressivo AGRESSIVA
```

## Estratégias dos Bots

### ALEATORIA
- **Comportamento**: Responde de forma completamente aleatória
- **Parada**: 10% de chance de parar após o nível 3
- **Uso**: Testes gerais, comportamento imprevisível

### CONSERVADORA
- **Comportamento**: Tenta garantir prêmio parando cedo
- **Parada**: 
  - 70% de chance de parar após nível 5 (R$ 50.000)
  - 90% de chance de parar após nível 7 (R$ 300.000)
- **Uso**: Simular jogador cauteloso, testes de parada

### AGRESSIVA
- **Comportamento**: Tenta ir até o fim
- **Parada**: Apenas 30% de chance de parar após nível 10 (R$ 1.500.000)
- **Uso**: Testar níveis avançados, simular jogador arriscado

## Cenários de Teste

### Teste Básico - 2 Bots

**Terminal 1:** Servidor
```bash
./executar_servidor.sh
```

**Terminal 2:** Bot 1
```bash
./executar_bot.sh Bot1 ALEATORIA
```

**Terminal 3:** Bot 2
```bash
./executar_bot.sh Bot2 ALEATORIA
```

### Teste com Múltiplos Bots

**Terminal 1:** Servidor
```bash
./executar_servidor.sh
```

**Terminal 2:** Criador de Bots
```bash
./executar_criador_bot.sh
# Criar 5 bots com estratégias variadas
```

### Teste de Estratégias

Compare o desempenho de diferentes estratégias:

**Terminal 1:** Servidor
```bash
./executar_servidor.sh
```

**Terminal 2:** Bot Conservador
```bash
./executar_bot.sh Conservador CONSERVADORA
```

**Terminal 3:** Bot Agressivo
```bash
./executar_bot.sh Agressivo AGRESSIVA
```

**Terminal 4:** Bot Aleatório
```bash
./executar_bot.sh Aleatorio ALEATORIA
```

### Teste de Carga

Teste o servidor com muitos jogadores simultâneos:

1. Inicie o servidor
2. Use o criador de bots para criar 10 bots
3. Observe o desempenho do servidor
4. Verifique o tratamento de eliminações e vitórias

## Exemplo de Saída do Bot

```
========================================
  BOT DO JOGO DO MILHÃO - TI
========================================
Nome: Robozinho
Estratégia: CONSERVADORA
========================================

[BOT Robozinho] Conectado ao servidor
[BOT Robozinho] Servidor: Bem-vindo ao Jogo do Milhão - TI! Aguardando outros jogadores...
[BOT Robozinho] === JOGO INICIADO ===

[BOT Robozinho] --- Nível 1 (R$ 1000.00) ---
[BOT Robozinho] Pergunta: O que significa a sigla 'HTML'?
[BOT Robozinho] Resposta escolhida: 0
[BOT Robozinho] ✓ ACERTOU! Nível: 1 | Prêmio: R$ 1000.00

[BOT Robozinho] --- Nível 2 (R$ 5000.00) ---
[BOT Robozinho] Pergunta: O que significa 'HTTP'?
[BOT Robozinho] Resposta escolhida: 2
[BOT Robozinho] ✗ ERROU! Eliminado. Prêmio final: R$ 0.00

[BOT Robozinho] === JOGO FINALIZADO ===
[BOT Robozinho] Vencedor: MegaBot
[BOT Robozinho] Prêmio: R$ 50000.00

[BOT Robozinho] Desconectado do servidor.
```

## Dicas

1. **Sempre inicie o servidor primeiro** antes de conectar os bots
2. **Aguarde 2+ bots conectados** para o jogo iniciar automaticamente
3. **Use estratégias variadas** para tornar o jogo mais interessante
4. **Combine bots com jogadores humanos** para testes mais realistas
5. **Observe os logs do servidor** para entender o fluxo do jogo
6. **Use Ctrl+C** para interromper bots individuais

## Solução de Problemas

### Bot não conecta
- Verifique se o servidor está rodando
- Confirme que a porta 12345 está disponível
- Verifique se há firewall bloqueando

### Jogo não inicia
- Certifique-se de que pelo menos 2 jogadores (bots ou humanos) estão conectados
- Aguarde alguns segundos após a conexão do segundo jogador

### Bot trava ou não responde
- Interrompa com Ctrl+C
- Verifique os logs do servidor para erros
- Reinicie o bot

### Estratégia não funciona como esperado
- Lembre-se que as estratégias envolvem probabilidade
- Conservadora não SEMPRE para cedo, apenas tem maior chance
- Agressiva pode parar antes do fim em alguns casos
- Aleatória é completamente imprevisível por design
