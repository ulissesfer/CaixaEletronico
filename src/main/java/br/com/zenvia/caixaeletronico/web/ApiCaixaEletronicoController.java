package br.com.zenvia.caixaeletronico.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.zenvia.caixaeletronico.service.ApiCaixaEletronicoService;
import br.com.zenvia.caixaeletronico.service.dto.CacheDispenser;
import br.com.zenvia.caixaeletronico.web.response.Response;
import br.com.zenvia.caixaeletronico.web.response.error.ErrorMessage;

@RestController
public class ApiCaixaEletronicoController {

	private final Logger log = LoggerFactory.getLogger(ErrorMessage.class);

	@Autowired
	private ApiCaixaEletronicoService apiCaixaEletronicoService;

	@GetMapping("/saque/{idCacheDispenser}")
	public Response realizaSaque(@PathVariable Long idCacheDispenser, @RequestParam Long valor) {
		Response resp = new Response();

		try {
			CacheDispenser saque = apiCaixaEletronicoService.getSaque(idCacheDispenser, valor);
			resp.getData().setCacheDispenser(saque);
			resp.setSuccess(true);
		} catch (Exception e) {
			resp.setSuccess(false);
			resp.setMessages(e.getMessage());
			log.error(e.getMessage());
		}
		return resp;
	}
}
