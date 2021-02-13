package com.enzitechnologies.sakinbank.repository;

import com.enzitechnologies.sakinbank.model.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

    Token findBySymbol(String symbol);
}
