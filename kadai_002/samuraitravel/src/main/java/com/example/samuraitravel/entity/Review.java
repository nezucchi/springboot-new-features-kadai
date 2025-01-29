package com.example.samuraitravel.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "reviews")
@Data
public class Review {
	// id(主キー。Integer型。テーブル側はidで作成)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// house(House型。テーブル側はhouse_idで作成。ManyToOneを利用)
	@ManyToOne
	@JoinColumn(name = "house_id")
	private House house;

	// user(User型。テーブル側はuser_idで作成。ManyToOneを利用)
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// content(String型。テーブル側はcontentで作成)
	@Column(name = "content")
	private String content;

	// score(Integer型。テーブル側はscoreで作成)
	@Column(name = "score")
	private Integer score;

	// ↓以下は作成してもしなくてもOK
	// createdAt(Timestamp型。テーブル側はcreated_atで作成。insertable = false, updatable = falseを利用)

	// updatedAt(Timestamp型。テーブル側はupdated_atで作成。insertable = false, updatable = falseを利用)
	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;
}