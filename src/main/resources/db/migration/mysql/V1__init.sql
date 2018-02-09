-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: 08-Fev-2018 às 23:11
-- Versão do servidor: 5.7.19
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ponto_inteligente`
--

-- --------------------------------------------------------

--
-- Estrutura das tabelas
--


CREATE TABLE `empresa` (
	`id` bigint(20) NOT NULL,
	`cnpj` varchar(255) NOT NULL,
	`data_atualizacao` datetime NOT NULL,
	`data_criacao` datetime NOT NULL,
	`razao_social` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

CREATE TABLE 'funcionario' (
'id' bigint(20) NOT NULL,
'cpf' varchar(255) NOT NULL,
'data_atualizacao' datetime NOT NULL,
'data_criacao' datetime NOT NULL,
'email' varchar(255) NOT NULL,
'nome' varchar(255) NOT NULL,
'perfil' varchar(255) NOT NULL,
'qtd_horas_almoco' float DEFAULT NULL,
'qtd_horas_trabalho_dia' float DEFAULT NULL,
'senha' varchar(255) NOT NULL,
'valor_hora' decimal(19,2) DEFAULT NULL,
'empresa_id' bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE 'lancamento' (
	'id' bigint(20) NOT NULL,
	'data' datetime not NULL,
	'data_atualizacao' datetime not NULL,
	'data_criacao' datetime not NULL,
	'descricao' varchar(255) DEFAULT NULL,
	'localizacao' varchar(255) DEFAULT NULL,
	'tipo' varchar(255) NOT NULL,
	'funcionario_id' bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

-- Indexes for table EMPRESA

ALTER TABLE 'empresa' 
	ADD PRIMARY KEY('id');

-- Indexes for table FUNCIONARIO

ALTER TABLE 'funcionario' 
	ADD PRIMARY KEY(id),
	ADD KEY(empresa_id);


-- Indexes for table LANCAMENTO

ALTER TABLE 'lancamento' 
	ADD PRIMARY KEY(id),
	ADD KEY(funcionario_id);

ALTER TABLE 'empresa' 
	MODIFY 'id' bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE 'funcionario' 
	MODIFY 'id' bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE 'lancamento' 
	MODIFY 'id' bigint(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE 'funcionario' 
	ADD CONSTRAINT foreign KEY('empresa_id') references 'empresa'('id');

ALTER TABLE 'lancamento' 
	ADD CONSTRAINT foreign KEY('funcionario_id') references 'funcionario'('id');


COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
