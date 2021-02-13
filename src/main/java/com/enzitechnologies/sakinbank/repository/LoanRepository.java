package com.enzitechnologies.sakinbank.repository;

import com.enzitechnologies.sakinbank.model.Loan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends CrudRepository<Loan, String> {
}
