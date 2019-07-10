package br.com.zenvia.caixaeletronico.service.dto;

import java.io.Serializable;

public class CacheDispenser implements Serializable {

	private static final long serialVersionUID = -7218444995941158792L;

	private long id;

	public CacheDispenser() {
	}

	public CacheDispenser(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
