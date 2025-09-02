package com.caixa_eletronico.controller;

import com.caixa_eletronico.model.CaixaEletronico;
import com.caixa_eletronico.model.Conta;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class MainController {

    @FXML private Label nomeLabel;
    @FXML private Label contaLabel;
    @FXML private Label saldoLabel;
    @FXML private TextArea mensagemArea;

    private CaixaEletronico caixa;
    private Conta contaAtual;

    // Chamado pelo LoginController
    public void initData(CaixaEletronico caixa, Conta conta) {
        this.caixa = caixa;
        this.contaAtual = conta;
        atualizarInfoUsuario();
    }

    private void atualizarInfoUsuario() {
        if (contaAtual != null) {
            nomeLabel.setText("Titular: " + contaAtual.getTitular().getNome());
            contaLabel.setText("Conta: " + contaAtual.getNumeroConta());
            saldoLabel.setText(String.format("Saldo: R$ %.2f", contaAtual.getSaldoDisponivel()));
        }
    }

    @FXML
    private void handleConsultarSaldo() {
        caixa.realizarConsultaSaldo();
        atualizarInfoUsuario();
        mensagemArea.appendText("Consulta de saldo realizada.\n");
    }

    @FXML
    private void handleSaque() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Saque");
        dialog.setHeaderText("Digite o valor do saque:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(valorStr -> {
            try {
                double valor = Double.parseDouble(valorStr);
                boolean sucesso = caixa.realizarSaque(valor);  // <- captura se deu certo
                atualizarInfoUsuario();
                if (sucesso) {
                    mensagemArea.appendText("Saque de R$" + valor + " realizado.\n");

                }
                else if(valor < 0){
                    mensagemArea.appendText("Erro: O valor para saque deve ser positivo." + valor);
                }
                else {
                    mensagemArea.appendText("Erro: Saldo insuficiente para realizar o saque de R$" + valor + "\n");
                }
            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido!");
            }
        });
    }

    @FXML
    private void handleDeposito() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Depósito");
        dialog.setHeaderText("Digite o valor do depósito:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(valorStr -> {
            try {
                double valor = Double.parseDouble(valorStr);
                caixa.realizarDeposito(valor);
                atualizarInfoUsuario();
                mensagemArea.appendText("Depósito de R$" + valor + " realizado.\n");
            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido!");
            }
        });
    }

    @FXML
    private void handleTransferencia() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Transferência");
        dialog.setHeaderText("Digite dados da transferência");

        ButtonType enviarButton = new ButtonType("Enviar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(enviarButton, ButtonType.CANCEL);

        VBox box = new VBox(10);
        TextField contaDestinoField = new TextField();
        contaDestinoField.setPromptText("Conta destino");
        TextField valorField = new TextField();
        valorField.setPromptText("Valor");
        box.getChildren().addAll(contaDestinoField, valorField);
        dialog.getDialogPane().setContent(box);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == enviarButton) {
                return new Pair<>(contaDestinoField.getText(), valorField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            try {
                String contaDestino = pair.getKey();
                double valor = Double.parseDouble(pair.getValue());
                boolean sucesso = caixa.realizarTransferencia(valor, contaDestino);
                atualizarInfoUsuario();

                if(sucesso){
                    mensagemArea.appendText("Transferência de R$" + valor + " enviada para conta " + contaDestino + ".\n");
                }
                else if(valor < 0){
                    mensagemArea.appendText("Erro: O valor da transferência deve ser positivo." + "\n");
                }
                else {
                    mensagemArea.appendText("Erro: Saldo insuficiente para realizar a transferência."+ "\n");
                }

            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido!");
            }
        });
    }

    @FXML
    private void handleSair() {
        caixa.encerrarSessao();
        System.exit(0);
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}