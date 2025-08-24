package com.caixa_eletronico;

import com.caixa_eletronico.model.*;
import com.caixa_eletronico.persistence.*;
import com.caixa_eletronico.repository.*;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        
        // --- FASE 1: INICIALIZAÇÃO E INJEÇÃO DE DEPENDÊNCIA MANUAL ---
        System.out.println("Inicializando sistema...");

        // Criamos as implementações concretas dos nossos repositories.
        ContaRepository contaRepo = new ContaRepositoryPostgreSQL();
        CartaoRepository cartaoRepo = new CartaoRepositoryPostgreSQL();
        TransacaoRepository transacaoRepo = new TransacaoRepositoryPostgreSQL();
        
        // No futuro, o gerente seria carregado do banco de dados com um GerenteRepository.
        // Por enquanto, vamos criá-lo na mão.
        Gerente gerente = new Gerente(1, "Gerente Padrão", "G001");

        // Criamos a instância principal do CaixaEletronico, injetando todas as suas dependências.
        CaixaEletronico caixa = new CaixaEletronico("GBA-ATM-001", "Rua Principal, 123", gerente, contaRepo, cartaoRepo, transacaoRepo);

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Sistema pronto para operar.");

        // --- FASE 2: LOOP PRINCIPAL DA APLICAÇÃO ---
        while(true) {
            System.out.println("\n\n=== BEM-VINDO AO CAIXA ELETRÔNICO " + caixa.getId() + " ===");
            System.out.print("Por favor, insira o número do seu cartão (ou 'sair' para desligar): ");
            String numeroCartaoInput = scanner.nextLine();

            if (numeroCartaoInput.equalsIgnoreCase("sair")) {
                break; // Encerra o loop principal e o programa
            }

            System.out.print("Por favor, digite seu PIN: ");
            String pinInput = scanner.nextLine();

            boolean autenticado = caixa.autenticarUsuario(numeroCartaoInput, pinInput);

            // --- FASE 3: SESSÃO DO USUÁRIO (SE AUTENTICADO) ---
            if (autenticado) {
                boolean sessaoAtiva = true;
                while (sessaoAtiva) {
                    System.out.println("\n--- MENU PRINCIPAL ---");
                    System.out.println("1. Consultar Saldo");
                    System.out.println("2. Saque");
                    System.out.println("3. Depósito");
                    System.out.println("4. Transferência");
                    System.out.println("5. Sair");
                    System.out.print("Escolha uma opção: ");

                    String opcao = scanner.nextLine();

                    try {
                        switch (opcao) {
                            case "1":
                                caixa.realizarConsultaSaldo();
                                break;
                            case "2":
                                System.out.print("Digite o valor do saque: ");
                                double valorSaque = Double.parseDouble(scanner.nextLine());
                                caixa.realizarSaque(valorSaque);
                                break;
                            case "3":
                                System.out.print("Digite o valor do depósito: ");
                                double valorDeposito = Double.parseDouble(scanner.nextLine());
                                caixa.realizarDeposito(valorDeposito);
                                break;
                            case "4":
                                System.out.print("Digite o número da conta de destino: ");
                                String contaDestino = scanner.nextLine();
                                System.out.print("Digite o valor da transferência: ");
                                double valorTransferencia = Double.parseDouble(scanner.nextLine());
                                caixa.realizarTransferencia(valorTransferencia, contaDestino);
                                break;
                            case "5":
                                sessaoAtiva = false; // Quebra o loop da sessão
                                break;
                            default:
                                System.out.println("Opção inválida. Tente novamente.");
                                break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Erro: Valor inválido. Por favor, digite apenas números.");
                    }
                }
                caixa.encerrarSessao(); // Encerra a sessão no objeto CaixaEletronico
            } else {
                System.out.println("\nNão foi possível iniciar a sessão. Verifique os dados e tente novamente.");
            }
        }

        System.out.println("Desligando o sistema. Obrigado!");
        scanner.close();
    }
}