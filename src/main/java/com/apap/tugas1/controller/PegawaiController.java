package com.apap.tugas1.controller;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;

@Controller
public class PegawaiController {
	
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	private PegawaiModel pegawai;
	
	@RequestMapping("/")
	private String home(Model model) {
		List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
		model.addAttribute("semuaJabatan", semuaJabatan);
		
		List<InstansiModel> semuaInstansi = instansiService.getAllInstansi();
		model.addAttribute("semuaInstansi", semuaInstansi);
		return "home";
	}
	
	@RequestMapping(value = "/pegawai/lihat", method = RequestMethod.GET)
	public String lihatPegawai(@RequestParam(value="nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		model.addAttribute("pegawai", pegawai);
		int gaji = pegawaiService.getGaji(pegawai);
		
//		JabatanModel tempJabatan = null;
//		double gajiTerbesar = 0;
//		for(int i = 0; i < pegawai.getJabatan_pegawai().size(); i++) {
//			tempJabatan = pegawai.getJabatan_pegawai().get(i).getJabatan();
//			if(tempJabatan.getGaji_pokok() > gajiTerbesar) {
//				gajiTerbesar = tempJabatan.getGaji_pokok();
//			}
//		}
//		double tunjangan = pegawai.getInstansi().getProvinsi().getPresentase_tunjangan();
//		int gaji = (int) (gajiTerbesar + (tunjangan * gajiTerbesar / 100));
	
		model.addAttribute("gaji", gaji);
		model.addAttribute("title", "Lihat Pegawai");
		return "lihat-pegawai";
	}
	
	@RequestMapping("/pegawai/lihat/semua")
	public String viewAll(Model model) {
		List<PegawaiModel> allPegawai = pegawaiService.getAllPegawai();
		model.addAttribute("allPegawai", allPegawai);
		model.addAttribute("title", "View All Pegawai");
		return "view-all-pegawai";
	}
	
	@RequestMapping(value="/pegawai/termuda-tertua", method = RequestMethod.GET)
	public String lihatTuaMuda(@RequestParam(value="id") String id, Model model) {
		
		InstansiModel instansi = instansiService.getInstansiDetailById(Long.parseLong(id));
		
		List<PegawaiModel> semuaPegawai = instansi.getPegawai();
		
		int tertua = 0;
		int termuda = 1000000000;
		
		PegawaiModel pegawaiTermuda = semuaPegawai.get(0);
		PegawaiModel pegawaiTertua = semuaPegawai.get(0);
		PegawaiModel pegawaiLoop;
		
		long millis=System.currentTimeMillis();  
		Date today = new Date(millis);  
		int age;
		Date birthday;
		for(int i = 0; i < semuaPegawai.size(); i++) {
			pegawaiLoop = semuaPegawai.get(i);
			birthday = pegawaiLoop.getTanggal_lahir();
			age = today.getYear() - birthday.getYear();
			if(age > tertua) {
				tertua = age;
				pegawaiTermuda = pegawaiLoop;
			}
			if(age < termuda) {
				termuda = age;
				pegawaiTertua = pegawaiLoop;
			}
		}
		
		model.addAttribute("gajiTermuda", pegawaiService.getGaji(pegawaiTermuda));
		model.addAttribute("gajiTertua", pegawaiService.getGaji(pegawaiTertua));
		
		model.addAttribute("pegawaiTermuda", pegawaiTermuda);
		model.addAttribute("pegawaiTertua", pegawaiTertua);
		model.addAttribute("title", "Pegawai Termuda Tertua");
		return "termuda-tertua";
	}
	
	@RequestMapping(value = "/pegawai/ubah/{nip}", method = RequestMethod.GET)
	private String ubahPegawai(@PathVariable(value ="nip") String nip, Model model) {
		PegawaiModel newPegawai = pegawaiService.getPegawaiDetailByNip(nip);
		model.addAttribute("pegawai", newPegawai);
		model.addAttribute("title", "Update Pegawai");
		return "updatePegawai";
	}
	
	@RequestMapping(value = "/pegawai/update", method = RequestMethod.POST)
	private String updatePegawaiSubmit(@ModelAttribute PegawaiModel pegawai) {
		PegawaiModel newPegawai = pegawaiService.getPegawaiDetailByNip(pegawai.getNip());
		newPegawai.setNama(pegawai.getNama());
		newPegawai.setTempat_lahir(pegawai.getTempat_lahir());
		newPegawai.setTanggal_lahir(pegawai.getTanggal_lahir());
		newPegawai.setTahun_masuk(pegawai.getTahun_masuk());
		newPegawai.setTempat_lahir(pegawai.getTempat_lahir());

		
		pegawaiService.addPegawai(newPegawai);
		return "update";	
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("pegawai", new PegawaiModel());
		model.addAttribute("title", "Tambah Pegawai");
		return "add-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST)
	private String addPegawaiSubmit(@ModelAttribute PegawaiModel pegawai) {
		pegawaiService.addPegawai(pegawai);
		return "add";
	}
	
	@RequestMapping("/pegawai/delete/{id}")
	public String deletePegawai(@RequestParam(value="id", required = false) long id, Model model) {
		String errorMessage = "";
		model.addAttribute("title", "Delete Pegawai");
		if(id != 0) {
			pegawai = pegawaiService.getPegawaiDetailById(id);
			if(pegawai != null) {
				pegawaiService.deletePegawai(pegawai);;
				String successMessage = "Pegawai dengan NIP " + pegawai.getNip() + "  berhasil dihapus!";
				model.addAttribute("successMessage", successMessage);
				return "delete";
			}
			else{
				errorMessage = "Error! Pegawai dengan NIP " + pegawai.getNip() + " tidak ditemukan";
				model.addAttribute("errorMessage", errorMessage);
				model.addAttribute("title", "Error");
				return "error";	
			}
		}
		else {
			errorMessage = "Error! Daftar Pegawai kosong";
			model.addAttribute("errorMessage", errorMessage);
			model.addAttribute("title", "Error");
			return "error";
		}
	}
}
