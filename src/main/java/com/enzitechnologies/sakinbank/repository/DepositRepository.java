package com.enzitechnologies.sakinbank.repository;

import com.enzitechnologies.sakinbank.model.Deposit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRepository extends CrudRepository<Deposit, String> {

    Optional<Deposit> findByDepositId(String deposit_id);
//    List<Deposit> findByDepositType(String deposit_type);
}
