package com.example.samuraitravel.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity  //そのクラスがエンティティとして機能するようになる
@Table(name = "houses")  //対応づけるテーブル名を指定
@Data  //ゲッターやセッターなどを自動生成する

public class House {//フィールド
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	//主キーには@Idおよび@GeneratedValueアノテーションをつける
	/* @GeneratedValueアノテーションをつけてstrategy = GenerationType.IDENTITYを指定することで、
	 * テーブル内のAUTO_INCREMENTを指定したカラム（idカラム）を利用して値を生成するようになりる。
	 * つまり、データの作成時や更新時にidの値を自分で指定しなくても、自動採番されるようになる */
    @Column(name = "id")  //対応づけるカラム名を指定
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_name")
    private String imageName;//フィールドネームはローワーキャメル

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;

}
