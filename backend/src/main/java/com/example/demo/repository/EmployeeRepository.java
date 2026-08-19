package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // ここに独自の検索メソッドを追加することも可能です
    // 例: 社員番号で検索するメソッド
    // Employee findByEmployeeNo(String employeeNo);
}

