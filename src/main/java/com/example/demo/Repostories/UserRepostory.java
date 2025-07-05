
package com.example.demo.Repostories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Users;
import org.bson.types.ObjectId;


@Repository
public interface UserRepostory extends MongoRepository<Users, String> {
    Users findByUsername(String username);
    Users findByEmail(String email);


}

