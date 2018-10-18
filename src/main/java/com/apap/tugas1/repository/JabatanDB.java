package com.apap.tugas1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apap.tugas1.model.JabatanModel;

/**
 * 
 * @author Muhammad Aulia Adil
 *
 */
@Repository
public interface JabatanDB extends JpaRepository<JabatanModel, Long> {
	JabatanModel findById(long id);
}
