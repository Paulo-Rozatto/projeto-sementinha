package db.dao;

import beans.Servico;
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
public class ServicoDAO implements IDAO<Servico> {

    public ServicoDAO() {

    }

    @Override
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

            if (rs.next()) {
                s.setId(rs.getInt(1));
            }

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na criação de registro do Serviço: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    @Override
    public List<Servico> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Servico> servicos = new ArrayList();

        try {
            stmt = con.prepareStatement("SELECT * FROM Servicos");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Servico s = new Servico();
                s.setId(rs.getInt("ser_id"));
                s.setTipo(rs.getString("tipo"));
                s.setPreco(rs.getDouble("preco"));

                servicos.add(s);

            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler os Serviços no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return servicos;
    }

    @Override
    public Servico read(int id) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Servico s = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM Servicos WHERE ser_id = ?");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            if (rs.next()) {
                s = new Servico();
                s.setId(rs.getInt("ser_id"));
                s.setTipo(rs.getString("tipo"));
                s.setPreco(rs.getDouble("preco"));
            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler o Serviço banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return s;
    }

    @Override
    public boolean update(Servico s) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE Servicos SET tipo = ?, preco = ? WHERE ser_id = ?");
            stmt.setString(1, s.getTipo());
            stmt.setDouble(2, s.getPreco());
            stmt.setInt(3, s.getId());

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na atualização de registro do Serviço: ", ex);
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
            stmt = con.prepareStatement("DELETE FROM Servicos WHERE  ser_id = ?");
            stmt.setInt(1, i);

            stmt.executeUpdate();

            return true;

        } catch (SQLIntegrityConstraintViolationException ex) {
            DialogBox dg = new DialogBox();
            dg.error("Não é possível apagar semente, pois a mesma está sendo utilizada por plantio(s)");
            return false;
        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível apagar o registro do Serviço: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

}
