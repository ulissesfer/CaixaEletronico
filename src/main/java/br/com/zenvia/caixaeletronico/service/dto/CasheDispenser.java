package br.com.zenvia.caixaeletronico.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CasheDispenser implements Serializable {

	private static final long serialVersionUID = -7218444995941158792L;

	private long valorTotal;
	private long qtdNota100;
	private long qtdNota50;
	private long qtdNota20;
	private long qtdNota10;

	public CasheDispenser() {
	}

	public CasheDispenser(long valorTotal, long qtdNota100, long qtdNota50, long qtdNota20, long qtdNota10) {
		this.valorTotal = valorTotal;
		this.qtdNota100 = qtdNota100;
		this.qtdNota50 = qtdNota50;
		this.qtdNota20 = qtdNota20;
		this.qtdNota10 = qtdNota10;
	}

	public long getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(long valorTotal) {
		this.valorTotal = valorTotal;
	}

	public long getQtdNota100() {
		return qtdNota100;
	}

	public void setQtdNota100(long qtdNota100) {
		this.qtdNota100 = qtdNota100;
	}

	public long getQtdNota50() {
		return qtdNota50;
	}

	public void setQtdNota50(long qtdNota50) {
		this.qtdNota50 = qtdNota50;
	}

	public long getQtdNota20() {
		return qtdNota20;
	}

	public void setQtdNota20(long qtdNota20) {
		this.qtdNota20 = qtdNota20;
	}

	public long getQtdNota10() {
		return qtdNota10;
	}

	public void setQtdNota10(long qtdNota10) {
		this.qtdNota10 = qtdNota10;
	}
}
