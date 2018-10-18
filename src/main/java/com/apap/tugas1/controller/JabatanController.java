package com.apap.tugas1.controller;

import java.text.NumberFormat;
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

import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.service.JabatanService;

@Controller
public class JabatanController {

	@Autowired
	private JabatanService jabatanService;
	private JabatanModel jabatan;
	
	@RequestMapping(value = "/jabatan/lihat", method = RequestMethod.GET)
	public String lihatJabatan(@RequestParam(value="id") String id, Model model) {
		long longId = Long.parseLong(id);
		jabatan = jabatanService.getJabatanDetailById(longId);
		model.addAttribute("jabatan", jabatan);
		int gaji_pokok = (int) jabatan.getGaji_pokok();
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		formatter.setMinimumIntegerDigits(gaji_pokok);
		model.addAttribute("gaji_pokok", gaji_pokok);
		
		model.addAttribute("title", "Lihat Jabatan");
		return "lihat-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.GET)
	private String tambahJabatan(Model model) {
		model.addAttribute("jabatan", new JabatanModel());
		model.addAttribute("title", "Tambah Pegawai");
		return "tambah-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/tambah", method = RequestMethod.POST, params={"save"})
	private String tambahJabatanSimpan(@ModelAttribute JabatanModel jabatan) {
		jabatanService.addJabatan(jabatan);
		return "tambah";
	}
	
	@RequestMapping(value = "/jabatan/ubah/{id}", method = RequestMethod.GET)
	private String ubah(@PathVariable(value ="id") String id, Model model) {
		JabatanModel newJabatan = jabatanService.getJabatanDetailById(Long.parseLong(id));
		model.addAttribute("jabatan", newJabatan);
		model.addAttribute("title", "Ubah Jabatan");
		return "ubah-jabatan";
	}
	
	@RequestMapping(value = "/jabatan/ubah", method = RequestMethod.POST)
	private String ubahJabatanSubmit(@ModelAttribute JabatanModel jabatan) {
		JabatanModel newJabatan = jabatanService.getJabatanDetailById(jabatan.getId());
		newJabatan.setNama(jabatan.getNama());
		newJabatan.setDeskripsi(jabatan.getDeskripsi());
		newJabatan.setGaji_pokok(jabatan.getGaji_pokok());
		jabatanService.addJabatan(newJabatan);
		return "ubah";	
	}
	
	@RequestMapping("/jabatan/viewall")
	public String viewAll(Model model) {
		List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
		model.addAttribute("semuaJabatan", semuaJabatan);
		model.addAttribute("title", "View All Jabatan");
		return "lihat-semua-jabatan";
	}
	
	@RequestMapping("/jabatan/hapus/{id}")
	public String hapusJabatan(@PathVariable(value ="id") String id, Model model) {
		String errorMessage = "";
		long longId = Long.parseLong(id);
		model.addAttribute("title", "Hapus Jabatan");
		if(!id.isEmpty()) {
			JabatanModel jabatan = jabatanService.getJabatanDetailById(longId);
			if(jabatan != null) {
				
				if(jabatan.getJabatan_pegawai().isEmpty()) {
					String header = "Jabatan " + jabatan.getNama() + " berhasil dihapus";
					String comment = "Database jabatan sudah diupdate";
					model.addAttribute("header", header);
					model.addAttribute("comment", comment);
					jabatanService.deleteJabatan(jabatan);
					return "hapus";
				}
				
				else {
					model.addAttribute("jabatan", jabatan);
					String header = "Jabatan " + jabatan.getNama() + " gagal dihapus";
					String comment = "Dikarenakan jabatan masih memiliki pegawai";
					model.addAttribute("header", header);
					model.addAttribute("comment", comment);
					return "tidak-dihapus";
				}
				
				
			}
			else{
				errorMessage = "Error! Jabatan " + jabatan.getNama() + " tidak ditemukan";
				model.addAttribute("errorMessage", errorMessage);
				model.addAttribute("title", "Error");
				return "error";	
			}
		}
		else {
			errorMessage = "Error! Daftar Jabatan kosong";
			model.addAttribute("errorMessage", errorMessage);
			model.addAttribute("title", "Error");
			return "error";
		}
	}
}
