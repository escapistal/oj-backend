package com.xc.oj.service;

import com.xc.oj.entity.AcmContestRank;
import com.xc.oj.entity.SingleSubmissionInfo;
import com.xc.oj.repository.AcmContestRankRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AcmContestRankService {

    private final AcmContestRankRepository acmContestRankRepository;

    public AcmContestRankService(AcmContestRankRepository acmContestRankRepository) {
        this.acmContestRankRepository = acmContestRankRepository;
    }

    public responseBase<List<AcmContestRank>> findByContestIdAndLocked(Long cid,Boolean locked){
        List<AcmContestRank> list=acmContestRankRepository.findByContestIdAndLocked(cid,locked);
        Collections.sort(list);
        return responseBuilder.success(list);
    }
}
