#!/bin/bash

# Script para compilar todos os arquivos Java do projeto

echo "Compilando projeto Jogo do Milhão - TI..."
cd src
javac *.java

if [ $? -eq 0 ]; then
    echo "Compilação concluída com sucesso!"
    echo "Arquivos .class gerados na pasta src/"
else
    echo "Erro na compilação!"
    exit 1
fi

