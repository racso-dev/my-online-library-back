package com.steamulo.persistence.repository;

import com.steamulo.enums.Page;
import com.steamulo.persistence.entity.TextPage;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TextPageRepository extends JpaRepository<TextPage, Long> {
    TextPage findByPage(Page page);
}
