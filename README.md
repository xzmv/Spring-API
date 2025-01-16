Here’s the updated `README.md` without the Postman section, emphasizing Swagger as the primary tool for testing.

---

# Spring-API

This project is a bank transaction scheduling API. It allows users to create, update, delete, and retrieve bank transaction schedules. The API also calculates a transfer fee based on predefined scheduling rules.

---

## How to Test the Application

1. Clone the repository to your local machine using the command:
   ```bash
   git clone https://github.com/xzmv/Spring-API
   ```

2. Navigate to the project directory:
   ```bash
   cd Spring-API
   ```

3. Build the project and run the tests:
   ```bash
   mvn clean install
   ```

   3.1. If you don't have Maven installed, you can download it from the official website: [Maven](https://maven.apache.org/download.cgi).

   3.2. Extract the Maven zip file to the directory where you want to install Maven (e.g., `C:\Program Files\Apache\Maven`).

   3.3. Set up the environment variables:
    - Open Control Panel -> System and Security -> System -> Advanced system settings -> Environment Variables.
    - Create a new environment variable named `M2_HOME` and set its value to the Maven installation directory.
    - Add the Maven `bin` directory to the system `Path` variable (e.g., `C:\Program Files\Apache\Maven\bin`).

   3.4. Verify Maven installation:
    - Open a command prompt and run:
      ```bash
      mvn -v
      ```
    - Ensure Maven's version information is displayed.

4. After the tests pass and the build completes with "BUILD SUCCESS", run the application:
   ```bash
   mvn spring-boot:run
   ```

5. The application will be available at `http://localhost:8080`.

6. Open Swagger UI in your browser at:
   ```plaintext
   http://localhost:8080/swagger-ui.html
   ```

   Use Swagger UI to explore and test all endpoints interactively.

---

## Endpoints

### `POST /transactions`
- **Description**: Creates a new transaction and calculates the transfer fee based on scheduling rules.
- **Request body example**:
  ```json
  {
    "amount": 1500,
    "transactionDate": "2025-01-15",
    "scheduleDate": "2025-01-20",
    "description": "Payment for services"
  }
  ```
- **Responses**:
    - `201 Created`: Returns the created transaction with the calculated transfer fee.
    - `400 Bad Request`: Indicates invalid data, such as invalid dates or amounts.
    - `409 Conflict`: Indicates a duplicate transaction ID.

---

### `GET /transactions/{id}`
- **Description**: Retrieves a transaction by its ID.
- **Response example**:
  ```json
  {
    "id": 1,
    "amount": 1500,
    "transactionDate": "2025-01-15",
    "scheduleDate": "2025-01-20",
    "description": "Payment for services",
    "transferFee": 135.0
  }
  ```

---

### `GET /transactions`
- **Description**: Retrieves all transactions.
- **Response example**:
  ```json
  [
    {
      "id": 1,
      "amount": 1500,
      "transactionDate": "2025-01-15",
      "scheduleDate": "2025-01-20",
      "description": "Payment for services",
      "transferFee": 135.0
    },
    {
      "id": 2,
      "amount": 2500,
      "transactionDate": "2025-01-10",
      "scheduleDate": "2025-01-30",
      "description": "Large transfer",
      "transferFee": 172.5
    }
  ]
  ```

---

### `PUT /transactions/{id}`
- **Description**: Updates an existing transaction and recalculates the transfer fee.
- **Request body example**:
  ```json
  {
    "amount": 2000,
    "transactionDate": "2025-01-15",
    "scheduleDate": "2025-01-20",
    "description": "Updated transaction"
  }
  ```
- **Responses**:
    - `200 OK`: Returns the updated transaction.
    - `400 Bad Request`: Indicates invalid data.
    - `404 Not Found`: Indicates the transaction ID does not exist.

---

### `DELETE /transactions/{id}`
- **Description**: Deletes a transaction by its ID.
- **Responses**:
    - `204 No Content`: Indicates the transaction was successfully deleted.
    - `404 Not Found`: Indicates the transaction ID does not exist.

---

## Tests

The unit tests are located in the `src/test/java/com/example/spring_api` directory. They test the individual services and controllers.

Run the tests using:
```bash
mvn test
```

---

## Technologies Used

- **Java**
- **Spring Boot**
- **Maven**
- **JUnit**
- **Mockito**
- **Swagger (SpringDoc OpenAPI)**
- **Git**
- **GitHub**
- **IntelliJ IDEA**

Here is the updated section for your README.md file, integrating the images provided:

---

## API Proofs

### Create a New Transaction

![Create a New Transaction](/img/post-transaction.png)

### Retrieve a Transaction by ID

![Retrieve a Transaction by ID](/img/get-byId-transaction.png)

### Retrieve All Transactions

![Retrieve All Transactions](/img/get-all-transaction.png)

### Update an Existing Transaction

![Update an Existing Transaction](/img/put-transaction.png)

### Delete a Transaction

![Delete a Transaction](/img/delete-transaltion.png)

---

Replace the placeholder file paths with the exact directory if necessary and commit these changes to your repository. Let me know if further updates are needed!

## Author

This project was created by **Alfonso Villanueva**. 

---
