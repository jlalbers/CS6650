import org.apache.commons.dbcp2.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LiftRideDao {
    private static BasicDataSource dataSource;

    public LiftRideDao() {
        dataSource = DBCPDataSource.getDataSource();
        System.out.println("Successfully initiated data source.");
    }

    public void createLiftRideTable() {
        Connection conn = null;
        PreparedStatement dropStatement = null;
        PreparedStatement createStatement = null;
        String dropTableStatement = "DROP TABLE IF EXISTS LiftRides;";
        String createTableStatement = "CREATE TABLE LiftRides (" +
                "skierId INTEGER," +
                "resortId INTEGER," +
                "seasonID VARCHAR(10)," +
                "dayID VARCHAR(2)," +
                "time INTEGER," +
                "liftId INTEGER);";
        try {
            conn = dataSource.getConnection();
            dropStatement = conn.prepareStatement(dropTableStatement);
            createStatement = conn.prepareStatement(createTableStatement);

            dropStatement.executeUpdate();
            createStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (dropStatement != null) {
                    dropStatement.close();
                }
                if (createStatement != null) {
                    createStatement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public void createLiftRide(LiftRide newLiftRide) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String insertQueryStatement = "INSERT INTO LiftRides (skierId, resortId, seasonId, dayId, time, liftId) " +
                "VALUES (?,?,?,?,?,?)";
        try {
            System.out.println("Attempting connection...");
            conn = dataSource.getConnection();
            System.out.println("Connected");
            preparedStatement = conn.prepareStatement(insertQueryStatement);
            preparedStatement.setInt(1, newLiftRide.getSkierId());
            preparedStatement.setInt(2, newLiftRide.getResortId());
            preparedStatement.setString(3, newLiftRide.getSeasonId());
            preparedStatement.setString(4, newLiftRide.getDayId());
            preparedStatement.setInt(5, newLiftRide.getWaitTime());
            preparedStatement.setInt(6, newLiftRide.getLiftId());

            // execute insert SQL statement
            System.out.println("Attempting update...");
            preparedStatement.executeUpdate();
            System.out.println("Update successful");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
