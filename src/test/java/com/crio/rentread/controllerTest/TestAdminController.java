package com.crio.rentread.controllerTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.crio.rentread.controller.AdminController;
import com.crio.rentread.model.Book;
import com.crio.rentread.repository.BookRepository;

public class TestAdminController {

    @Mock
    private BookRepository bookRepository;

    private AdminController adminController;

    

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(bookRepository);
    }

    @Test
    public void testAddBook_Success() {
        // Mock HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic encoded_credentials");

        // Mock Book to add
        Book bookToAdd = new Book();
        bookToAdd.setTitle("Sample Book");
        bookToAdd.setAuthor("Sample Author");
        bookToAdd.setGenre("Sample Genre");

        // Mock behavior of bookRepository.save()
        when(bookRepository.save(any(Book.class))).thenReturn(bookToAdd);

        // Test addBook method
        ResponseEntity<?> response = adminController.addBook(bookToAdd, headers);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Book);

        Book addedBook = (Book) response.getBody();
        assertEquals("Sample Book", addedBook.getTitle());
        assertEquals("Sample Author", addedBook.getAuthor());
        assertEquals("Sample Genre", addedBook.getGenre());
        assertTrue(addedBook.isAvailability());
    }

    @Test
    public void testUpdateBook_Success() {
        // Mock HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic encoded_credentials");

        // Mock existing Book
        Long existingBookId = 1L;
        Book existingBook = new Book();
        existingBook.setId(existingBookId);
        existingBook.setTitle("Existing Book");
        existingBook.setAuthor("Existing Author");
        existingBook.setGenre("Existing Genre");

        // Mock updated Book details
        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Book Title");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setGenre("Updated Genre");
        updatedBook.setAvailability(true); // Ensure availability is set

        // Mock behavior of bookRepository.findById()
        when(bookRepository.findById(existingBookId)).thenReturn(Optional.of(existingBook));

        // Mock behavior of bookRepository.save()
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            return savedBook;
        });

        // Test updateBook method
        ResponseEntity<?> response = adminController.updateBook(existingBookId, updatedBook, headers);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Book);

        Book updatedBookResponse = (Book) response.getBody();
        assertEquals("Updated Book Title", updatedBookResponse.getTitle());
        assertEquals("Updated Author", updatedBookResponse.getAuthor());
        assertEquals("Updated Genre", updatedBookResponse.getGenre());
        assertTrue(updatedBookResponse.isAvailability());
    }

    @Test
    public void testDeleteBook_Success() {
        // Mock HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Basic encoded_credentials");

        // Mock existing Book
        Long existingBookId = 1L;

        // Test deleteBook method
        ResponseEntity<?> response = adminController.deleteBook(existingBookId, headers);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Successfully", response.getBody());
        verify(bookRepository, times(1)).deleteById(existingBookId);
    }

    @Test
    public void testDeleteBook_Unauthorized() {
        // Mock HttpHeaders without AUTHORIZATION header
        HttpHeaders headers = new HttpHeaders();

        // Mock existing Book
        Long existingBookId = 1L;

        // Test deleteBook method without AUTHORIZATION header
        ResponseEntity<?> response = adminController.deleteBook(existingBookId, headers);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
        verify(bookRepository, never()).deleteById(existingBookId); // Ensure deleteById is never called
    }

    @Test
    public void testGetAllBooks() {
        // Mock list of books
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(new Book(1L, "Book 1", true, "Author 1", "Genre 1"));
        mockBooks.add(new Book(2L, "Book 2", true, "Author 2", "Genre 2"));

        // Mock behavior of bookRepository.findAll()
        when(bookRepository.findAll()).thenReturn(mockBooks);

        // Test getAllBooks method
        List<Book> books = adminController.getAllBooks();

        // Assertions
        assertEquals(2, books.size());
        assertEquals("Book 1", books.get(0).getTitle());
        assertEquals("Author 2", books.get(1).getAuthor());
    }
}

