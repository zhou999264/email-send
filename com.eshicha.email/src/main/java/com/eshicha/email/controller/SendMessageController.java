package com.eshicha.email.controller;

import com.eshicha.email.config.SameUrlData;
import com.eshicha.email.entities.CommonResult;
import com.eshicha.email.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class SendMessageController {

    @Autowired
    private MailService mailService;

    @Value("${spring.mail.send}")
    private String send;


//    @GetMapping("/sendMessage/{userName}/{phone}/{title}/{contet}")
//    public CommonResult testSend(@PathVariable String userName,
//                                 @PathVariable String phone,@PathVariable String title,@PathVariable String contet) {
//        String to = send;
//        mailService.send(to, "网站咨询邮件", contet,phone,title,userName);
//
//        return new CommonResult(200,"发送成功！");
//    }

    @SameUrlData
    @PostMapping("/sendMessage")
    @ResponseBody
    public CommonResult sendMessage(@RequestParam("name")String name,
                                 @RequestParam("phone")String phone,
                                 @RequestParam("subject")String subject,
                                 @RequestParam("message")String message) {
        String to = send;
        //mailService.send(to, "网站咨询邮件", name,phone,subject,message);

        return new CommonResult(200,"发送成功！");
    }

    @SameUrlData
    @GetMapping("/fangshua")
    public CommonResult fangshua(){
        return new CommonResult(200,"发送成功！");
    }
}
