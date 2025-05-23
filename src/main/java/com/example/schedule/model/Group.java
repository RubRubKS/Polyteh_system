package com.example.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "groups")
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Group {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String groupNumber;
    @Column(unique = true, nullable = false)
    private String groupId;
    @Column(nullable = false)
    private String faculty;
}
