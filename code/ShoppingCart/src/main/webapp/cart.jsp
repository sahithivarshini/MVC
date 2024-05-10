<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart</title>
    <style>
        body {
            background-image: url('bgimg.jpg');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            margin: 0;
            padding: 0;
            height: 100vh; 
        }
        .cart-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
        }
        .cart-item {
            border: 2px solid black;
            border-radius: 5px;
            margin: 10px;
            padding: 10px;
            width: calc(20% - 20px); /* 20% width with margin calculation */
            box-sizing: border-box; /* Include padding and border in the width calculation */
            background-color: white;
        }
        .cart-item img {
            width: 100%;
            border-radius: 5px;
        }
        .cart-item-details {
            margin-top: 10px;
        }
        .remove-btn {
            border: none;
            padding: 5px 10px;
            border-radius: 5px;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <center>
        <h1>Cart</h1>
        <div class="cart-container" id="cartItems">
            <!-- Cart items will be displayed here -->
        </div>
        <br><br>
        <button id="saveCart">Save Cart</button>
        <br><br>
        <form action="CheckoutServlet" method="get">
            <div class="abc">
                <center>
                    <input type="submit" value="Order">
                </center>
            </div>
        </form>
    </center>

    <script>
        // Retrieve cart items from local storage
        var cartItems = JSON.parse(localStorage.getItem('cart')) || [];
        var cartContainer = document.getElementById('cartItems');

        // Function to remove all cart items from page and local storage
        function clearCart() {
            cartContainer.innerHTML = ''; // Clear cart items from the page
            cartItems = []; // Clear cartItems array
            localStorage.removeItem('cart'); // Clear cartItems from local storage
        }

        // Display cart items using divs
        function displayCartItems() {
            cartContainer.innerHTML = ''; // Clear existing cart items
            cartItems.forEach(function(item, index) {
                var cartItemDiv = document.createElement('div');
                cartItemDiv.classList.add('cart-item');

                var image = document.createElement('img');
                image.src = item.image;

                var itemDetailsDiv = document.createElement('div');
                itemDetailsDiv.classList.add('cart-item-details');
                itemDetailsDiv.innerHTML = '<p><strong>Product ID:</strong> ' + item.pid + '</p>' +
                                           '<p><strong>Product Name:</strong> ' + item.pname + '</p>' +
                                           '<p><strong>Price:</strong> ' + item.price + '</p>' +
                                           '<p><strong>HSN Code:</strong> ' + item.hsncode + '</p>';

                var removeBtn = document.createElement('button');
                removeBtn.classList.add('remove-btn');
                removeBtn.textContent = 'Remove From Cart';
                removeBtn.addEventListener('click', function() {
                    cartItems.splice(index, 1); // Remove item from cartItems array
                    localStorage.setItem('cart', JSON.stringify(cartItems)); // Update local storage
                    displayCartItems(); // Refresh cart display
                    if (cartItems.length === 0) clearCart(); // If cart is empty, clear all items
                });

                cartItemDiv.appendChild(image);
                cartItemDiv.appendChild(itemDetailsDiv);
                cartItemDiv.appendChild(removeBtn);
                cartContainer.appendChild(cartItemDiv);
            });
        }

        // Save cart button event listener
        document.getElementById('saveCart').addEventListener('click', function() {
            // Check if cart is empty before sending
            if (cartItems.length === 0) {
                alert('Your cart is empty!');
                return;
            }

            // Check stock availability for each item in the cart
            var outOfStockItems = [];
            var promises = [];
            cartItems.forEach(function(item) {
                var promise = $.ajax({
                    url: 'StockCheckServlet',
                    type: 'GET',
                    data: { pid: item.pid }, // Send product ID
                    dataType: 'json'
                }).then(function(response) {
                    if (response.stock <= 0) {
                        outOfStockItems.push(item.pname);
                    }
                });
                promises.push(promise);
            });

            // Once all requests are completed, display the result
            $.when.apply($, promises).then(function() {
                if (outOfStockItems.length > 0) {
                    alert('The following items are not available: ' + outOfStockItems.join(', ') +
                        '. We will let you know once they are available.');
                } else {
                    // If all items are available, proceed to save the cart
                    saveCartToServer();
                }
            });
        });

        // Function to save cart to server
        function saveCartToServer() {
            $.ajax({
                url: 'CartManagerServlet',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify({items: cartItems}),
                success: function(response) {
                    console.log('Server response:', response);
                    alert('Cart saved successfully!');
                },
                error: function(xhr, status, error) {
                    console.error('Error while saving cart:', error);
                    alert('Failed to save cart.');
                }
            });
        }

        // Display cart items when the page loads
        displayCartItems();

    </script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
</body>
</html>
