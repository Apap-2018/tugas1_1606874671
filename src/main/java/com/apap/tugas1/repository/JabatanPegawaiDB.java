package com.apap.tugas1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apap.tugas1.model.JabatanPegawaiModel;

/**
 * 
 * @author Muhammad Aulia Adil
 *
 */
@Repository
public interface JabatanPegawaiDB extends JpaRepository<JabatanPegawaiModel, Long> {
}
