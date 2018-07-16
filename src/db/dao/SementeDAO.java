/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.dao;

import beans.Semente;
import db.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.AlertBox;

/**
 *
 * @author paulo
 */
public class SementeDAO {

    public SementeDAO() {

    }

    public boolean create(Semente s) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("INSERT INTO Sementes(nome,especie,preco,preco_em_gramas,tipoPlantio,dormencia) values(?,?,?,?,?,?)");
            stmt.setString(1, s.getNome());
            stmt.setString(2, s.getEspecie());
            stmt.setDouble(3, s.getPreco());
            stmt.setBoolean(4, s.isPrecoEmGramas());
            stmt.setString(5, s.getTipoPlantio());
            stmt.setString(6, s.getDormencia());

            stmt.executeUpdate();

            System.out.println("Criação de registro executada com sucesso.");
            return true;

        } catch (SQLException ex) {
            AlertBox.exception("Falha na criação de registro: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public List<Semente> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Semente> sementes = new ArrayList();

        try {
            stmt = con.prepareStatement("SELECT * FROM Sementes");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Semente s = new Semente(
                        rs.getInt("sem_id"),
                        rs.getString("nome"),
                        rs.getString("especie"),
                        rs.getDouble("preco"),
                        rs.getBoolean("preco_em_gramas"),
                        rs.getString("tipoPlantio"),
                        rs.getString("dormencia"));

                sementes.add(s);

            }

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler o banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return sementes;
    }

    public Semente readLast() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Semente s = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM Sementes ORDER BY sem_id DESC LIMIT 1");
            rs = stmt.executeQuery();

            if (rs.last()) {
                s = new Semente(
                        rs.getInt("sem_id"),
                        rs.getString("nome"),
                        rs.getString("especie"),
                        rs.getDouble("preco"),
                        rs.getBoolean("preco_em_gramas"),
                        rs.getString("tipoPlantio"),
                        rs.getString("dormencia"));
            }

            System.out.println("Leitura do último campo executada com sucesso.");

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler último campo do banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return s;
    }

    public boolean update(Semente s) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE Sementes SET nome = ?, especie = ?, preco = ?, preco_em_gramas = ?,tipoPlantio = ?, dormencia = ? WHERE sem_id = ?");
            stmt.setString(1, s.getNome());
            stmt.setString(2, s.getEspecie());
            stmt.setDouble(3, s.getPreco());
            stmt.setBoolean(4, s.isPrecoEmGramas());
            stmt.setString(5, s.getTipoPlantio());
            stmt.setString(6, s.getDormencia());
            stmt.setInt(7, s.getId());

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
            stmt = con.prepareStatement("DELETE FROM Sementes WHERE  sem_id = ?");
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
