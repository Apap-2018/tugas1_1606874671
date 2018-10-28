package com.apap.tugas1.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Muhammad Aulia Adil
 *
 */
@Entity
@Table(name = "jabatan_pegawai")
public class JabatanPegawaiModel implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	@Size (max = 20)
	@Column (name= "id", nullable = false)
	private long id_jabatan_pegawai;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_pegawai", referencedColumnName = "id", nullable = false)
	@JsonIgnore
	private PegawaiModel pegawai;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_jabatan", referencedColumnName = "id", nullable = false)
	@JsonIgnore
	private JabatanModel jabatan;
	
	public long getId() {
		return id_jabatan_pegawai;
	}

	public void setId(long id) {
		this.id_jabatan_pegawai = id;
	}

	public PegawaiModel getPegawai() {
		return pegawai;
	}

	public void setPegawai(PegawaiModel pegawai) {
		this.pegawai = pegawai;
	}

	public JabatanModel getJabatan() {
		return jabatan;
	}

	public void setJabatan(JabatanModel jabatan) {
		this.jabatan = jabatan;
	}
}
