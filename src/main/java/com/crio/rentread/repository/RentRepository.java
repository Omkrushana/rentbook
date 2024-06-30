package com.crio.rentread.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

import com.crio.rentread.model.Rent;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByUserId(Long userId);

    Optional<Rent> findByUserIdAndBookId(Long userId, Long bookId);

}