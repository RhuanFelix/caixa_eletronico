package com.caixa_eletronico.controller;

import com.caixa_eletronico.model.CaixaEletronico;
import com.caixa_eletronico.model.Cartao;
import com.caixa_eletronico.repository.CartaoRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtSenha;

    private CaixaEletronico caixa;

    // CONSTRUTOR VAZIO obrigatoriamente
    public LoginController() {
    }

    // Setter para injetar o CaixaEletronico
    public void setCaixa(CaixaEletronico caixa) {
        this.caixa = caixa;
    }

    @FXML
    private void handleLogin() {
        String numeroCartao = txtUsuario.getText();
        String pin = txtSenha.getText();

        if (caixa.autenticarUsuario(numeroCartao, pin)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/caixa_eletronico/view/MainView.fxml"));
                Parent root = loader.load();

                // Pega o MainController e injeta os dados
                MainController mainController = loader.getController();
                mainController.initData(caixa, caixa.getContaAtual());

                Stage stage = (Stage) txtUsuario.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Caixa Eletrônico");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Cartão ou PIN incorretos.");
            alert.showAndWait();
        }
    }
}
