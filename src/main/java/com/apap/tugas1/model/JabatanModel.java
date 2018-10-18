package com.apap.tugas1.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * @author Muhammad Aulia Adil
 *
 */
@Entity
@Table(name = "jabatan")
public class JabatanModel implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private long id;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "nama", nullable = false)
	private String nama;
	
	@NotNull
	@Size(max = 255)
	@Column(name = "deskripsi", nullable = false)
	private String deskripsi;
	
	@NotNull
	@Column(name = "gaji_pokok")
	private double gaji_pokok;
	
	@OneToMany(mappedBy = "jabatan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<JabatanPegawaiModel> jabatan_pegawai;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public double getGaji_pokok() {
		return gaji_pokok;
	}

	public void setGaji_pokok(double gaji_pokok) {
		this.gaji_pokok = gaji_pokok;
	}
	
	public List<JabatanPegawaiModel> getJabatan_pegawai() {
		return jabatan_pegawai;
	}

	public void setJabatan_pegawai(List<JabatanPegawaiModel> jabatan_pegawai) {
		this.jabatan_pegawai = jabatan_pegawai;
	}
}
