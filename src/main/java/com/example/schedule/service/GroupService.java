package com.example.schedule.service;

import com.example.schedule.model.Group;

import java.util.List;

public interface GroupService {

    public List<Group> getAllGroups();
    public String saveGroup(Group group);
    public String deleteGroupByGroupNumber(String groupNumber);

}
