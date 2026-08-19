package com.example.demo.repository;

import com.example.demo.entity.Notice;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    @Query("SELECT n FROM Notice n WHERE n.publishedUntil IS NULL OR n.publishedUntil >= :now ORDER BY n.createdAt DESC")
    List<Notice> findValidNotices(@Param("now") LocalDate now, Pageable pageable);
}