package com.ccb.pontointeligente.api.services;

import java.util.Optional;

import com.ccb.pontointeligente.api.entities.Funcionario;

public interface FuncionarioService {
	
	Funcionario persist(Funcionario funcionario);
	
	Optional<Funcionario> buscarPorCpf(String cpf);
	
	Optional<Funcionario> buscarPorEmail(String email);
	
	Optional<Funcionario> buscarPorId(Long id);

	
}
