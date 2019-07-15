package br.com.zenvia.caixaeletronico.web;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.zenvia.caixaeletronico.service.ApiCaixaEletronicoService;
import br.com.zenvia.caixaeletronico.web.response.Response;
import br.com.zenvia.caixaeletronico.web.response.exception.CasheDispenserValidatorException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ApiCaixaEletronicoControllerTest{

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private ApiCaixaEletronicoService apiCaixaEletronicoService;

	@Test
	public void realizaSaqueComSucessoTest() {
		Map<String, String> valores = new HashMap<String, String>();
		valores.put("idCacheDispenser", "1");
		valores.put("valor", "100");

		ResponseEntity<Response> response = restTemplate.getForEntity("/saque/{idCacheDispenser}?valor={valor}", Response.class, valores);

		Assertions.assertThat(response.getBody().getSuccess()).isEqualTo(true);
	}

	@Test
	public void realizaSaqueDeveRetornarErroTest() throws Exception {
		String errorMsg = "Mensagem de erro";
		long idCacheDispenser = 1;
		long valor = 100;
		
		Map<String, Long> valores = new HashMap<String, Long>();
		valores.put("idCacheDispenser", idCacheDispenser);
		valores.put("valor", valor);

		Mockito.when(apiCaixaEletronicoService.getSaque(idCacheDispenser, valor)).thenThrow(new CasheDispenserValidatorException(errorMsg));
		
		ResponseEntity<Response> response = restTemplate.getForEntity("/saque/{idCacheDispenser}?valor={valor}", Response.class, valores);
		Assertions.assertThat(response.getBody().getSuccess()).isEqualTo(false);
		Assertions.assertThat(response.getBody().getMessages()).contains(errorMsg);
	}
}
