package com.apap.tugas1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apap.tugas1.model.InstansiModel;

/**
 * 
 * @author Muhammad Aulia Adil
 *
 */
@Repository
public interface InstansiDB extends JpaRepository<InstansiModel, Long> {
	InstansiModel findById(long id);
}