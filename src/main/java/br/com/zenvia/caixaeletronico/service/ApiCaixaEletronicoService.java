package br.com.zenvia.caixaeletronico.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.zenvia.caixaeletronico.service.dto.CacheDispenser;
import br.com.zenvia.caixaeletronico.service.dto.Notas;
import br.com.zenvia.caixaeletronico.service.entity.CacheDispenserEntity;
import br.com.zenvia.caixaeletronico.service.repository.CacheDispenserRepository;
import br.com.zenvia.caixaeletronico.web.response.error.ErrorMessage;
import br.com.zenvia.caixaeletronico.web.response.exception.CacheDispenserValidatorException;

@Service
public class ApiCaixaEletronicoService {

	private final Logger log = LoggerFactory.getLogger(ErrorMessage.class);
	
	@Autowired
	private CacheDispenserRepository cacheDispenserRepository;

	/**
	 * Realiza o saque do valor no cache dispenser
	 * 
	 * @param Long idCacheDispenser
	 * @param Long valor
	 * @return CacheDispenser
	 * @throws Exception
	 */
	public CacheDispenser getSaque(Long idCacheDispenser, Long valorParaSaque) throws CacheDispenserValidatorException {
		log.debug("getSaque");
		CacheDispenserEntity cacheDispenserEntity = cacheDispenserRepository.findById(idCacheDispenser).orElseThrow(
				() -> new CacheDispenserValidatorException("Cache dispenser não encontrado."));

		getValorTotalDisponivelCD(cacheDispenserEntity, valorParaSaque);

		CacheDispenser saque = realizaSaque(cacheDispenserEntity, valorParaSaque);
		return saque;
	}

	/**
	 * Realiza sque e contagem das notas
	 * 
	 * @param CacheDispenserEntity cacheDispenserEntity
	 * @param Long valor
	 * @return CacheDispenser
	 * @throws Exception
	 */
	private CacheDispenser realizaSaque(CacheDispenserEntity cacheDispenserEntity, Long valorParaSaque)
			throws CacheDispenserValidatorException {
		
		log.debug("saque -> cache dispenser "+cacheDispenserEntity.getId()+" / valor R$"+valorParaSaque);
		CacheDispenser resp = new CacheDispenser();

		resp.setValorTotal(valorParaSaque);

		long notasDe100 = getTotalNotas(cacheDispenserEntity, resp, Notas.CEM);
		long notasDe50 = getTotalNotas(cacheDispenserEntity, resp, Notas.CINQUENTA);
		long notasDe20 = getTotalNotas(cacheDispenserEntity, resp, Notas.VINTE);
		long notasDe10 = getTotalNotas(cacheDispenserEntity, resp, Notas.DEZ);

		if (notasDe100 == 0 && notasDe50 == 0 && notasDe20 == 0 && notasDe10 == 0) {
			throw new CacheDispenserValidatorException("Valor indisponível para saque, tente outro!");
		}

		resp.setValorTotal(valorParaSaque);
		resp.setQtdNota10(notasDe10);
		resp.setQtdNota20(notasDe20);
		resp.setQtdNota50(notasDe50);
		resp.setQtdNota100(notasDe100);

		updateQtdNotasCD(cacheDispenserEntity, resp);
		return resp;
	}

	/**
	 * Realiza update no BD das notas
	 * @param CacheDispenserEntity cdEntity
	 * @param CacheDispenser cd
	 */
	private void updateQtdNotasCD(CacheDispenserEntity cdEntity, CacheDispenser cd) {
		log.debug("Func updateQtdNotasCD");
		long notas10 = cdEntity.getQtdNota10() - cd.getQtdNota10();
		long notas20 = cdEntity.getQtdNota20() - cd.getQtdNota20();
		long notas50 = cdEntity.getQtdNota50() - cd.getQtdNota50();
		long notas100 = cdEntity.getQtdNota100() - cd.getQtdNota100();

		cdEntity.setQtdNota10(notas10);
		cdEntity.setQtdNota20(notas20);
		cdEntity.setQtdNota50(notas50);
		cdEntity.setQtdNota100(notas100);
		cacheDispenserRepository.save(cdEntity);
	}

