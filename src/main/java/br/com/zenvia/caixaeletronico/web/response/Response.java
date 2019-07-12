package br.com.zenvia.caixaeletronico.web.response;

import java.io.Serializable;

public class Response implements Serializable {

	private static final long serialVersionUID = -7846243675689622652L;

	private boolean success;
	private String messages;
	private Data data;

	public Response() {
		this.data = new Data();
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return data;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}
}