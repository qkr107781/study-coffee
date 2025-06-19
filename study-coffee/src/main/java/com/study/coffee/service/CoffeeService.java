package com.study.coffee.service;

import java.util.List;

import com.study.coffee.domain.Coffee;
import com.study.coffee.repository.CoffeeRepository;

public class CoffeeService {
    
    private final CoffeeRepository coffeeRepository;

    public CoffeeService(CoffeeRepository coffeeRepository){ //== @Autowired
        this.coffeeRepository = coffeeRepository;
    }

    public List<Coffee> getCoffeeList(){
        return coffeeRepository.getCoffeeList();
    }
}
