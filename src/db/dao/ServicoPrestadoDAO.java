package db.dao;

import beans.Plantio;
import beans.Servico;
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
public class ServicoPrestadoDAO implements IDAO<ServicoPrestado> {

    public ServicoPrestadoDAO() {
    }
    
    @Override
    public boolean create(ServicoPrestado sp){
        List<ServicoPrestado> lista = new ArrayList();
        lista.add(sp);
        return create(lista);
    }
    public boolean create(List<ServicoPrestado> spList){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con.setAutoCommit(false);

            stmt = con.prepareStatement("INSERT INTO ServicoPrestado(pla_id,ser_id,horas) values(?,?,?)",Statement.RETURN_GENERATED_KEYS);
            for (ServicoPrestado sp : spList) {
                stmt.setInt(1, sp.getPlantio().getId());
                stmt.setInt(2, sp.getServico().getId());
                stmt.setDouble(3, Double.parseDouble(sp.getHoras()));
                stmt.addBatch();
            }
            stmt.executeBatch();
            rs = stmt.getGeneratedKeys();
            
            int i = 0;
            while(rs.next()){
                spList.get(i).setId(rs.getInt("id"));
                i++;
            }
            con.commit();

            return true;
        } catch (SQLException ex) {
            AlertBox.exception("Falha na criação de registro de Serviços Prestados: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    @Override
    public List<ServicoPrestado> read(){
        System.out.println("Aqui!!!");
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ServicoPrestado> servicosPrestados = new ArrayList();
        IDAO plaDAO = new PlantioDAO();
        IDAO serDAO = new ServicoDAO();
        
        try{
            stmt = con.prepareStatement("Select * from ServicoPrestado");
            rs = stmt.executeQuery();
            
            while(rs.next()){
                ServicoPrestado sp = new ServicoPrestado();
                sp.setId(rs.getInt(1));
                sp.setPlantio((Plantio) plaDAO.read(rs.getInt(3)));
                sp.setServico((Servico) serDAO.read(rs.getInt(2)));
                sp.setHoras(String.valueOf(rs.getDouble(4)));
            }
        
        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler Serviços Prestados no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return servicosPrestados;
    }
    
    @Override
    public ServicoPrestado read(int id){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ServicoPrestado sp = null;
        IDAO plaDAO = new PlantioDAO();
        IDAO serDAO = new ServicoDAO();
        
        try{
            stmt = con.prepareStatement("Select * from ServicoPrestado WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if(rs.next()){
                sp = new ServicoPrestado();
                sp.setPlantio((Plantio) plaDAO.read(rs.getInt("pla_id")));
                sp.setServico((Servico) serDAO.read(rs.getInt("ser_id")));
                sp.setHoras(String.valueOf(rs.getDouble("horas")));
            }
        
        } catch (SQLException ex) {
            AlertBox.exception("Não foi possível ler Serviços Prestados no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return sp;
    }

    public List<ServicoPrestado> read(Plantio p) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ServicoPrestado> servicosPrestados = new ArrayList();
        IDAO ser = new ServicoDAO();

        try {
            stmt = con.prepareStatement("SELECT id, ser_id, horas FROM ServicoPrestado WHERE pla_id = ?");
            stmt.setInt(1, p.getId());

            rs = stmt.executeQuery();
            while (rs.next()) {
                ServicoPrestado sp = new ServicoPrestado();
                sp.setPlantio(p);
                sp.setId(rs.getInt(1));
                sp.setServico((Servico) ser.read(rs.getInt("ser_id")));
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
    
    @Override
    public boolean update(ServicoPrestado sp){
        List<ServicoPrestado> lista = new ArrayList();
        lista.add(sp);
        return update(lista);
    }
    
    public boolean update(List<ServicoPrestado> spList){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            con.setAutoCommit(false);

            stmt = con.prepareStatement("UPDATE ServicoPrestado SET horas = ? WHERE id = ?");
            for (ServicoPrestado sp : spList) {
                stmt.setDouble(1, Double.parseDouble(sp.getHoras()));
                stmt.setInt(2, sp.getId());
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
    
    @Override
    public boolean delete(int i){
        return false;
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
