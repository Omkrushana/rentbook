package com.crio.rentread.controllerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.crio.rentread.controller.UserController;
import com.crio.rentread.repository.*;
import com.crio.rentread.model.Book;
import com.crio.rentread.model.Rent;
import com.crio.rentread.model.User;

@RunWith(MockitoJUnitRunner.class)
public class TestUserController {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RentRepository rentRepository;

    @Test
    public void testGetAllBooks() {
        // Mock data
        List<Book> mockBooks = Arrays.asList(
                new Book(1L, "Book 1", true, "Auther 1", "Genre 1"),
                new Book(2L, "Book 2", true, "Auther 2", "Genre 2"));

        // Mock repository behavior
        when(bookRepository.findAll()).thenReturn(mockBooks);

        // Call controller method
        ResponseEntity<List<Book>> response = userController.getAllBooks();

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockBooks.size(), response.getBody().size());
    }

    

    @Test(expected = RuntimeException.class)
    public void testRentBook_bookNotAvailable() {
        // Mock authenticated user
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser@example.com");

        // Mock data
        User mockUser = new User(1L, "testuser@example.com", "password", "John", "Doe", 1L, false);
        Book mockBook = new Book(1L, "Book 1", false, "Author 1", "Genre 1"); // Book not available

        // Mock repository behavior
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));

        // Call controller method (should throw RuntimeException)
        userController.rentBook(1L);
    }

    @Test(expected = RuntimeException.class)
    public void testRentBook_tooManyRentals() {
        // Mock authenticated user
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("testuser@example.com");

        // Mock data
        User mockUser = new User(1L, "testuser@example.com", "password", "John", "Doe", 1L, false);
        Book mockBook = new Book(1L, "Book 1", true, "Genre 1", "Author 1");

        // Mock repository behavior
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(mockUser));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(rentRepository.findByUserId(1L)).thenReturn(Arrays.asList(new Rent(), new Rent())); // Two active rentals

        // Call controller method (should throw RuntimeException)
        userController.rentBook(1L);
    }
}
