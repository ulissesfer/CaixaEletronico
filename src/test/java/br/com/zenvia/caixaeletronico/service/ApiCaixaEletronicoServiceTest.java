package br.com.zenvia.caixaeletronico.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.zenvia.caixaeletronico.service.dto.CasheDispenser;
import br.com.zenvia.caixaeletronico.service.entity.CasheDispenserEntity;
import br.com.zenvia.caixaeletronico.service.repository.CasheDispenserRepository;
import br.com.zenvia.caixaeletronico.web.response.exception.CasheDispenserValidatorException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiCaixaEletronicoServiceTest {

	@Mock
	private CasheDispenserRepository cdRepository;

	@InjectMocks
	private ApiCaixaEletronicoService apiService;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private CasheDispenserEntity casheDispenserEntity;
	static final String ERROR_MSG_VALOR_MAIOR_DISPONIVEL = "Valor do saque maior que valor disponível no momento.";
	static final String ERROR_MSG_CASHE_DISPENSER_INDISPONIVEL = "Cashe dispenser não encontrado.";
	static final String ERROR_MSG_VALOR_INDISPONIVEL = "Valor indisponível para saque, tente outro!";

	@Before
    public void initMocks() {
		MockitoAnnotations.initMocks(this);
	
		long notasDe10MaiorQueZero = 20;
		long notasDe20MaiorQueZero = 20;
		long notasDe50MaiorQueZero = 20;
		long notasDe100MaiorQueZero = 20;
		long idCasheDispenser = 1;

		casheDispenserEntity = new CasheDispenserEntity(notasDe10MaiorQueZero, notasDe20MaiorQueZero, notasDe50MaiorQueZero, notasDe100MaiorQueZero);
		casheDispenserEntity.setId(idCasheDispenser);
	}

	@Test
	public void realizaSaqueComSucessoTest() throws Exception {
		long valorParaSaque = 100;

		Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void naoEncontraCasheDispenserTest() throws Exception {
		long valorParaSaque = 100;
		long idCasheDispenserErrado = 9999;

		casheDispenserEntity.setId(idCasheDispenserErrado);
		
		thrown.expect(CasheDispenserValidatorException.class);
        thrown.expectMessage(containsString(ERROR_MSG_CASHE_DISPENSER_INDISPONIVEL));

		Mockito.when(cdRepository.findById(Mockito.anyLong())).thenThrow(new CasheDispenserValidatorException(ERROR_MSG_CASHE_DISPENSER_INDISPONIVEL));

		apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);
	}

	@Test
	public void naoRealizaSaqueComValorMaiorQueOTotalDisponivelNoCasheDispenserTest() throws Exception {
		long valorParaSaque = 100;
		
		casheDispenserEntity.setQtdNota10(2L);
		casheDispenserEntity.setQtdNota20(0L);
		casheDispenserEntity.setQtdNota50(0L);
		casheDispenserEntity.setQtdNota100(0L);

		thrown.expect(CasheDispenserValidatorException.class);
        thrown.expectMessage(containsString(ERROR_MSG_VALOR_MAIOR_DISPONIVEL));

		Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);
	}

	@Test
	public void naoRealizaSaqueComTotalDeNotasIndisponivelNoCasheDispenserTest() throws Exception {
		long valorParaSaque = 90;

		casheDispenserEntity.setQtdNota10(0L);
		casheDispenserEntity.setQtdNota20(0L);
		casheDispenserEntity.setQtdNota50(0L);
		casheDispenserEntity.setQtdNota100(1L);

		thrown.expect(CasheDispenserValidatorException.class);
        thrown.expectMessage(containsString(ERROR_MSG_VALOR_INDISPONIVEL));

        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);
	}
	
	@Test
	public void realizaSaqueSomenteNotasDe100Test() throws Exception {
		long valorParaSaque = 300;

        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(0);
		assertThat(cd.getQtdNota20()).isEqualTo(0);
		assertThat(cd.getQtdNota50()).isEqualTo(0);
		assertThat(cd.getQtdNota100()).isEqualTo(3);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void realizaSaqueSomenteNotasDe50Test() throws Exception {
		long valorParaSaque = 50;

        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));
		
		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(0);
		assertThat(cd.getQtdNota20()).isEqualTo(0);
		assertThat(cd.getQtdNota50()).isEqualTo(1);
		assertThat(cd.getQtdNota100()).isEqualTo(0);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void realizaSaqueSomenteNotasDe20Test() throws Exception {
		long valorParaSaque = 40;

        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));
		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(0);
		assertThat(cd.getQtdNota20()).isEqualTo(2);
		assertThat(cd.getQtdNota50()).isEqualTo(0);
		assertThat(cd.getQtdNota100()).isEqualTo(0);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}

	@Test
	public void realizaSaqueSomenteNotasDe10Test() throws Exception {
		long valorParaSaque = 10;

        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(1);
		assertThat(cd.getQtdNota20()).isEqualTo(0);
		assertThat(cd.getQtdNota50()).isEqualTo(0);
		assertThat(cd.getQtdNota100()).isEqualTo(0);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}

	@Test
	public void saqueSemNotasDe10Test() throws Exception {
		long valorParaSaque = 210;

		casheDispenserEntity.setQtdNota10(0L);
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(0);
		assertThat(cd.getQtdNota20()).isEqualTo(3);
		assertThat(cd.getQtdNota50()).isEqualTo(1);
		assertThat(cd.getQtdNota100()).isEqualTo(1);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void saqueSemNotasDe20Test() throws Exception {
		long valorParaSaque = 190;

		casheDispenserEntity.setQtdNota20(0L);
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(4);
		assertThat(cd.getQtdNota20()).isEqualTo(0);
		assertThat(cd.getQtdNota50()).isEqualTo(1);
		assertThat(cd.getQtdNota100()).isEqualTo(1);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void saqueSemNotasDe50Test() throws Exception {
		long valorParaSaque = 150;

		casheDispenserEntity.setQtdNota50(0L);
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(1);
		assertThat(cd.getQtdNota20()).isEqualTo(2);
		assertThat(cd.getQtdNota50()).isEqualTo(0);
		assertThat(cd.getQtdNota100()).isEqualTo(1);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void saqueSemNotasDe100Test() throws Exception {
		long valorParaSaque = 180;

		casheDispenserEntity.setQtdNota100(0L);
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(1);
		assertThat(cd.getQtdNota20()).isEqualTo(1);
		assertThat(cd.getQtdNota50()).isEqualTo(3);
		assertThat(cd.getQtdNota100()).isEqualTo(0);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}

	@Test
	public void saque30ReaisTest() throws Exception {
		long valorParaSaque = 30;
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(1);
		assertThat(cd.getQtdNota20()).isEqualTo(1);
		assertThat(cd.getQtdNota50()).isEqualTo(0);
		assertThat(cd.getQtdNota100()).isEqualTo(0);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}

	@Test
	public void saque80ReaisTest() throws Exception {
		long valorParaSaque = 80;
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(1);
		assertThat(cd.getQtdNota20()).isEqualTo(1);
		assertThat(cd.getQtdNota50()).isEqualTo(1);
		assertThat(cd.getQtdNota100()).isEqualTo(0);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void saqueSoNotaDe10SucessoTest() throws Exception {
		long valorParaSaque = 80;
		
		casheDispenserEntity.setQtdNota20(0L);
		casheDispenserEntity.setQtdNota50(0L);
		casheDispenserEntity.setQtdNota100(0L);
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(8);
		assertThat(cd.getQtdNota20()).isEqualTo(0);
		assertThat(cd.getQtdNota50()).isEqualTo(0);
		assertThat(cd.getQtdNota100()).isEqualTo(0);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void saqueSoNotaDe10FalhaTest() throws Exception {
		long valorParaSaque = 19;
		
		casheDispenserEntity.setQtdNota20(0L);
		casheDispenserEntity.setQtdNota50(0L);
		casheDispenserEntity.setQtdNota100(0L);

		thrown.expect(CasheDispenserValidatorException.class);
        thrown.expectMessage(containsString(ERROR_MSG_VALOR_INDISPONIVEL));

        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);
	}
	
	@Test
	public void saqueSoNotaDe20SucessoTest() throws Exception {
		long valorParaSaque = 80;
		
		casheDispenserEntity.setQtdNota10(0L);
		casheDispenserEntity.setQtdNota50(0L);
		casheDispenserEntity.setQtdNota100(0L);
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(0);
		assertThat(cd.getQtdNota20()).isEqualTo(4);
		assertThat(cd.getQtdNota50()).isEqualTo(0);
		assertThat(cd.getQtdNota100()).isEqualTo(0);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void saqueSoNotaDe20FalhaTest() throws Exception {
		long valorParaSaque = 30;
		
		casheDispenserEntity.setQtdNota10(0L);
		casheDispenserEntity.setQtdNota50(0L);
		casheDispenserEntity.setQtdNota100(0L);

		thrown.expect(CasheDispenserValidatorException.class);
        thrown.expectMessage(containsString(ERROR_MSG_VALOR_INDISPONIVEL));

        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);
	}
	
	@Test
	public void saqueSoNotaDe50SucessoTest() throws Exception {
		long valorParaSaque = 500;
		
		casheDispenserEntity.setQtdNota10(0L);
		casheDispenserEntity.setQtdNota20(0L);
		casheDispenserEntity.setQtdNota100(0L);
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(0);
		assertThat(cd.getQtdNota20()).isEqualTo(0);
		assertThat(cd.getQtdNota50()).isEqualTo(10);
		assertThat(cd.getQtdNota100()).isEqualTo(0);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}
	
	@Test
	public void saqueSoNotaDe50FalhaTest() throws Exception {
		long valorParaSaque = 480;
		
		casheDispenserEntity.setQtdNota10(0L);
		casheDispenserEntity.setQtdNota20(0L);
		casheDispenserEntity.setQtdNota100(0L);

		thrown.expect(CasheDispenserValidatorException.class);
        thrown.expectMessage(containsString(ERROR_MSG_VALOR_INDISPONIVEL));

        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);
	}

	@Test
	public void saqueSoNotaDe100SucessoTest() throws Exception {
		long valorParaSaque = 1000;
		
		casheDispenserEntity.setQtdNota10(0L);
		casheDispenserEntity.setQtdNota20(0L);
		casheDispenserEntity.setQtdNota50(0L);
		
        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		CasheDispenser cd = apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);

		assertThat(cd.getQtdNota10()).isEqualTo(0);
		assertThat(cd.getQtdNota20()).isEqualTo(0);
		assertThat(cd.getQtdNota50()).isEqualTo(0);
		assertThat(cd.getQtdNota100()).isEqualTo(10);
		assertThat(cd.getValorTotal()).isEqualTo(valorParaSaque);
	}

	@Test
	public void saqueSoNotaDe100FalhaTest() throws Exception {
		long valorParaSaque = 480;
		
		casheDispenserEntity.setQtdNota10(0L);
		casheDispenserEntity.setQtdNota20(0L);
		casheDispenserEntity.setQtdNota50(0L);

		thrown.expect(CasheDispenserValidatorException.class);
        thrown.expectMessage(containsString(ERROR_MSG_VALOR_INDISPONIVEL));

        Mockito.when(cdRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(casheDispenserEntity));

		apiService.getSaque(casheDispenserEntity.getId(), valorParaSaque);
	}
}