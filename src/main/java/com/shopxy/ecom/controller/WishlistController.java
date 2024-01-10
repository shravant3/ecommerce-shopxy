package com.shopxy.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shopxy.ecom.helper.Message;
import com.shopxy.ecom.helper.MycustomException;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.User;
import com.shopxy.ecom.service.UserDtlsService;
import com.shopxy.ecom.service.WishlistService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/user/wishlist")
public class WishlistController {

    @Autowired
    private UserDtlsService userService;

    @Autowired
    private WishlistService wishlistService;

    @ModelAttribute
    public void Userdetails(Model m, Principal p) {

        String email = p.getName();
        User user = userService.getUserByMail(email);
        m.addAttribute("user", user);

    }

    @GetMapping("")
    public String getWishlist(Model model, Principal principal) {
        User user = userService.getUserByMail(principal.getName());
        List<Product> productList = wishlistService.getAllProductFromWishList(user);
        model.addAttribute("productList", productList);
        return "user/wishlist";

    }

    @GetMapping("/add/{id}")
    public String addToWishList(@PathVariable("id") Long id, Principal principal, HttpSession session) {
        try {
            User user = userService.getUserByMail(principal.getName());
            wishlistService.addToWishList(user, id);
            session.setAttribute("message", new Message("product added to wishlist", "alert-success"));
            return "redirect:/user/";
        } catch (MycustomException e) {
            session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
        }
        return "redirect:/user/";
    }

    @GetMapping("/remove/{id}")
    public String removefromWishlist(@PathVariable("id") Long id, Principal principal) {
        User user = userService.getUserByMail(principal.getName());
        wishlistService.removeProduct(user, id);
        return "redirect:/user/wishlist";
    }

}
