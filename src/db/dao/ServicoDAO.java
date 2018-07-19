/*
 * To change this license header, choose License Headers in Project Propertier.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.dao;

import beans.Servico;
import db.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.AlertBox;

/**
 *
 * @author paulo
 */
public class ServicoDAO {

    public ServicoDAO() {

    }

    public boolean create(Servico s) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("INSERT INTO Servicos(tipo,preco) values(?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, s.getTipo());
            stmt.setDouble(2, s.getPreco());

            stmt.executeUpdate();
            
            rs = stmt.getGeneratedKeys();
            
            if(rs.next()){
                s.setId(rs.getInt(1));
            }

            System.out.println("Criação de registro executada com sucesso.");
            return true;

        } catch (SQLException ex) {
            AlertBox.exception("Falha na criação de registro: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public List<Servico> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Servico> servicos = new ArrayList();

        try {
            stmt = con.prepareStatement("SELECT * FROM Servicos");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Servico s = new Servico(
                        rs.getInt("ser_id"),
                        rs.getString("tipo"),
                        rs.getDouble("preco"));

                servicos.add(s);

            }

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler o banco de dados: ", ex);
        } finally {
           ConnectionFactory.closeConection(con, stmt, rs);
        }
        return servicos;
    }

    public boolean update(Servico s) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE Servicos SET tipo = ?, preco = ? WHERE ser_id = ?");
            stmt.setString(1, s.getTipo());
            stmt.setDouble(2, s.getPreco());
            stmt.setInt(3, s.getId());

            stmt.executeUpdate();

            System.out.println("Atualização de registro executada com sucesso.");
            return true;

        } catch (SQLException ex) {
            AlertBox.exception("Falha na atualização de registro: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public boolean delete(int i) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("DELETE FROM Servicos WHERE  ser_id = ?");
            stmt.setInt(1, i);

            stmt.executeUpdate();

            System.out.println("Registro apagado com sucesso.");
            return true;

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível apagar o registro: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

}
