package com.poo2ex6.controller;

import com.poo2ex6.model.ImcRegistry;
import com.poo2ex6.service.ImcService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.security.PrivateKey;
import java.util.List;


public class ImcController {
    private final ImcService imcService;

    public ImcController(ImcService imcService) {
        this.imcService = imcService;
    }


    @FXML
    private Button btnCalcular;

    @FXML
    private Button btnCarregar;

    @FXML
    private Button btnSalvar;

    @FXML
    private TextField inputAltura;

    @FXML
    private TextField inputNome;

    @FXML
    private TextField inputPeso;

    @FXML
    private Text outputImc;

    @FXML
    private Text outputClassificacao;

    @FXML
    private TableView<ImcRegistry> tableImc;

    @FXML
    private TableColumn<ImcRegistry, Long> colId;

    @FXML
    private TableColumn<ImcRegistry, String> colNome;

    @FXML
    private TableColumn<ImcRegistry, Float> colAltura;

    @FXML
    private TableColumn<ImcRegistry, Float> colPeso;

    @FXML
    private TableColumn<ImcRegistry, Float> colImc;

    private ObservableList<ImcRegistry> tableImcItems = FXCollections.observableArrayList();


    @FXML
    void initialize(){
        Platform.runLater(this::setupTableImc);
    }

    private void setupTableImc() {
        this.tableImc.setItems(tableImcItems);

        this.setupColId();
        this.setupColNome();
        this.setupColPeso();
        this.setupColAltura();
        this.setupColImc();
    }

    private void setupColId() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    private void setupColNome() {
        this.colNome.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void setupColAltura() {
        this.colAltura.setCellValueFactory(new PropertyValueFactory<>("height"));

        this.colAltura.setCellFactory(column -> new TableCell<>(){
            @Override
            protected void updateItem(Float item, boolean empty){
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
    }

    private void setupColPeso() {
        this.colPeso.setCellValueFactory(new PropertyValueFactory<>("weight"));

        this.colPeso.setCellFactory(column -> new TableCell<>(){
            @Override
            protected void updateItem(Float item, boolean empty){
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
    }

    private void setupColImc() {
        this.colImc.setCellValueFactory(new PropertyValueFactory<>("imc"));

        this.colImc.setCellFactory(column -> new TableCell<>(){
            @Override
            protected void updateItem(Float item, boolean empty){
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
    }


    @FXML
    void calcular(ActionEvent event) {
        String nome;
        float altura;
        float peso;

        nome = inputNome.getText();

        try {
            altura = Float.parseFloat(inputAltura.getText());
        } catch (NumberFormatException e) {
            showAlertError("Entrada inválida", "Entrada de altura inválida.");
            return;
        }

        try {
            peso = Float.parseFloat(inputPeso.getText());
        } catch (NumberFormatException e) {
            showAlertError("Entrada inválida", "Entrada de peso inválida.");
            return;
        }

        var thread = getCalcularThread(nome, altura, peso);

        thread.start();
    }

    private Thread getCalcularThread(String nome, Float altura, Float peso) {
        Task<Float> task = new Task<>() {
            @Override
            protected Float call() throws Exception {
                return imcService.calculateImc(nome, altura, peso);
            }
        };

        task.setOnFailed(e -> {
//            showAlertError("Erro", "Erro ao calcular IMC");
            showAlertError("Erro ao calcular IMC", e.getSource().getException().getMessage());
        });

        task.setOnSucceeded(e -> {
            outputImc.setText(
                    String.format("%.2f", task.getValue())
            );
            outputClassificacao.setText(
                    getClassificacaoImc(task.getValue())
            );
        });

        return new Thread(task);
    }

    private String getClassificacaoImc(Float value) {
        if (value < 18.5)
            return "Abaixo do peso";
        else if (value < 24.9)
            return "Peso ideal";
        else if (value < 29.9)
            return "Sobrepeso";
        else if (value < 34.9)
            return "Obesidade grau I";
        else if (value < 39.9)
            return "Obesidade grau II";
        else
            return "Obesidade grau III";
    }


    @FXML
    void carregar(ActionEvent event) {
        getCarregarThread().start();
    }

    private Thread getCarregarThread(){
        Task<List<ImcRegistry>> task = new Task<>() {
            @Override
            protected List<ImcRegistry> call() throws Exception {
                return imcService.getAllCalculations();
            }
        };

        task.setOnFailed(e -> {
//            showAlertError("Erro", "Erro ao carregar IMC");
            showAlertError("Erro ao carregar IMC", e.getSource().getException().getMessage());
        });

        task.setOnSucceeded(e -> {
            updateTableImc(task.getValue());
        });

        return new Thread(task);
    }

    private void updateTableImc(List<ImcRegistry> values) {
        this.tableImcItems.clear();
        this.tableImcItems.addAll(values);
    }


    @FXML
    void salvar(ActionEvent event) {
        getSalvarThread().start();
    }

    private Thread getSalvarThread(){
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                imcService.saveLastCalculation();
                return null;
            }
        };

        task.setOnSucceeded(e -> {
            inputNome.clear();
            inputAltura.clear();
            inputPeso.clear();
            outputImc.setText("0.00");
            outputClassificacao.setText("Classificação");
            showAlertInfo("Sucesso", "IMC salvo com sucesso");
        });

        task.setOnFailed(e -> {
//            showAlertError("Erro", "Erro ao salvar IMC");
            showAlertError("Erro ao salvar IMC", e.getSource().getException().getMessage());
        });

        return new Thread(task);
    }


    void showAlertError(String title, String text){
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(text);

        alert.showAndWait();
    }

    void showAlertInfo(String title, String text){
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(text);

        alert.showAndWait();
    }
}
