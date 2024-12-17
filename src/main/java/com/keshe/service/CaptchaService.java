package com.keshe.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaService {

    private final Map<String, String> captchaStore = new ConcurrentHashMap<>();

    public void storeCaptcha(String sessionId, String captcha) {
        captchaStore.put(sessionId, captcha);
    }

    public boolean validateCaptcha(String sessionId, String userInputCaptcha) {
        String storedCaptcha = captchaStore.get(sessionId);
        return storedCaptcha != null && storedCaptcha.equalsIgnoreCase(userInputCaptcha);
    }
}