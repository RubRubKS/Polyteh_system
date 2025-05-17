package com.example.schedule.repository;

import com.example.schedule.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User getUserById(Long id);

}