	/**
	 * Verifica a quantidade de notas por valor deverá ser dispensada
	 * @param CacheDispenserEntity cacheDispenserEntity
	 * @param CacheDispenser cd
	 * @param Notas nota
	 * @return
	 */
	private long getTotalNotas(CacheDispenserEntity cacheDispenserEntity, CacheDispenser cd, Notas nota) {
		log.debug("Func getTotalNotas");
		long resp = 0;
		boolean temNotaDe100 = cacheDispenserEntity.getQtdNota100() > 0 ? true : false;
		boolean temNotaDe50 = cacheDispenserEntity.getQtdNota50() > 0 ? true : false;
		boolean temNotaDe20 = cacheDispenserEntity.getQtdNota20() > 0 ? true : false;
		boolean temNotaDe10 = cacheDispenserEntity.getQtdNota10() > 0 ? true : false;

		long notas = getTotalDeNotas(cacheDispenserEntity, nota);

		long valorParaSaque = cd.getValorTotal();
		long tmpValorSaque = valorParaSaque;

		while (tmpValorSaque > 0 && notas > 0) {
			tmpValorSaque -= nota.getValor();
			long tmpSobra = valorParaSaque % nota.getValor();
			if (tmpValorSaque == 0
					|| (temNotaDe100 && tmpValorSaque >= Notas.CEM.getValor())
					|| (temNotaDe50 && tmpValorSaque >= Notas.CINQUENTA.getValor())
					|| (temNotaDe20 && tmpValorSaque >= Notas.VINTE.getValor())
					|| (temNotaDe10 && tmpValorSaque >= Notas.DEZ.getValor())) {
				
				resp = tmpSobra != 0 ? (valorParaSaque - tmpValorSaque) / nota.getValor() : valorParaSaque / nota.getValor();
				valorParaSaque -= (nota.getValor() * resp);

				cd.setValorTotal(valorParaSaque);
				break;
			}
		}

		return resp;
	}

	/**
	 * Busca a quantidade de notas, conforme a nota passada
	 * @param CacheDispenserEntity cacheDispenserEntity
	 * @param Notas valorNota
	 * @return long
	 */
	private long getTotalDeNotas(CacheDispenserEntity cacheDispenserEntity, Notas valorNota) {
		log.debug("Func getTotalDeNotas");
		long resp = 0;

		switch (valorNota) {
		case CEM:
			resp = cacheDispenserEntity.getQtdNota100();
			break;
		case CINQUENTA:
			resp = cacheDispenserEntity.getQtdNota50();
			break;
		case VINTE:
			resp = cacheDispenserEntity.getQtdNota20();
			break;
		case DEZ:
			resp = cacheDispenserEntity.getQtdNota10();
			break;
		}
		return resp;
	}

	/**
	 * Busca o valor total disponível no Cache Dispenser, fazendo a contagem das
	 * notas disponíveis.
	 * 
	 * @param CacheDispenserEntity cacheDispenserEntity
	 * @param Long valorSaque
	 * @return long
	 * @throws Exception
	 */
	private long getValorTotalDisponivelCD(CacheDispenserEntity cacheDispenserEntity, Long valorSaque)
			throws CacheDispenserValidatorException {
		log.debug("Func getValorTotalDisponivelCD");
		Long valorTotalDe100 = cacheDispenserEntity.getQtdNota100() * 100;
		Long valorTotalDe50 = cacheDispenserEntity.getQtdNota50() * 50;
		Long valorTotalDe20 = cacheDispenserEntity.getQtdNota20() * 20;
		Long valorTotalDe10 = cacheDispenserEntity.getQtdNota10() * 10;

		long valorTotalCacheDispenser = valorTotalDe100 + valorTotalDe50 + valorTotalDe20 + valorTotalDe10;

		if (valorTotalCacheDispenser < valorSaque) {
			throw new CacheDispenserValidatorException("Valor do saque maior que valor disponível no momento");
		}

		return valorTotalCacheDispenser;
	}

}
