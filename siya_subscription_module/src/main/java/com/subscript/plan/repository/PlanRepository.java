package com.subscript.plan.repository;
import com.subscript.plan.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PlanRepository extends JpaRepository<SubscriptionPlan,Long>{}
