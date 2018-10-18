package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.InstansiModel;

/**
 * JabatanService
 * @author Muhammad Aulia Adil
 *
 */
public interface InstansiService {
	InstansiModel getInstansiDetailById(long id);
	List<InstansiModel> getAllInstansi();
}
