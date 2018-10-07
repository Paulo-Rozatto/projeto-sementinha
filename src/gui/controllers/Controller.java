/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import java.io.File;
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
    
    protected String saveDialog(String nomePadrao){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Planilha","*.csv"));
        fileChooser.setInitialFileName(nomePadrao + ".csv");
        File savedFile = fileChooser.showSaveDialog(new Stage());
        
        return savedFile.getPath();
    }

    protected abstract void salvar();

    protected abstract void excluir();

    protected abstract void changeDisable(boolean opt);

    protected abstract void clean();

    protected abstract void exportar();

    protected abstract void pesquisar();

}
