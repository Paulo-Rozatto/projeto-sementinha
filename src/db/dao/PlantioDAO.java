package db.dao;

import beans.Plantio;
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
public class PlantioDAO implements IDAO<Plantio>{

    public PlantioDAO() {

    }

    @Override
    public boolean create(Plantio p) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ServicoPrestadoDAO spd = new ServicoPrestadoDAO();

        try {
            stmt = con.prepareStatement("INSERT INTO Plantios(data,sem_id,quant_sem,rec_id,quant_rec,sub_id,quant_sub,status,total) "
                    + "values(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, p.getData());
            stmt.setInt(2, p.getSemente().getId());
            stmt.setDouble(3, p.getQuantSem());
            stmt.setInt(4, p.getRecipiente().getId());
            stmt.setDouble(5, p.getQuantRec());
            stmt.setInt(6, p.getSubstrato().getId());
            stmt.setDouble(7, p.getQuantSub());
            stmt.setString(8, p.getStatus());
            stmt.setDouble(9, p.getTotal());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                p.setId(rs.getInt(1));
                spd.create(p.getServicosPrestados());
            }

            System.out.println("Criação de registro executada com sucesso.");
            return true;

        } catch (SQLException ex) {
            AlertBox.exception("Falha na criação de registro de Plantio: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    @Override
    public List<Plantio> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Plantio> plantios = new ArrayList();
        SementeDAO sem = new SementeDAO();
        RecipienteDAO rec = new RecipienteDAO(); 
        SubstratoDAO sub = new SubstratoDAO();
        ServicoPrestadoDAO spd = new ServicoPrestadoDAO();

        try {
            stmt = con.prepareStatement("SELECT * FROM Plantios");

            rs = stmt.executeQuery();
            while (rs.next()) {
                Plantio p = new Plantio();
                p.setId(rs.getInt(1));
                p.setData(rs.getString("data"));
                p.setSemente(sem.read(rs.getInt("sem_id")));
                p.setQuantSem(rs.getDouble("quant_sem"));
                p.setRecipiente(rec.read(rs.getInt("rec_id")));
                p.setQuantRec(rs.getInt("quant_rec"));
                p.setSubstrato(sub.read(rs.getInt("sub_id")));
                p.setQuantSub(rs.getDouble("quant_sub"));
                p.setStatus(rs.getString("status"));
                p.setTotal(rs.getDouble("total"));
                p.setServicosPrestados(spd.read(p));

                plantios.add(p);
            }

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler Plantios no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return plantios;
    }
    
    @Override
    public Plantio read(int id){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Plantio p = null;
        SementeDAO sem = new SementeDAO();
        RecipienteDAO rec = new RecipienteDAO(); 
        SubstratoDAO sub = new SubstratoDAO();
        ServicoPrestadoDAO spd = new ServicoPrestadoDAO();

        try {
            stmt = con.prepareStatement("SELECT * FROM Plantios WHERE pla_id = ?");
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                p.setId(rs.getInt("pla_id"));
                p.setData(rs.getString("data"));
                p.setSemente(sem.read(rs.getInt("sem_id")));
                p.setQuantSem(rs.getDouble("quant_sem"));
                p.setRecipiente(rec.read(rs.getInt("rec_id")));
                p.setQuantRec(rs.getInt("quant_rec"));
                p.setSubstrato(sub.read(rs.getInt("sub_id")));
                p.setQuantSub(rs.getDouble("quant_sub"));
                p.setStatus(rs.getString("status"));
                p.setTotal(rs.getDouble("total"));
                p.setServicosPrestados(spd.read(p));
            }

        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler Plantios no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return p;
    }
    
    @Override
    public boolean update(Plantio p) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE Plantios SET data = ?, sem_id = ?,quant_sem =?,"
                    + "rec_id = ?, quant_rec = ?, sub_id = ?, quant_sub = ?, status = ?, total = ? WHERE pla_id = ?");
            stmt.setString(1, p.getData());
            stmt.setInt(2, p.getSemente().getId());
            stmt.setDouble(3, p.getQuantSem());
            stmt.setInt(4, p.getRecipiente().getId());
            stmt.setInt(5, p.getQuantRec());
            stmt.setInt(6, p.getSubstrato().getId());
            stmt.setDouble(7, p.getQuantSub());
            stmt.setString(8, p.getStatus());
            stmt.setDouble(9, p.getTotal());
            stmt.setInt(10, p.getId());

            stmt.executeUpdate();

            System.out.println("Atualização de registro executada com sucesso.");
            return true;

        } catch (SQLException ex) {
            AlertBox.exception("Falha na atualização de registro de Plantios: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    @Override
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
            AlertBox.exception("Não foi possível apagar o registro do Plantio: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
}
