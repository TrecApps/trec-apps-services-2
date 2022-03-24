package com.trecapps.base.FalsehoodModel.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table
@Data
public class FalsehoodCommonTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;

    @ManyToOne
    @JoinColumn(name = "falsehood_id")
    Falsehood falsehood;

    String tag;
}
