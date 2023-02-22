package com.example.springbootinit.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "inner_policy")
public class InnerPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "file")
    private String file;

    @Column(name = "department")
    private String department;

    @Column(name = "chapter")
    private String chapter;

    @Column(name = "article")
    private Integer article;

    @Column(name = "content")
    private String content;

    public InnerPolicy(Integer id, String file, String department, String chapter, Integer article, String content) {
        this.id = id;
        this.file = file;
        this.department = department;
        this.chapter = chapter;
        this.article = article;
        this.content = content;
    }

    public InnerPolicy() {
    }
}
