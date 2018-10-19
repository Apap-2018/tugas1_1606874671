package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.ProvinsiModel;

/**
 * JabatanService
 * @author Muhammad Aulia Adil
 *
 */
public interface ProvinsiService {
	ProvinsiModel getProvinsiDetailById(long id);
	List<ProvinsiModel> getAllProvinsi();
}
