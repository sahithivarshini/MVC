<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cart Store</title>
    <style>
        /* Your CSS styles */
        .product-container {
            margin-top: 70px;
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
        }
        .product {
            width: 200px;
            margin: 10px;
            padding: 10px;
            border: 2px solid rgba(255, 255, 255, 0.5); /* Transparent white border */
            border-radius: 5px;
            text-align: center;
        }
        .product img {
            width: 150px;
            height: 150px;
            border-radius: 5px;
        }
        .registerandcart {
            position: absolute;
            top: 10px;
            right: 10px;
            text-align: right;
        }
        body {
            background-image: url('bgimg.jpg'); /* Replace 'bgimg.jpg' with the path to your image */
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
            margin: 0; /* Reset default body margin */
            padding: 0; /* Reset default body padding */
            min-height: 100vh; /* Ensure the background covers the entire viewport */
            position: relative; /* Ensure correct stacking context for child elements */
        }
    </style>
</head>
<body>
<center><h1 style="color:white">Cart Store</h1></center>
<div class="registerandcart">
    <button id="login">Sign in/Register</button><br><br>
    <a href="cart.jsp">
        <img style="height:50px;width:50px" src="cart.png"/>
    </a>
</div>
<div class="catsort">
    <label style="color:white">Category:</label>
    <select id="category" style="width:150px;" required>
        <option value="">Select your category</option>
    </select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <label style="color:white">Sort:</label>
    <select id="price" style="width:150px;" required>
        <option value="">Sort By Price</option>
        <option>Low to High</option>
        <option>High to Low</option>
    </select><br><br>
</div>
<div class="product-container">
    <!-- Products will be displayed here -->
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script>
    document.getElementById("login").addEventListener("click", function() {
        window.location.href = "login.jsp";
    });

    $(document).ready(function () {
        // Fetch and display all products
        $.ajax({
            type: 'GET',
            url: 'AllProductServlet',
            dataType: 'json',
            success: function (response) {
                $('.product-container').empty();
                $.each(response, function(index, product) {
                    var productDiv = '<div class="product">' +
                        '<img src="' + product.image + '" alt="Product Image">' +
                        '<p>Product ID: ' + product.pid + '</p>' +
                        '<p>Product Name: ' + product.pname + '</p>' +
                        '<p>Price: ' + product.price + '</p>' +
                        '<p>HSN Code: ' + product.hsncode + '</p>' +
                        '<p>Category ID: ' + product.pcid + '</p>' + // Display category ID
                        '<button class="add-to-cart" data-product=\'' + JSON.stringify(product) + '\'>Add to Cart</button>' +
                        '</div>';
                    $('.product-container').append(productDiv);
                });
            },
            error: function () {
                console.error('Error occurred while fetching products');
            }
        });
        
        // Load categories
        $.ajax({
            type: 'GET',
            url: 'cat',
            dataType: 'html',
            success: function (response) {
                $('#category').append(response);
            },
            error: function () {
                console.error('Error occurred while invoking ProductCategoryServlet');
            }
        });

        // Handle category selection
        $('#category').change(function () {
            var categoryId = $(this).val();
            if (categoryId) {
                // Fetch products for the selected category
                $.ajax({
                    type: 'GET',
                    url: 'products',
                    data: {categoryId: categoryId},
                    dataType: 'json',
                    success: function (response) {
                        $('.product-container').empty();
                        $.each(response, function(index, product) {
                            var productDiv = '<div class="product">' +
                                '<img src="' + product.image + '" alt="Product Image">' +
                                '<p>Product ID: ' + product.pid + '</p>' +
                                '<p>Product Name: ' + product.pname + '</p>' +
                                '<p>Price: ' + product.price + '</p>' +
                                '<p>HSN Code: ' + product.hsncode + '</p>' +
                                '<p>Category ID: ' + product.pcid + '</p>' + // Display category ID
                                '<button class="add-to-cart" data-product=\'' + JSON.stringify(product) + '\'>Add to Cart</button>' +
                                '</div>';
                            $('.product-container').append(productDiv);
                        });
                    },
                    error: function () {
                        console.error('Error occurred while fetching products');
                    }
                });
            } else {
                // If "Select your category" is selected, display all products again
                $.ajax({
                    type: 'GET',
                    url: 'products',
                    dataType: 'json',
                    success: function (response) {
                        $('.product-container').empty();
                        $.each(response, function(index, product) {
                            var productDiv = '<div class="product">' +
                                '<img src="' + product.image + '" alt="Product Image">' +
                                '<p>Product ID: ' + product.pid + '</p>' +
                                '<p>Product Name: ' + product.pname + '</p>' +
                                '<p>Price: ' + product.price + '</p>' +
                                '<p>HSN Code: ' + product.hsncode + '</p>' +
                                '<p>Category ID: ' + product.pcid + '</p>' + // Display category ID
                                '<button class="add-to-cart" data-product=\'' + JSON.stringify(product) + '\'>Add to Cart</button>' +
                                '</div>';
                            $('.product-container').append(productDiv);
                        });
                    },
                    error: function () {
                        console.error('Error occurred while fetching products');
                    }
                });
            }
        });
    });

    function isProductServiceable(pinCode, pcid) {
        // Validate pin code and product category ID against serviceable regions table
        var serviceable = false;
        var sid = null;
        $.ajax({
            type: 'GET',
            url: 'checkServiceability',
            data: { pinCode: pinCode, productCategoryId: pcid }, // Include product category ID in the request data
            async: false, // Ensure synchronous execution
            success: function (response) {
                if (response.serviceable) {
                    serviceable = true;
                    sid = response.sid;
                }
            },
            error: function () {
                console.error('Error occurred while checking serviceability');
            }
        });
        return { serviceable: serviceable, sid: sid };
    }

    $(document).on('click', '.add-to-cart', function() {
        var productData = $(this).data('product');
        var pinCode = prompt("Enter your pin code:");
        if (pinCode !== null) {
            // Validate the pin code
            var serviceability = isProductServiceable(pinCode, productData.pcid);
            if (serviceability.serviceable) {
                var cartItems = JSON.parse(localStorage.getItem('cart') || '[]');
                productData.sid = serviceability.sid; // Add sid to product data
                cartItems.push(productData);
                localStorage.setItem('cart', JSON.stringify(cartItems));
                // Send cart data to the server
                $.ajax({
                    type: 'POST',
                    url: 'CartManagerServlet',
                    contentType: 'application/json; charset=utf-8',
                    data: JSON.stringify({ cart: cartItems }),
                    success: function(response) {
                        alert('Product added to cart and cart data saved to session!');
                    },
                    error: function(xhr, status, error) {
                        console.error('Error saving cart to session:', error);
                    }
                });
                alert('Product added to cart!');
            } else {
                alert('This product is not serviceable to your location.');
            }
        }
    });
</script>
</body>
</html>
