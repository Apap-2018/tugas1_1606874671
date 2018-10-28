package com.apap.tugas1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
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
	List<PegawaiModel> findByInstansi(InstansiModel instansi);
	List<PegawaiModel> findByJabatan(JabatanModel jabatan);
}