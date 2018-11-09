package gui.controllers;

import beans.Servico;
import db.dao.IDAO;
import db.dao.ServicoDAO;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
public class ServicosController extends Controller<Servico> implements Initializable {

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
    private TableColumn<Servico, String> colPreco;
    
    @FXML
    private TextField tfPesquisar;

    private ObservableList<Servico> lista;
    private boolean novoItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//         cellData.getValue().precoProperty().asString()
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        colPreco.setCellValueFactory(cellData ->{
            String value = String.valueOf(cellData.getValue().getPreco()).replace(".", ",");
            return cellData.getValue().precoProperty().asString(value);
        });
    }

    @FXML
    @Override
    protected void novo() {
        super.novo();
        novoItem = true;
    }

    @FXML
    private void editar() {
        super.editar(tbl, novoItem);
        novoItem = false;
    }

    @FXML
    @Override
    protected void salvar() {
        IDAO dao = new ServicoDAO();
        Servico s;

        String tipo = tfTipo.getText();
        String precoString = tfPreco.getText().replace(",", ".");

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
                s.setId(selectedObject(tbl).getId());
                lista.set(selectedIndex(tbl), s);
                dao.update(s);
            }
            changeDisable(true);
            tbl.getSelectionModel().select(s);
        }
    }

    @FXML
    @Override
    protected void excluir() {
        IDAO dao = new ServicoDAO();

        try {
            Servico s = selectedObject(tbl);

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
            Servico s = selectedObject(tbl);

            tfTipo.setText(s.getTipo());
            tfPreco.setText(String.valueOf(s.getPreco()).replace(".", ","));
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
    
    @FXML
    protected void exportar() {
        String text;
            text = "ID" + "," + "Tipo" + "," + "Preço/hora" + ",-,";
            for (Servico s : lista) {
                text += s.getId() + "," + s.getTipo() + "," + s.getPreco() + ",-,";
            }
        super.exportar("servicos", text);
    }
    
    @FXML
    @Override
    protected void pesquisar() {
        try {
            ObservableList<Servico> filtLista = FXCollections.observableArrayList();
            filtLista.setAll(lista.stream().filter(arg -> arg.getTipo().toLowerCase().contains(tfPesquisar.getText().toLowerCase())).collect(Collectors.toList()));
            tbl.setItems(filtLista);
        } catch (RuntimeException ex) {
        }
    }
    
    @Override
    protected void changeDisable(boolean opt) {
        tfTipo.setDisable(opt);
        tfPreco.setDisable(opt);
        btnSalvar.setDisable(opt);
        btnCancelar.setDisable(opt);

        btnNovo.setDisable(!opt);
        btnEditar.setDisable(!opt);
        btnExcluir.setDisable(!opt);
        tbl.setDisable(!opt);
    }

    @Override
    protected void clean() {
        tfTipo.setText("");
        tfPreco.setText("");
        tbl.getSelectionModel().select(null);
    }

    @Override
    protected void load() {
        IDAO dao = new ServicoDAO();
        lista = FXCollections.observableArrayList();
        lista.setAll(dao.read());
        tbl.setItems(lista);
    }
    
    @Override
    protected void free(){
        clean();
        lista.clear();
        lista = null;
    }
}
