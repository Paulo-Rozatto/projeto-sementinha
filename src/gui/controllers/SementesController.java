package gui.controllers;

import beans.Semente;
import db.dao.IDAO;
import db.dao.SementeDAO;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import util.AlertBox;
import util.Validate;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class SementesController extends Controller<Semente> implements Initializable {

    @FXML
    private TextField tfNome;

    @FXML
    private TextField tfEspecie;

    @FXML
    private TextField tfPreco;

    @FXML
    private RadioButton rbGramas;

    @FXML
    private ToggleGroup tgPreco;

    @FXML
    private RadioButton rbUnidades;

    @FXML
    private ComboBox<String> cbPlantio;

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
    private ComboBox<String> cbDormencia;

    @FXML
    private TableView<Semente> tbl;

    @FXML
    private TableColumn<Semente, Integer> colId;

    @FXML
    private TableColumn<Semente, String> colNome;

    @FXML
    private TableColumn<Semente, String> colPlantio;

    @FXML
    private TableColumn<Semente, String> colPreco;

    @FXML
    private TableColumn<Semente, String> colDormencia;

    @FXML
    private TextField tfPesquisar;

    private  ObservableList<Semente> lista;
    private boolean novoItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //adição dos componetes das combo boxes
        cbDormencia.getItems().addAll("Nenhuma", "Quebra Química", "Quebra Física", "Estratificação");
        cbPlantio.getItems().addAll("Direto", "Indireto");

        //Atribuição dos atributos da classe Semente para cada coluna da  tabela
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colPlantio.setCellValueFactory(cellData -> cellData.getValue().tipoPlantioProperty());
        colPreco.setCellValueFactory(cellData -> {
            String value = String.valueOf(cellData.getValue().getPreco()).replace(".", ",");
            return cellData.getValue().precoProperty().asString(value);
        });
        colDormencia.setCellValueFactory(cellData -> cellData.getValue().dormenciaProperty());
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
        IDAO dao = new SementeDAO();
        Semente s;
        String nome, especie, plantio, dormencia, precoString;
        boolean precoEmGramas;

        nome = tfNome.getText();
        especie = tfEspecie.getText();
        plantio = cbPlantio.getValue();
        dormencia = cbDormencia.getValue();
        precoString = tfPreco.getText().replace(",",".");
        precoEmGramas = rbGramas.isSelected();

        if (Validate.semente(nome, especie, precoString, plantio, dormencia)) {
            double preco = Double.parseDouble(precoString);
            s = new Semente();
            s.setNome(nome);
            s.setEspecie(especie);
            s.setTipoPlantio(plantio);
            s.setDomercia(dormencia);
            s.setPreco(preco);
            s.setPrecoEmGramas(precoEmGramas);

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
        IDAO dao = new SementeDAO();

        try {
            Semente s = selectedObject(tbl);

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
    private void selecionar() {
        try {
            Semente s = selectedObject(tbl);

            tfNome.setText(s.getNome());
            tfEspecie.setText(s.getEspecie());
            tfPreco.setText(String.valueOf(s.getPreco()).replace(".", ","));
            rbGramas.setSelected(s.isPrecoEmGramas());
            rbUnidades.setSelected(!s.isPrecoEmGramas());
            cbPlantio.setValue(s.getTipoPlantio());
            cbDormencia.setValue(s.getDormencia());

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

        text = "ID" + "," + "Nome" + "," + "Espécie" + "," + "Preço" + "," + "Tipo de plantio" + "," + "Quebra de dormência" + ",-,";
        for (Semente s : lista) {
            String medida = s.isPrecoEmGramas() ? "grama" : "unidade";
            text += s.getId() + "," + s.getNome() + "," + s.getEspecie() + "," + "R$" + s.getPreco() + "/" + medida + "," + s.getTipoPlantio() + "," + s.getDormencia() + ",-,";
        }
        super.exportar("sementes", text);
    }

    @FXML
    @Override
    protected void pesquisar() {
        try {
            ObservableList<Semente> filtLista = FXCollections.observableArrayList();
            filtLista.setAll(lista.stream().filter(arg -> arg.getNome().toLowerCase().contains(tfPesquisar.getText().toLowerCase())).collect(Collectors.toList()));
            tbl.setItems(filtLista);
        } catch (RuntimeException ex) {
        }
    }

    @Override
    protected void changeDisable(boolean opt) {
        tfNome.setDisable(opt);
        tfEspecie.setDisable(opt);
        tfPreco.setDisable(opt);
        rbGramas.setDisable(opt);
        rbUnidades.setDisable(opt);
        cbPlantio.setDisable(opt);
        cbDormencia.setDisable(opt);
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
        tfEspecie.setText("");
        tfPreco.setText("");
        rbGramas.setSelected(true);
        cbPlantio.setValue(null);
        cbDormencia.setValue(null);
        tbl.getSelectionModel().select(null);
    }

    @Override
    protected void load() {
        IDAO dao = new SementeDAO();
        lista = FXCollections.observableArrayList();
        lista.addAll(dao.read());
        tbl.setItems(lista);
    }

    @Override
    protected void free() {
        clean();
        lista.clear();
        lista  = null;
    }
}
