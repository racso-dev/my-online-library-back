package com.steamulo.persistence.repository;

import com.steamulo.persistence.entity.Borrow;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRepository extends CrudRepository<Borrow, Long> {
    Optional<Borrow> findByBookId(String bookId);

    Optional<List<Borrow>> findByUserId(Long userId);
}
