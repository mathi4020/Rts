import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TransferMoneyServlet")
public class TransferMoneyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Get parameters from the request
        String sourceUserId = request.getSession().getAttribute("userid").toString();
        String destinationUserId = request.getParameter("destinationUserId");
        String transferAmountStr = request.getParameter("transferAmount");

        try {
            // Parse transfer amount to double
            double transferAmount = Double.parseDouble(transferAmountStr);

            // Call method to perform transfer operation
            boolean transferSuccess = performTransfer(sourceUserId, destinationUserId, transferAmount);

            // Prepare response
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String jscode="<script>";

            if (transferSuccess) {
                out.println("Transfer successful!");
                
            } else {
                out.println("Transfer failed. Please check your inputs.");
            }
        } catch (NumberFormatException e) {
            // Handle invalid transfer amount format
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid transfer amount format");
        }
    }

    private boolean performTransfer(String sourceUserId, String destinationUserId, double transferAmount) {
        // Database connection parameters
        String jdbcUrl = "jdbc:mysql://localhost:3306/firsttable";
        String dbUser = "root";
        String dbPassword = "";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            // Update source user's amount (subtract transfer amount)
            updateAmount(connection, sourceUserId, -transferAmount);

            // Update destination user's amount (add transfer amount)
            updateAmount(connection, destinationUserId, transferAmount);

            return true; // Transfer successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Transfer failed
        }
    }

    private void updateAmount(Connection connection, String userId, double amountChange) throws SQLException {
        // SQL query to update the amount for a user
        String updateAmountQuery = "UPDATE user_details SET balance = balance + ? WHERE userid = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateAmountQuery)) {
            preparedStatement.setDouble(1, amountChange);
            preparedStatement.setString(2, userId);

            preparedStatement.executeUpdate();
        }
    }
}
