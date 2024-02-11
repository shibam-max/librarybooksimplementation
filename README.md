Book Management System

Welcome to the Book Management System! This application allows users to manage books through RESTful endpoints.

Prerequisites:

Java Development Kit (JDK) version 8 or later Apache Maven Postman (optional, for API testing) MySql Create any database in MySql server

Installation Clone the repository to your local machine: git clone https://github.com/shibam-max/librarybooksimplementation.git

Navigate to the project directory: cd jwt2

Update Database connection credentials in application.properties file: update this credentials: spring.datasource.url = jdbc:mysql://localhost:3306/ spring.datasource.username = spring.datasource.password = server.port=

Build the project using Maven: mvn clean install

Run the application: java -jar target/jwt2-0.0.1-SNAPSHOT.jar

Application should be up and running on port 8082

Usage:

Authentication: Use the /auth/login endpoint to authenticate and obtain a JWT token. Provide your username and password in the request body. Copy the token from the response.

Adding a User: Use the /auth/addNewUser endpoint to add a new user. Provide the user details (username, password, role) in the request body.

Adding a Book: Use the /auth/admin/addBook endpoint to add a new book. Provide the book details (name, author, publication year) in the request body. Include the Bearer JWT token in the request header.

Deleting a Book Use the /auth/admin/deleteBook endpoint to delete a book. Provide the book name as a query parameter (bookName) in the URL. Include the Bearer JWT token in the request header.

Viewing Books Use the /auth/admin/home endpoint to view all books (admin). Use the /auth/user/home endpoint to view all books (user). Include the Bearer JWT token in the request header.

Examples (Postman) Authentication: Send a POST request to /auth/login with credentials in the request body. Copy the bearer token from the response. Adding a User: Send a POST request to /auth/addNewUser with the user details in the request body. Adding a Book: Send a POST request to /auth/admin/addBook with the book details in the request body and the bearer token in the header. Deleting a Book: Send a DELETE request to /auth/admin/deleteBook?bookName=BookName with the bearer token in the header. Viewing Books: Send a GET request to /auth/admin/home or /auth/user/home with the bearer token in the header.

Support For any issues or questions, please contact shibamsamaddar1999@gmail.com.
