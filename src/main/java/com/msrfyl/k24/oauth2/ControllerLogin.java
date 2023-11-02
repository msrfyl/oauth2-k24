package com.msrfyl.k24.oauth2;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class ControllerLogin {

    @GetMapping("/login")
    private String login(
            Model model, HttpServletRequest httpServletRequest,
            @RequestParam(value = "error", defaultValue = "false") Boolean error
    ) {
        if (error) {
            String errorMessage = "";
            try {
                BadCredentialsException bad = (BadCredentialsException) httpServletRequest.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                if (bad.getMessage() == null) {
                    errorMessage = "username and password didn't match";
                } else {
                    errorMessage = bad.getMessage();
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = "username and password didn't match";
            }
            model.addAttribute("errorMessage", errorMessage);
        }
        return "login";
    }

    @GetMapping("/bg")
    private void Background(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("image/jpg");
        File file = new File("bg.jpg");
        OutputStream outputStream  = httpServletResponse.getOutputStream();
        outputStream.write(FileCopyUtils.copyToByteArray(file));
    }

}
