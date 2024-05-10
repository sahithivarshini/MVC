<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.SQLException" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f8f8;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
        }
        form {
            margin-top: 20px;
            text-align: center;
        }
        select {
            padding: 8px;
            font-size: 16px;
            border-radius: 5px;
        }
        .total {
            font-weight: bold;
            text-align: right;
            margin-top: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            padding: 8px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }
    </style>
</head>
<body>

    <div class="container">
        <h1>Checkout</h1>
        <table>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Mobile</th>
                    <th>Location</th>
                    <th>Address</th>
                </tr>
            </thead>
            <tbody>
               <%
               String username = (String) session.getAttribute("username");

    // Retrieve username from request parameter
    System.out.println("Username: " + username);
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    
    try {
        // Establish database connection
        System.out.println("Establishing database connection...");
        conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
                "plf_training_admin", "pff123");
        System.out.println("Database connection established.");
        
        // Prepare SQL query to retrieve user details
        String sql = "SELECT name, mobile, location, address FROM customer2003 WHERE username = ?";
        System.out.println("SQL Query: " + sql);
        stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        
        // Execute query
        System.out.println("Executing query...");
        rs = stmt.executeQuery();
        System.out.println("Query executed successfully.");
        
        // Display user details
        while (rs.next()) {
%>
<tr>
    <td><%= rs.getString("name") %></td>
    <td><%= rs.getString("mobile") %></td>
    <td><%= rs.getString("location") %></td>
    <td><%= rs.getString("address") %></td>
</tr>
<%
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Close resources
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
%>
            </tbody>
        </table><br><br>
        <center><button onclick="showForm()">Confirm Your Credentials</button></center>
         
    </div>
    <center><button id="rzp-button1">Pay Now</button></center>
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
<script>
var options = {
	    "key": "rzp_test_DvOXJPYtWpr5MM", // Enter the Key ID generated from the Dashboard
	    "amount": "1000",
	    "currency": "INR",
	    "description": "Acme Corp",
	    "image": "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg",
	    "prefill":
	    {
	      "email": "gaurav.kumar@example.com",
	      "contact": +919900000000,
	    },
	    config: {
	      display: {
	        blocks: {
	          utib: { //name for Axis block
	            name: "Pay using Axis Bank",
	            instruments: [
	              {
	                method: "card",
	                issuers: ["UTIB"]
	              },
	              {
	                method: "netbanking",
	                banks: ["UTIB"]
	              },
	            ]
	          },
	          other: { //  name for other block
	            name: "Other Payment modes",
	            instruments: [
	              {
	                method: "card",
	                issuers: ["ICIC"]
	              },
	              {
	                method: 'netbanking',
	              }
	            ]
	          }
	        },
	        hide: [
	          {
	          method: "upi"
	          }
	        ],
	        sequence: ["block.utib", "block.other"],
	        preferences: {
	          show_default_blocks: false // Should Checkout show its default blocks?
	        }
	      }
	    },
	    "handler": function (response) {
	      alert(response.razorpay_payment_id);
	    },
	    "modal": {
	      "ondismiss": function () {
	        if (confirm("Are you sure, you want to close the form?")) {
	          txt = "You pressed OK!";
	          console.log("Checkout form closed by the user");
	        } else {
	          txt = "You pressed Cancel!";
	          console.log("Complete the Payment")
	        }
	      }
	    }
	  };
	  
	  var rzp1 = new Razorpay(options);
	  document.getElementById('rzp-button1').onclick = function (e) {
	    rzp1.open();
	    e.preventDefault();
	  }
</script>
    <script>
    function showForm() {
        alert("Please click on Pay Now to proceed with payment.");
    }
    // Function to toggle the visibility of the payment form and enable/disable the Pay Now button
    

    </script>
    


    
</body>
</html>
