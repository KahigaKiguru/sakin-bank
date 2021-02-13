package com.enzitechnologies.sakinbank.repository;

import com.enzitechnologies.sakinbank.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account,String> {

    Account findByUserName(String username);
}
