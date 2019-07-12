package br.com.zenvia.caixaeletronico.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ApiCaixaEletronicoControllerTest{

	@Autowired
	private MockMvc mockMvc;
	
	/*@Autowired
	private ApiCaixaEletronicoController apiCaixaEletronicoController;
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(apiCaixaEletronicoController).build();
	}*/
	
	@Test
	public void testPost() throws Exception {
		/*Conta conta = new Conta(1012L, 12345L, 1);
		
		Gson gson = new Gson();
		String json = gson.toJson(conta);
		
		this.mockMvc.perform(MockMvcRequestBuilders.post("/conta/saldo")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				).andExpect(MockMvcResultMatchers.status().isOk());*/
	}
}
