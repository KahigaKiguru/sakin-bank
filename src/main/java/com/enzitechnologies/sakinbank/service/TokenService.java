package com.enzitechnologies.sakinbank.service;

import com.enzitechnologies.sakinbank.model.Token;
import com.enzitechnologies.sakinbank.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public TokenService() {}

//    get SSP token
    public Token getToken(){
        return tokenRepository.findBySymbol("SSP");
    }

}
