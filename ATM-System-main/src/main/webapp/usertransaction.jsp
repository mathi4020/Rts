<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    
    <title>User Transaction Page</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">

    <!-- jQuery library -->
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>

    <!-- Popper JS -->
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <style>
        h1 {
            margin-top: 2cm;
            margin-left: 12cm;
        }
        #b2 {
            margin-left: 4cm;
        }
        #b1 {
            margin-left: 7cm;
        }
        #b3 {
            margin-left: 5.5cm;
        }
        #b4{
        margin-top:4cm;	
        margin-left:19cm;
        text-align:center;
        }
        #p1 {
            margin-left: 84%;
        }
        #balanceLabel {
            display: none;
            width: 250px; /* Set the width as needed */
            margin-top: 3cm;
            margin-left: 17cm;
            text-align:center;
        }
        #depInput {
            display: none;
            width: 650px; /* Set the width as needed */
            margin-top: 3cm;
            margin-left: 12cm;
            text-align:center;
        }
        #transactionInput{
        	display:none;
        	width: 650px; /* Set the width as needed */
            margin-top: 3cm;
            margin-left: 12cm;
            text-align:center;
        }
    </style>
    <script>
    function logout(){
    	window.location.replace("http://localhost:8080/ATM/")
    }</script>
</head>
<body>
    <%-- Retrieve userid from the session --%>
    <% String userid = (String) request.getSession().getAttribute("userid"); %>
    <%-- Display userid in the HTML --%>
    <p id="p1">Welcome, <%= userid %></p>
    <!-- AJAX OPERATIONS Add a new script tag for handling AJAX requests -->
    <script>
        function checkBalance() {
            // Get userid from the session
            var userid = '<%= userid %>';

            // Create an XMLHttpRequest object
            var xhr = new XMLHttpRequest();

            // Configure it to make a GET request to the servlet for checkBalance
            xhr.open("GET", "CheckBalanceServlet?operation=checkBalance&userid=" + encodeURIComponent(userid), true);

            document.getElementById("depositInput").value = "";
            document.getElementById("transferAmount").value = "";
            document.getElementById("depInput").style.display = "none";
            document.getElementById("transactionInput").style.display = "none";

            // Define the function to handle the response
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // Display the balance in the p1 element
                    document.getElementById("p1").innerHTML = "Welcome, " + userid;
                    // Display the balance in the label below the buttons
                    document.getElementById("balanceLabel").innerHTML = "Balance: " + xhr.responseText;
                    // Show the balance label
                    document.getElementById("balanceLabel").style.display = "block";
                }
            };

            // Send the request
            xhr.send();
        }

        function deposit() {
            // Get userid from the session
            var userid = '<%= userid %>';

            // Get the deposit amount from the user
            var amount = document.getElementById("depositInput").value;

            // Create an XMLHttpRequest object
            var xhr = new XMLHttpRequest();

            // Configure it to make a GET request to the servlet for deposit
            xhr.open("GET", "CheckBalanceServlet?operation=deposit&userid=" + encodeURIComponent(userid) + "&amount=" + encodeURIComponent(amount), true);

            document.getElementById("transactionInput").value = "";
            document.getElementById("balanceLabel").style.display = "none";
            document.getElementById("transactionInput").style.display = "none";
            document.getElementById("depInput").style.display = "block";

            // Define the function to handle the response
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // Display the balance in the p1 element
                    document.getElementById("p1").innerHTML = "Welcome, " + userid;

                    document.getElementById("depositInput").value = "";
                }
            };

            // Send the request
            xhr.send();
        }

        function transferMoney() {
            // Get userids from the session
            var sourceUserId = '<%= userid %>';

            // Get the destination userid and transfer amount from the user
            var destinationUserId = document.getElementById("destinationUserId").value;
            var transferAmount = document.getElementById("transferAmount").value;

            // Create an XMLHttpRequest object
            var xhr = new XMLHttpRequest();

            // Configure it to make a GET request to the servlet for transferMoney
            xhr.open("GET", "TransferMoneyServlet?sourceUserId=" + encodeURIComponent(sourceUserId) +
                "&destinationUserId=" + encodeURIComponent(destinationUserId) +
                "&transferAmount=" + encodeURIComponent(transferAmount), true);

            document.getElementById("depositInput").value = "";
            document.getElementById("balanceLabel").style.display = "none";
            document.getElementById("depInput").style.display = "none";
            document.getElementById("transactionInput").style.display = "block";

            // Define the function to handle the response
            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4 && xhr.status === 200) {
                    // Display the response (e.g., success or error message) in the p1 element
                    document.getElementById("p1").innerHTML = xhr.responseText;

                    // Optionally, clear the input fields
                    document.getElementById("destinationUserId").value = "";
                    document.getElementById("transferAmount").value = "";
                }
            };

            // Send the request
            xhr.send();
        }
    </script>

    <h1 style="font-size: 2cm;"><big><b>Transaction Page</b></big></h1></br></br></br><br>

    <form action="CheckBalanceServlet" method="get">

        <div class="btns">
            <button style="font-size: 1.2cm;" type="button" class="btn btn-primary" id="b2" onclick="checkBalance()">Check Balance</button>
            <button style="font-size: 1.2cm;" type="button" class="btn btn-secondary" id="b3" onclick="deposit()">Deposit</button>
            <button style="font-size: 1.2cm;" type="button" class="btn btn-warning" id="b1" onclick="transferMoney()">Transfer Money</button>
        </div>
        <!-- Display balance in this label -->
        <label style="font-size:25px;" class="p-3 mb-2 bg-info text-white" id="balanceLabel"></label>
        <div id="depInput">
            <label style="font-size:16x; font-family:Arial; text-align:center;" class="p-3 mb-2 bg-success text-white" for="depositInput">Enter the Amount to Deposit&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label> 
            <input type="text" style="font-size:15px;" name="depositInput" id="depositInput"><br><br>
        </div>

        <!-- Input fields for transfer money -->
        <div id="transactionInput">
        	<p><big><b>Details for the Transaction</b></big></p><br>
            <label style="font-size: 16px;" for="destinationUserId">Destination User ID&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label> 
            <input type="text" name="destinationUserId" id="destinationUserId"><br><br>
            <label style="font-size: 16px;" for="transferAmount">Enter the Amount to Transfer&nbsp;&nbsp;&nbsp;</label> 
            <input type="text" name="transferAmount" id="transferAmount"><br><br>
        </div>

        
    </form>
    
    <button style="font-size: 0.5cm;" type="button" class="btn btn-danger" id="b4" onclick="logout()">Log out</button>
            
</body>
</html>
