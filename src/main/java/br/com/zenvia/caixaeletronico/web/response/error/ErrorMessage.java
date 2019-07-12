package br.com.zenvia.caixaeletronico.web.response.error;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;


public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = -7633481672253411343L;

	private final Logger log = LoggerFactory.getLogger(ErrorMessage.class);

	private final String message;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime date;

	public ErrorMessage(final String message) {
		this.message = message;
		this.date = LocalDateTime.now();
		log.error("ErrorMessage - "+ message);
	}

	public String getMessage() {
		return message;
	}
}
