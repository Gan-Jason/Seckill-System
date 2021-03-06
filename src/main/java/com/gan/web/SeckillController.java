package com.gan.web;

import com.gan.dto.Exposer;
import com.gan.dto.SeckillExecution;
import com.gan.dto.SeckillResult;
import com.gan.entity.Seckill;
import com.gan.enums.SeckillStatEnum;
import com.gan.exception.RepeatKillException;
import com.gan.exception.SeckillCloseException;
import com.gan.service.ISeckillService;
import com.gan.service.impl.SeckillServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/seckill")//url:模块/资源/{}/细分
public class SeckillController {


    Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISeckillService seckillService;

    @RequestMapping(value = "/list",method= RequestMethod.GET)
    public String list(Model model){

        //list.jsp+model=ModelAndView
        //获取列表页
        List<Seckill> list=seckillService.getSeckillList();
        model.addAttribute("list",list);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId,Model model){
        if(seckillId==null){
            return "redirect:/seckill/list";
        }
        Seckill seckill=seckillService.getById(seckillId);
        if(seckill==null){
            return "forward:/seckill/list";
        }

        model.addAttribute("seckill",seckill);
        return "detail";
    }


    //ajax,json暴露秒杀接口的方法
    @RequestMapping(value="/{seckillId}/exposer",
                    method=RequestMethod.GET,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposerSeckillResult(@PathVariable("seckillId") Long seckillId){
            SeckillResult<Exposer> result;

            try{
                Exposer exposer=seckillService.exportSeckillUrl(seckillId);
                result=new SeckillResult<Exposer>(true,exposer);
            }catch (Exception e){
                logger.error(e.getMessage(),e);
                result=new SeckillResult<Exposer>(false,e.getMessage());

            }
            return result;
    }


    @RequestMapping(value = "/{seckillId}/{md5}/execution",
                    method = RequestMethod.POST,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> executionSeckillResult(@PathVariable("seckillId") Long seckillId,
                                                                  @PathVariable("md5") String md5,
                                                                  @CookieValue(value="userPhone",required = false) Long userPhone){
        if(userPhone==null){
            return new SeckillResult<SeckillExecution>(false,"未注册");

        }

        SeckillResult<SeckillExecution> result;

        try{
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            return new SeckillResult<>(true,execution);
        }catch (RepeatKillException e1){
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false,execution);
        }catch (SeckillCloseException e2){
            SeckillExecution execution=new SeckillExecution(seckillId,SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(false,execution);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            SeckillExecution execution=new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false,execution);
        }
    }


    //获取系统时间
    @RequestMapping(value="/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now=new Date();
        return new SeckillResult<>(true,now.getTime());
    }
}



