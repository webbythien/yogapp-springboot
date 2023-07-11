package com.swp.yogaapp.repository;

import com.swp.yogaapp.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

    public interface AccountRepository extends MongoRepository<Account,String> {

}
