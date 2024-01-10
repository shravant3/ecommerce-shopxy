package com.shopxy.ecom.controller;


import java.util.Random;
import java.io.*;
import java.util.regex.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopxy.ecom.dto.UserDto;
import com.shopxy.ecom.helper.Message;
import com.shopxy.ecom.model.User;
import com.shopxy.ecom.repository.UserRepository;
import com.shopxy.ecom.service.ProductService;
import com.shopxy.ecom.service.ReferalService;
import com.shopxy.ecom.service.ReviewService;
import com.shopxy.ecom.service.TwilioOtpservice;
import com.shopxy.ecom.service.UserDtlsService;
import com.shopxy.ecom.service.WalletService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PageController {

    Random randotp = new Random();

    String savedotp;

    @Autowired
    private UserDtlsService service;

    @Autowired
    private TwilioOtpservice twilioOtpservice;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private ReferalService referalService;


    @Autowired
    private UserRepository userRepo;

    @GetMapping("/")
    public String home(){
        return "common/home";
    }

    @GetMapping("/registration")
    public String userreg(){
        return "common/userreg";
    }

    @GetMapping("/signin")
    public String login(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "common/login";
        }

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {

            return "redirect:/admin/";
        }
        else if(authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_SELLER"))){
            return "redirect:/seller/";
        } else {
            return "redirect:/user/";
        }
    }

    @GetMapping("/sellerregistration")
    public String sellerreg(){
        return "common/sellerreg";
    }

     @PostMapping("/createUser")
    public String createuser(@ModelAttribute UserDto user,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, HttpSession session,
            @RequestParam("confirmpassword") String password, @RequestParam("referralcode") String referalcode) {

        Boolean check = service.checkUserMail(user.getEmail());
        try {
            if (!password.equals(user.getPassword())) {
                throw new Exception("password doesn't match");
            }

            Pattern p = Pattern.compile("^\\d{10}$");
            Matcher m=p.matcher(user.getPhonenumber());
            if(!m.matches()){
                throw new Exception("Invalid mobile number");
            }

            if (!agreement) {
                throw new Exception("you have not agreed terms and conditions");

            }
            if (check) {
                session.setAttribute("message", new Message("email id already exist!!", "alert-danger"));
            } else {
                if (!referalcode.isEmpty()) {
                    Boolean valid = referalService.getValidcode(referalcode);
                    if (!valid)
                        throw new Exception("referal code is invalid");
                }
                
                User userdtls = service.creaUser(service.convertUserDtoUserDtls(user));
                if (userdtls != null && agreement) {
                    session.setAttribute("email",user.getEmail());
                    session.setAttribute("phoneNumber", user.getPhonenumber()); 
                    session.setAttribute("message", new Message("successfull registration !!", "alert-success"));
                    if (!referalcode.isEmpty()) {
                        Boolean refer = referalService.UseRefferalByCode(referalcode);
                        User referer = referalService.getUserBycode(referalcode);
                        if (!refer) {
                            throw new Exception("referal code is expired");
                        }
                        walletService.referalAmount(referer, userdtls);
                    }
                    return "redirect:/otpserve";
                }
            }
            return "redirect:/registration";

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));
        }

        return "redirect:/registration";
    }

    @PostMapping("/createSeller")
    public String createseller(@ModelAttribute UserDto user,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, HttpSession session,
            @RequestParam("confirmpassword") String password) {

        Boolean check = service.checkUserMail(user.getEmail());
        try {
            if (!password.equals(user.getPassword())) {
                throw new Exception("password doesn't match");
            }

            if (!agreement) {
                throw new Exception("you have not agreed terms and conditions");

            }
            if (check) {
                session.setAttribute("message", new Message("email id already exist!!", "alert-danger"));
            } else {
                
                User userdtls = service.creaSell(service.convertUserDtoUserDtls(user));
                if (userdtls != null && agreement) {
                    session.setAttribute("email",user.getEmail());
                    session.setAttribute("gstin",user.getGstin());
                    session.setAttribute("phoneNumber", user.getPhonenumber()); 
                    session.setAttribute("message", new Message("successfull registration !!", "alert-success"));
                    return "redirect:/otpserve";
                }
            }
            return "redirect:/sellerregistration";

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("something went wrong !!" + e.getMessage(), "alert-danger"));
        }

        return "redirect:/sellerregistration";
    }

    @GetMapping("/otpserve")
    public String otpservice(HttpSession session) {

        User users = userRepo.findByEmail((String) session.getAttribute("email"));
        session.setAttribute("users", users);
        String phoneNumber = "+91" + (String) session.getAttribute("phoneNumber");
        int otp = 1000 + randotp.nextInt(9000);
        savedotp = "" + otp;
        twilioOtpservice.sendOtp(phoneNumber, savedotp);
        return "common/otpverify";
    }

    @PostMapping("/otpverify")
    public String otpverify(@RequestParam("otp") String otp, HttpSession session) {
                
        if (savedotp.equals(otp)) {
            User user=(User)session.getAttribute("users");
            user.setOtpverified(true);
            userRepo.save(user);
            session.setAttribute("message", new Message("otp verification successfull", "alert-success"));
            return "common/otpverified";

        } else {
            session.setAttribute("message", new Message("invalid otp", "alert-danger"));

        }
        return "redirect:/otpserve";
    }
    
    
}
