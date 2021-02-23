package com.enzitechnologies.sakinbank.service;

import com.enzitechnologies.sakinbank.model.Deposit;
import com.enzitechnologies.sakinbank.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService {

    @Autowired
    private DepositRepository depositRepository;

    public void getAllDeposits(){}

//    public List<Deposit> getAllPreDeposits(){
//        return depositRepository.findByDepositType("pre-deposit");
//    }
//
//
//    public List<Deposit> getAllPostDeposits(){
//        return depositRepository.findByDepositType("post-deposit");
//    }

    public Deposit findByDepositId(String depositId){
        return depositRepository.findByDepositId(depositId).get();
    }

    public void saveDeposit(Deposit deposit){
        depositRepository.save(deposit);
    }
}
