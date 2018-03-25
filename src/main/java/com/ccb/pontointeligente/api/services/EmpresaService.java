package com.ccb.pontointeligente.api.services;

import java.util.Optional;

import com.ccb.pontointeligente.api.entities.Empresa;

public interface EmpresaService {
	
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	Empresa persist(Empresa empresa);
	
}
