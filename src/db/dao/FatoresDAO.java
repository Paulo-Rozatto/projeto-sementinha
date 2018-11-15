package db.dao;

import db.connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DialogBox;

/**
 *
 * @author paulo
 */
public class FatoresDAO {

    public FatoresDAO() {
    }
    
    public void checkRegisters(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("SELECT plantio_indireto FROM Fatores");
            rs = stmt.executeQuery();

            if (!rs.next()) {
                create();
            }
        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler os fatores no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
    }
    
    private void create(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        System.out.println("!!");
        try {
            stmt = con.prepareStatement("INSERT INTO Fatores() VALUES()");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível criar os fatores no banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public double[] read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double fatores[] = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM Fatores");
            rs = stmt.executeQuery();

            if (rs.next()) {
                fatores = new double[4];
                fatores[0] = rs.getDouble("plantio_indireto");
                fatores[1] = rs.getDouble("quebra_quimica");
                fatores[2] = rs.getDouble("quebra_fisica");
                fatores[3] = rs.getDouble("quebra_estratificacao");
            }
            return fatores;
        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível ler os fatores no banco de dados: ", ex);
            return null;
        } finally {
            ConnectionFactory.closeConection(con, stmt, rs);
        }
    }

    public void update(double plantIndireto, double quebraQuimica, double quebraFisica, double quebraEstratificacao) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE Fatores SET "
                    + "plantio_indireto = ?, "
                    + "quebra_quimica = ?, "
                    + "quebra_fisica = ?, "
                    + "quebra_estratificacao = ?");

            stmt.setDouble(1, plantIndireto);
            stmt.setDouble(2, quebraQuimica);
            stmt.setDouble(3, quebraFisica);
            stmt.setDouble(4, quebraEstratificacao);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            DialogBox dg = new DialogBox();
            dg.exception("Não foi possível atualizar os fatores banco de dados: ", ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
}
