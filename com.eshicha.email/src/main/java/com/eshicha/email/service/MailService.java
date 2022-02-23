package com.eshicha.email.service;

public interface MailService {
    public void send(String to,String subject,String userName,String phone,String title,String contet);
}
