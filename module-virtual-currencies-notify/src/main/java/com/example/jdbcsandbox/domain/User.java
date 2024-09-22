package com.example.jdbcsandbox.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("SAMPLE_USERS")
public class User {
    @Id
    private int id;
    @Column
    private String name;
    @Column
    private final String gender;
    @Column
    private boolean deletedYn;

    public User(String name, String gender) {
        this.name = name;
        this.gender = gender;
        this.deletedYn = false;
    }

    public void delete() {
        this.deletedYn = true;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
