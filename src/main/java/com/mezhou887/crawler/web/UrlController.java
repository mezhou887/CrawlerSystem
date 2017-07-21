package com.mezhou887.crawler.web;
import com.mezhou887.system.core.Result;
import com.mezhou887.system.core.ResultGenerator;
import com.mezhou887.crawler.model.Url;
import com.mezhou887.crawler.service.UrlService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* Created by CodeGenerator on 2017/07/17.
*/
@RestController
@RequestMapping("/url")
public class UrlController {
    @Resource
    private UrlService urlService;

    @PostMapping("/add")
    public Result add(Url url) {
        urlService.save(url);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        urlService.deleteById(id);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/update")
    public Result update(Url url) {
        urlService.update(url);
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        Url url = urlService.findById(id);
        return ResultGenerator.genSuccessResult(url);
    }

    @PostMapping("/list")
    public Result list(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Url> list = urlService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        return ResultGenerator.genSuccessResult(pageInfo);
    }
}
