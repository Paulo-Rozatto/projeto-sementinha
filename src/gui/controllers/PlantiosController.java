package gui.controllers;

import beans.Plantio;
import beans.Recipiente;
import beans.Semente;
import beans.Servico;
import beans.ServicoPrestado;
import beans.Substrato;
import db.dao.PlantioDAO;
import db.dao.RecipienteDAO;
import db.dao.SementeDAO;
import db.dao.ServicoDAO;
import db.dao.SubstratoDAO;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import util.AlertBox;
import util.Validate;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class PlantiosController implements Initializable {

    @FXML
    private DatePicker date;

    @FXML
    private Button btnAtualizar;
    
    @FXML
    private TextField tfTotal;

    //<editor-fold defaultstate="collapsed" desc="Elementos Semente.">
    @FXML
    private ComboBox<String> cbSemente;

    @FXML
    private TextField tfSemente;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos Recipiente.">
    @FXML
    private ComboBox<String> cbRecipiente;

    @FXML
    private TextField tfRecipiente;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos Substrato">
    @FXML
    private ComboBox<String> cbSubstrato;

    @FXML
    private TextField tfSubstrato;

    @FXML
    private Button btnDescricao;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos Servicos">
    //<editor-fold defaultstate="collapsed" desc="ComboBox e Botões">
    @FXML
    private ComboBox<String> cbServico;

    @FXML
    private Button btnAddServico;

    @FXML
    private Button btnRmServico;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tabela">
    @FXML
    private TableView<ServicoPrestado> tblServicos;

    @FXML
    private TableColumn<ServicoPrestado, String> colServicos;

    @FXML
    private TableColumn<ServicoPrestado, String> colHoras;
    //</editor-fold>

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Tabela Principal">
    @FXML
    private TableView<Plantio> tbl;

    @FXML
    private TableColumn<Plantio, Integer> colId;

    @FXML
    private TableColumn<Plantio, String> colSemente;

    @FXML
    private TableColumn<Plantio, String> colRecipiente;

    @FXML
    private TableColumn<Plantio, String> colSubstrato;

    @FXML
    private TableColumn<Plantio, String> colData;

    @FXML
    private TableColumn<Plantio, Double> colTotal;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Botões CRUD">
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Listas">
    List<Semente> sementes;
    List<Substrato> substratos;
    List<Recipiente> recipientes;
    List<Servico> servicos;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Index dos seleceionados nas ComboBoxes">
    int semInd = -1;
    int subInd = -1;
    int recInd = -1;
    //</editor-fold>

    private final ObservableList<ServicoPrestado> listaServicoPrestado = FXCollections.observableArrayList();
    private final ObservableList<Plantio> lista = FXCollections.observableArrayList();
    private final List<ServicoPrestado> spBackUp = new ArrayList();

    private boolean novoItem;
    private Plantio plantio = null;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    //date.setValue(LocalDate.parse(data, format));

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        readSementes();
        readSubstratos();
        readRecipientes();
        readServicos();

        //tabela serviços
        colServicos.setCellValueFactory(cellData -> cellData.getValue().getServico().tipoProperty());
        colHoras.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
        colHoras.setCellFactory(TextFieldTableCell.forTableColumn());
        colHoras.setEditable(true);
        tblServicos.setEditable(true);
        tblServicos.setItems(listaServicoPrestado);

        colHoras.setOnEditCommit(commit -> {
            backUpHoras(commit.getTablePosition().getRow(), commit.getOldValue());
        });

        //tabela pricipal
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colSemente.setCellValueFactory(cellData -> cellData.getValue().getSemente().nomeProperty());
        colRecipiente.setCellValueFactory(cellData -> cellData.getValue().getRecipiente().nomeProperty());
        colSubstrato.setCellValueFactory(cellData -> cellData.getValue().getSubstrato().nomeProperty());
        colData.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        colTotal.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        readPlantios();
        tbl.setItems(lista);

    }

    @FXML
    void atualizar() {
        readSementes();
        readSubstratos();
        readRecipientes();
        readServicos();
    }

    @FXML
    private void descreverSubstrato() {
        if (subInd != -1) {
            Substrato s = substratos.get(subInd);
            AlertBox.describe(s.getDescricao());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Métdos que atualizam os Index">
    @FXML
    private void selectedSemente() {
        semInd = cbSemente.getSelectionModel().getSelectedIndex();

        if (semInd > -1) {
            if (sementes.get(semInd).isPrecoEmGramas()) {
                tfSemente.setPromptText("grama");
            } else {
                tfSemente.setPromptText("unidade");
            }
        }
    }

    @FXML
    private void selectedSubstrato() {
        subInd = cbSubstrato.getSelectionModel().getSelectedIndex();

    }

    @FXML
    private void selectedRecipiente() {
        recInd = cbRecipiente.getSelectionModel().getSelectedIndex();

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos do Servico Prestado">
    @FXML
    private void addServico() {
        try {
            ServicoPrestado sp;
            boolean isServicoAdcionado = false;

            int i = cbServico.getSelectionModel().getSelectedIndex();
            Servico s = servicos.get(i);

            for (ServicoPrestado lsp : listaServicoPrestado) {
                if (lsp.getServico().getTipo().equals(s.getTipo())) {
                    isServicoAdcionado = true;
                }
            }

            if (!isServicoAdcionado) {
                sp = new ServicoPrestado();
                sp.setServico(s);
                sp.setHoras("0.0");

                listaServicoPrestado.add(sp);
            } else {
                AlertBox.warning("Serviço já adiciondo");
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            AlertBox.warning("Nehum serviço selecionado");
        }
        
        cbServico.getSelectionModel().select(null);
    }

    @FXML
    private void rmServico() {
        try {
            ServicoPrestado sp = tblServicos.getSelectionModel().getSelectedItem();

            listaServicoPrestado.remove(sp);
        } catch (ArrayIndexOutOfBoundsException ex) {
            AlertBox.warning("Nehum serviço selecionado");
        }
    }
    
    private void backUpHoras(int pos, String oldHoras){
        spBackUp.get(pos).setHoras(oldHoras);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos btn CRUD">
    @FXML
    private void novo() {
        clean();
        changeDisable(false);
        novoItem = true;
        plantio = new Plantio();
    }

    @FXML
    private void editar() {
        try {
            if (selectionedObject() != null) {
                spBackUp.clear();
                spBackUp.addAll(listaServicoPrestado);
                plantio = selectionedObject();
                novoItem = false;
                changeDisable(false);
            }
        } catch (RuntimeException ex) {
            AlertBox.warning("Nenhuma coluna selecionada.");
        }
    }

    @FXML
    private void salvar() {
        PlantioDAO dao = new PlantioDAO();
        LocalDate data = date.getValue();
        String sQuantSem = tfSemente.getText();
        String sQuantRec = tfRecipiente.getText();
        String sQuantSub = tfSubstrato.getText();

        if (Validate.plantio(data, semInd, sQuantSem, recInd, sQuantRec, subInd, sQuantSub, listaServicoPrestado)) {
            Semente semente = sementes.get(semInd);
            double quantSem = Double.parseDouble(sQuantSem);
            Recipiente recipiente = recipientes.get(recInd);
            int quantRec = Integer.parseInt(sQuantRec);
            Substrato substrato = substratos.get(subInd);
            double quantSub = Double.parseDouble(sQuantSub);

            plantio.setData(data.format(format));
            plantio.setSemente(semente);
            plantio.setQuantSem(quantSem);
            plantio.setRecipiente(recipiente);
            plantio.setQuantRec(quantRec);
            plantio.setSubstrato(substrato);
            plantio.setQuantSub(quantSub);
            
            plantio.getServicosPrestados().clear();
            plantio.getServicosPrestados().addAll(listaServicoPrestado);
            
            

            if (novoItem) {
                if (dao.create(plantio)) {
                    lista.add(plantio);
                } else {
                    System.out.println("Registro não foi criado");
                }
            }//novo item
            else {
                dao.update(plantio);
            }
            
            tfTotal.setText("R$ " + plantio.precificar());
            changeDisable(true);
        }// fim if validadte

    }

    @FXML
    private void excluir() {
        PlantioDAO dao = new PlantioDAO();
        try {
            Plantio p = selectionedObject();

            if (AlertBox.confirmDelete()) {
                if (dao.delete(p.getId())) {
                    lista.remove(p);
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
            selectionedObject().getServicosPrestados().clear();
            selectionedObject().getServicosPrestados().addAll(spBackUp);
        }
        changeDisable(true);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos read">
    private void readSementes() {
        cbSemente.getItems().clear();
        SementeDAO dao = new SementeDAO();
        sementes = dao.read();

        sementes.forEach((semente) -> {
            cbSemente.getItems().add(semente.getNome());
        });

    }

    private void readRecipientes() {
        cbRecipiente.getItems().clear();
        RecipienteDAO dao = new RecipienteDAO();
        recipientes = dao.read();

        recipientes.forEach((recipiente) -> {
            cbRecipiente.getItems().add(recipiente.getNome());
        });
    }

    private void readSubstratos() {
        cbSubstrato.getItems().clear();
        SubstratoDAO dao = new SubstratoDAO();
        substratos = dao.read();

        substratos.forEach((substrato) -> {
            cbSubstrato.getItems().add(substrato.getNome());
        });
    }

    private void readServicos() {
        cbServico.getItems().clear();
        ServicoDAO dao = new ServicoDAO();
        servicos = dao.read();

        servicos.forEach((servico) -> {
            cbServico.getItems().add(servico.getTipo());
        });
    }

    private void readPlantios() {
        PlantioDAO dao = new PlantioDAO();
        lista.addAll(dao.read());
    }
    //</editor-fold>

    @FXML
    void selecionar() {
        try {
            Plantio p = selectionedObject();

            date.setValue(LocalDate.parse(p.getData(), format));
            cbSemente.getSelectionModel().select(p.getSemente().getNome());
            tfSemente.setText(String.valueOf(p.getQuantSem()));
            cbRecipiente.getSelectionModel().select(p.getRecipiente().getNome());
            tfRecipiente.setText(String.valueOf(p.getQuantRec()));
            cbSubstrato.getSelectionModel().select(p.getSubstrato().getNome());
            tfSubstrato.setText(String.valueOf(p.getQuantSub()));

            semInd = cbSemente.getSelectionModel().getSelectedIndex();
            subInd = cbSubstrato.getSelectionModel().getSelectedIndex();
            recInd = cbRecipiente.getSelectionModel().getSelectedIndex();
            
            listaServicoPrestado.clear();
            listaServicoPrestado.addAll(p.getServicosPrestados());

            tfTotal.setText("R$" + p.getTotal());
        } catch (RuntimeException ex) {
        }
    }
    
    private int selectionedIndex() {
        int selectedIndex = tbl.getSelectionModel().getSelectedIndex();
        return selectedIndex;
    }

    private Plantio selectionedObject() {
        Plantio p = tbl.getItems().get(selectionedIndex());
        return p;
    }

    private void changeDisable(boolean opt) {
        date.setDisable(opt);
        btnAtualizar.setDisable(opt);
        cbSemente.setDisable(opt);
        cbRecipiente.setDisable(opt);
        cbSubstrato.setDisable(opt);
        btnDescricao.setDisable(opt);
        cbServico.setDisable(opt);
        btnAddServico.setDisable(opt);
        btnRmServico.setDisable(opt);
        tblServicos.setDisable(opt);
        btnSalvar.setDisable(opt);
        btnCancelar.setDisable(opt);
        tfSemente.setDisable(opt);
        tfRecipiente.setDisable(opt);
        tfSubstrato.setDisable(opt);

        btnNovo.setDisable(!opt);
        btnEditar.setDisable(!opt);
        btnExcluir.setDisable(!opt);
        tbl.setDisable(!opt);
    }

    private void clean() {
        date.setValue(null);
        cbSemente.getSelectionModel().select(null);
        cbRecipiente.getSelectionModel().select(null);
        cbSubstrato.getSelectionModel().select(null);
        cbServico.getSelectionModel().select(null);
        tfSemente.setText(null);
        tfRecipiente.setText(null);
        tfSubstrato.setText(null);
        listaServicoPrestado.clear();
    }
}
