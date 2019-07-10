package br.com.zenvia.caixaeletronico.web.response;

import static org.hamcrest.CoreMatchers.instanceOf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.zenvia.caixaeletronico.web.response.error.ErrorMessage;

public class DefaultResponseWrapper<T> {

	 private final Logger log = LoggerFactory.getLogger(DefaultResponseWrapper.class);
	
    private final Boolean success;

    private final T data;

    private final String messages;

    private DefaultResponseWrapper(final Boolean success, final String messages, final T data) {
        this.success = success;
        this.data = data;
        this.messages = messages;

        log.info("REQUEST SUCCESS");
    }

    private DefaultResponseWrapper(final Boolean success, final String messages) {
        this.success = success;
        this.messages = messages;
        this.data = null;

        log.error("REQUEST ERROR - " + messages.toString());
    }

    public static <T> DefaultResponseWrapper<T> ofSuccessful(final String messages, final T data) {
        return new DefaultResponseWrapper<T>(true, messages, data);
    }

    public static DefaultResponseWrapper<?> ofFailure(final ErrorMessage error) {
        return new DefaultResponseWrapper<>(false, error.getMessage());
    }

    public Boolean getSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessages() {
        return messages;
    }
}
