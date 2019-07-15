package br.com.zenvia.caixaeletronico.service.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class CasheDispenserEntity {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Long qtdNota100;

	@NotNull
	private Long qtdNota50;

	@NotNull
	private Long qtdNota20;

	@NotNull
	private Long qtdNota10;

	public CasheDispenserEntity() {
	}

	public CasheDispenserEntity(Long qtdNota100, Long qtdNota50, Long qtdNota20, Long qtdNota10) {
		this.qtdNota100 = qtdNota100;
		this.qtdNota50 = qtdNota50;
		this.qtdNota20 = qtdNota20;
		this.qtdNota10 = qtdNota10;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQtdNota100() {
		return qtdNota100;
	}

	public void setQtdNota100(Long qtdNota100) {
		this.qtdNota100 = qtdNota100;
	}

	public Long getQtdNota50() {
		return qtdNota50;
	}

	public void setQtdNota50(Long qtdNota50) {
		this.qtdNota50 = qtdNota50;
	}

	public Long getQtdNota20() {
		return qtdNota20;
	}

	public void setQtdNota20(Long qtdNota20) {
		this.qtdNota20 = qtdNota20;
	}

	public Long getQtdNota10() {
		return qtdNota10;
	}

	public void setQtdNota10(Long qtdNota10) {
		this.qtdNota10 = qtdNota10;
	}

}
