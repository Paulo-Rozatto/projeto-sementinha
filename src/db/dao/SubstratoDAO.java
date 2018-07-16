/*
 * To change this license header, choose License Headers in Project Propertier.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.dao;

import beans.Substrato;
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
public class SubstratoDAO {

    public SubstratoDAO() {

    }

    public boolean create(Substrato s) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("INSERT INTO Substratos(nome,preco,descricao) values(?,?,?)");
            stmt.setString(1, s.getNome());
            stmt.setDouble(2, s.getPreco());
            stmt.setString(3, s.getDescricao());

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

    public List<Substrato> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Substrato> substratos = new ArrayList();

        try {
            stmt = con.prepareStatement("SELECT * FROM Substratos");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Substrato s = new Substrato(
                        rs.getInt("sub_id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("descricao"));

                substratos.add(s);

            }

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler o banco de dados: ", ex);
        } finally {
           ConnectionFactory.closeConection(con, stmt, rs);
        }
        return substratos;
    }

    public Substrato readLast() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Substrato s = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM Substratos ORDER BY sub_id DESC LIMIT 1");
            rs = stmt.executeQuery();

            if (rs.last()) {
               s = new Substrato(
                        rs.getInt("sub_id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getString("descricao"));
            }

            System.out.println("Leitura do último campo executada com sucesso.");

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler último campo do banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return s;
    }

    public boolean update(Substrato s) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE Substratos SET nome = ?, preco = ?, descricao = ? WHERE sub_id = ?");
            stmt.setString(1, s.getNome());
            stmt.setDouble(2, s.getPreco());
            stmt.setString(3, s.getDescricao());
            stmt.setInt(4, s.getId());

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
            stmt = con.prepareStatement("DELETE FROM Substratos WHERE  sub_id = ?");
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
