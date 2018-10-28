package com.apap.tugas1.service;

import java.util.List;
import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;


/**
 * PegawaiService
 * @author Muhammad Aulia Adil
 *
 */
public interface PegawaiService {
	void addPegawai(PegawaiModel pegawai);
	void deletePegawai(PegawaiModel pegawai);
	PegawaiModel getPegawaiDetailById(long id);
	PegawaiModel getPegawaiDetailByNip(String nip);
	String generateNip(PegawaiModel pegawai);
	int getGaji(PegawaiModel pegawai);
	List<PegawaiModel> getAllPegawai();
	List<PegawaiModel> getPegawaiByProvinsi(long provinsiId);
	List<PegawaiModel> getPegawaiByInstansi(InstansiModel instansi);
	List<PegawaiModel> getPegawaiByInstansiAndJabatan(InstansiModel instansi, JabatanModel jabatan);
	List<PegawaiModel> getPegawaiByProvinsiAndJabatan(long provinsiId, JabatanModel jabatan);
	List<PegawaiModel> getPegawaiByJabatan(JabatanModel jabatan);
}