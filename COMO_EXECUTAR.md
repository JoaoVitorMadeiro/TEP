# Como Executar o Jogo

## Status Atual

✅ **Servidor está rodando na porta 12345**

## Para Jogar

### Opção 1: Usando os Scripts

1. **Abrir um novo terminal** e executar o primeiro cliente:
   ```bash
   cd /home/joao/TEP
   ./executar_cliente.sh
   ```
   Digite seu nome quando solicitado.

2. **Abrir outro terminal** e executar o segundo cliente:
   ```bash
   cd /home/joao/TEP
   ./executar_cliente.sh
   ```
   Digite outro nome quando solicitado.

3. Quando houver pelo menos 2 jogadores conectados, o jogo iniciará automaticamente!

### Opção 2: Executando Manualmente

1. **Terminal 1 - Cliente 1:**
   ```bash
   cd /home/joao/TEP/src
   java Cliente
   ```
   Digite seu nome quando solicitado.

2. **Terminal 2 - Cliente 2:**
   ```bash
   cd /home/joao/TEP/src
   java Cliente
   ```
   Digite outro nome quando solicitado.

3. O jogo iniciará automaticamente quando houver 2+ jogadores!

## Comandos Durante o Jogo

- **0, 1, 2 ou 3**: Responder a pergunta
- **parar**: Parar e garantir o prêmio atual
- **/chat <mensagem>**: Enviar mensagem no chat
- **sair**: Desconectar do servidor

## Exemplo de Sessão

```
Cliente 1:
> Digite seu nome: João
> [Aguarda outros jogadores...]
> [Jogo iniciado!]
> [Pergunta aparece]
> 2  [Responde alternativa 2]
> [Resultado: ACERTOU!]
> [Próxima pergunta...]

Cliente 2:
> Digite seu nome: Maria
> [Jogo iniciado!]
> [Pergunta aparece]
> 1  [Responde alternativa 1]
> [Resultado: ACERTOU!]
> [Próxima pergunta...]
```

## Parar o Servidor

Para parar o servidor, você pode:
1. Pressionar Ctrl+C no terminal onde o servidor está rodando
2. Ou usar: `pkill -f "java Servidor"`

