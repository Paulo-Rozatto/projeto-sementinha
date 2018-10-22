package gui.controllers;

import beans.Plantio;
import beans.Recipiente;
import beans.Semente;
import beans.Servico;
import beans.ServicoPrestado;
import beans.Substrato;
import db.dao.IDAO;
import db.dao.PlantioDAO;
import db.dao.RecipienteDAO;
import db.dao.SementeDAO;
import db.dao.ServicoDAO;
import db.dao.ServicoPrestadoDAO;
import db.dao.SubstratoDAO;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
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
public class PlantiosController extends Controller<Plantio> implements Initializable {

    @FXML
    private DatePicker date;

    @FXML
    private Button btnAtualizar;

    @FXML
    private TextField tfTotal;

    @FXML
    private TextField tfPesquisar;

    @FXML
    private ComboBox<String> cbStatus;

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
    private TableColumn<ServicoPrestado, Integer> colServicosId;

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
    private TableColumn<Plantio, String> colStatus;

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
    private List<Semente> sementes;
    private List<Substrato> substratos;
    private List<Recipiente> recipientes;
    private List<Servico> servicos;
    //</editor-fold>

    private final ObservableList<ServicoPrestado> listaServicoPrestado = FXCollections.observableArrayList();
    private final ObservableList<Plantio> lista = FXCollections.observableArrayList();
    private List<ServicoPrestado> spBackUp;

    private boolean novoItem;
    private Plantio plantio = null;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //lê sementes, recipientes, substratos e servicos
        atualizar();

        cbStatus.getItems().addAll("desenvolvendo", "finalizado", "despachado");

        //tabela serviços
        colServicosId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colServicos.setCellValueFactory(cellData -> cellData.getValue().getServico().tipoProperty());
        colHoras.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
        colHoras.setCellFactory(TextFieldTableCell.forTableColumn());
        colHoras.setEditable(true);
        tblServicos.setEditable(true);
        tblServicos.setItems(listaServicoPrestado);

        colHoras.setOnEditCommit(commit -> {
            updateHoras(commit.getTablePosition().getRow(), commit.getNewValue());
        });

