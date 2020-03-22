package com.xc.oj.service;

import com.xc.oj.entity.ProblemBase;
import com.xc.oj.repository.ProblemBaseRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProblemBaseService {
    final ProblemBaseRepository problemBaseRepository;

    public ProblemBaseService(ProblemBaseRepository problemBaseRepository) {
        this.problemBaseRepository = problemBaseRepository;
    }

    public Optional<ProblemBase> findById(Long id){
        return problemBaseRepository.findById(id);
    }

    public void save(ProblemBase problemBase){
        problemBaseRepository.save(problemBase);
    }
}
