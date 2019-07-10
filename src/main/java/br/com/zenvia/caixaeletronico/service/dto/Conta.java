package br.com.zenvia.caixaeletronico.service.dto;

import java.io.Serializable;

public class Conta implements Serializable {

	private static final long serialVersionUID = 856579457601387950L;

	private Long agencia;
	private Long conta;
	private int digito;

	public Conta() {
	}

	public Conta(Long agencia, Long conta, int digito) {
		this.agencia = agencia;
		this.conta = conta;
		this.digito = digito;
	}

	public Long getAgencia() {
		return agencia;
	}

	public void setAgencia(Long agencia) {
		this.agencia = agencia;
	}

	public Long getConta() {
		return conta;
	}

	public void setConta(Long conta) {
		this.conta = conta;
	}

	public int getDigito() {
		return digito;
	}

	public void setDigito(int digito) {
		this.digito = digito;
	}

}
