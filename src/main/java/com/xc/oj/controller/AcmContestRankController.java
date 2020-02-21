package com.xc.oj.controller;

import com.xc.oj.entity.AcmContestRank;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.AcmContestRankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acmContestRank")
public class AcmContestRankController {
    private final AcmContestRankService acmContestRankService;

    public AcmContestRankController(AcmContestRankService acmContestRankService) {
        this.acmContestRankService = acmContestRankService;
    }

    @RequestMapping(value = "/{cid}", method = RequestMethod.GET)
    public responseBase<List<AcmContestRank>> findByContestId(@PathVariable Long cid, @RequestParam Boolean locked){
        return acmContestRankService.findByContestIdAndLocked(cid,locked);
    }
}
