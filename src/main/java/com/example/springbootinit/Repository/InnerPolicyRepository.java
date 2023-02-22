package com.example.springbootinit.Repository;

import com.example.springbootinit.Entity.InnerPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InnerPolicyRepository extends JpaRepository<InnerPolicy, Integer> {
}
