package com.caixa_eletronico;

import com.caixa_eletronico.controller.LoginController;
import com.caixa_eletronico.model.CaixaEletronico;
import com.caixa_eletronico.model.Gerente;
import com.caixa_eletronico.persistence.CartaoRepositoryPostgreSQL;
import com.caixa_eletronico.persistence.ContaRepositoryPostgreSQL;
import com.caixa_eletronico.persistence.TransacaoRepositoryPostgreSQL;
import com.caixa_eletronico.repository.CartaoRepository;
import com.caixa_eletronico.repository.ContaRepository;
import com.caixa_eletronico.repository.TransacaoRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private CaixaEletronico caixa;

    @Override
    public void start(Stage stage) throws Exception {

        // --- Cria os repositórios ---
        CartaoRepository cartaoRepo = new CartaoRepositoryPostgreSQL();
        ContaRepository contaRepo = new ContaRepositoryPostgreSQL();
        TransacaoRepository transacaoRepo = new TransacaoRepositoryPostgreSQL();

        // --- Cria gerente ---
        Gerente gerente = new Gerente(1L, "Gerente Padrão", "gerente@banco.com");

        // --- Cria CaixaEletronico com todos os parâmetros ---
        caixa = new CaixaEletronico("ATM-001", "Rua Principal, 123", gerente,
                contaRepo, cartaoRepo, transacaoRepo);

        // --- Carrega tela de login com controllerFactory ---
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/caixa_eletronico/view/LoginView.fxml"));
        loader.setControllerFactory(c -> {
            LoginController controller = new LoginController();
            controller.setCaixa(caixa); // injeta o CaixaEletronico
            return controller;
        });

        Parent root = loader.load();

        // --- Configura a cena e mostra ---
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Caixa Eletrônico - Login");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}