package com.keshe.controller;

import com.google.code.kaptcha.Producer;
import com.keshe.service.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/generate")
    public void generateCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        String captchaText = captchaProducer.createText();
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);
        captchaService.storeCaptcha(request.getSession().getId(), captchaText);
        ImageIO.write(captchaImage, "jpg", response.getOutputStream());
    }

    @PostMapping("/validate")
    public boolean validateCaptcha(HttpServletRequest request, @RequestParam String captcha) {
        String sessionId = request.getSession().getId();
        return captchaService.validateCaptcha(sessionId, captcha);
    }
}