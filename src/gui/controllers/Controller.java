/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.AlertBox;

/**
 *
 * @author paulo
 * @param <Object>
 */
public abstract class Controller<Object> {

    protected int selectedIndex(TableView tbl) {
        return tbl.getSelectionModel().getSelectedIndex();
    }

    protected Object selectedObject(TableView tbl) {
        return (Object) tbl.getItems().get(selectedIndex(tbl));
    }

    protected void novo() {
        clean();
        changeDisable(false);
    }

    protected void editar(TableView tbl, boolean novoItem) {
        try {
            if (selectedObject(tbl) != null) {
                changeDisable(false);
                novoItem = false;
            }
        } catch (RuntimeException ex) {
            AlertBox.warning("Nenhuma coluna selecionada.");
        }
    }

    protected void limparPesquisa(TextField tfPesquisar, TableView tbl, ObservableList<Object> lista) {
        tfPesquisar.setText(null);
        tbl.setItems(lista);
    }

    protected String saveDialog(String nomePadrao) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("","*.xlsx"));
        fileChooser.setInitialFileName(nomePadrao + ".xlsx");
        File savedFile = fileChooser.showSaveDialog(new Stage());

        try {
            return savedFile.getPath();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    protected void exportar(String title, String text) {
        Writer writer = null;
        String path = saveDialog(title);
        try {
            if (path != null) {
                File file = new File(path);
                writer = new BufferedWriter(new FileWriter(file));

                writer.write(text);
            }
        } catch (IOException ex) {
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException | NullPointerException ex1) {
            }
        }
    }

    protected abstract void salvar();

    protected abstract void excluir();

    protected abstract void changeDisable(boolean opt);

    protected abstract void clean();

    protected abstract void pesquisar();

}
