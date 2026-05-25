package com.coroto.backend.repository;

import com.coroto.backend.model.OrdenItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdenItemRepository  extends JpaRepository<OrdenItem, Long> {
}

