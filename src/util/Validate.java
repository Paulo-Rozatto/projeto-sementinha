/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import beans.ServicoPrestado;
import java.time.LocalDate;
import java.util.List;

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

    private static boolean text(String txt) {
        return (txt.length() <= 512 || txt == null);
    }

    private static boolean decimal(String stg) {
        boolean valid = false;

        try {
            if (Double.parseDouble(stg) >= 0) {
                valid = true;
            }
        } catch (RuntimeException ex) {
        }

        return valid;
    }

    public static boolean semente(String nome, String especie, String preco, String tipoPlantio, String dormencia) {
        boolean valid = false;
        String error = "";
        if (!string(nome)) {
            error += "O campo \"Nome\" não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if (!string(especie)) {
            error += "O campo \"Espécie\" não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if (!decimal(preco)) {
            error += "O campo \"Preço\" não pode ser vazio, conter letras, ou ser negativo.\n";
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
            AlertBox.error(error);
        }
        return valid;
    }

    public static boolean recipiente(String nome, String volume, String preco) {
        boolean valid = false;
        String error = "";
        if (!string(nome)) {
            error += "O campo \"Nome\"   não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if (!decimal(volume)) {
            error += "O campo \"Volume\" não pode ser vazio, conter letras, ou ser negativo.\n";
        }
        if (!decimal(preco)) {
            error += "O campo \"Preço\"  não pode ser vazio, conter letras, ou ser negativo.\n";
        }

        if (error.equals("")) {
            valid = true;
        } else {
            AlertBox.error(error);
        }
        return valid;
    }

    public static boolean substrato(String nome, String preco, String descricao) {
        boolean valid = false;
        String error = "";
        if (!string(nome)) {
            error += "O campo \"Nome\"  não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if (!decimal(preco)) {
            error += "O campo \"Preço\" não pode ser vazio, conter letras, ou ser negativo.\n";
        }
        if (!text(descricao)) {
            error += "O campo \"Descrição\"  não ter mais de 512 caracteres.\n";
        }

        if (error.equals("")) {
            valid = true;
        } else {
            AlertBox.error(error);
        }
        return valid;
    }

    public static boolean servico(String tipo, String preco) {
        boolean valid = false;
        String error = "";
        if (!string(tipo)) {
            error += "O campo \"Tipo\"  não pode ser vazio ou ter mais de 255 caracteres.\n";
        }
        if (!decimal(preco)) {
            error += "O campo \"Preço\" não pode ser vazio, conter letras, ou ser negativo.\n";
        }

        if (error.equals("")) {
            valid = true;
        } else {
            AlertBox.error(error);
        }
        return valid;
    }

    public static boolean plantio(LocalDate data,int semId, String quantSem, int recId, String quantRec, int subId, String quantSub, List<ServicoPrestado> sp) {
        boolean valid = false;
        String error = "";
        if (data == null) {
            error += "O campo \"Data\"  não pode ser vazio.\n";
        }
        if(semId == -1){
            error += "O campo \"Semente\" precisa ter um valor selecionado.\n";
        }
        if (!decimal(quantSem)) {
            error += "O campo \"Quantidade de sementes\" não pode ser vazio, conter letras, ou ser negativo.\n";
        }
        if(recId == -1){
            error += "O campo \"Recipiente\" precisa ter um valor selecionado.\n";
        }
        if (!decimal(quantRec)) {
            error += "O campo \"Quantidade de Recipientes\" não pode ser vazio, conter letras, ou ser negativo.\n";
        }
        if(subId == -1){
            error += "O campo \"Substrato\" precisa ter um valor selecionado.\n";
        }
        if (!decimal(quantSub)) {
            error += "O campo \"Quantidade de substrato\" não pode ser vazio, conter letras,\n ou ser negativo.\n";
        }
        for (ServicoPrestado s : sp) {
            if (!decimal(s.getHoras())) {
                error += "O campo \"Horas\" não pode conter letras, ou ser negativo.\n";
                break;
            }
        }
        
        if (error.equals("")) {
            valid = true;
        } else {
            AlertBox.error(error);
        }

        return valid;
    }

}
