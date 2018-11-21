package db.dao;

import beans.QuebraDormencia;
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
public class QuebraDormenciaDAO implements IDAO<QuebraDormencia> {

    @Override
    public boolean create(QuebraDormencia qd) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("INSERT INTO QuebrasDormencia(nome,fator) values(?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, qd.getNome());
            stmt.setDouble(2, qd.getFator());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                qd.setId(rs.getInt(1));
            }

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na criação de registro da quebra de dormência: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    @Override
    public List<QuebraDormencia> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<QuebraDormencia> qbrsDormencia = new ArrayList();

        try {
            stmt = con.prepareStatement("SELECT * FROM QuebrasDormencia");
            rs = stmt.executeQuery();
            while (rs.next()) {
                QuebraDormencia tp = new QuebraDormencia();
                tp.setId(rs.getInt("qd_id"));
                tp.setNome(rs.getString("nome"));
                tp.setFator(rs.getDouble("fator"));

                 qbrsDormencia.add(tp);
            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler as quebras de dormencia no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return  qbrsDormencia;
    }

    @Override
    public QuebraDormencia read(int id) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        QuebraDormencia qd = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM QuebrasDormencia WHERE qd_id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                qd = new QuebraDormencia();
                qd.setId(rs.getInt("qd_id"));
                qd.setNome(rs.getString("nome"));
                qd.setFator(rs.getDouble("fator"));

            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler as quebras de dormencia no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return qd;
    }

    @Override
    public boolean update(QuebraDormencia qd) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE QuebrasDormencia SET nome = ?, fator = ? WHERE qd_id = ?");
            stmt.setString(1, qd.getNome());
            stmt.setDouble(2, qd.getFator());
            stmt.setInt(3, qd.getId());

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na atualização de registro da quebra de dormencia: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public boolean update(int id, double fator) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE QuebrasDormencia SET fator = ? WHERE qd_id = ?");
            stmt.setDouble(1, fator);
            stmt.setInt(2, id);

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na atualização de registro da quebra de dormencia: ", ex);
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
            stmt = con.prepareStatement("DELETE FROM QuebrasDormencia WHERE  qd_id = ?");
            stmt.setInt(1, i);

            stmt.executeUpdate();

            return true;

        } catch (SQLIntegrityConstraintViolationException ex) {
            DialogBox dg = new DialogBox();
            dg.error("Não é possível apagar quebra de dormencia, pois a mesma está sendo utilizado por uma ou mais sementes");
            return false;
        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível apagar o registro da quebra de dormencia: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
}
