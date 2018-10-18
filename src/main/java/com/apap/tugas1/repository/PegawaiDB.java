package com.apap.tugas1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apap.tugas1.model.PegawaiModel;

/**
 * 
 * @author Muhammad Aulia Adil
 *
 */
@Repository
public interface PegawaiDB extends JpaRepository<PegawaiModel, Long> {
	PegawaiModel findById(long id);
	PegawaiModel findByNip(String nip);
}
