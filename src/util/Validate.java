/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;

/**
 *
 * @author paulo
 */
public abstract class Validate {

    private static boolean string(String stg) {
        int size;
        boolean valid = false;

        if (stg != null) {
            size = stg.length();
            valid = !(size == 0 || size > 255);
        }

        return valid;
    }
    
    private static boolean decimal(String stg){
        boolean valid = false;
        
        try{
            Double.parseDouble(stg);
            valid = true;
        }
        catch(RuntimeException ex){}
        
        return valid;
    }

    public static boolean semente(String nome, String especie,String preco,String tipoPlantio, String dormencia) {
        boolean valid = false;
        String error = "";
        if (!string(nome)) {
            error += "O campo \"Nome\" não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if (!string(especie)) {
            error += "O campo \"Espécie\" não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if(!decimal(preco)){
            error += "O campo \"Preço\" não pode ser vazio e precisa ser numérico.\n";
        }
        if (!string(tipoPlantio)) {
            error += "O campo \"Tipo de Plantio\" precisa ter um valor selecionado.\n";
        }
        if (!string(dormencia)) {
            error += "O campo \"Quebra de Dormência\" precisa ter um valor selecionado.\n";
        }
        
        if (error.equals("")) {
            valid = true;
        } else {
            createAlertBox(error);
        }
        return valid;
    }
    
    public static boolean recipiente(String nome, String volume, String preco){
        boolean valid = false;
        String error = "";
        if (!string(nome)) {
            error += "O campo \"Nome\"   não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if (!decimal(volume)) {
            error += "O campo \"Volume\" não pode ser vazio e precisa ser numérico.\n";
        }
        if(!decimal(preco)){
            error += "O campo \"Preço\"  não pode ser vazio e precisa ser numérico.\n";
        }
        
        
        if (error.equals("")) {
            valid = true;
        } else {
            createAlertBox(error);
        }
        return valid;
    }
    
    public static boolean substrato(String nome, String preco){
        boolean valid = false;
        String error = "";
        if (!string(nome)) {
            error += "O campo \"Nome\"  não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if(!decimal(preco)){
            error += "O campo \"Preço\" não pode ser vazio e precisa ser numérico.\n";
        }
        
        
        if (error.equals("")) {
            valid = true;
        } else {
            createAlertBox(error);
        }
        return valid;
    }
    
    public static boolean servico(String tipo, String preco){
         boolean valid = false;
        String error = "";
        if (!string(tipo)) {
            error += "O campo \"Tipo\"  não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if(!decimal(preco)){
            error += "O campo \"Preço\" não pode ser vazio e precisa ser numérico.\n";
        }
        
        
        if (error.equals("")) {
            valid = true;
        } else {
            createAlertBox(error);
        }
        return valid;
    }

    private static void createAlertBox(String text) {
        Alert alert = new Alert(AlertType.ERROR);
        
        alert.initStyle(StageStyle.UNDECORATED);
        alert.getDialogPane().setStyle("-fx-pref-width: 600px; -fx-background-color: #ABCDEF;");
        alert.setHeaderText(null);
        alert.setTitle("Campo Inválido");
        alert.setContentText(text);

        alert.showAndWait();
    }

}
