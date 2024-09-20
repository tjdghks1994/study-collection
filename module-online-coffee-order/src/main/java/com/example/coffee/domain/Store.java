package com.example.coffee.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Getter
@Table(name = "stores")
public class Store {
    @Id
    private String storeId;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String phoneNumber;
    @Column
    private ZonedDateTime openAt;
    @Column
    private ZonedDateTime closeAt;
}
