package com.apap.tugas1.controller;

import java.util.Date;
import java.util.HashSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.JabatanPegawaiModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	private PegawaiModel pegawai;
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("home", "home");
		List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
		model.addAttribute("semuaJabatan", semuaJabatan);
		List<InstansiModel> semuaInstansi = instansiService.getAllInstansi();
		model.addAttribute("semuaInstansi", semuaInstansi);
		return "home";
	}
	
	@RequestMapping(value = "/pegawai/lihat", method = RequestMethod.GET)
	public String viewPegawai(@RequestParam(value="nip", required = false) Optional<String> nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip.get());
		if(pegawai == null) {
			String header = "Pegawai dengan NIP " + nip.get() + " tidak dapat ditemukan";
			String comment = "Ayo kembali ke halaman sebelumnya untuk melihat pegawai lainnya";
			model.addAttribute("header", header);
			model.addAttribute("comment", comment);
			return "error/500";
		}
		else {
			model.addAttribute("pegawai", pegawai);
			int gaji = pegawaiService.getGaji(pegawai);
			pegawai.setGaji(gaji);
			return "lihat-pegawai";
		}
	}
	
	@RequestMapping("/pegawai/lihat/semua")
	public String viewAll(Model model) {
		List<PegawaiModel> allPegawai = pegawaiService.getAllPegawai();
		model.addAttribute("allPegawai", allPegawai);
		return "view-all-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
	public String tambahPegawai (Model model) {
		model.addAttribute("tambahPegawai", "active");
		
		PegawaiModel pegawai = new PegawaiModel();
		pegawai.setJabatan(new ArrayList<JabatanModel>());
		pegawai.getJabatan().add(new JabatanModel());
		
		List<ProvinsiModel> semuaProvinsi = provinsiService.getAllProvinsi();
		List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
		List<InstansiModel> semuaInstansi = instansiService.getAllInstansi();
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("semuaProvinsi", semuaProvinsi);
		model.addAttribute("semuaJabatan", semuaJabatan);
		model.addAttribute("semuaInstansi", semuaInstansi);
		return "tambah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params={"submit"})
	private String tambahPegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		System.out.println(pegawai.getNama());
		System.out.println(pegawai.getTahun_masuk());
		System.out.println(pegawai.getTempat_lahir());
		System.out.println(pegawai.getTanggal_lahir());
		System.out.println(pegawai.getInstansi().getNama());
		
		String nip = pegawaiService.generateNip(pegawai);
		pegawai.setNip(nip);
		int gaji = pegawaiService.getGaji(pegawai);
		pegawai.setGaji(gaji);
		
		pegawai.setId(pegawaiService.getAllPegawai().size());
		
		System.out.println("pegawai dengan id=" + pegawai.getId() + " dan nip=" + pegawai.getNip() + " berhasil dimasukkan");
		
		pegawaiService.addPegawai(pegawai);
		String header = "Pegawai " + pegawai.getNama() + " berhasil ditambah";
		String comment = "Database pegawai sudah diupdate";
		model.addAttribute("header", header);
		model.addAttribute("comment", comment);
		return "tambah";
	}
	
	@RequestMapping(value="/pegawai/tambah",method = RequestMethod.POST, params= {"addRow"})
	private String addRow (@ModelAttribute PegawaiModel pegawai, Model model, BindingResult bindingResult) {
		pegawai.getJabatan().add(new JabatanModel());
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("tanggalLahir", pegawai.getTanggal_lahir());
		
		List<InstansiModel> listInstansi = instansiService.getInstansiFromProvinsi(pegawai.getInstansi().getProvinsi());
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		
		model.addAttribute("semuaInstansi",	listInstansi);
		model.addAttribute("semuaJabatan",	listJabatan);
		model.addAttribute("semuaProvinsi", listProvinsi);
		return "tambah-pegawai";
	}
	
	@RequestMapping(value="/pegawai/tambah", params={"removeRow"}, method = RequestMethod.POST)
	public String removeRow(PegawaiModel pegawai, BindingResult bindingResult, HttpServletRequest req, Model model) {
	   Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
	   pegawai.getJabatan().remove(rowId.intValue());
	   model.addAttribute("pegawai", pegawai);
	   return "tambah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.GET)
	private String updatePegawai(@RequestParam String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		model.addAttribute("pegawai", pegawai);
		
		List<ProvinsiModel> semuaProvinsi = provinsiService.getAllProvinsi();
		List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
		List<InstansiModel> semuaInstansi = instansiService.getInstansiFromProvinsi(pegawai.getInstansi().getProvinsi());
		model.addAttribute("semuaProvinsi", semuaProvinsi);
		model.addAttribute("semuaJabatan", semuaJabatan);
		model.addAttribute("semuaInstansi", semuaInstansi);
		
		return "ubah-pegawai";
	}
	
	@RequestMapping(value="/pegawai/ubah", method = RequestMethod.POST, params= {"addRow"})
	private String addRowUbah (@ModelAttribute PegawaiModel pegawai, Model model, BindingResult bindingResult) {
		pegawai.getJabatan().add(new JabatanModel());
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("tanggalLahir", pegawai.getTanggal_lahir());
		
		List<InstansiModel> listInstansi = instansiService.getInstansiFromProvinsi(pegawai.getInstansi().getProvinsi());
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		
		model.addAttribute("semuaInstansi",	listInstansi);
		model.addAttribute("semuaJabatan",	listJabatan);
		model.addAttribute("semuaProvinsi", listProvinsi);
		return "ubah-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/ubah", method = RequestMethod.POST, params={"submit"})
	private String updatePegawaiSubmit(@ModelAttribute PegawaiModel pegawai, Model model) {
		PegawaiModel newPegawai = pegawaiService.getPegawaiDetailByNip(pegawai.getNip());
		
		newPegawai.setNama(pegawai.getNama());
		System.out.println(newPegawai.getNama());
		newPegawai.setTempat_lahir(pegawai.getTempat_lahir());
		System.out.println(newPegawai.getTempat_lahir());
		newPegawai.setTanggal_lahir(pegawai.getTanggal_lahir());
		System.out.println(newPegawai.getTanggal_lahir());
		newPegawai.setTahun_masuk(pegawai.getTahun_masuk());
		System.out.println(newPegawai.getTahun_masuk());
		newPegawai.setTempat_lahir(pegawai.getTempat_lahir());
		System.out.println(newPegawai.getTempat_lahir());
		newPegawai.setInstansi(pegawai.getInstansi());
		System.out.println(newPegawai.getInstansi().getNama());
		newPegawai.setJabatan(pegawai.getJabatan());
		
		String nip = pegawaiService.generateNip(newPegawai);
		newPegawai.setNip(nip);
		int gaji = pegawaiService.getGaji(newPegawai);
		newPegawai.setGaji(gaji);
		
		System.out.println(newPegawai.getNip());
		pegawaiService.addPegawai(newPegawai);
		String header = "Pegawai " + pegawai.getNama() + " berhasil diubah";
		String comment = "Database pegawai sudah diupdate";
		model.addAttribute("header", header);
		model.addAttribute("comment", comment);
		return "ubah";	
	}
	
	@RequestMapping(value="/pegawai/termuda-tertua", method = RequestMethod.GET)
	public String viewTuaMuda(@RequestParam(value="id", required = true) String id, Model model) {		
		InstansiModel instansi = instansiService.getInstansiDetailById(Long.parseLong(id));
		List<PegawaiModel> semuaPegawai = instansi.getPegawai();
		
		int tertua = 0;
		int termuda = 1000000000;
		
		PegawaiModel pegawaiTermuda = semuaPegawai.get(0);
		PegawaiModel pegawaiTertua = semuaPegawai.get(0);
		PegawaiModel pegawaiLoop;
		
		long millis=System.currentTimeMillis();  
		Date today = new Date(millis);  
		
		for(int i = 0; i < semuaPegawai.size(); i++) {
			pegawaiLoop = semuaPegawai.get(i);
			Date birthday = pegawaiLoop.getTanggal_lahir();
			@SuppressWarnings("deprecation")
			int age = today.getYear() - birthday.getYear();
			if(age > tertua) {
				tertua = age;
				pegawaiTertua = pegawaiLoop;
			}
			if(age < termuda) {
				termuda = age;
				pegawaiTermuda = pegawaiLoop;
			}
		}
		
		model.addAttribute("gajiTermuda", pegawaiService.getGaji(pegawaiTermuda));
		model.addAttribute("gajiTertua", pegawaiService.getGaji(pegawaiTertua));
		model.addAttribute("pegawaiTermuda", pegawaiTermuda);
		model.addAttribute("pegawaiTertua", pegawaiTertua);
		return "termuda-tertua";
	}
	
//	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.GET)
//	public String tambahPegawai (Model model) {
//		model.addAttribute("tambahPegawai", "active");
//		
//		PegawaiModel pegawai = new PegawaiModel();
//		pegawai.setJabatan(new ArrayList<JabatanModel>());
//		pegawai.getJabatan().add(new JabatanModel());
//		
//		List<ProvinsiModel> semuaProvinsi = provinsiService.getAllProvinsi();
//		List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
//		List<InstansiModel> semuaInstansi = instansiService.getAllInstansi();
//		
//		model.addAttribute("pegawai", pegawai);
//		model.addAttribute("semuaProvinsi", semuaProvinsi);
//		model.addAttribute("semuaJabatan", semuaJabatan);
//		model.addAttribute("semuaInstansi", semuaInstansi);
//		return "tambah-pegawai";
//	}
	
//	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
//	private String searchPegawai(Model model) {
//		model.addAttribute("cariPegawai", "active");
//		
//		List<PegawaiModel> semuaPegawai = pegawaiService.getAllPegawai();
//		List<ProvinsiModel> semuaProvinsi = provinsiService.getAllProvinsi();
//		List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
//		List<InstansiModel> semuaInstansi = instansiService.getAllInstansi();
//		model.addAttribute("semuaProvinsi", semuaProvinsi);
//		model.addAttribute("semuaJabatan", semuaJabatan);
//		model.addAttribute("semuaInstansi", semuaInstansi);
//		model.addAttribute("semuaPegawai", semuaPegawai);
//		return "cari-pegawai";	
//	}
	
	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
	public String searchPegawaiSubmit (@RequestParam(value="idProvinsi", required = false) Optional<Long> idProvinsi, 
			@RequestParam(value="idInstansi", required = false) Optional<Long> idInstansi, 
			@RequestParam(value="idJabatan", required = false) Optional<Long> idJabatan, Model model) {
		model.addAttribute("cariPegawai", "active");
		List<PegawaiModel> semuaPegawai = null;
		List<ProvinsiModel> semuaProvinsi = provinsiService.getAllProvinsi();
		List<InstansiModel> semuaInstansi = instansiService.getAllInstansi();
		List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
		ProvinsiModel provinsi = null;
		InstansiModel instansi = null;
		JabatanModel jabatan = null;
		if (idProvinsi.isPresent()) {
			provinsi = provinsiService.getProvinsiDetailById(idProvinsi.get());
			if (idInstansi.isPresent()) {
				instansi = instansiService.getInstansiDetailById(idInstansi.get());	
				if (idJabatan.isPresent()) {
					jabatan = jabatanService.getJabatanDetailById(idJabatan.get());	
					semuaPegawai = pegawaiService.getPegawaiByInstansiAndJabatan(instansi, jabatan);
				}
				else {
					semuaPegawai = pegawaiService.getPegawaiByInstansi(instansi);
				}
			}
			else if (idJabatan.isPresent()) {
				jabatan = jabatanService.getJabatanDetailById(idJabatan.get());	
				semuaPegawai = pegawaiService.getPegawaiByProvinsiAndJabatan(idProvinsi.get(), jabatan);
			}
			else {
				semuaPegawai = pegawaiService.getPegawaiByProvinsi(idProvinsi.get());
			}
		}
		else {
			if (idJabatan.isPresent()) {
				jabatan = jabatanService.getJabatanDetailById(idJabatan.get());
				semuaPegawai = pegawaiService.getPegawaiByJabatan(jabatan);
			}
			else {
				semuaPegawai = pegawaiService.getAllPegawai();
			}
		}
		
		model.addAttribute("provinsi", provinsi);
		model.addAttribute("instansi", instansi);
		model.addAttribute("jabatan", jabatan);
		model.addAttribute("semuaProvinsi", semuaProvinsi);
		model.addAttribute("semuaInstansi", semuaInstansi);
		model.addAttribute("semuaJabatan", semuaJabatan);
		model.addAttribute("semuaPegawai", semuaPegawai);
		
		return"cari-pegawai";
	}
	
//	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
//    public String searchPegawaiResult(@RequestParam(value="provinsi", required = false) long idProvinsi,
//	            @RequestParam(value="instansi", required = false) long idInstansi,
//	            @RequestParam(value="jabatan", required = false) long idJabatan, Model model){
//	
//	List<PegawaiModel> semuaPegawai = pegawaiService.getAllPegawaiForSearch(idProvinsi, idInstansi, idJabatan);
//	InstansiModel instansiTujuan = instansiService.getInstansiDetailById(idInstansi);
//	JabatanModel jabatanTujuan = jabatanService.getJabatanDetailById(idJabatan);
//	
//	for (int i=0 ; i<semuaPegawai.size() ;i++){
//	System.out.println(semuaPegawai.get(i));
//	}
//	
//	List<ProvinsiModel> semuaProvinsi = provinsiService.getAllProvinsi();
//	List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
//	
//	model.addAttribute("semuaProvinsi", semuaProvinsi);
//	model.addAttribute("semuaJabatan", semuaJabatan);
//	model.addAttribute("semuaPegawai", semuaPegawai);
//	
//	model.addAttribute("instansiTujuan", instansiTujuan);
//	model.addAttribute("jabatanTujuan", jabatanTujuan);
//	return"cari-pegawai";
//	}

	
//	@RequestMapping(value = "/pegawai/cari", method = RequestMethod.GET)
//	public String searchPegawai(Model model) {
//
//		model.addAttribute("cariPegawai", "active");
//		List<ProvinsiModel> semuaProvinsi = provinsiService.getAllProvinsi();
//		List<JabatanModel> semuaJabatan = jabatanService.getAllJabatan();
//		List<InstansiModel> semuaInstansi = instansiService.getAllInstansi();
//		model
//		
//		model.addAttribute("semuaProvinsi", semuaProvinsi);
//		model.addAttribute("semuaJabatan", semuaJabatan);
//		model.addAttribute("semuaInstansi", semuaInstansi);
//		
//		List<PegawaiModel> semuaPegawai = pegawaiService.getAllPegawai();
//		model.addAttribute("semuaPegawai", semuaPegawai);
//		return "cari-pegawai";
//	}
	
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
