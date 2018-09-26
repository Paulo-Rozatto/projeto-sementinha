package db.dao;

import beans.Plantio;
import beans.ServicoPrestado;
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
public class ServicoPrestadoDAO {

    public ServicoPrestadoDAO() {
    }

    
    public boolean create(List<ServicoPrestado> spList){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con.setAutoCommit(false);

            stmt = con.prepareStatement("INSERT INTO ServicoPrestado(pla_id,ser_id,horas) values(?,?,?)");
            for (ServicoPrestado sp : spList) {
                stmt.setInt(1, sp.getPlantio().getId());
                stmt.setInt(2, sp.getServico().getId());
                stmt.setDouble(3, Double.parseDouble(sp.getHoras()));
                stmt.addBatch();
            }
            stmt.executeBatch();
            con.commit();

            return true;
        } catch (SQLException ex) {
            AlertBox.exception("Falha na criação de registro de Serviços Prestados: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public List<ServicoPrestado> read(Plantio p) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ServicoPrestado> servicosPrestados = new ArrayList();
        ServicoDAO ser = new ServicoDAO();

        try {
            stmt = con.prepareStatement("SELECT ser_id, horas FROM ServicoPrestado WHERE pla_id = ?");
            stmt.setInt(1, p.getId());

            rs = stmt.executeQuery();
            while (rs.next()) {
                ServicoPrestado sp = new ServicoPrestado();
                sp.setPlantio(p);
                sp.setServico(ser.read(rs.getInt("ser_id")));
                sp.setHoras(String.valueOf(rs.getDouble("horas")));
                servicosPrestados.add(sp);
            }

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler Serviços Prestados no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return servicosPrestados;
    }
    
    public boolean update(List<ServicoPrestado> spList){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            con.setAutoCommit(false);

            stmt = con.prepareStatement("UPDATE ServicoPrestado SET horas = ? WHERE pla_id = ? AND ser_id = ?");
            for (ServicoPrestado sp : spList) {
                stmt.setDouble(1, Double.parseDouble(sp.getHoras()));
                stmt.setInt(2, sp.getPlantio().getId());
                stmt.setInt(3, sp.getServico().getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
            con.commit();

            return true;
        } catch (SQLException ex) {
            AlertBox.exception("Falha na atualização de registro de Serviços Prestados: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    public boolean delete(List<ServicoPrestado> spList){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            con.setAutoCommit(false);

            stmt = con.prepareStatement("DELETE FROM ServicoPrestado WHERE  pla_id = ? AND ser_id = ?");
            for (ServicoPrestado sp : spList) {
                stmt.setInt(1, sp.getPlantio().getId());
                stmt.setInt(2, sp.getServico().getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
            con.commit();

            return true;
        } catch (SQLException ex) {
            AlertBox.exception("Falha na atualização de registro de Serviços Prestados: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
}
