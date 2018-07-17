/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import beans.Recipiente;
import beans.Semente;
import beans.Servico;
import beans.ServicoPrestado;
import beans.Substrato;
import db.dao.RecipienteDAO;
import db.dao.SementeDAO;
import db.dao.ServicoDAO;
import db.dao.SubstratoDAO;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import util.AlertBox;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class PlantiosController implements Initializable {

    @FXML
    private DatePicker date;

    @FXML
    private Button btnAtualizar;

    //<editor-fold defaultstate="collapsed" desc="Elementos Semente.">
    @FXML
    private ComboBox<String> cbSemente;

    @FXML
    private TextField tfSemente;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos Recipiente.">
    @FXML
    private ComboBox<String> cbRecipiente;

    @FXML
    private TextField tfRecipiente;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos Substrato">
    @FXML
    private ComboBox<String> cbSubstrato;

    @FXML
    private Button btnDescricao;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos Servicos">
    //<editor-fold defaultstate="collapsed" desc="ComboBox e Botões">
    @FXML
    private ComboBox<String> cbServico;

    @FXML
    private Button btnAddServico;

    @FXML
    private Button btnRmServico;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tabela">
    @FXML
    private TableView<ServicoPrestado> tblServicos;

    @FXML
    private TableColumn<ServicoPrestado, String> colServicos;

    @FXML
    private TableColumn<ServicoPrestado, String> colHoras;
    //</editor-fold>

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Elementos Total">
    @FXML
    private Label lblTotal;

    @FXML
    private Button btnCalcularTotal;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tabela Principal">
    @FXML
    private TableView<?> tbl;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colSemente;

    @FXML
    private TableColumn<?, ?> colRecipiente;

    @FXML
    private TableColumn<?, ?> colSubstrato;

    @FXML
    private TableColumn<?, ?> colData;

    @FXML
    private TableColumn<?, ?> colTotal;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Listas">
    List<Semente> sementes;
    List<Substrato> substratos;
    List<Recipiente> recipientes;
    List<Servico> servicos;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Index dos seleceionados nas ComboBoxes">
    int semInd = -1;
    int subInd = -1;
    int recInd = -1;
    //</editor-fold>

    private ObservableList<ServicoPrestado> listaServico = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        readSementes();
        readSubstratos();
        readRecipientes();
        readServicos();

        colServicos.setCellValueFactory(cellData -> cellData.getValue().servicoProperty());
        colHoras.setCellValueFactory(cellData -> cellData.getValue().horaStringProperty());
        colHoras.setCellFactory(TextFieldTableCell.forTableColumn());
        colHoras.setEditable(true);
        tblServicos.setEditable(true);
        tblServicos.setItems(listaServico);
    }

    @FXML
    void atualizar(ActionEvent event) {
        readSementes();
        readSubstratos();
        readRecipientes();
        readServicos();
    }

    @FXML
    private void descreverSubstrato(ActionEvent event) {
        int i = cbSubstrato.getSelectionModel().getSelectedIndex();
        Substrato s = substratos.get(i);

        AlertBox.describe(s.getDescricao());
    }

    @FXML
    private void calcularTotal(ActionEvent event) {
        double total = 0.0,
                qtdSem = Double.parseDouble(tfSemente.getText()),
                qtdRec = Double.parseDouble(tfRecipiente.getText());

        total += sementes.get(semInd).getPreco() * qtdSem;
        total += recipientes.get(recInd).getPreco() * qtdRec;

        for (ServicoPrestado sp : listaServico) {
            double horas = Double.parseDouble(sp.getHoraString());

            for (Servico servico : servicos) {
                if (servico.getId() == sp.getSerId()) {
                    total += servico.getPreco() * horas;
                }
            }
        }

        System.out.println("Total :" + total);
    }

    //<editor-fold defaultstate="collapsed" desc="Métdos que atualizam os Index">
    @FXML
    private void selectedSemente(ActionEvent event) {
        semInd = cbSemente.getSelectionModel().getSelectedIndex();
    }

    @FXML
    private void selectedSubstrato(ActionEvent event) {
        subInd = cbSubstrato.getSelectionModel().getSelectedIndex();
    }

    @FXML
    private void selectedRecipiente(ActionEvent event) {
        recInd = cbRecipiente.getSelectionModel().getSelectedIndex();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos dos btn Servico">
    @FXML
    private void addServico(ActionEvent event) {
        ServicoPrestado sp;

        int i = cbServico.getSelectionModel().getSelectedIndex();
        Servico s = servicos.get(i);

        sp = new ServicoPrestado(s.getId(), s.getTipo(), "");

        listaServico.add(sp);
        
        cbServico.getSelectionModel().select(null);
    }

    @FXML
    private void rmServico(ActionEvent event) {
        int i = tblServicos.getSelectionModel().getSelectedIndex();

        listaServico.remove(i);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos btn CRUD">
    @FXML
    private void novo(ActionEvent event) {
    }

    @FXML
    private void editar(ActionEvent event) {
    }

    @FXML
    private void salvar(ActionEvent event) {
    }

    @FXML
    private void excluir(ActionEvent event) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos read">
    private void readSementes() {
        cbSemente.getItems().clear();
        SementeDAO dao = new SementeDAO();
        sementes = dao.read();

        sementes.forEach((semente) -> {
            cbSemente.getItems().add(semente.getNome());
        });

    }

    private void readRecipientes() {
        cbRecipiente.getItems().clear();
        RecipienteDAO dao = new RecipienteDAO();
        recipientes = dao.read();

        recipientes.forEach((recipiente) -> {
            cbRecipiente.getItems().add(recipiente.getNome());
        });
    }

    private void readSubstratos() {
        cbSubstrato.getItems().clear();
        SubstratoDAO dao = new SubstratoDAO();
        substratos = dao.read();

        substratos.forEach((substrato) -> {
            cbSubstrato.getItems().add(substrato.getNome());
        });
    }

    private void readServicos() {
        cbServico.getItems().clear();
        ServicoDAO dao = new ServicoDAO();
        servicos = dao.read();

        servicos.forEach((servico) -> {
            cbServico.getItems().add(servico.getTipo());
        });
    }
    //</editor-fold>

}
