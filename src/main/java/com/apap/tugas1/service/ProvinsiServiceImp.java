package com.apap.tugas1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.repository.ProvinsiDB;

@Service
@Transactional
public class ProvinsiServiceImp implements ProvinsiService{

	@Autowired
	private ProvinsiDB provinsiDb;
	
	@Override
	public ProvinsiModel getProvinsiDetailById(long id) {
		return provinsiDb.findById(id);
	}

	@Override
	public List<ProvinsiModel> getAllProvinsi() {
		return provinsiDb.findAll();
	}
}
