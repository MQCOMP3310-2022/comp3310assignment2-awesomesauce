package wordle;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SQLiteConnectionManager {

    private static final Logger logger = Logger.getLogger(SQLiteConnectionManager.class.getName());

    // private Connection wordleDBConn = null;
    private String databaseURL = "";

    private String wordleDropTableString = "DROP TABLE IF EXISTS wordlist;";
    private String wordleCreateString = "CREATE TABLE wordlist (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	word text NOT NULL\n"
            + ");";

    private String validWordsDropTableString = "DROP TABLE IF EXISTS validWords;";
    private String validWordsCreateString = "CREATE TABLE validWords (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	word text NOT NULL\n"
            + ");";

    // private String populateWordle;
    // private String populateValidWords;

    /**
     * Set the database file name in the sqlite project to use
     *
     * @param fileName the database file name
     */
    public SQLiteConnectionManager(String filename) {
        databaseURL = "jdbc:sqlite:sqlite/" + filename;
    }

    /**
     * Connect to a sample database
     *
     * @param fileName the database file name
     */
    public void createNewDatabase(String fileName) {

        try (Connection conn = DriverManager.getConnection(databaseURL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                if(logger.isLoggable(Level.INFO)) {
                    logger.log(Level.INFO,"The driver name is " + meta.getDriverName());
                    logger.log(Level.INFO,"A new database has been created.");
                }
            }

        } catch (SQLException e) {
            if(logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING,e.getMessage());
            }
        }
    }

    /**
     * Check that the file has been cr3eated
     *
     * @return true if the file exists in the correct location, false otherwise. If
     *         no url defined, also false.
     */
    public boolean checkIfConnectionDefined() {
        if ("".equals(databaseURL)) {
            return false;
        } else {
            try (Connection conn = DriverManager.getConnection(databaseURL)) {
                if (conn != null) {
                    return true;
                }
            } catch (SQLException e) {
                if(logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING,e.getMessage());
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Create the table structures (2 tables, wordle words and valid words)
     *
     * @return true if the table structures have been created.
     * @throws SQLException
     */
    public boolean createWordleTables() throws SQLException {
        if (!"".equals(databaseURL)) {
            Connection conn = null;
            Statement stmt = null;
            try {
                conn = DriverManager.getConnection(databaseURL);
                stmt = conn.createStatement();
                stmt.execute(wordleDropTableString);
                stmt.execute(wordleCreateString);
                stmt.execute(validWordsDropTableString);
                stmt.execute(validWordsCreateString);
                return true;
            } catch (SQLException e) {
                if(logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING,e.getMessage());
                }
                return false;
            } finally {
                conn.close();
                stmt.close();
            }

        }
        return false;

    }

    /**
     * Take an id and a word and store the pair in the valid words
     * 
     * @param id   the unique id for the word
     * @param word the word to store
     */
    public void addValidWord(int id, String word) {

        String sql = "INSERT INTO validWords(id,word) VALUES(?,?)";

        try (Connection conn = DriverManager.getConnection(databaseURL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, word);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            if(logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING,e.getMessage());
            }
        }
    }

    /**
     * get the entry in the validWords database
     * 
     * @param index the id of the word entry to get
     * @return
     * @throws SQLException
     */
    public String getWordAtIndex(int index) throws SQLException {
        String sql = "SELECT word FROM validWords where id=" + index + ";";
        String result = "";
        ResultSet cursor = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // pstmt.setInt(1, index);
            conn = DriverManager.getConnection(databaseURL);
            pstmt = conn.prepareStatement(sql);
            cursor = pstmt.executeQuery();
            if (cursor.next()) {
                if(logger.isLoggable(Level.INFO)) {
                    logger.log(Level.INFO, "successful next curser sqlite");
                }
                result = cursor.getString(1);
            }
        } catch (SQLException e) {
            if(logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING,e.getMessage());
            }
        } finally {
            cursor.close();
            conn.close();
            pstmt.close();
        }
        if(logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO,"getWordAtIndex===========================");
            logger.log(Level.INFO,"sql: " + sql);
            logger.log(Level.INFO,"result: " + result);
        }


        return result;
    }

    /**
     * Possible weakness here?
     * 
     * @param guess the string to check if it is a valid word.
     * @return true if guess exists in the database, false otherwise
     * @throws SQLException
     */
    public boolean isValidWord(String guess) throws SQLException {
        String sql = "SELECT count(id) as total FROM validWords WHERE word like'" + guess + "';";
        ResultSet resultRows = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(databaseURL);
            stmt = conn.prepareStatement(sql);
            resultRows = stmt.executeQuery();
            int result = resultRows.getInt("total");
            while (resultRows.next()) {
                result = resultRows.getInt("total");
                if(logger.isLoggable(Level.INFO)) {
                    logger.log(Level.INFO, "Total found:" + result);
                }
            }
            return result >= 1;
        } catch (SQLException e) {
            if(logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING,e.getMessage());
            }
            return false;
        } finally {
            resultRows.close();
            conn.close();
            stmt.close();
        }
    }
}
