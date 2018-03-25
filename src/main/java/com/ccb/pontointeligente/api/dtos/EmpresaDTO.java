package com.ccb.pontointeligente.api.dtos;

public class EmpresaDTO {
	
	private Long id;
	private String razaoSocial;
	private String cnpj;

	public EmpresaDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("EmpresaDTO [id=").append(getId());
		buffer.append(", razaoSocial=").append(getRazaoSocial());
		buffer.append(", cnpj=").append(getCnpj()).append("]");
		return buffer.toString();
	}
}