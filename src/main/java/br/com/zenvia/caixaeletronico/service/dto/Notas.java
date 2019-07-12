package br.com.zenvia.caixaeletronico.service.dto;

public enum Notas {
	CEM(100),
	CINQUENTA(50),
	VINTE(20),
	DEZ(10);
	
	private int valor;
	
	Notas(int valor) {
		this.valor = valor;
	}
	
	public int getValor() {
		return valor;
	}
}
