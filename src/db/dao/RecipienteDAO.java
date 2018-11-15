package db.dao;

import beans.Recipiente;
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
public class RecipienteDAO implements IDAO<Recipiente> {

    public RecipienteDAO() {

    }

    @Override
    public boolean create(Recipiente r) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("INSERT INTO Recipientes(nome,volume,preco) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, r.getNome());
            stmt.setDouble(2, r.getVolume());
            stmt.setDouble(3, r.getPreco());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                r.setId(rs.getInt(1));
            }

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na criação de registro de Recipiente: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    @Override
    public List<Recipiente> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Recipiente> recipientes = new ArrayList();

        try {
            stmt = con.prepareStatement("SELECT * FROM Recipientes");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Recipiente r = new Recipiente();
                r.setId(rs.getInt("rec_id"));
                r.setNome(rs.getString("nome"));
                r.setVolume(rs.getDouble("volume"));
                r.setPreco(rs.getDouble("preco"));

                recipientes.add(r);
            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler Recipientes no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return recipientes;
    }

    public Recipiente read(int id) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Recipiente r = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM Recipientes WHERE rec_id = ?");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            if (rs.next()) {
                r = new Recipiente();
                r.setId(rs.getInt("rec_id"));
                r.setNome(rs.getString("nome"));
                r.setVolume(rs.getDouble("volume"));
                r.setPreco(rs.getDouble("preco"));
            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler o Recipiente no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }

        return r;
    }

    @Override
    public boolean update(Recipiente r) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE Recipientes SET nome = ?, volume = ?, preco = ? WHERE rec_id = ?");
            stmt.setString(1, r.getNome());
            stmt.setDouble(2, r.getVolume());
            stmt.setDouble(3, r.getPreco());
            stmt.setInt(4, r.getId());

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na atualização de registro do Recipiente: ", ex);
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
            stmt = con.prepareStatement("DELETE FROM Recipientes WHERE  rec_id = ?");
            stmt.setInt(1, i);

            stmt.executeUpdate();

            return true;

        } catch (SQLIntegrityConstraintViolationException ex) {
            DialogBox dg = new DialogBox();
            dg.error("Não é possível apagar recipiente, pois o mesmo está sendo utilizado por plantio(s)");
            return false;
        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível apagar o registro do Recipiente: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

}
