package db.dao;

import beans.QuebraDormencia;
import beans.Semente;
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
public class SementeDAO implements IDAO<Semente> {

    public SementeDAO() {

    }

    @Override
    public boolean create(Semente s) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("INSERT INTO Sementes(nome,especie,preco,preco_em_gramas,sem_tplantio,sem_qdormencia) values(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, s.getNome());
            stmt.setString(2, s.getEspecie());
            stmt.setDouble(3, s.getPreco());
            stmt.setBoolean(4, s.isPrecoEmGramas());
            stmt.setInt(5, s.getTipoPlantio().getId());
            stmt.setInt(6, s.getQuebraDormencia().getId());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                s.setId(rs.getInt(1));
            }

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na criação de registro de Semente: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

    @Override
    public List<Semente> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Semente> sementes = new ArrayList();
        IDAO<TipoPlantio> tpDAO = new TipoPlantioDAO();
        IDAO<QuebraDormencia> qdDAO = new QuebraDormenciaDAO();

        try {
            stmt = con.prepareStatement("SELECT * FROM Sementes");
            rs = stmt.executeQuery();
            while (rs.next()) {
                Semente s = new Semente();
                s.setId(rs.getInt("sem_id"));
                s.setNome(rs.getString("nome"));
                s.setEspecie(rs.getString("especie"));
                s.setPreco(rs.getDouble("preco"));
                s.setPrecoEmGramas(rs.getBoolean("preco_em_gramas"));
                s.setTipoPlantio(tpDAO.read(rs.getInt("sem_tplantio")));
                s.setQuebraDormencia(qdDAO.read(rs.getInt("sem_qdormencia")));

                sementes.add(s);

            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler as Sementes no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
        return sementes;
    }

    @Override
    public Semente read(int id) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Semente s = null;
        IDAO<TipoPlantio> tpDAO = new TipoPlantioDAO();
        IDAO<QuebraDormencia> qdDAO = new QuebraDormenciaDAO();

        try {
            stmt = con.prepareStatement("SELECT * FROM Sementes WHERE sem_id = ?");
            stmt.setInt(1, id);

            rs = stmt.executeQuery();
            if (rs.next()) {
                s = new Semente();
                s.setId(rs.getInt("sem_id"));
                s.setNome(rs.getString("nome"));
                s.setEspecie(rs.getString("especie"));
                s.setPreco(rs.getDouble("preco"));
                s.setPrecoEmGramas(rs.getBoolean("preco_em_gramas"));
                s.setTipoPlantio(tpDAO.read(rs.getInt("sem_tplantio")));
                s.setQuebraDormencia(qdDAO.read(rs.getInt("sem_qdormencia")));
            }

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler a Semente no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }

        return s;
    }

    @Override
    public boolean update(Semente s) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE Sementes SET nome = ?, especie = ?, preco = ?, preco_em_gramas = ?,sem_tplantio = ?, sem_qdormencia = ? WHERE sem_id = ?");
            stmt.setString(1, s.getNome());
            stmt.setString(2, s.getEspecie());
            stmt.setDouble(3, s.getPreco());
            stmt.setBoolean(4, s.isPrecoEmGramas());
            stmt.setInt(5, s.getTipoPlantio().getId());
            stmt.setInt(6, s.getQuebraDormencia().getId());
            stmt.setInt(7, s.getId());

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Falha na atualização de registro da Semente: ", ex);
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
            stmt = con.prepareStatement("DELETE FROM Sementes WHERE  sem_id = ?");
            stmt.setInt(1, i);

            stmt.executeUpdate();

            return true;

        } catch (SQLIntegrityConstraintViolationException ex) {
            DialogBox dg = new DialogBox();
            dg.error("Não é possível apagar semente, pois a mesma está sendo utilizada por plantio(s)");
            return false;
        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível apagar o registro da Semente: ", ex);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }

}
