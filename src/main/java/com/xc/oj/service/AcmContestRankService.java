package com.xc.oj.service;

import com.xc.oj.entity.AcmContestRank;
import com.xc.oj.repository.AcmContestRankRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AcmContestRankService {

    private final AcmContestRankRepository acmContestRankRepository;

    public AcmContestRankService(AcmContestRankRepository acmContestRankRepository) {
        this.acmContestRankRepository = acmContestRankRepository;
    }

    public Optional<AcmContestRank> findById(Long id){
        return acmContestRankRepository.findById(id);
    }

    public void save(AcmContestRank acmContestRank){
        acmContestRankRepository.save(acmContestRank);
    }

    public Optional<AcmContestRank> findByContestIdAndUserIdAndLocked(Long cid, Long uid, boolean locked) {
        return acmContestRankRepository.findByContestIdAndUserIdAndLocked(cid,uid,locked);
    }

    public responseBase<List<AcmContestRank>> findByContestIdAndLocked(Long cid,Boolean locked){
        if(!AuthUtil.has("admin")&&locked==false)
            return responseBuilder.fail(responseCode.FORBIDDEN);
        List<AcmContestRank> list=acmContestRankRepository.findByContestIdAndLocked(cid,locked);
        Collections.sort(list);
        return responseBuilder.success(list);
    }

}
