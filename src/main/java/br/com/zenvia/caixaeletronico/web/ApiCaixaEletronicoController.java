package br.com.zenvia.caixaeletronico.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.zenvia.caixaeletronico.service.dto.CacheDispenser;
import br.com.zenvia.caixaeletronico.service.dto.Conta;
import br.com.zenvia.caixaeletronico.service.dto.Saque;

@RestController
public class ApiCaixaEletronicoController {

	@GetMapping("/conta/saldo")
	public Double getSaldoConta(@RequestBody Conta conta) {
		return new Double(2.0);
	}

	@PostMapping("/conta/saque")
	public String realizaSaqueConta(@RequestBody Saque saque) {
		return "";
	}

	@GetMapping("/cash-dispenser/saldo")
	public Long getSaldoCD(@RequestParam CacheDispenser cashDispenser) {
		return new Long(100);
	}
}
