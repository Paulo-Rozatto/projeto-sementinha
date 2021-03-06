package gui.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import util.DialogBox;

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
            DialogBox dg = new DialogBox();
            dg.warning("Nenhuma coluna selecionada.");
        }
    }

    protected String saveDialog(String nomePadrao) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar");
        fileChooser.setInitialFileName(nomePadrao + ".xls");
        File savedFile = fileChooser.showSaveDialog(new Stage());

        try {
            return savedFile.getPath();
        } catch (NullPointerException ex) {
            return null;
        }
    }

    protected void exportar(String title, String text) {
        try {
            OutputStream fileOut = new FileOutputStream(saveDialog(title));
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet(title);
            CreationHelper createHelper = wb.getCreationHelper();
            int cellIndex = 0;
            int rowIndex = 0;
            Row row = sheet.createRow(rowIndex);

            for (String data : text.split(",")) {
                if (data.equals("-")) {
                    cellIndex = 0;
                    rowIndex++;
                    row = sheet.createRow(rowIndex);
                } else {
                    row.createCell(cellIndex).setCellValue(
                            createHelper.createRichTextString(data)
                    );
                    cellIndex++;
                }
            }

            wb.write(fileOut);
        } catch (FileNotFoundException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível criar o arquivo", ex);
        } catch (IOException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível criar o arquivo", ex);
        }
    }

    protected abstract void load();

    protected abstract void salvar();

    protected abstract void excluir();

    protected abstract void changeDisable(boolean opt);

    protected abstract void clean();

    protected abstract void pesquisar();

    protected abstract void free();
}
