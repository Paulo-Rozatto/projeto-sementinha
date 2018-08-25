package gui.controllers;

import beans.Recipiente;
import db.dao.RecipienteDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import util.AlertBox;
import util.Validate;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class RecipientesController implements Initializable {

    @FXML
    private TextField tfNome;
    
    @FXML
    private TextField tfVolume;
    
    @FXML
    private TextField tfPreco;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnExcluir;
    
    @FXML
    private Button btnCancelar;

    @FXML
    private TableView<Recipiente> tbl;
    @FXML
    private TableColumn<Recipiente, Integer> colId;
    @FXML
    private TableColumn<Recipiente, String> colNome;
    @FXML
    private TableColumn<Recipiente, Double> colVolume;
    @FXML
    private TableColumn<Recipiente, Double> colPreco;

    private ObservableList<Recipiente> lista = FXCollections.observableArrayList();
    private boolean novoItem;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Atribuição dos atributos da classe Recipiente para cada coluna da  tabela
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colVolume.setCellValueFactory(cellData -> cellData.getValue().volumeProperty().asObject());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        RecipienteDAO dao = new RecipienteDAO();
        lista.addAll(dao.read());
        tbl.setItems(lista);
        
    }

    @FXML
    private void novo() {
        clean();

        changeDisable(false);
        novoItem = true;
    }

    @FXML
    private void editar() {
        try {
            if (selectionedObject() != null) {
                changeDisable(false);
                novoItem = false;
            }
        } catch (RuntimeException ex) {
            AlertBox.warning("Nenhuma coluna selecionada.");
        }
    }

    @FXML
    private void salvar() {
        RecipienteDAO dao = new RecipienteDAO();
        Recipiente r;

        String nome = tfNome.getText();
        String volumeString = tfVolume.getText();
        String precoString = tfPreco.getText();

        if (Validate.recipiente(nome, volumeString, precoString)) {
            double volume = Double.parseDouble(volumeString);
            double preco = Double.parseDouble(precoString);
            r = new Recipiente();
            r.setNome(nome);
            r.setVolume(volume);
            r.setPreco(preco);
            
            if (novoItem) {
                if (dao.create(r)) {
                    lista.add(r);
                    clean();
                }
            } else {
                lista.set(selectionedIndex(), r);
                dao.update(r);
            }
            changeDisable(true);
        }
    }

    @FXML
    private void excluir() {
        RecipienteDAO dao = new RecipienteDAO();

        try {
            Recipiente r = selectionedObject();

            if (AlertBox.confirmDelete()) {
                if (dao.delete(r.getId())) {
                    lista.remove(r);
                    clean();
                }
            }
        } catch (RuntimeException ex) {
            AlertBox.warning("Nenhuma coluna selecionada.");
        }
    }

    @FXML
    private void selecionar() {
        try {
            Recipiente r = selectionedObject();

            tfNome.setText(r.getNome());
            tfVolume.setText(String.valueOf(r.getVolume()));
            tfPreco.setText(String.valueOf(r.getPreco()));
        } catch (RuntimeException ex) {
        }
    }
    
    @FXML
    private void cancelar(){
        if(novoItem){
            clean();
        }
        else{
            selecionar();
        }
        changeDisable(true);
    }

    private int selectionedIndex() {
        int selectedIndex = tbl.getSelectionModel().getSelectedIndex();
        return selectedIndex;
    }

    private Recipiente selectionedObject() {
        Recipiente r = tbl.getItems().get(selectionedIndex());
        return r;
    }

    private void changeDisable(boolean opt) {
        tfNome.setDisable(opt);
        tfVolume.setDisable(opt);
        tfPreco.setDisable(opt);
        btnSalvar.setDisable(opt);
        btnCancelar.setDisable(opt);

        btnNovo.setDisable(!opt);
        btnEditar.setDisable(!opt);
        btnExcluir.setDisable(!opt);
        tbl.setDisable(!opt);
    }

    private void clean() {
        tfNome.setText("");
        tfVolume.setText("");
        tfPreco.setText("");
    }

}
