package com.example.schedule.service.impl;

import com.example.schedule.model.Deadline;
import com.example.schedule.model.ErrorResponse;
import com.example.schedule.model.User;
import com.example.schedule.repository.DeadlineRepository;
import com.example.schedule.repository.UserRepository;
import com.example.schedule.service.DeadlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class DeadlineServiceImpl implements DeadlineService {

    private final DeadlineRepository deadlineRepository;
    private final UserRepository userRepository;

    @Override
    public List<Deadline> findAllDeadlines(Long userId) {
        return deadlineRepository.findAllDeadlineByUserId(userId);
    }

    @Override
    public String saveDeadline(Long userId, Deadline deadline) {
        User user = userRepository.getUserById(userId);
        user.addDeadline(deadline);
        return "Дедлайн успешно сохранён.";
    }

    @Override
    public String updateDeadline(Long userId, Deadline deadline) {
        deadline.setUser(userRepository.getUserById(userId));
        deadlineRepository.save(deadline);
        return "Дедлайн успешно обновлён.";
    }

    @Override
    public String deleteDeadline(Long userId, Long deadlineId) {
        Deadline deadline = deadlineRepository.findById(deadlineId)
                .orElseThrow(() -> new ErrorResponse("Дедлайна не существует."));
        if (!Objects.equals(deadline.getUser().getId(), userId)) {
            throw new ErrorResponse("Нельзя удалять чужие дедлайны!");
        }
        deadlineRepository.deleteDeadlineById(deadlineId);
        return "Дедлайн успешно удалён.";
    }

}
