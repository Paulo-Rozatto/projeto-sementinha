package db.dao;

import beans.TipoPlantio;
import db.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import util.DialogBox;

/**
 *
 * @author paulo
 */
public class TipoPlantioDAO implements IDAO<TipoPlantio>{

    @Override
    public boolean create(TipoPlantio tp) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("INSERT INTO TiposPlantio(nome,fator) values(?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, tp.getNome());
            stmt.setDouble(2, tp.getFator());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                tp.setId(rs.getInt(1));
            }

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na criação de registro do tipo de plantio: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    @Override
    public List<TipoPlantio> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<TipoPlantio> tpsPlantio = new ArrayList();

        try {
            stmt = con.prepareStatement("SELECT * FROM TiposPlantio");
            rs = stmt.executeQuery();
            while (rs.next()) {
                TipoPlantio tp = new TipoPlantio();
                tp.setId(rs.getInt("tp_id"));
                tp.setNome(rs.getString("nome"));
                tp.setFator(rs.getDouble("fator"));

                tpsPlantio.add(tp);
            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler os Tipos de plantio no banco de dados: ", ex);
            
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return tpsPlantio;
    }

    @Override
    public TipoPlantio read(int id) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        TipoPlantio tp = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM TiposPlantio WHERE tp_id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                tp = new TipoPlantio();
                tp.setId(rs.getInt("tp_id"));
                tp.setNome(rs.getString("nome"));
                tp.setFator(rs.getDouble("fator"));

            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler os tipos de plantio no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return tp;
    }

    @Override
    public boolean update(TipoPlantio tp) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE TiposPlantio SET nome = ?, fator = ? WHERE tp_id = ?");
            stmt.setString(1, tp.getNome());
            stmt.setDouble(2, tp.getFator());
            stmt.setInt(3, tp.getId());

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na atualização de registro do tipo de plantio: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public boolean update(int id, double fator) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE TiposPlantio SET fator = ? WHERE tp_id = ?");
            stmt.setDouble(1, fator);
            stmt.setInt(2, id);

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na atualização de registro do tipo de plantio: ", ex);
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
            stmt = con.prepareStatement("DELETE FROM TiposPlantio WHERE  tp_id = ?");
            stmt.setInt(1, i);

            stmt.executeUpdate();

            return true;

        } catch (SQLIntegrityConstraintViolationException ex) {
            DialogBox dg = new DialogBox();
            dg.error("Não é possível apagar tipo de plantio, pois o mesmo está sendo utilizado por uma ou mais sementes");
            return false;
        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível apagar o registro do tipo de plantio: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
}
