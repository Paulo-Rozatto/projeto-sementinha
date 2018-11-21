package gui.controllers;

import beans.Recipiente;
import db.dao.IDAO;
import db.dao.RecipienteDAO;
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
import util.DialogBox;
import util.Validate;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class RecipientesController extends Controller<Recipiente> implements Initializable {

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
    private TableColumn<Recipiente, String> colVolume;
    @FXML
    private TableColumn<Recipiente, String> colPreco;

    @FXML
    private TextField tfPesquisar;

    private ObservableList<Recipiente> lista;

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
        colVolume.setCellValueFactory(cellData -> {
            String volume = String.valueOf(cellData.getValue().getVolume()).replace(".", ",");
            return cellData.getValue().volumeProperty().asString(volume);
        });
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
    private void editar() {
        super.editar(tbl, novoItem);
        novoItem = false;
    }

    @FXML
    @Override
    protected void salvar() {
        IDAO dao = new RecipienteDAO();
        Validate validar = new Validate();
        Recipiente r;
        String nome = tfNome.getText();
        String volumeString = tfVolume.getText().replace(",", ".");
        String precoString = tfPreco.getText().replace(",", ".");

        if (validar.recipiente(nome, volumeString, precoString)) {
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
                r.setId(selectedObject(tbl).getId());
                lista.set(selectedIndex(tbl), r);
                dao.update(r);
            }
            changeDisable(true);
            tbl.getSelectionModel().select(r);
        }
    }

    @FXML
    @Override
    protected void excluir() {
        IDAO dao = new RecipienteDAO();

        try {
            Recipiente r = selectedObject(tbl);

            DialogBox dg = new DialogBox();
            if (dg.confirmDelete()) {
                if (dao.delete(r.getId())) {
                    lista.remove(r);
                    clean();
                }
            }
        } catch (RuntimeException ex) {
            DialogBox dg = new DialogBox();
            dg.warning("Nenhuma coluna selecionada.");
        }
    }

    @FXML
    private void selecionar() {
        try {
            Recipiente r = selectedObject(tbl);
            tfNome.setText(r.getNome());
            tfVolume.setText(String.valueOf(r.getVolume()).replace(".", ","));
            tfPreco.setText(String.valueOf(r.getPreco()).replace(".", ","));
        } catch (RuntimeException ex) {
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
    protected void exportar() {
        String text;

        text = "ID" + "," + "Nome" + "," + "Volume/cm³" + "," + "Preço/cm³" + ",-,";

        for (Recipiente r : lista) {
            text += r.getId() + "," + r.getNome() + "," + r.getVolume() + "," + r.getPreco() + ",-,";
        }
        super.exportar("recipientes", text);
    }

    @FXML
    @Override
    protected void pesquisar() {
        try {
            ObservableList<Recipiente> filtLista = FXCollections.observableArrayList();
            filtLista.setAll(lista.stream().filter(arg -> arg.getNome().toLowerCase().contains(tfPesquisar.getText().toLowerCase())).collect(Collectors.toList()));
            tbl.setItems(filtLista);
        } catch (RuntimeException ex) {
        }
    }

    @Override
    protected void changeDisable(boolean opt) {
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

    @Override
    protected void clean() {
        tfNome.setText("");
        tfVolume.setText("");
        tfPreco.setText("");
        tbl.getSelectionModel().select(null);
    }

    @Override
    protected void load() {
        IDAO dao = new RecipienteDAO();
        lista = FXCollections.observableArrayList();
        lista.addAll(dao.read());
        tbl.setItems(lista);
    }

    @Override
    protected void free() {
        clean();
        lista.clear();
        lista = null;
    }
}