        //tabela pricipal
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colSemente.setCellValueFactory(cellData -> cellData.getValue().getSemente().nomeProperty());
        colRecipiente.setCellValueFactory(cellData -> cellData.getValue().getRecipiente().nomeProperty());
        colSubstrato.setCellValueFactory(cellData -> cellData.getValue().getSubstrato().nomeProperty());
        colData.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        colTotal.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        readPlantios();
        tbl.setItems((ObservableList) lista);
    }

    @FXML
    private void atualizar() {
        readSementes();
        readSubstratos();
        readRecipientes();
        readServicos();
    }

    @FXML
    private void descreverSubstrato() {
        int subInd = cbSubstrato.getSelectionModel().getSelectedIndex();
        if (subInd != -1) {
            Substrato s = substratos.get(subInd);
            AlertBox.describe(s.getDescricao());
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Métodos do Servico Prestado">
    @FXML
    private void addServico() {
        try {
            ServicoPrestado sp;

            int i = cbServico.getSelectionModel().getSelectedIndex();
            Servico s = servicos.get(i);

            sp = new ServicoPrestado();
            sp.setServico(s);
            sp.setHoras("0.0");
            sp.setPlantio(plantio);

//            spAddList.add(sp);
            listaServicoPrestado.add(sp);
        } catch (ArrayIndexOutOfBoundsException ex) {
            AlertBox.warning("Nehum serviço selecionado");
        }

        cbServico.getSelectionModel().select(null);
    }

    @FXML
    private void rmServico() {
        try {
            ServicoPrestado sp = tblServicos.getSelectionModel().getSelectedItem();

//            spRmList.add(sp);
            listaServicoPrestado.remove(sp);
        } catch (ArrayIndexOutOfBoundsException ex) {
            AlertBox.warning("Nehum serviço selecionado");
        }
    }

    private void updateHoras(int pos, String newHoras) {
        try {
            ServicoPrestado sp = listaServicoPrestado.get(pos);
            sp.setHoras(newHoras);
//            spUpdateList.add(sp);
        } catch (RuntimeException ex) {

        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos btn CRUD">
    @FXML
    @Override
    protected void novo() {
        super.novo();
        plantio = new Plantio();
        novoItem = true;
    }

    @FXML
    private void editar() {
        try {
            if (selectedObject(tbl) != null) {
                spBackUp = new ArrayList();
                listaServicoPrestado.forEach((sp) -> spBackUp.add(sp.clone()));
                plantio = selectedObject(tbl);
                novoItem = false;
                changeDisable(false);
            }
        } catch (RuntimeException ex) {
            AlertBox.warning("Nenhuma coluna selecionada.");
        }
    }

    @FXML
    @Override
    protected void salvar() {
        IDAO dao = new PlantioDAO();
        ServicoPrestadoDAO spDao = new ServicoPrestadoDAO();
        LocalDate data = date.getValue();
        String status = cbStatus.getSelectionModel().isEmpty() ? "desenvolvendo"
                : cbStatus.getSelectionModel().getSelectedItem();

        String sQuantSem = tfSemente.getText();
        String sQuantRec = tfRecipiente.getText();
        String sQuantSub = tfSubstrato.getText();

        int semInd = cbSemente.getSelectionModel().getSelectedIndex();
        int recInd = cbRecipiente.getSelectionModel().getSelectedIndex();
        int subInd = cbSubstrato.getSelectionModel().getSelectedIndex();

        if (Validate.plantio(data, semInd, sQuantSem, recInd, sQuantRec, subInd, sQuantSub, listaServicoPrestado)) {
            Semente semente = sementes.get(semInd);
            double quantSem = Double.parseDouble(sQuantSem);
            Recipiente recipiente = recipientes.get(recInd);
            int quantRec = Integer.parseInt(sQuantRec);
            Substrato substrato = substratos.get(subInd);
            double quantSub = Double.parseDouble(sQuantSub);
            double total;
            
            plantio.setData(data.format(format));
            plantio.setSemente(semente);
            plantio.setQuantSem(quantSem);
            plantio.setRecipiente(recipiente);
            plantio.setQuantRec(quantRec);
            plantio.setSubstrato(substrato);
            plantio.setQuantSub(quantSub);
            plantio.setStatus(status);

            plantio.getServicosPrestados().clear();
            plantio.getServicosPrestados().addAll(listaServicoPrestado);

            if (novoItem) {
                total = plantio.precificar();
                if (dao.create(plantio)) {
                    lista.add(plantio);
                } else {
                    System.out.println("Registro não foi criado");
                }
            } else {
                List<ServicoPrestado> spRmList = new ArrayList();
                List<ServicoPrestado> spUpdateList = new ArrayList();
                List<ServicoPrestado> spCreateList = new ArrayList();
                spBackUp.forEach((sp) -> {
                    if (!listaServicoPrestado.contains(sp)) {
                        spRmList.add(sp);
                    } else {
                        spUpdateList.add(sp);
                    }
                });
                listaServicoPrestado.forEach((sp) -> {
                    if (!spUpdateList.contains(sp)) {
                        spCreateList.add(sp);
                    }
                });

                spDao.create(spCreateList);
                spDao.delete(spRmList);
                spDao.update(spUpdateList);
                total = plantio.precificar();
                dao.update(plantio);
            }
            spBackUp = null;
            DecimalFormat df = new DecimalFormat("####,##0.00");
            String preco = "R$" + df.format(total);
            tfTotal.setText(preco.replace(".", ","));
            changeDisable(true);
        }// fim if validadte

    }

    @FXML
    @Override
    protected void excluir() {
        IDAO dao = new PlantioDAO();
        try {
            Plantio p = selectedObject(tbl);

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
    private void cancelar() {
        if (novoItem) {
            plantio = null;
            clean();
        } else {
            selecionar();
            selectedObject(tbl).getServicosPrestados().clear();
            selectedObject(tbl).getServicosPrestados().addAll(spBackUp);
            tblServicos.getItems().setAll(spBackUp);
        }
        changeDisable(true);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos read">
    private void readSementes() {
        cbSemente.getItems().clear();
        IDAO dao = new SementeDAO();
        sementes = dao.read();
        sementes.forEach((semente) -> {
            cbSemente.getItems().add(semente.getNome());
        });
    }

    private void readRecipientes() {
        cbRecipiente.getItems().clear();
        IDAO dao = new RecipienteDAO();
        recipientes = dao.read();

        recipientes.forEach((recipiente) -> {
            cbRecipiente.getItems().add(recipiente.getNome());
        });
    }

    private void readSubstratos() {
        cbSubstrato.getItems().clear();
        IDAO dao = new SubstratoDAO();
        substratos = dao.read();

        substratos.forEach((substrato) -> {
            cbSubstrato.getItems().add(substrato.getNome());
        });
    }

    private void readServicos() {
        cbServico.getItems().clear();
        IDAO dao = new ServicoDAO();
        servicos = dao.read();

        servicos.forEach((servico) -> {
            cbServico.getItems().add(servico.getTipo());
        });
    }

    private void readPlantios() {
        IDAO dao = new PlantioDAO();
        lista.addAll(dao.read());
    }
    //</editor-fold>

    @FXML
    private void selecionar() {
        try {
            Plantio p = selectedObject(tbl);

            date.setValue(LocalDate.parse(p.getData(), format));
            cbSemente.getSelectionModel().select(p.getSemente().getNome());
            tfSemente.setText(String.valueOf(p.getQuantSem()));
            cbRecipiente.getSelectionModel().select(p.getRecipiente().getNome());
            tfRecipiente.setText(String.valueOf(p.getQuantRec()));
            cbSubstrato.getSelectionModel().select(p.getSubstrato().getNome());
            tfSubstrato.setText(String.valueOf(p.getQuantSub()));
            cbStatus.getSelectionModel().select(p.getStatus());

            listaServicoPrestado.clear();
            listaServicoPrestado.addAll(p.getServicosPrestados());

            DecimalFormat df = new DecimalFormat("####,##0.00");
            String preco = "R$" + df.format(p.getTotal());
            tfTotal.setText(preco.replace(".", ","));
        } catch (RuntimeException ex) {
        }
    }

    @FXML
    protected void exportar() {
        String text;
        text = "ID" + "," + "Data" + "," + "Semente" + "," + "Quantidade/Medida" + "," + "Recipiente" + ","
                + "Quantidade" + "," + "Substrato" + "," + "Quantidade" + "," + "Serviços/horas" + "," + "Total" + "\n";

        for (Plantio p : lista) {
            String srvcs = "";
            String quantMedida;

            for (ServicoPrestado sp : p.getServicosPrestados()) {
                srvcs += sp.getServico().getTipo() + " " + sp.getHoras() + "h/ ";
            }
            quantMedida = p.getQuantSem() + "/" + (p.getSemente().isPrecoEmGramas() ? "grama" : "unidade");

            text += p.getId() + "," + p.getData() + "," + p.getSemente().getNome() + "," + quantMedida
                    + "," + p.getRecipiente().getNome() + "," + p.getQuantRec() + "," + p.getSubstrato().getNome()
                    + "," + p.getQuantSub() + "/cm³" + "," + srvcs + "," + "R$ " + p.getTotal() + "\n";

        }
        super.exportar("plantio", text);
    }

    @FXML
    @Override
    protected void pesquisar() {
        try {
            ObservableList<Plantio> filtLista = FXCollections.observableArrayList();
            filtLista.setAll(lista.stream().filter(arg -> arg.getSemente().getNome().toLowerCase().contains(tfPesquisar.getText().toLowerCase())).collect(Collectors.toList()));
            tbl.setItems(filtLista);
        } catch (RuntimeException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void limparPesquisa() {
        super.limparPesquisa(tfPesquisar, tbl, (ObservableList) lista);
    }

    @Override
    protected void changeDisable(boolean opt) {
        date.setDisable(opt);
        btnAtualizar.setDisable(opt);
        cbSemente.setDisable(opt);
        cbRecipiente.setDisable(opt);
        cbSubstrato.setDisable(opt);
        cbStatus.setDisable(opt);
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

    @Override
    protected void clean() {
        date.setValue(null);
        cbSemente.getSelectionModel().select(null);
        cbRecipiente.getSelectionModel().select(null);
        cbSubstrato.getSelectionModel().select(null);
        cbServico.getSelectionModel().select(null);
        tfSemente.setText(null);
        tfRecipiente.setText(null);
        tfSubstrato.setText(null);
        tfTotal.setText(null);
        listaServicoPrestado.clear();
        tbl.getSelectionModel().select(null);
    }
}
