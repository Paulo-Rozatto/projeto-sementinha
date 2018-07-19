/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
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
    //<editor-fold defaultstate="collapsed" desc="Elementos Total">
    @FXML
    private Label lblTotal;

    @FXML
    private Button btnCalcularTotal;
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

    private final ObservableList<ServicoPrestado> listaServico = FXCollections.observableArrayList();
    private final ObservableList<Plantio> lista = FXCollections.observableArrayList();

    private boolean novoItem;
    private double total = 0.0;
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
        colServicos.setCellValueFactory(cellData -> cellData.getValue().servicoProperty());
        colHoras.setCellValueFactory(cellData -> cellData.getValue().horasProperty());
        colHoras.setCellFactory(TextFieldTableCell.forTableColumn());
        colHoras.setEditable(true);
        tblServicos.setEditable(true);
        tblServicos.setItems(listaServico);

        //tabela pricipal
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colSemente.setCellValueFactory(cellData -> cellData.getValue().sementeProperty());
        colRecipiente.setCellValueFactory(cellData -> cellData.getValue().recipienteProperty());
        colSubstrato.setCellValueFactory(cellData -> cellData.getValue().substratoProperty());
        colData.setCellValueFactory(cellData -> cellData.getValue().dataProperty());
        colTotal.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        readPlantios();
        tbl.setItems(lista);

    }

    @FXML
    void atualizar(ActionEvent event) {
        readSementes();
        readSubstratos();
        readRecipientes();
        readServicos();
    }

    @FXML
    private void descreverSubstrato(ActionEvent event) {
        if (subInd > -1) {
            Substrato s = substratos.get(subInd);
            AlertBox.describe(s.getDescricao());
        }
    }

    @FXML
    private void calcularTotal(ActionEvent event) {
        double qtdSem = Double.parseDouble(tfSemente.getText()),
                qtdRec = Double.parseDouble(tfRecipiente.getText());

        total += sementes.get(semInd).getPreco() * qtdSem;
        total += recipientes.get(recInd).getPreco() * qtdRec;

        for (ServicoPrestado sp : listaServico) {
            double horas = Double.parseDouble(sp.getHoras());

            for (Servico servico : servicos) {
                if (servico.getId() == sp.getSerId()) {
                    total += servico.getPreco() * horas;
                }
            }
        }
        lblTotal.setText("Total: R$" + total);
    }

    //<editor-fold defaultstate="collapsed" desc="Métdos que atualizam os Index">
    @FXML
    private void selectedSemente(ActionEvent event) {
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
    private void selectedSubstrato(ActionEvent event) {
        subInd = cbSubstrato.getSelectionModel().getSelectedIndex();

    }

    @FXML
    private void selectedRecipiente(ActionEvent event) {
        recInd = cbRecipiente.getSelectionModel().getSelectedIndex();

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Métodos dos btn Servico">
    @FXML
    private void addServico(ActionEvent event) {
        ServicoPrestado sp;

        int i = cbServico.getSelectionModel().getSelectedIndex();
        Servico s = servicos.get(i);

        sp = new ServicoPrestado(s.getId(), s.getTipo(), "0.0");

        listaServico.add(sp);

        cbServico.getSelectionModel().select(null);
    }

    @FXML
    private void rmServico(ActionEvent event) {
        int i = tblServicos.getSelectionModel().getSelectedIndex();

        listaServico.remove(i);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos btn CRUD">
    @FXML
    private void novo(ActionEvent event) {
        clean();
        changeDisable(false);
        novoItem = true;
    }

    @FXML
    private void editar(ActionEvent event) {
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
    private void salvar(ActionEvent event) {
        PlantioDAO dao = new PlantioDAO();
        Plantio p;
        LocalDate data = date.getValue();

        String sQuantSem = tfSemente.getText();

        String sQuantRec = tfRecipiente.getText();

        String sQuantSub = tfSubstrato.getText();

        if (novoItem) {
            p = new Plantio();
        } else {
            p = selectionedObject();
        }
        if (Validate.plantio(data, semInd, sQuantSem, recInd, sQuantRec, subInd, sQuantSub, listaServico)) {
            int semId = sementes.get(semInd).getId();
            double quantSem = Double.parseDouble(sQuantSem);
            String semente = sementes.get(semInd).getNome();
            
            int recId = recipientes.get(recInd).getId();
            int quantRec = Integer.parseInt(sQuantRec);
            String recipiente = recipientes.get(recInd).getNome();
            
            int subId = substratos.get(subInd).getId();
            double quantSub = Double.parseDouble(sQuantSub);
            String substrato = substratos.get(subInd).getNome();
            
            p.setData(data.format(format));

            p.setSemente(semente);
            p.setSementeId(semId);
            p.setQuantSem(quantSem);

            p.setRecipiente(recipiente);
            p.setRecipienteId(recId);
            p.setQuantRec(quantRec);

            p.setSubstrato(substrato);
            p.setSubstratoId(subId);
            p.setQuantSub(quantSub);

            p.setServicos(listaServico);
            p.setTotal(total);

            if (novoItem) {
                dao.create(p);
                lista.add(p);
                clean();
            } else {
                dao.update(p);
            }
            changeDisable(true);

        }// fim if validadte

    }

    @FXML
    private void excluir(ActionEvent event) {
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

        lista.forEach((p) -> {
            int id = p.getSementeId();
            p.setSemente(searchSemente(id).getNome());
            id = p.getRecipienteId();
            p.setRecipiente(searchRecipiente(id).getNome());
            id = p.getSubstratoId();
            p.setSubstrato(searchSubstrato(id).getNome());

            p.getServicos().forEach((s) -> {
                s.setServico(searchServico(s.getSerId()));
            });
        });
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos Search">
    private Semente searchSemente(int id) {
        Semente semente = null;

        for (Semente s : sementes) {
            if (s.getId() == id) {
                semente = s;
                break;
            }
        }

        return semente;
    }

    private Recipiente searchRecipiente(int id) {
        Recipiente recipiente = null;

        for (Recipiente r : recipientes) {
            if (r.getId() == id) {
                recipiente = r;
                break;
            }
        }

        return recipiente;
    }

    private Substrato searchSubstrato(int id) {
        Substrato substrato = null;

        for (Substrato s : substratos) {
            if (s.getId() == id) {
                substrato = s;
                break;
            }
        }

        return substrato;
    }

    private String searchServico(int id) {
        String servico = null;
        for (Servico s : servicos) {
            if (s.getId() == id) {
                servico = s.getTipo();
                break;
            }
        }
        return servico;
    }
    //</editor-fold>

    @FXML
    void selecionar(MouseEvent event) {
        try {
            Plantio p = selectionedObject();

            date.setValue(LocalDate.parse(p.getData(), format));
            cbSemente.getSelectionModel().select(p.getSemente());
            tfSemente.setText(String.valueOf(p.getQuantSem()));
            cbRecipiente.getSelectionModel().select(p.getRecipiente());
            tfRecipiente.setText(String.valueOf(p.getQuantRec()));
            cbSubstrato.getSelectionModel().select(p.getSubstrato());
            tfSubstrato.setText(String.valueOf(p.getQuantSub()));

            semInd = cbSemente.getSelectionModel().getSelectedIndex();
            subInd = cbSubstrato.getSelectionModel().getSelectedIndex();
            recInd = cbRecipiente.getSelectionModel().getSelectedIndex();

            tblServicos.getItems().clear();
            tblServicos.getItems().addAll(p.getServicos());

            lblTotal.setText("Total: R$" + p.getTotal());
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
        btnCalcularTotal.setDisable(opt);
        btnSalvar.setDisable(opt);
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
        tblServicos.getItems().clear();
        lblTotal.setText("Total: R$___,__");
    }
}
