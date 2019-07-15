package br.com.zenvia.caixaeletronico.service.repository;

import static org.assertj.core.api.Assertions.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.zenvia.caixaeletronico.service.entity.CasheDispenserEntity;
import br.com.zenvia.caixaeletronico.service.repository.CasheDispenserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CasheDispenserRepositoryTest {
	@Autowired
	private CasheDispenserRepository cdRepository;
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void saveCacheDispenserEntityTest() {
		CasheDispenserEntity cdEntity = new CasheDispenserEntity(10L, 10L, 10L, 10L);		
		cdRepository.save(cdEntity);

		assertThat(cdEntity.getId()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getId());
	}

	@Test
	public void deleteCacheDispenserEntityTest() {
		CasheDispenserEntity cdEntity = new CasheDispenserEntity(10L, 10L, 10L, 10L);		
		cdRepository.save(cdEntity);
		assertThat(cdEntity.getId()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getId());
		
		cdRepository.deleteById(cdEntity.getId());
		assertThat(cdRepository.findById(cdEntity.getId())).isEmpty();
	}

	@Test
	public void updateCacheDispenserEntityTest() {
		CasheDispenserEntity cdEntity = new CasheDispenserEntity(10L, 10L, 10L, 10L);		
		cdRepository.save(cdEntity);
		assertThat(cdEntity.getQtdNota10()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getQtdNota10());
		assertThat(cdEntity.getQtdNota20()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getQtdNota20());
		assertThat(cdEntity.getQtdNota50()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getQtdNota50());
		assertThat(cdEntity.getQtdNota100()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getQtdNota100());

		cdEntity.setQtdNota10(5L);
		cdEntity.setQtdNota20(21L);
		cdEntity.setQtdNota50(15L);
		cdEntity.setQtdNota100(1L);
		
		cdRepository.save(cdEntity);
		assertThat(cdEntity.getId()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getId());
		assertThat(cdEntity.getQtdNota10()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getQtdNota10());
		assertThat(cdEntity.getQtdNota20()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getQtdNota20());
		assertThat(cdEntity.getQtdNota50()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getQtdNota50());
		assertThat(cdEntity.getQtdNota100()).isEqualTo(cdRepository.findById(cdEntity.getId()).get().getQtdNota100());
	}
}
