import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CheckBalanceServlet")
public class CheckBalanceServlet extends HttpServlet {

    private static final String SELECT_BALANCE_QUERY = "SELECT BALANCE FROM USER_DETAILS WHERE USERID=?";
    private static final String UPDATE_BALANCE_QUERY = "UPDATE USER_DETAILS SET BALANCE = BALANCE + ? WHERE USERID=?";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String operation = req.getParameter("operation");
        String userid = req.getParameter("userid");

        try (Connection con = DriverManager.getConnection("jdbc:mysql:///firsttable", "root", "")) {
            if ("checkBalance".equals(operation)) {
                // Check Balance Operation
                checkBalance(con, userid, resp);
            } else if ("deposit".equals(operation)) {
                // Deposit Operation
                String amountStr = req.getParameter("amount");
                double amount = Double.parseDouble(amountStr);

                deposit(con, userid, amount, resp);
            } else {
                // Handle invalid operation
                resp.getWriter().write("Invalid operation");
            }
        } catch (SQLException se) {
            resp.getWriter().write("Error: " + se.getMessage());
            se.printStackTrace();
        }
    }

    private void checkBalance(Connection con, String userid, HttpServletResponse resp) throws SQLException, IOException {
        try (PreparedStatement ps = con.prepareStatement(SELECT_BALANCE_QUERY)) {
            ps.setString(1, userid);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("BALANCE");
                resp.getWriter().write(Double.toString(balance));
            } else {
                resp.getWriter().write("User not found");
            }
        }
    }

    private void deposit(Connection con, String userid, double amount, HttpServletResponse resp) throws SQLException, IOException {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_BALANCE_QUERY)) {
            ps.setDouble(1, amount);
            ps.setString(2, userid);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                // Update successful
                checkBalance(con, userid, resp);
            } else {
                resp.getWriter().write("Error updating balance");
            }
        }
    }
}
