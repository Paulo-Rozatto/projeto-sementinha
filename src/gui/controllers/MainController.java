package gui.controllers;

import beans.Semente;
import db.dao.FatoresDAO;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import util.DialogBox;

/**
 * FXML Controller class
 *
 * @author paulo
 */
public class MainController implements Initializable {

    @FXML
    private Button btnSementes;

    @FXML
    private Button btnRecipientes;

    @FXML
    private Button btnSubstratos;

    @FXML
    private Button btnServicos;

    @FXML
    private Button btnPlantio;

    @FXML
    private StackPane stackPane;
    private Controller controllers[] = new Controller[5];
    private int focus = -1;

    @FXML
    void switchToSementes() {
        if (focus != 0) {
            switchToChild(0);
            switchStyles(btnSementes);
        }
    }

    @FXML
    void switchToRecipientes() {
        if (focus != 1) {
            switchToChild(1);
            switchStyles(btnRecipientes);
        }
    }

    @FXML
    void switchToSubstratos() {
        if (focus != 2) {
            switchToChild(2);
            switchStyles(btnSubstratos);
        }
    }

    @FXML
    void switchToServicos() {
        if (focus != 3) {
            switchToChild(3);
            switchStyles(btnServicos);
        }
    }

    @FXML
    void switchToPlantio() {
        if (focus != 4) {
            switchToChild(4);
            switchStyles(btnPlantio);
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            String[] paths = {
                "/gui/panes/sementes.fxml",
                "/gui/panes/recipientes.fxml",
                "/gui/panes/substratos.fxml",
                "/gui/panes/servicos.fxml",
                "/gui/panes/plantios.fxml"
            };

            int i = 0;
            for (String p : paths) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(p));
                Parent root = loader.load();
                controllers[i] = loader.getController();
                stackPane.getChildren().add(root);
                i++;
            }

            stackPane.getChildren().get(0).setVisible(false);
            stackPane.getChildren().get(1).setVisible(false);
            stackPane.getChildren().get(2).setVisible(false);
            stackPane.getChildren().get(3).setVisible(false);
            stackPane.getChildren().get(4).setVisible(false);

            FatoresDAO fdao = new FatoresDAO();
            double ft[] = fdao.read();
            Semente.setFatores(ft[0], ft[1], ft[2], ft[3]);

            switchToChild(0);
        } catch (IOException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Erro ao carregar o progama.", ex);
        }
    }

    private void switchToChild(int index) {
        try {
            if (focus != -1) {
                controllers[focus].free();
                stackPane.getChildren().get(focus).setVisible(false);
            }

            stackPane.getChildren().get(index).setVisible(true);
            focus = index;
            controllers[index].load();
        } catch (NullPointerException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha no carregamento", ex);
        }
    }

    private void switchStyles(Button btn) {
        btnSementes.getStyleClass().remove("botao-tela-atual");
        btnRecipientes.getStyleClass().remove("botao-tela-atual");
        btnSubstratos.getStyleClass().remove("botao-tela-atual");
        btnServicos.getStyleClass().remove("botao-tela-atual");
        btnPlantio.getStyleClass().remove("botao-tela-atual");

        btn.getStyleClass().add("botao-tela-atual");
    }
}
