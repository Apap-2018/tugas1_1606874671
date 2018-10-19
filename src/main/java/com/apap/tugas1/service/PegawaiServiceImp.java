package com.apap.tugas1.service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.InstansiDB;
import com.apap.tugas1.repository.PegawaiDB;
import com.apap.tugas1.repository.ProvinsiDB;

@Service
@Transactional
public class PegawaiServiceImp implements PegawaiService {

	@Autowired
	private PegawaiDB pegawaiDb;
	
	@Autowired
	private ProvinsiDB provinsiDb;
	
	@Autowired
	private InstansiDB instansiDb;
	
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

	@Override
	public String generateNip(PegawaiModel pegawai) {
		
		String idInstansi = "" + pegawai.getInstansi().getId();
		Date tanggalLahir = pegawai.getTanggal_lahir();
		
		int hari = tanggalLahir.getDay() + 1;
		int bulan = tanggalLahir.getMonth() + 1;
		String sHari = "" + hari;
		if(hari < 10) {
			sHari = "0" + sHari;
		}
		
		String sBulan = "" + bulan;
		if(bulan < 10) {
			sBulan = "0" + sBulan;
		}
				
		System.out.println("hari = " + sHari + " bulan = " + sBulan + " tahun = " + tanggalLahir.getYear());
		String tahunMasuk = pegawai.getTahun_masuk();
		String nip = idInstansi + sHari + sBulan + tanggalLahir.getYear() + tahunMasuk + "01";
		
		for(int i = 0; i < pegawaiDb.findAll().size(); i++) {
			if(pegawaiDb.findAll().get(i).getNip().substring(0, 14).equalsIgnoreCase(nip)) {
				int digitLain = Integer.parseInt(pegawaiDb.findAll().get(i).getNip().substring(14, 16));
				int digitSendiri = digitLain + 1;
				nip = nip.substring(0, 14) + digitSendiri;
			}
		}
		return nip;
	}
}
