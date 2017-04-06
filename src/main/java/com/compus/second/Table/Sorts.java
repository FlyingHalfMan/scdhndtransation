package com.compus.second.Table;

import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Created by cai on 2017/3/17.
 */

@Table(name = "cs_sorts")
@Component
@Entity
public class Sorts {
    private int id;
    private String sortName;

    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }
}
