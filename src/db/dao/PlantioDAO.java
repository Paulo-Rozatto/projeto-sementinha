/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.dao;

import beans.Plantio;
import beans.ServicoPrestado;
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
public class PlantioDAO {

    public PlantioDAO() {

    }

    public boolean create(Plantio p) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int pla_id;

        try {
            stmt = con.prepareStatement("INSERT INTO Plantios(data,sem_id,quant_sem,rec_id,quant_rec,sub_id,quant_sub,total) "
                    + "values(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, p.getData());
            stmt.setInt(2, p.getSementeId());
            stmt.setDouble(3, p.getQuantSem());
            stmt.setInt(4, p.getRecipienteId());
            stmt.setDouble(5, p.getQuantRec());
            stmt.setInt(6, p.getSubstratoId());
            stmt.setDouble(7, p.getQuantSub());
            stmt.setDouble(8, p.getTotal());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                pla_id = rs.getInt(1);
                p.setId(rs.getInt(1));
                createServicosPrestados(pla_id, p.getServicos());
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

    private boolean createServicosPrestados(int plaId, List<ServicoPrestado> servicosPrestados) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con.setAutoCommit(false);

            stmt = con.prepareStatement("INSERT INTO ServicoPrestado(pla_id,ser_id,horas) values(?,?,?)");
            for (ServicoPrestado sb : servicosPrestados) {
                stmt.setInt(1, plaId);
                stmt.setInt(2, sb.getSerId());
                stmt.setDouble(3, Double.parseDouble(sb.getHoras()));
                stmt.addBatch();
            }
            stmt.executeBatch();
            con.commit();

            return true;
        } catch (SQLException ex) {
            AlertBox.exception("Falha na criação de registro: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public List<Plantio> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Plantio> plantios = new ArrayList();

        try {
            stmt = con.prepareStatement("SELECT * FROM Plantios");

            rs = stmt.executeQuery();
            while (rs.next()) {
                Plantio p = new Plantio();
                p.setId(rs.getInt(1));
                p.setData(rs.getString("data"));
                p.setSementeId(rs.getInt("sem_id"));
                p.setQuantSem(rs.getDouble("quant_sem"));
                p.setRecipienteId(rs.getInt("rec_id"));
                p.setQuantRec(rs.getInt("quant_rec"));
                p.setSubstratoId(rs.getInt("sub_id"));
                p.setQuantSub(rs.getDouble("quant_sub"));
                p.setTotal(rs.getDouble("total"));
                p.setServicos(readSP(p.getId()));

                plantios.add(p);
            }

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler o banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return plantios;
    }
    
    private List<ServicoPrestado> readSP(int id){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ServicoPrestado> servicos = new ArrayList();

        try {
            stmt = con.prepareStatement("SELECT * FROM ServicoPrestado WHERE pla_id = ?");
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                ServicoPrestado sp = new ServicoPrestado(
                        rs.getInt("pla_id"), 
                        rs.getInt("ser_id"),
                        String.valueOf(rs.getDouble("horas"))
                );
                servicos.add(sp);
            }

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler o banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return servicos;
    }

    public boolean update(Plantio p) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE Plantios SET data = ?, sem_id = ?,quant_sem =?,"
                    + "rec_id = ?, quant_rec = ?, sub_id = ?, quant_sub = ?,total = ? WHERE pla_id = ?");
            stmt.setString(1, p.getData());
            stmt.setInt(2, p.getSementeId());
            stmt.setDouble(3, p.getQuantSem());
            stmt.setInt(4, p.getRecipienteId());
            stmt.setInt(5, p.getQuantRec());
            stmt.setInt(6, p.getSubstratoId());
            stmt.setDouble(7, p.getQuantSub());
            stmt.setDouble(8, p.getTotal());
            stmt.setInt(9, p.getId());

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
            stmt = con.prepareStatement("DELETE FROM Plantios WHERE  pla_id = ?");
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
