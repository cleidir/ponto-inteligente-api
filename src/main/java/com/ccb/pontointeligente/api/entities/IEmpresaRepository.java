package com.ccb.pontointeligente.api.entities;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmpresaRepository extends JpaRepository<Empresa, Long>{

	Empresa findByCnpj(String cnpj);
	
}
