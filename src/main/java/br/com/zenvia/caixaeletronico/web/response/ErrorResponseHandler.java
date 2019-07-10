package br.com.zenvia.caixaeletronico.web.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import br.com.zenvia.caixaeletronico.web.response.error.ErrorMessage;

@ControllerAdvice(basePackages = "br.com.zenvia")
public class ErrorResponseHandler<T> implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(final MethodParameter returnType, final  Class<? extends HttpMessageConverter<?>> converterType) {
        final Class<?> methodReturnType = returnType.getMethod().getReturnType();

        return methodReturnType == ErrorMessage.class;
    }

    @Override
    public Object beforeBodyWrite(final Object body, 
    							  final MethodParameter returnType,
                                  final MediaType selectedContentType, 
                                  final Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  final ServerHttpRequest request, final ServerHttpResponse response) {
        final ErrorMessage errorMessage = (ErrorMessage) body;
        
        return DefaultResponseWrapper.ofFailure(errorMessage);
    }

}

