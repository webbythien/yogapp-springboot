package com.swp.yogaapp.repository;

import com.swp.yogaapp.model.Account;

import java.util.List;

public interface SearchRepository {
    Account findByEmail(String email);
    Account findById(String _id);

}
