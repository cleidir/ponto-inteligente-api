package com.ccb.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccb.pontointeligente.api.dtos.CadastroPJDTO;
import com.ccb.pontointeligente.api.entities.Empresa;
import com.ccb.pontointeligente.api.entities.Funcionario;
import com.ccb.pontointeligente.api.enums.PerfilEnum;
import com.ccb.pontointeligente.api.response.Response;
import com.ccb.pontointeligente.api.services.EmpresaService;
import com.ccb.pontointeligente.api.services.FuncionarioService;
import com.ccb.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPJController.class);

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private EmpresaService empresaService;

	public CadastroPJController() {
	}

	/**
	 * Cadastra uma pessoa jurídica no sistema.
	 * 
	 * @param cadastroPJDTO
	 * @param result
	 * @return ResponseEntity<Response<CadastroPJDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<CadastroPJDTO>> cadastrar(@Valid @RequestBody CadastroPJDTO cadastroPJDTO, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PJ: {}", cadastroPJDTO.toString());
		Response<CadastroPJDTO> response = new Response<CadastroPJDTO>();

		validarDadosExistentes(cadastroPJDTO, result);
		Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDTO);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDTO, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PJ: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		this.empresaService.persist(empresa);
		funcionario.setEmpresa(empresa);
		this.funcionarioService.persist(funcionario);

		response.setData(this.converterCadastroPJDto(funcionario));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa ou funcionário já existem na base de dados.
	 * 
	 * @param cadastroPJDto
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPJDTO cadastroPJDTO, BindingResult result) {
		this.empresaService.buscarPorCnpj(cadastroPJDTO.getCnpj()).ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));

		this.funcionarioService.buscarPorCpf(cadastroPJDTO.getCpf()).ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

		this.funcionarioService.buscarPorEmail(cadastroPJDTO.getEmail()).ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para empresa.
	 * 
	 * @param cadastroPJDto
	 * @return Empresa
	 */
	private Empresa converterDtoParaEmpresa(CadastroPJDTO cadastroPJDto) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastroPJDto.getCnpj());
		empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());

		return empresa;
	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param cadastroPJDto
	 * @param result
	 * @return Funcionario
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPJDTO cadastroPJDto, BindingResult result) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPJDto.getNome());
		funcionario.setEmail(cadastroPJDto.getEmail());
		funcionario.setCpf(cadastroPJDto.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.generateHashWithBCrypt(cadastroPJDto.getSenha()));

		return funcionario;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param funcionario
	 * @return CadastroPJDto
	 */
	private CadastroPJDTO converterCadastroPJDto(Funcionario funcionario) {
		CadastroPJDTO cadastroPJDTO = new CadastroPJDTO();
		cadastroPJDTO.setId(funcionario.getId());
		cadastroPJDTO.setNome(funcionario.getNome());
		cadastroPJDTO.setEmail(funcionario.getEmail());
		cadastroPJDTO.setCpf(funcionario.getCpf());
		cadastroPJDTO.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPJDTO.setCnpj(funcionario.getEmpresa().getCnpj());

		return cadastroPJDTO;
	}
}