package com.apap.tugas1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apap.tugas1.model.ProvinsiModel;

/**
 * 
 * @author Muhammad Aulia Adil
 *
 */
@Repository
public interface ProvinsiDB extends JpaRepository<ProvinsiModel, Long> {
}
