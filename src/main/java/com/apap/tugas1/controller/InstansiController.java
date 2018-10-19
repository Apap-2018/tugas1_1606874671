package com.apap.tugas1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class InstansiController {
    @Autowired
    private ProvinsiService provinsiService;
    
    @Autowired
    private InstansiService instansiService;

	@RequestMapping(value = "/instansi/getFromProvinsi", method = RequestMethod.GET)
	@ResponseBody
	public List<InstansiModel> getInstansi(@RequestParam (value = "provinsiId", required = true) long id) {
	    ProvinsiModel provinsi = provinsiService.getProvinsiDetailById(id);
		return instansiService.getInstansiFromProvinsi(provinsi);
	}
}
