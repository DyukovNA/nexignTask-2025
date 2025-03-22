package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Subscriber {
    @Id
    private String msisdn;
}
