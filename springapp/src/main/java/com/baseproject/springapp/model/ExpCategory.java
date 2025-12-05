package com.baseproject.springapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_expense_categories")
public class ExpCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long cat_id;

    @Column(name = "category_name")
    private String cat_name;

    @Column(name = "description")
    private String description;

    @Column(name = "delete_status")
    private Integer deleteStatus;

    // getters & setters

    public Long getCat_id() { return cat_id; }
    public void setCat_id(Long cat_id) { this.cat_id = cat_id; }

    public String getCat_name() { return cat_name; }
    public void setCat_name(String cat_name) { this.cat_name = cat_name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDeleteStatus() { return deleteStatus; }
    public void setDeleteStatus(Integer deleteStatus) { this.deleteStatus = deleteStatus; }
}
