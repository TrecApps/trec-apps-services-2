package com.trecapps.base.FalsehoodModel.models;

import lombok.Data;

import javax.persistence.*;
@Entity
@Table
@Data
public class CommonLieUncommonTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;

    @ManyToOne
    @JoinColumn
    CommonLie falsehood;

    String tag;
}
