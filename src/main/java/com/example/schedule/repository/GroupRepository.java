package com.example.schedule.repository;

import com.example.schedule.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {

    public Optional<Group> getGroupByGroupNumber(String groupNumber);

}
