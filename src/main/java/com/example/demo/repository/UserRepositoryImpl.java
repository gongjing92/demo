package com.example.demo.repository;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;
import com.example.demo.exception.InvalidParamsException;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@PostConstruct
	private void init() {
		//init database if empty
		if(mongoTemplate.count(new Query(), User.class) == 0) {
			mongoTemplate.save(new User("Tim",600), "users");
			mongoTemplate.save(new User("Mueller",6000), "users");
			mongoTemplate.save(new User("Gerald",3000), "users");
			mongoTemplate.save(new User("Zofia",2000), "users");
			mongoTemplate.save(new User("Eleasha",1000), "users");
			mongoTemplate.save(new User("Alan",1500), "users");
		}
	}

	public List<User> getAllUsers(double min, double max, int offset, int limit, String sort) {
		Query query = new Query();
		query.addCriteria(Criteria.where("salary").gte(min).lte(max));
		query.skip(offset).limit(limit);
		if (StringUtils.isNotBlank(sort)) {
			if (sort.toLowerCase().equals("name") || sort.toLowerCase().equals("salary")) {
				query.with(Sort.by(Sort.Direction.ASC, sort.toLowerCase()));
			}
			else {
				throw new InvalidParamsException("Invalid params for sort");
			}
		}
		return mongoTemplate.find(query, User.class);
	}
	
	public void saveUser(User user) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(user.getName()));
		Update update = new Update();
		update.set("salary", user.getSalary());
		mongoTemplate.upsert(query, update, User.class);
	}

}
