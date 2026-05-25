package com.example.portfolio_simple_spring_mvc.domain.planner;

import com.example.portfolio_simple_spring_mvc.domain.plan.Plan;
import com.example.portfolio_simple_spring_mvc.domain.state.State;

public interface Planner<T extends Plan, S extends State> {
    T createPlan(S state);
}
