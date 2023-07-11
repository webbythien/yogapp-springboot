package com.swp.yogaapp.repository;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.swp.yogaapp.model.Account;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SearchRepositoryImpl implements  SearchRepository{
    @Autowired
    MongoClient client;

    @Autowired
    MongoConverter converter;
    @Override
    public Account findByEmail(String email) {
            Account account = null;
            final List<Account> accounts = new ArrayList<>();
            MongoDatabase database = client.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("accounts");
            AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                    new Document("text",
                            new Document("query", email)
                                    .append("path", "email")))));

            result.forEach(doc -> accounts.add(converter.read(Account.class,doc)));
            if(!accounts.isEmpty()){
                return account = accounts.get(0);

            }else {
                return account;
            }

    }

    public Account findById(String _id) {
        Account account = null;
        final List<Account> accounts = new ArrayList<>();
        MongoDatabase database = client.getDatabase("test");
        MongoCollection<Document> collection = database.getCollection("accounts");

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                new Document("text",
                        new Document("query", _id)
                                .append("path", "_id")))));

        result.forEach(doc -> accounts.add(converter.read(Account.class,doc)));
        if(!accounts.isEmpty()){
            return account = accounts.get(0);

        }else {
            return account;
        }

    }
}
