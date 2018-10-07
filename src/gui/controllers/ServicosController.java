package gui.controllers;

import beans.Servico;
import db.dao.ServicoDAO;
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
    private TableColumn<Servico, Double> colPreco;
    
    @FXML
    private TextField tfPesquisar;

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
        ServicoDAO dao = new ServicoDAO();

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
    
    @FXML
    @Override
    protected void exportar() {
        Writer writer = null;
        String path = super.saveDialog("servico");
        try {
            File file = new File(path);
            writer = new BufferedWriter(new FileWriter(file));
            String text;

            text = "ID" + "," + "Tipo" + "," + "Preço/hora" + "\n";
            writer.write(text);

            for (Servico s : lista) {
                
                text = s.getId() + "," + s.getTipo() + "," + s.getPreco() + "\n";

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
            ObservableList<Servico> filtLista = FXCollections.observableArrayList();
            filtLista.setAll(lista.stream().filter(arg -> arg.getTipo().toLowerCase().contains(tfPesquisar.getText().toLowerCase())).collect(Collectors.toList()));
            tbl.setItems(filtLista);
        } catch (RuntimeException ex) {
        }
    }
    
    @FXML
    private void limparPesquisa(){
        super.limparPesquisa(tfPesquisar, tbl, lista);
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

}
