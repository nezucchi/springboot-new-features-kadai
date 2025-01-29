package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Review;
import com.example.samuraitravel.entity.User;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
           // List<Review>型のfindTop6ByHouseOrderByCreatedAtDescメソッドを作成(引数はHouse house)
	public List<Review> findTop6ByHouseOrderByCreatedAtDesc(House house);

           // ReviewクラスのfindByHouseAndUserメソッドを作成(引数はHouse house, User user)
	public Review findByHouseAndUser(House house, User user);

           // long型でcountByHouseメソッドを作成(引数はHouse house)
	public long countByHouse(House house);

           // Page<Review>型でfindByHouseOrderByCreatedAtDescメソッドを作成(引数はHouse house, Pageable pageable)
	public Page<Review> findByHouseOrderByCreatedAtDesc(House house, Pageable pageable);
}
