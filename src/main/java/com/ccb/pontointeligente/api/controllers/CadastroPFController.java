package com.ccb.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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

import com.ccb.pontointeligente.api.dtos.CadastroPFDTO;
import com.ccb.pontointeligente.api.entities.Empresa;
import com.ccb.pontointeligente.api.entities.Funcionario;
import com.ccb.pontointeligente.api.enums.PerfilEnum;
import com.ccb.pontointeligente.api.response.Response;
import com.ccb.pontointeligente.api.services.EmpresaService;
import com.ccb.pontointeligente.api.services.FuncionarioService;
import com.ccb.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private FuncionarioService funcionarioService;

	public CadastroPFController() {
	}

	/**
	 * Cadastra um funcionário pessoa física no sistema.
	 * 
	 * @param cadastroPFDTO
	 * @param result
	 * @return ResponseEntity<Response<CadastroPFDTO>>
	 * @throws NoSuchAlgorithmException
	 */
	@PostMapping
	public ResponseEntity<Response<CadastroPFDTO>> cadastrar(@Valid @RequestBody CadastroPFDTO cadastroPFDTO, BindingResult result) throws NoSuchAlgorithmException {
		log.info("Cadastrando PF: {}", cadastroPFDTO.toString());
		Response<CadastroPFDTO> response = new Response<CadastroPFDTO>();

		validarDadosExistentes(cadastroPFDTO, result);
		Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDTO, result);

		if (result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDTO.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		this.funcionarioService.persist(funcionario);

		response.setData(this.converterCadastroPFDTO(funcionario));
		return ResponseEntity.ok(response);
	}

	/**
	 * Verifica se a empresa está cadastrada e se o funcionário não existe na base de dados.
	 * 
	 * @param CadastroPFDTO
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPFDTO cadastroPFDTO, BindingResult result) {
		Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDTO.getCnpj());
		if (!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
		}

		this.funcionarioService.buscarPorCpf(cadastroPFDTO.getCpf()).ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

		this.funcionarioService.buscarPorEmail(cadastroPFDTO.getEmail()).ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
	}

	/**
	 * Converte os dados do DTO para funcionário.
	 * 
	 * @param CadastroPFDTO
	 * @param result
	 * @return Funcionario
	 * @throws NoSuchAlgorithmException
	 */
	private Funcionario converterDtoParaFuncionario(CadastroPFDTO cadastroPFDTO, BindingResult result) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPFDTO.getNome());
		funcionario.setEmail(cadastroPFDTO.getEmail());
		funcionario.setCpf(cadastroPFDTO.getCpf());
		funcionario.setPerfil(PerfilEnum.ROLE_USER);
		funcionario.setSenha(PasswordUtils.generateHashWithBCrypt(cadastroPFDTO.getSenha()));
		cadastroPFDTO.getQtdHorasAlmoco().ifPresent(qtdHorasAlmoco -> funcionario.setQtHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPFDTO.getQtdHorasTrabalhoDia().ifPresent(qtdHorasTrabDia -> funcionario.setQtHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));
		cadastroPFDTO.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

		return funcionario;
	}

	/**
	 * Popula o DTO de cadastro com os dados do funcionário e empresa.
	 * 
	 * @param funcionario
	 * @return cadastroPFDTO
	 */
	private CadastroPFDTO converterCadastroPFDTO(Funcionario funcionario) {
		CadastroPFDTO cadastroPFDTO = new CadastroPFDTO();
		cadastroPFDTO.setId(funcionario.getId());
		cadastroPFDTO.setNome(funcionario.getNome());
		cadastroPFDTO.setEmail(funcionario.getEmail());
		cadastroPFDTO.setCpf(funcionario.getCpf());
		cadastroPFDTO.setCnpj(funcionario.getEmpresa().getCnpj());
		funcionario.getQtHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> cadastroPFDTO.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtHorasTrabalhoDiaOpt().ifPresent(qtdHorasTrabDia -> cadastroPFDTO.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		funcionario.getValorHoraOpt().ifPresent(valorHora -> cadastroPFDTO.setValorHora(Optional.of(valorHora.toString())));

		return cadastroPFDTO;
	}

}
