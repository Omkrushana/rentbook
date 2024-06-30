
## Setup Instructions

1. Clone the repository.
2. Configure your database settings in application.properties.
3. Build and run the application.
4. Use Postman or any API client to interact with the Admin API endpoints.
### Notes
Replace {id} with the actual ID of the book when making requests.
Ensure proper authorization headers (Authorization: Basic <base64-encoded-credentials>) are included in each request.
# Rent Book

RESTful API service using Spring Boot to manage an online book rental system while using MySQL to persist the data.


## Admin 


`Username` user1

`Password` password


## Admin API

#### Add a Book

```http
  POST /admin/books
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `user1:password` | `Basic_Auth` | **Required**. Body {
    "title": "Book Title",
    "author": "Book Author",
    "genre": "Book Genre",
    "availability": true
}
 |

Response
200 OK: Returns details of the added book.
401 Unauthorized: If the request lacks proper authorization headers.
#### Update a Book

```http
  PUT /admin/books/{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `user1:password` | `Basic_Auth` | **Required**. Body {
    "title": "Updated Book Title",
    "author": "Updated Book Author",
    "genre": "Updated Book Genre",
    "availability": true
}
 |

#### Delete a Book
```http
  DELETE /admin/books/{id}
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `user1:password` | `Basic_Auth` | {id}: ID of the book to delete. |

Response
200 OK: If the book is deleted successfully.
401 Unauthorized: If the request lacks proper authorization headers.
404 Not Found: If the book with the specified ID does not exist.

#### Get All Books
```http
  GET /admin/books
```
| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `user1:password` | `Basic_Auth` | Retrieves a list of all books in the system.|

Response
200 OK: Returns a list of all books.
401 Unauthorized: If the request lacks proper authorization headers.


## User API
### Authentication

All endpoints require Basic Authentication with registered user credentials:

Username: `<your_registered_username>`

Password: `<your_registered_password>`
#### Get All Books

```http
   GET /user/books
```
#### Retrieves a list of all available books.

#### Response:
```
 1. 200 OK: Returns a list of all books.
 2. 401 Unauthorized: If the request lacks proper authorization headers.
 ```

#### Rent a Book
```http
   POST /user/books/{bookId}/rent
```

#### Rents a book with the specified ID.

#### Path Parameters
{bookId}: ID of the book to rent.
#### Response
```
1. 200 OK: Returns details of the rented book.
2. 400 Bad Request: If the book is not available for rent or the user has reached the limit of active rentals.
3. 401 Unauthorized: If the request lacks proper authorization headers.
```

#### Return a Book
```http
 POST /user/books/{bookId}/return
```
#### Returns a rented book with the specified ID.

#### Path Parameters
{bookId}: ID of the book to return.
#### Response
```
200 OK: Message confirming successful book return.
400 Bad Request: If the book is not rented by the current user.
401 Unauthorized: If the request lacks proper authorization headers.
404 Not Found: If the rental record or book with the specified ID does not exist.
```