package com.crio.rentread.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.rentread.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
