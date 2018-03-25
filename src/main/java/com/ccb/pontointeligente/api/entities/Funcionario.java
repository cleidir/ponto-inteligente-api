package com.ccb.pontointeligente.api.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ccb.pontointeligente.api.enums.PerfilEnum;

@Entity
@Table(name = "funcionario")
public class Funcionario implements Serializable {

	private static final long serialVersionUID = -1816264399230163460L;
	
	private long id;
	private String nome;
	private String email;
	private String senha;
	private String cpf;
	
	private BigDecimal valorHora;
	
	private Float qtHorasTrabalhoDia;
	private Float qtHorasAlmoco;
	
	private PerfilEnum perfil;
	
	private Date dataCriacao;
	private Date dataAtualizacao;

	private Empresa empresa;
	
	private List<Lancamento> lancamentos;
	
	public Funcionario() {

	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "nome", nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "senha", nullable = false)
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Column(name = "cpf", nullable = false)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Column(name = "valor_hora", nullable = true)
	public BigDecimal getValorHora() {
		return valorHora;
	}

	public void setValorHora(BigDecimal valorHora) {
		this.valorHora = valorHora;
	}

	@Transient
	public Optional<BigDecimal> getValorHoraOpt(){
		return Optional.ofNullable(getValorHora());
	}
	
	@Column(name = "qtd_horas_trabalho_dia", nullable = true)
	public Float getQtHorasTrabalhoDia() {
		return qtHorasTrabalhoDia;
	}

	public void setQtHorasTrabalhoDia(Float qtHorasTrabalhoDia) {
		this.qtHorasTrabalhoDia = qtHorasTrabalhoDia;
	}
	
	@Transient
	public Optional<Float> getQtHorasTrabalhoDiaOpt(){
		return Optional.ofNullable(getQtHorasTrabalhoDia());
	}
	
	@Transient
	public Optional<Float> getQtHorasAlmocoOpt(){
		return Optional.ofNullable(getQtHorasAlmoco());
	}

	@Column(name = "qtd_horas_almoco", nullable = true)
	public Float getQtHorasAlmoco() {
		return qtHorasAlmoco;
	}

	public void setQtHorasAlmoco(Float qtHorasAlmoco) {
		this.qtHorasAlmoco = qtHorasAlmoco;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "perfil", nullable = false)
	public PerfilEnum getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilEnum perfil) {
		this.perfil = perfil;
	}

	@Column(name = "data_criacao", nullable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "data_atualizacao", nullable = false)
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	@OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	@PreUpdate
	public void preUpdate() {
		dataAtualizacao = new Date();
	}
	
	@PrePersist
	public void prePersist() {
		final Date dataAtual = new Date();
		dataCriacao = dataAtual;
		dataAtualizacao = dataAtual;
	}
}
	
