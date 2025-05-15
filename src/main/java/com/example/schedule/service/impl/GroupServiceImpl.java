package com.example.schedule.service.impl;

import com.example.schedule.model.Group;
import com.example.schedule.repository.GroupRepository;
import com.example.schedule.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class GroupServiceImpl implements GroupService {

    GroupRepository groupRepository;

    @Override
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @Override
    public String saveGroup(Group group) {
        groupRepository.save(group);
        return "Группа успешно сохранена.";
    }

    @Override
    public String deleteGroupByGroupNumber(String groupNumber) {
        groupRepository.deleteGroupByGroupNumber(groupNumber);
        return "Группа успешно удалена.";
    }

}
