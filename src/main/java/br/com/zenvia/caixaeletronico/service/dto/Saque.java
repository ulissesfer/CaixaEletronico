package br.com.zenvia.caixaeletronico.service.dto;

import java.io.Serializable;

public class Saque implements Serializable {

	private static final long serialVersionUID = -5214439835080657048L;

	private Conta conta;
	private Long valor;
	private CacheDispenser cashDisp;

	public Saque() {
	}

	public Saque(Conta conta, Long valor) {
		this.conta = conta;
		this.valor = valor;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public Long getValor() {
		return valor;
	}

	public void setValor(Long valor) {
		this.valor = valor;
	}

	public CacheDispenser getCashDisp() {
		return cashDisp;
	}

	public void setCashDisp(CacheDispenser cashDisp) {
		this.cashDisp = cashDisp;
	}

}
