package com.crio.rentread.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.crio.rentread.model.Book;
import com.crio.rentread.model.Rent;
import com.crio.rentread.model.User;
import com.crio.rentread.repository.BookRepository;
import com.crio.rentread.repository.RentRepository;
import com.crio.rentread.repository.UserRepository;

import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        // Retrieve authenticated user details

        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // String currentPrincipalName = authentication.getName();
        // System.out.println("Current user: " + currentPrincipalName);

        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok().body(books);
    }

    @PostMapping("/books/{bookId}/rent")
    public Rent rentBook(@PathVariable Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isAvailability()) {
            throw new RuntimeException("Book is not available for rent");
        }

        List<Rent> rentals = rentRepository.findByUserId(user.getId());
        if (rentals.size() >= 2) {
            throw new RuntimeException("User cannot have more than two active rentals");
        }

        book.setAvailability(false);
        bookRepository.save(book);

        Rent rent = new Rent();
        rent.setUser(user);
        rent.setBook(book);
        rent.setRentDT(LocalDateTime.now());
        return rentRepository.save(rent);
    }

    @PostMapping("/books/{bookId}/return")
    public ResponseEntity<String> returnBook(@PathVariable Long bookId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Find the rental record for the current user and book
        Rent rental = rentRepository.findByUserIdAndBookId(user.getId(), bookId)
                .orElse(null); // Use orElse(null) to handle non-existing rental

        if (rental == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Rental record not found or book is not rented by the current user");
        }

        // Check if the rental record belongs to the current user
        if (!rental.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You are not authorized to return this book");
        }

        // Mark the book as available
        book.setAvailability(true);
        bookRepository.save(book);

        // Set return date/time
        rental.setReturnDT(LocalDateTime.now());

        // Save the updated rental record
        rentRepository.save(rental);

        return ResponseEntity.ok("Book returned successfully");
    }

}
