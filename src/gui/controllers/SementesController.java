package gui.controllers;

import beans.Semente;
import db.dao.SementeDAO;
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
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
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
    private TableColumn<Semente, Double> colPreco;

    @FXML
    private TableColumn<Semente, String> colDormencia;
    
    @FXML
    private TextField tfPesquisar;

    private ObservableList<Semente> lista = FXCollections.observableArrayList();
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
        colPreco.setCellValueFactory(cellData -> cellData.getValue().precoProperty().asObject());
        colDormencia.setCellValueFactory(cellData -> cellData.getValue().dormenciaProperty());

        SementeDAO dao = new SementeDAO();
        lista.addAll(dao.read());
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
        SementeDAO dao = new SementeDAO();
        Semente s;
        String nome, especie, plantio, dormencia, precoString;
        boolean precoEmGramas;

        nome = tfNome.getText();
        especie = tfEspecie.getText();
        plantio = cbPlantio.getValue();
        dormencia = cbDormencia.getValue();
        precoString = tfPreco.getText();
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
                lista.set(selectedIndex(tbl), s);
                dao.update(s);
            }
            changeDisable(true);
        }
    }

    @FXML
    @Override
    protected void excluir() {
        SementeDAO dao = new SementeDAO();

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
            tfPreco.setText(String.valueOf(s.getPreco()));
            rbGramas.setSelected(s.isPrecoEmGramas());
            rbUnidades.setSelected(!s.isPrecoEmGramas());
            cbPlantio.setValue(s.getTipoPlantio());
            cbDormencia.setValue(s.getDormencia());

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
    @Override
    protected void exportar() {
        Writer writer = null;
        String path = super.saveDialog("Semente");
        try {
            File file = new File(path);
            writer = new BufferedWriter(new FileWriter(file));
            String text;

            text = "ID" + "," + "Nome" + "," + "Espécie" + "," +"Preço" + ","+ "Medida"+ ","+ "Tipo de plantio" + ","+ "Quebra de dormência" + "\n";
            writer.write(text);
            
            for (Semente s : lista) {
                String medida = s.isPrecoEmGramas() ? "gramas":"unidade";
                
                text = s.getId() + "," + s.getNome()+ "," + s.getEspecie() + "," + s.getPreco() + "," + medida + "," + s.getTipoPlantio() + "," + s.getDormencia() + "\n";

                writer.write(text);
            }
        } catch (IOException | NullPointerException ex) {
        }finally {
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
            ObservableList<Semente> filtLista = FXCollections.observableArrayList();
            filtLista.setAll(lista.stream().filter(arg -> arg.getNome().toLowerCase().contains(tfPesquisar.getText().toLowerCase())).collect(Collectors.toList()));
            tbl.setItems(filtLista);
        } catch (RuntimeException ex) {
            System.out.println(ex);
        }
    }
    
    @FXML
    private void limparPesquisa(){
        super.limparPesquisa(tfPesquisar, tbl, lista);
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
}
