package com.enzitechnologies.sakinbank;

import com.enzitechnologies.sakinbank.model.Account;
import com.enzitechnologies.sakinbank.model.Token;
import com.enzitechnologies.sakinbank.repository.AccountRepository;
import com.enzitechnologies.sakinbank.repository.TokenRepository;
import com.enzitechnologies.sakinbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class SakinBankApplication implements CommandLineRunner {


	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private AccountRepository accountRepository;

	public static void main(String[] args) {
		SpringApplication.run(SakinBankApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {

//	load SSP data into persistence when the application starts
		if(tokenRepository.findById("0.0.321647").isEmpty()){
			tokenRepository.save(
					new Token(
							"0.0.321647",
							"Sakin Savings Points",
							"SSP",
							0,
							5000,
							"0.0.14410",
							"0.0.14410",
							"0.0.14410",
							"0.0.14410",
							"0.0.14410")
			);
		}

//		load treasury account into persistence when the application starts

		if(accountRepository.findById("0.0.14410").isEmpty()){
			accountRepository.save(
					new Account(
							"0.0.14410",
							"treasury",
							"302e020100300506032b657004220420c2b949ec8f503faaa1533e20d29378a67a0e35e46affef9d296a84354c299367",
							"302a300506032b65700321009195ba61f7ee96660284de8aa40bf469c638f00d6bd6f9de3b86109b21473579",
							0,
							false,
							true,
							null,
							null,
							null,
							null
							)
			);
		}
	}
}
