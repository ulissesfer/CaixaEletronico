package br.com.zenvia.caixaeletronico.service;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zenvia.caixaeletronico.service.dto.CasheDispenser;
import br.com.zenvia.caixaeletronico.service.dto.Notas;
import br.com.zenvia.caixaeletronico.service.entity.CasheDispenserEntity;
import br.com.zenvia.caixaeletronico.service.repository.CasheDispenserRepository;
import br.com.zenvia.caixaeletronico.web.response.error.ErrorMessage;
import br.com.zenvia.caixaeletronico.web.response.exception.CasheDispenserValidatorException;

@Service
public class ApiCaixaEletronicoService {

	private final Logger log = LoggerFactory.getLogger(ErrorMessage.class);
	
	@Autowired
	private CasheDispenserRepository casheDispenserRepository;

	/**
	 * Realiza o saque do valor no cashe dispenser
	 * 
	 * @param Long idCasheDispenser
	 * @param Long valor
	 * @return CasheDispenser
	 * @throws Exception
	 */
	public CasheDispenser getSaque(Long idCasheDispenser, Long valorParaSaque) {
		log.debug("getSaque");
		CasheDispenserEntity casheDispenserEntity = casheDispenserRepository.findById(idCasheDispenser).orElseThrow(
				() -> new CasheDispenserValidatorException("Cashe dispenser não encontrado."));

		getValorTotalDisponivelCD(casheDispenserEntity, valorParaSaque);

		return realizaSaque(casheDispenserEntity, valorParaSaque);
	}

	/**
	 * Realiza sque e contagem das notas
	 * 
	 * @param CasheDispenserEntity casheDispenserEntity
	 * @param Long valor
	 * @return CasheDispenser
	 * @throws Exception
	 */
	private CasheDispenser realizaSaque(CasheDispenserEntity casheDispenserEntity, Long valorParaSaque) {
		log.debug( "saque -> cashe dispenser "+casheDispenserEntity.getId()+" / valor R$"+valorParaSaque);
		CasheDispenser resp = new CasheDispenser();

		resp.setValorTotal(valorParaSaque);

		long notasDe100 = getTotalNotas(casheDispenserEntity, resp, Notas.CEM);
		long notasDe50 = getTotalNotas(casheDispenserEntity, resp, Notas.CINQUENTA);
		long notasDe20 = getTotalNotas(casheDispenserEntity, resp, Notas.VINTE);
		long notasDe10 = getTotalNotas(casheDispenserEntity, resp, Notas.DEZ);

		if (resp.getValorTotal() > 0) {
			throw new CasheDispenserValidatorException("Valor indisponível para saque, tente outro!");
		}

		resp.setValorTotal(valorParaSaque);
		resp.setQtdNota10(notasDe10);
		resp.setQtdNota20(notasDe20);
		resp.setQtdNota50(notasDe50);
		resp.setQtdNota100(notasDe100);

		updateQtdNotasCD(casheDispenserEntity, resp);
		return resp;
	}

	/**
	 * Realiza update no BD das notas
	 * @param CasheDispenserEntity cdEntity
	 * @param CasheDispenser cd
	 */
	private void updateQtdNotasCD(CasheDispenserEntity cdEntity, CasheDispenser cd) {
		log.debug("Func updateQtdNotasCD");
		long notas10 = cdEntity.getQtdNota10() - cd.getQtdNota10();
		long notas20 = cdEntity.getQtdNota20() - cd.getQtdNota20();
		long notas50 = cdEntity.getQtdNota50() - cd.getQtdNota50();
		long notas100 = cdEntity.getQtdNota100() - cd.getQtdNota100();

		cdEntity.setQtdNota10(notas10);
		cdEntity.setQtdNota20(notas20);
		cdEntity.setQtdNota50(notas50);
		cdEntity.setQtdNota100(notas100);
		casheDispenserRepository.save(cdEntity);
	}

	/**
	 * Verifica a quantidade de notas por valor deverá ser dispensada
	 * @param CasheDispenserEntity casheDispenserEntity
	 * @param CasheDispenser cd
	 * @param Notas nota
	 * @return
	 */
	private long getTotalNotas(CasheDispenserEntity casheDispenserEntity, CasheDispenser cd, Notas nota) {
		log.debug("Func getTotalNotas");
		long resp = 0;

		Map<Notas, Long> notasDisponiveisNoCD = getNotasDisponiveisNoCD(casheDispenserEntity);
		long qtdDeNotasNoCD = notasDisponiveisNoCD.get(nota).longValue();

		long valorParaSaque = cd.getValorTotal();
		long tmpSobra = valorParaSaque % nota.getValor();

		boolean temNotaDe10 = notasDisponiveisNoCD.get(Notas.DEZ).longValue() > 0;

		if (qtdDeNotasNoCD > 0 && valorParaSaque >= nota.getValor()) {
			if (tmpSobra == 0) {
				resp = valorParaSaque / nota.getValor();
				valorParaSaque = 0L;
			} else {
				while (valorParaSaque >= nota.getValor()) {
					if((valorParaSaque - nota.getValor()) <= Notas.DEZ.getValor() && !temNotaDe10) {
						break;
					}
					resp += 1;
					valorParaSaque -= nota.getValor();
				}
			}			
		}
		
		cd.setValorTotal(valorParaSaque);
		return resp;
	}

	/**
	 * Busca a quantidade de notas, conforme a nota passada
	 * @param CasheDispenserEntity casheDispenserEntity
	 * @param Notas valorNota
	 * @return long
	 */
	private Map<Notas, Long> getNotasDisponiveisNoCD(CasheDispenserEntity casheDispenserEntity) {
		log.debug("Func getTotalDeNotas");
		Map<Notas, Long> resp = new EnumMap<>(Notas.class);

		resp.put(Notas.CEM, casheDispenserEntity.getQtdNota100());
		resp.put(Notas.CINQUENTA, casheDispenserEntity.getQtdNota50());
		resp.put(Notas.VINTE, casheDispenserEntity.getQtdNota20());
		resp.put(Notas.DEZ, casheDispenserEntity.getQtdNota10());

		return resp;
	}

	/**
	 * Busca o valor total disponível no Cashe Dispenser, fazendo a contagem das
	 * notas disponíveis.
	 * 
	 * @param CasheDispenserEntity casheDispenserEntity
	 * @param Long valorSaque
	 * @return long
	 * @throws Exception
	 */
	private long getValorTotalDisponivelCD(CasheDispenserEntity casheDispenserEntity, Long valorSaque) {
		log.debug("Func getValorTotalDisponivelCD");
		Long valorTotalDe100 = casheDispenserEntity.getQtdNota100() * 100;
		Long valorTotalDe50 = casheDispenserEntity.getQtdNota50() * 50;
		Long valorTotalDe20 = casheDispenserEntity.getQtdNota20() * 20;
		Long valorTotalDe10 = casheDispenserEntity.getQtdNota10() * 10;

		long valorTotalCasheDispenser = valorTotalDe100 + valorTotalDe50 + valorTotalDe20 + valorTotalDe10;

		if (valorTotalCasheDispenser < valorSaque) {
			throw new CasheDispenserValidatorException("Valor do saque maior que valor disponível no momento.");
		}

		return valorTotalCasheDispenser;
	}

}
