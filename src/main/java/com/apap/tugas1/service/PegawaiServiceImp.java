package com.apap.tugas1.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.InstansiModel;
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
		for(int i = 0; i < pegawai.getJabatan().size(); i++) {
			tempJabatan = pegawai.getJabatan().get(i);
			if(tempJabatan.getGaji_pokok() > gajiTerbesar) {
				gajiTerbesar = tempJabatan.getGaji_pokok();
			}
		}
		double tunjangan = pegawai.getInstansi().getProvinsi().getPresentase_tunjangan();
		int gaji = (int) (gajiTerbesar + (tunjangan * gajiTerbesar / 100));
		return gaji;
	}

	@Override
	public String generateNip(PegawaiModel pegawai) {
		
		DateFormat df = new SimpleDateFormat("ddMMyy");
		Date tglLahir = pegawai.getTanggal_lahir();
		String tglLahirFormatted = df.format(tglLahir);
		System.out.println("date->"+tglLahirFormatted);
		
		String idInstansi = "" + pegawai.getInstansi().getId();
		int idAkhir = 0;
		for (PegawaiModel peg : this.getAllPegawai()) {
			if (peg.getTanggal_lahir().equals(pegawai.getTanggal_lahir()) && peg.getTahun_masuk().equals(pegawai.getTahun_masuk())) {
				idAkhir+=1;
			}
		}
		idAkhir+=1;
		
		String kodeUnik;
		if(idAkhir<10) kodeUnik = "0" + idAkhir;
		else kodeUnik = "" + idAkhir;
		
		String tahunMasuk = pegawai.getTahun_masuk();
		String nip = idInstansi + tglLahirFormatted + tahunMasuk + kodeUnik;
		
		return nip;
	}

	@Override
	public List<PegawaiModel> getPegawaiByProvinsi(long idProvinsi) {
		List<PegawaiModel> hasil = new ArrayList<>();
		for(PegawaiModel pegawai : pegawaiDb.findAll()) {
			if (pegawai.getInstansi().getProvinsi().getId() == idProvinsi) hasil.add(pegawai);
		}
		return hasil;
	}
	
	@Override
	public List<PegawaiModel> getPegawaiByInstansi(InstansiModel instansi) {
		return pegawaiDb.findByInstansi(instansi);
	}
	
	@Override 
	public List<PegawaiModel> getPegawaiByInstansiAndJabatan(InstansiModel instansi, JabatanModel jabatan) {
		List<PegawaiModel> hasil = new ArrayList<>();
		for(PegawaiModel pegawai : pegawaiDb.findByInstansi(instansi)) {
			if (pegawai.getJabatan().contains(jabatan)) hasil.add(pegawai);
		}
		return hasil;
	}
	
	@Override
	public List<PegawaiModel> getPegawaiByProvinsiAndJabatan(long idProvinsi, JabatanModel jabatan){
		List<PegawaiModel> hasil = new ArrayList<>();
		
		for(PegawaiModel pegawai : this.getPegawaiByProvinsi(idProvinsi)) {
			if (pegawai.getJabatan().contains(jabatan)) hasil.add(pegawai);
		}
		return hasil;
	}
	
	@Override
	public List<PegawaiModel> getPegawaiByJabatan(JabatanModel jabatan){
		return pegawaiDb.findByJabatan(jabatan);
	}
	
}
