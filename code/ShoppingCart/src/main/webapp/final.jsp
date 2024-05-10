<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
 
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Order Details</title>
    <style>
       body {
            background-image: url('fbg.jpg'); /* Replace 'background-image.jpg' with the path to your image */
            background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    margin: 0; /* Reset default body margin */
    padding: 0; /* Reset default body padding */
    height: 100vh; 
        }
    th,td{
    border:2px solid black;
    
    }
    .pay-button {
            color: black;
            background-color: transparent;
            padding: 10px 20px;
            border: 2px solid black;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
        }
        .pay-button:hover {
        color: white;
            background-color: #333; /* Darken the background color on hover */
        }
    
    </style>
</head>
<body>
<div class="finaldiv"> 
<center>
    <h2>Order ID: ${sessionScope.oid != null ? sessionScope.oid : "No Order ID found"} Is Successful!</h2>
    </center>
    <% // Get the oid from the session
    int oid = (int) session.getAttribute("oid");
    double t = 0.0; // Initialize t to avoid compilation errors
    ResultSet rs = null; // Initialize rs to avoid compilation errors
    double shippingCharges =0.0;
    // Use oid in your SQL query
    String sqlQuery = "SELECT * FROM ordered_products2003 WHERE oid = ?";
    String s = "SELECT oname, totalprice FROM orders2003 WHERE oid = ?";
   // String sc="select shippingcharges from products2003 where pid=?";
    try {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
                "plf_training_admin", "pff123");
        Connection conn2 = DriverManager.getConnection("jdbc:postgresql://192.168.110.48:5432/plf_training",
                "plf_training_admin", "pff123");
        PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
        PreparedStatement p = conn.prepareStatement(s);
       // PreparedStatement pc = conn.prepareStatement(sc);
        preparedStatement.setInt(1, oid);
        p.setInt(1, oid);
        //pc.setInt(1,pid);
        ResultSet r = p.executeQuery();
        rs = preparedStatement.executeQuery();
       // rsc=pc.executeQuery();
        while (r.next()) {
            System.out.println(r.getString("oname"));
            t = r.getDouble("totalprice");
        }
//while(rsc.next())
//{
//	 shippingCharges = shippingResult.getDouble("shippingcharges");
//}
        // Process the result set as needed
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle any errors
    }
    %>
    <center>
    <table style="border-collapse:collapse;width:50%">
        <thead>
            <tr>
                <th>Product Name</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>GST</th>
                <th>PricewithGst</th>
             <!--  <th>Shipping Charges</th>  --> 
            </tr>
        </thead>
        <tbody>
            <% while (rs != null && rs.next()) { %>
            <tr>
                <td><%= rs.getString("pname") %></td>
                <td><%= rs.getInt("qty") %></td>
                <td><%= rs.getDouble("price") %></td>
                <td><%= rs.getDouble("gst") %></td>
                 <td><%= (rs.getDouble("gst")+rs.getDouble("price")) %></td>
                 <%--   <td><%= rsc.getDouble("shippingcharges") %></td>--%> 
            </tr>
            <% } %>
        </tbody>
    </table>
    </center>
   <center><h3>Total Price: <%= String.format("%.2f", t) %></h3></center></div>  <!-- Convert double to String and format it -->
   <center><button id="payButton" class="pay-button" >Proceed To Pay</button></center>
   <script>
   
  
// Get a reference to the button
var payButton = document.getElementById('payButton');

// Add a click event listener to the button
payButton.addEventListener('click', function() {
    // Construct the URL with payment details as query parameters
    var totalPrice = <%= t %>; // Get total price from JSP variable
    var totalPriceWithShipping = <%= t + 250 %>; // Calculate total price with shipping
    var url = 'checkout.jsp?totalPrice=' + totalPrice + '&totalPriceWithShipping=' + totalPriceWithShipping;

    // Redirect the user to checkout.jsp with the constructed URL
    window.location.href = url;
});



   </script>
</body>
</html>