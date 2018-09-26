package gui.controllers;

import beans.Substrato;
import db.dao.SubstratoDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import util.AlertBox;
import util.Validate;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class SubstratosController implements Initializable {

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfPreco;

    @FXML
    private TextArea taDescricao;

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
    private TableView<Substrato> tbl;

    @FXML
    private TableColumn<Substrato, Integer> colId;

    @FXML
    private TableColumn<Substrato, String> colNome;

    @FXML
    private TableColumn<Substrato, Double> colPreco;

    private ObservableList<Substrato> lista = FXCollections.observableArrayList();
    private boolean novoItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        SubstratoDAO dao = new SubstratoDAO();
        lista.addAll(dao.read());

        tbl.setItems(lista);
    }

    @FXML
    void novo() {
        clean();

        changeDisable(false);
        novoItem = true;
    }

    @FXML
    void editar() {
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
    void salvar() {
        SubstratoDAO dao = new SubstratoDAO();
        Substrato s;

        String nome = tfNome.getText();
        String precoString = tfPreco.getText();
        String descricao = taDescricao.getText();

        if (Validate.substrato(nome, precoString, descricao)) {
            double preco = Double.parseDouble(precoString);
            s = new Substrato();
            s.setNome(nome);
            s.setPreco(preco);
            s.setDescricao(descricao);

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
        }
    }

    @FXML
    void excluir() {
        SubstratoDAO dao = new SubstratoDAO();

        try {
            Substrato s = selectionedObject();

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
    private void cancelar(){
        if(novoItem){
            clean();
        }
        else{
            selecionar();
        }
        changeDisable(true);
    }

    @FXML
    void selecionar() {
        try {
            Substrato s = selectionedObject();

            tfNome.setText(s.getNome());
            tfPreco.setText(String.valueOf(s.getPreco()));
            taDescricao.setText(s.getDescricao());
        } catch (RuntimeException ex) {
        }
    }

    private int selectionedIndex() {
        int selectedIndex = tbl.getSelectionModel().getSelectedIndex();
        return selectedIndex;
    }

    private Substrato selectionedObject() {
        Substrato s = tbl.getItems().get(selectionedIndex());
        return s;
    }

    private void changeDisable(boolean opt) {
        tfNome.setDisable(opt);
        tfPreco.setDisable(opt);
        taDescricao.setDisable(opt);
        btnSalvar.setDisable(opt);
        btnCancelar.setDisable(opt);

        btnNovo.setDisable(!opt);
        btnEditar.setDisable(!opt);
        btnExcluir.setDisable(!opt);
        tbl.setDisable(!opt);
    }

    private void clean() {
        tfNome.setText("");
        tfPreco.setText("");
        taDescricao.setText("");
        tbl.getSelectionModel().select(null);
    }
}
