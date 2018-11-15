package gui.controllers;

import beans.Substrato;
import db.dao.IDAO;
import db.dao.SubstratoDAO;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import util.DialogBox;
import util.Validate;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class SubstratosController extends Controller<Substrato> implements Initializable {

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
    private TableColumn<Substrato, String> colPreco;

    @FXML
    private TextField tfPesquisar;

    private ObservableList<Substrato> lista;
    private boolean novoItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colPreco.setCellValueFactory(cellData -> {
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
    void editar() {
        super.editar(tbl, novoItem);
        novoItem = false;
    }

    @FXML
    @Override
    protected void salvar() {
        IDAO dao = new SubstratoDAO();
        Substrato s;

        String nome = tfNome.getText();
        String precoString = tfPreco.getText().replace(",", ".");
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
        IDAO dao = new SubstratoDAO();

        try {
            Substrato s = selectedObject(tbl);

            DialogBox dg = new DialogBox();
            if (dg.confirmDelete()) {
                if (dao.delete(s.getId())) {
                    lista.remove(s);
                    clean();
                }
            }
        } catch (RuntimeException ex) {
            DialogBox dg = new DialogBox();
            dg.warning("Nenhuma coluna selecionada.");
        }

    }

    @FXML
    private void cancelar() {
        if (novoItem) {
            clean();
        } else {
            selecionar();
        }
        changeDisable(true);
    }

    @FXML
    void selecionar() {
        try {
            Substrato s = selectedObject(tbl);

            tfNome.setText(s.getNome());
            tfPreco.setText(String.valueOf(s.getPreco()).replace(".", ","));
            taDescricao.setText(s.getDescricao());
        } catch (RuntimeException ex) {
        }
    }

    @FXML
    protected void exportar() {
        String text;

        text = "ID" + "," + "Nome" + "," + "Preço/cm³" + "," + "Descrição" + ",-,";

        for (Substrato s : lista) {
            text += s.getId() + "," + s.getNome() + "," + s.getPreco() + "," + s.getDescricao().replaceAll("\n", " ") + ",-,";
        }
        super.exportar("substratos", text);
    }

    @FXML
    @Override
    protected void pesquisar() {
        try {
            ObservableList<Substrato> filtLista = FXCollections.observableArrayList();
            filtLista.setAll(lista.stream().filter(arg -> arg.getNome().toLowerCase().contains(tfPesquisar.getText().toLowerCase())).collect(Collectors.toList()));
            tbl.setItems(filtLista);
        } catch (RuntimeException ex) {
        }
    }

    @Override
    protected void changeDisable(boolean opt) {
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

    @Override
    protected void clean() {
        tfNome.setText("");
        tfPreco.setText("");
        taDescricao.setText("");
        tbl.getSelectionModel().select(null);
    }

    @Override
    protected void load() {
        IDAO dao = new SubstratoDAO();
        lista = FXCollections.observableArrayList();
        lista.setAll(dao.read());
        tbl.setItems(lista);
    }

    @Override
    protected void free() {
        clean();
        lista.clear();
        lista = null;
    }
}
