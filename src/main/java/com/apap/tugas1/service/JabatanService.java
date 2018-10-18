package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.JabatanModel;

/**
 * JabatanService
 * @author Muhammad Aulia Adil
 *
 */
public interface JabatanService {
	void addJabatan(JabatanModel jabatan);
	void deleteJabatan(JabatanModel jabatan);
	JabatanModel getJabatanDetailById(long id);
	List<JabatanModel> getAllJabatan();
}
