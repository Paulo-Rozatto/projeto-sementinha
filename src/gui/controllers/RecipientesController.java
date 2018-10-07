package gui.controllers;

import beans.Recipiente;
import db.dao.RecipienteDAO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private TableColumn<Recipiente, Double> colVolume;
    @FXML
    private TableColumn<Recipiente, Double> colPreco;

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
        //inicializa a lista
        lista = FXCollections.observableArrayList();
        RecipienteDAO dao = new RecipienteDAO();
        lista.addAll(dao.read());

        //Atribuição dos atributos da classe Recipiente para cada coluna da  tabela
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colVolume.setCellValueFactory(cellData -> cellData.getValue().volumeProperty().asObject());
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());

        //coloca itens na lista
        tbl.setItems(lista);
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
                r.setId(selectedObject(tbl).getId());
                lista.set(selectedIndex(tbl), r);
                dao.update(r);
            }
            changeDisable(true);
        }
    }

    @FXML
    @Override
    protected void excluir() {
        RecipienteDAO dao = new RecipienteDAO();

        try {
            Recipiente r = selectedObject(tbl);

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
            Recipiente r = selectedObject(tbl);
            tfNome.setText(r.getNome());
            tfVolume.setText(String.valueOf(r.getVolume()));
            tfPreco.setText(String.valueOf(r.getPreco()));
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
    @Override
    protected void exportar() {
        Writer writer = null;
        String path = super.saveDialog("recipiente");
        try {
            File file = new File(path);
            writer = new BufferedWriter(new FileWriter(file));
            String text;

            text = "ID" + "," + "Nome" + "," + "Volume" + "," + "Preço" + "\n";
            writer.write(text);

            for (Recipiente r : lista) {

                text = r.getId() + "," + r.getNome() + "," + r.getVolume() + "," + r.getPreco() + "\n";

                writer.write(text);
            }
        } catch (IOException ex) {
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException ex1) {
                Logger.getLogger(RecipientesController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    @FXML
    @Override
    protected void pesquisar() {
        try {
            ObservableList<Recipiente> filtLista = FXCollections.observableArrayList();
            filtLista.setAll(lista.stream().filter(arg -> arg.getNome().toLowerCase().contains(tfPesquisar.getText().toLowerCase())).collect(Collectors.toList()));
            tbl.setItems(filtLista);
        } catch (RuntimeException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    protected void limparPesquisa() {
        super.limparPesquisa(tfPesquisar, tbl, lista);
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

}
