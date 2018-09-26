package gui.controllers;

import beans.Servico;
import db.dao.ServicoDAO;
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
public class ServicosController implements Initializable {

    @FXML
    private TextField tfTipo;
    
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
    
    @FXML Button btnCancelar;

    @FXML
    private TableView<Servico> tbl;
    @FXML
    private TableColumn<Servico, Integer> colId;
    @FXML
    private TableColumn<Servico, String> colTipo;
    @FXML
    private TableColumn<Servico, Double> colPreco;

    private ObservableList<Servico> lista = FXCollections.observableArrayList();
    private boolean novoItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());
        
        ServicoDAO dao = new ServicoDAO();
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
        ServicoDAO dao = new ServicoDAO();
        Servico s;

        String tipo = tfTipo.getText();
        String precoString = tfPreco.getText();

        if (Validate.servico(tipo, precoString)) {
            double preco = Double.parseDouble(precoString);
            s = new Servico();
            s.setTipo(tipo);
            s.setPreco(preco);
            
            if (novoItem) {
                if (dao.create(s)) {
                    lista.add(s);
                    clean();
                }
            } else {
                lista.set(selectionedIndex(), s);
                dao.update(s);
            }
            changeDisable(true);
            tbl.getSelectionModel().select(s);
        }
    }

    @FXML
    private void excluir() {
        ServicoDAO dao = new ServicoDAO();

        try {
            Servico s = selectionedObject();

            if (AlertBox.confirmDelete()) {
                if (dao.delete(s.getId())) {
                    lista.remove(s);
                    clean();
                }
            }
        } catch (RuntimeException ex) {
            AlertBox.warning("Nenhuma coluna selecionada.");
        }
    }

    @FXML
    void selecionar() {
        try {
            Servico s = selectionedObject();

            tfTipo.setText(s.getTipo());
            tfPreco.setText(String.valueOf(s.getPreco()));
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

    private Servico selectionedObject() {
        Servico s = tbl.getItems().get(selectionedIndex());
        return s;
    }

    private void changeDisable(boolean opt) {
        tfTipo.setDisable(opt);
        tfPreco.setDisable(opt);
        btnSalvar.setDisable(opt);
        btnCancelar.setDisable(opt);

        btnNovo.setDisable(!opt);
        btnEditar.setDisable(!opt);
        btnExcluir.setDisable(!opt);
        tbl.setDisable(!opt);
    }

    private void clean() {
        tfTipo.setText("");
        tfPreco.setText("");
        tbl.getSelectionModel().select(null);
    }

}
