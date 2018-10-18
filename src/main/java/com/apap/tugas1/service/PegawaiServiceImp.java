package com.apap.tugas1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDB;

@Service
@Transactional
public class PegawaiServiceImp implements PegawaiService {

	@Autowired
	private PegawaiDB pegawaiDb;
	
	@Override
	public void addPegawai(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
	}

	@Override
	public void deletePegawai(PegawaiModel pegawai) {
		pegawaiDb.delete(pegawai);
	}

	@Override
	public PegawaiModel getPegawaiDetailById(long id) {
		return pegawaiDb.findById(id);
	}
	
	@Override
	public PegawaiModel getPegawaiDetailByNip(String nip) {
		return pegawaiDb.findByNip(nip);
	}

	@Override
	public List<PegawaiModel> getAllPegawai() {
		return pegawaiDb.findAll();
	}

	@Override
	public int getGaji(PegawaiModel pegawai) {
		JabatanModel tempJabatan = null;
		double gajiTerbesar = 0;
		for(int i = 0; i < pegawai.getJabatan_pegawai().size(); i++) {
			tempJabatan = pegawai.getJabatan_pegawai().get(i).getJabatan();
			if(tempJabatan.getGaji_pokok() > gajiTerbesar) {
				gajiTerbesar = tempJabatan.getGaji_pokok();
			}
		}
		double tunjangan = pegawai.getInstansi().getProvinsi().getPresentase_tunjangan();
		int gaji = (int) (gajiTerbesar + (tunjangan * gajiTerbesar / 100));
		return gaji;
	}
}
