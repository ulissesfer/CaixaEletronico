package br.com.zenvia.caixaeletronico.web.response;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import br.com.zenvia.caixaeletronico.service.dto.CacheDispenser;
import br.com.zenvia.caixaeletronico.web.response.error.ErrorMessage;

@JsonInclude(Include.NON_NULL)
public class Data implements Serializable {

	private static final long serialVersionUID = 7804106551938959635L;

	private CacheDispenser cacheDispenser;
	private ErrorMessage error;

	public CacheDispenser getCacheDispenser() {
		return cacheDispenser;
	}

	public void setCacheDispenser(CacheDispenser cacheDispenser) {
		this.cacheDispenser = cacheDispenser;
	}

	public ErrorMessage getError() {
		return error;
	}

	public void setError(ErrorMessage error) {
		this.error = error;
	}

}
