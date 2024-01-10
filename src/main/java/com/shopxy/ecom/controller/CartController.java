package com.shopxy.ecom.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.shopxy.ecom.model.Cart;
import com.shopxy.ecom.model.CartItem;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.User;
import com.shopxy.ecom.service.ProductService;
import com.shopxy.ecom.service.ShoppingCartService;
import com.shopxy.ecom.service.UserDtlsService;

@Controller
@RequestMapping("/user")
public class CartController {

    @Autowired
    private UserDtlsService userService;


    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @ModelAttribute
    public void productdisplay(Model pro) {
        pro.addAttribute("product", productService.getallproduct());
    }

    @ModelAttribute
    public void Userdetails(Model m, Principal p) {
        String email = p.getName();
        User user = userService.getUserByMail(email); 
        m.addAttribute("user", user);

    }

    @GetMapping("/cartItems")
    public String cart(Model model, Principal principal) {
        String name = principal.getName();
        User user = userService.getUserByMail(name);
        double shippingcharges=150;
        
        // for(CartItem item:shoppingCartService.getCartItemByUserDtls(user)){
        //     totalAmount+=(item.getProduct().getPrice()*item.getQuantity());
            
        // }
        // System.out.println(totalAmount);
        Cart cart = shoppingCartService.getCartByUserId(user.getId());
        // if (cart != null) {

        //     cart.setItems(shoppingCartService.getCartItemByUserDtls(user));
        //     double totalcartamout = cart.getTotalamount();
        //     cart.setTotalamount(totalcartamout);
        //     shoppingCartService.saveCart(cart);   
        // }
        double totalAmount=cart.getTotalamount();
        double realtotal=cart.getTotalamount();
        List<CartItem> cartlist = shoppingCartService.getCartItemByUserDtls(user);
        // double totalAmount = shoppingCartService.getTotalamount(cart);
        // if(totalAmount==0){
        //     shippingcharges=0;
        // }
        double grandTotal = totalAmount+shippingcharges;
        
        model.addAttribute("cartitems", cartlist);
        model.addAttribute("totalamount", totalAmount);
        model.addAttribute("realtotal", realtotal);
        model.addAttribute("grandtotal", grandTotal);

        return "user/cart";
    }

    @PostMapping("/addtocart")
    public String getcartitems(@RequestParam("productId") Long id, Principal principal,
            @RequestParam("quantities") int quantity) {

        if (principal == null || principal.getName() == null) {
            return "redirect:/";
        }
        String userName = principal.getName();

        User user = userService.getUserByMail(userName);
        if (user == null) {
            return "redirect:/";
        }
        Cart cart = shoppingCartService.getCartByUserId(user.getId());
        if (cart == null) {
            cart = new Cart();
        }
        cart.setUserDtls(user);
        Product product = productService.getproductbyid(id);
        Boolean check = shoppingCartService.isProductExist(product);
        

        if (check) {
            System.out.println("product already exist in cart");
            CartItem cartitems = shoppingCartService.findbyUserProduct(user, product);
            cartitems.setQuantity(cartitems.getQuantity() + quantity);
            cart.setItems(shoppingCartService.getCartItemByUserDtls(user));
            shoppingCartService.saveCart(cart);
            cartitems.setCart(cart);
            shoppingCartService.saveCartItem(cartitems);
            
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserDtls(user);
            cart.setItems(shoppingCartService.getCartItemByUserDtls(user));
            shoppingCartService.saveCart(cart);
            cartItem.setCart(cart);
            shoppingCartService.saveCartItem(cartItem);
        }
        shoppingCartService.UpdateStockItem(cart);
        return "redirect:/user/cartItems";
    }

    @PostMapping("/updatecart")
    public String updatecart(@RequestParam("quantities") int quantity, @RequestParam("cartId") Long id) {

        CartItem cartItem = shoppingCartService.getCartItemById(id);
        // int stock=cartItem.getProduct().getStockQuantity()-(int)quantity;
        // Product product=cartItem.getProduct();
        // product.setStockQuantity(stock);
        // cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        shoppingCartService.saveCartItem(cartItem);

        return "redirect:/user/cartItems";
    }

    @PostMapping("/deletecart")
    public String deletecart(@RequestParam("quantities") int quantity, @RequestParam("cartId") Long id) {

        shoppingCartService.deleteCartById(id);
        // int stock=cartItem.getProduct().getStockQuantity()-(int)quantity;
        // Product product=cartItem.getProduct();
        // product.setStockQuantity(stock);
        // cartItem.setProduct(product);
        return "redirect:/user/cartItems";
    }
}
