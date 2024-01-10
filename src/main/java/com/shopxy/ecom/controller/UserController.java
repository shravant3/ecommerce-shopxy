package com.shopxy.ecom.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.RazorpayClient;
import com.shopxy.ecom.Util.CouponStatus;
import com.shopxy.ecom.Util.OrderStatus;
import com.shopxy.ecom.Util.WalletTransactionStatus;
import com.shopxy.ecom.helper.Message;
import com.shopxy.ecom.helper.MycustomException;
import com.shopxy.ecom.model.Address;
import com.shopxy.ecom.model.Cart;
import com.shopxy.ecom.model.CartItem;
import com.shopxy.ecom.model.Category;
import com.shopxy.ecom.model.Country;
import com.shopxy.ecom.model.Coupon;
import com.shopxy.ecom.model.Order;
import com.shopxy.ecom.model.OrderItem;
import com.shopxy.ecom.model.Payment;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.Refferal;
import com.shopxy.ecom.model.User;
import com.shopxy.ecom.model.Wallet;
import com.shopxy.ecom.model.WalletHistory;
import com.shopxy.ecom.repository.OrderItemRepository;
import com.shopxy.ecom.service.AddressService;
import com.shopxy.ecom.service.CategoryService;
import com.shopxy.ecom.service.CountryService;
import com.shopxy.ecom.service.CouponService;
import com.shopxy.ecom.service.OrderService;
import com.shopxy.ecom.service.PaymentService;
import com.shopxy.ecom.service.ProductService;
import com.shopxy.ecom.service.ReferalService;
import com.shopxy.ecom.service.ShoppingCartService;
import com.shopxy.ecom.service.UserDtlsService;
import com.shopxy.ecom.service.WalletService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserDtlsService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private CategoryService catservice;

    @Autowired
    private ReferalService referalService;

    DecimalFormat df = new DecimalFormat("#.00");
    
    @GetMapping("/")
    public String userhome(Model model){
      model.addAttribute("pageTitle", "Home Page");
      model.addAttribute("product", productService.getallproduct());
      model.addAttribute("Categories", catservice.getallcategory());
        return "user/userhome";
    }

    @ModelAttribute
    public void Userdetails(Model m, Principal p) {

    String email = p.getName();
    User user = userService.getUserByMail(email);
    m.addAttribute("user", user);

    }

    @GetMapping("/productdescription/{id}")
    public String productdescription(@PathVariable("id") Long id, Model model) {

      Product product = productService.getproductbyid(id);
      model.addAttribute("products", product);
    //  ProductPriceHistory productPriceHistory= ProductHistoryService.getOriginalPriceOfProduct(product);
    //  model.addAttribute("productPriceHistory",productPriceHistory);

      List<Product> productlist = productService.getallproduct();
      model.addAttribute("productlist", productlist);
      return "user/productdescription";
    }


    @PostMapping("/buynow")
    public String buyNow1(Principal principal, @RequestParam("productId") Long id,
        @RequestParam("quantities") int quantity, Model m) {
      Product product = productService.getproductbyid(id);
      int stock = product.getStockQuantity() - quantity;
      String email = principal.getName();
      User user = userService.getUserByMail(email);
      m.addAttribute("stock", stock);
      m.addAttribute("user", user);
      m.addAttribute("product", product);
      m.addAttribute("quantity", quantity);
      m.addAttribute("totalprice", (product.getPrice() * quantity) + 150);
      m.addAttribute("deliverycharge", 150.0);
      List<Address> addresslist = addressService.getAlladdress();
      m.addAttribute("addresslist", addresslist);
      return "/user/buynow";
  
    }


  
  @GetMapping("/userprofile")
  public String userProfile(Model model, Principal principal) {
    User user = userService.getUserByMail(principal.getName());
    model.addAttribute("users", user);
    return "common/userprofile";
  }


  @PostMapping("/updateprofile")
  public String updateProfile(Principal principal, @RequestParam("name") String usename,
      @RequestParam("email") String email, @RequestParam("phonenumber") String phonenumber,
      HttpSession session) throws IOException {
    User principalUser = userService.getUserByMail(principal.getName());
    principalUser.setUsename(usename);
    principalUser.setEmail(email);
    principalUser.setPhonenumber(phonenumber);
    // System.out.println(file);
    
    // if (!file.isEmpty()) {
    //   String imagepath = file.getOriginalFilename();
    //   File filestore = new ClassPathResource("static/img/").getFile();
    //   Path path = Paths.get(filestore.getAbsolutePath() + File.separator + imagepath);
    //   Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    //   principalUser.setImagepath(imagepath);
    // }
    userService.saveUser(principalUser);

    session.setAttribute("message", new Message("updated successfully", "alert-success"));
    return "redirect:/user/userprofile";
  }

  @PostMapping("/updatepassword")
  public String updatePassword(Principal principal, @RequestParam("currentPassword") String currentPassword,
      @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
      HttpSession session) {
    User user = userService.getUserByMail(principal.getName());
    try {
      Boolean equality = confirmPassword.equals(newPassword);
      Boolean success = encoder.matches(currentPassword, user.getPassword());
      if (!success) {
        throw new MycustomException("enter correct Password");
      }
      if (!equality) {
        throw new MycustomException("newly entered password doesn't match");
      }
      user.setPassword(encoder.encode(newPassword));
      userService.saveUser(user);
      session.setAttribute("message", new Message("password updated successfully", "alert-success"));

    } catch (MycustomException e) {

      session.setAttribute("message", new Message(e.getMessage() + "", "alert-danger"));

    }
    return "redirect:/user/userprofile";
  }

  @GetMapping("/addaddress")
  public String addAddress(Model m){
    List<Country> countlist = countryService.getallCountries();
    m.addAttribute("country", countlist);
    return "user/addaddress";
  }

  @PostMapping("/addaddress")
  public String addAddress(@RequestParam("fullname") String fname,
  @RequestParam("phonenumber") String phone,@RequestParam("buildingnumber") String bnumber,
  @RequestParam("street") String street, @RequestParam("city") String city,
  @RequestParam("landmark")String landmark, @RequestParam("state") String state , @RequestParam("pincode") String pincode,
  @RequestParam("country") Integer counid,
  HttpSession session, Principal p)throws IOException{
    try{
      if(fname.isEmpty()) {
        throw new MycustomException("name not added");
      }else if(bnumber.isEmpty()){
        throw new MycustomException("building number not added");
      }else if(city.isEmpty()){
        throw new MycustomException("city not added");
      }else if(street.isEmpty()){
        throw new MycustomException("street not added");
      }else if(pincode.isEmpty()){
        throw new MycustomException("pincode not added");
      }else if(state.isEmpty()){
        throw new MycustomException("state not added");
      }else if(counid==0){
        throw new MycustomException("country not added");
      }else{
        Address address = new Address();
        address.setFullName(fname);
        address.setMobilePhoneNumber(phone);
        address.setBuildingNo(bnumber);
        address.setStreet(street);
        address.setCity(city);
        address.setLandmark(landmark);
        address.setState(state);
        address.setPinCode(pincode);
        address.setCountry(countryService.getcountryByid(counid));
        address.setUserDtls(userService.getUserByMail(p.getName()));
        addressService.saveAddress(address);
      }
      
    }
    catch (MycustomException e) {

      session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
  }

    return "redirect:/user/viewaddress";
  }

  @GetMapping("/viewaddress")
  public String viewaddress(Model model){
    List<Address> addresslist = addressService.getNonDeletedAddress();
    model.addAttribute("addresslist", addresslist);
    return "user/viewaddress";
  }

  @GetMapping("/deleteAddress/{id}")
  public String deleteAddress(@PathVariable("id") Long id) {
  addressService.deleteById(id);
    return "redirect:/user/viewaddress";
  }

  @PostMapping("/create_order")
  @ResponseBody
  public String createOrder(@RequestBody Map<String, Object> data, Principal principal) {
    try {
      double amount = Double.parseDouble(data.get("amount").toString());
      var client = new RazorpayClient("rzp_test_uaxhcNM7pjJmAt", "kn0bFvUAcvkuWpSfYWuJp7LV");
      JSONObject orderRequest = new JSONObject();
      orderRequest.put("amount", amount * 100);
      orderRequest.put("currency", "INR");
      orderRequest.put("receipt", "txn_123435");
      // creating new order

      com.razorpay.Order newOrder = client.orders.create(orderRequest);

      // save the order in database
      Payment myOrder = new Payment();
      myOrder.setAmount(newOrder.get("amount") + "");
      myOrder.setOrderId(newOrder.get("id"));
      myOrder.setPaymentId("rayzorpay");
      myOrder.setPaymentStatus("created");
      myOrder.setUser(userService.getUserByMail(principal.getName()));
      myOrder.setReceipt(newOrder.get("receipt"));
      paymentService.savePaymentOrder(myOrder);
      return newOrder.toString();
    } catch (Exception e) {
      e.printStackTrace(); // Log the exception
      return "Error occurred: " + e.getMessage();
      // TODO: handle exception
    }

  }

  @PostMapping("/checkoutall")
  public String getallcartItemsCheckout(Model model, Principal principal) {
    String userName = principal.getName();

    User user = userService.getUserByMail(userName);
    Cart cart = shoppingCartService.getCartByUserId(user.getId());
    List<CartItem> cartitemslist = cart.getItems();

    model.addAttribute("cartItems", cartitemslist);
    // System.out.println(cart.getTotalamount());
    model.addAttribute("boforeprice", cart.getTotalamount()+150);
    model.addAttribute("totalprice", cart.getTotalamount()+150);

    // System.out.println(cartitemslist);
    List<Address> addresslist = addressService.getAlladdress();
    model.addAttribute("wallets", walletService.getWalletByUser(user));
    model.addAttribute("addresslist", addresslist);
    return "/user/checkoutall";
  }

  

  @PostMapping("/orderproduct")
  public String orderProduct(Model model, @RequestParam("addressId") Long addressid, Principal principal,
      @RequestParam("payment") String payment, @RequestParam("quantity") int quantity,
      @RequestParam("totalprice") double price, @RequestParam("productId") Long productid) {

    String username = principal.getName();
    User user = userService.getUserByMail(username);
    orderService.createSingleorder(addressid, price, user, payment, quantity, productid);
    return "/user/ordersuccess";
  }

  @GetMapping("/orders")
  public String getOrders(Model model, Principal principal) {
    String username = principal.getName();
    User user = userService.getUserByMail(username);
    List<Order> orders = orderService.getOrderByUser(user);
    model.addAttribute("orders", orders);
    List<OrderItem> orderitems = new ArrayList<>();
    for (Order order : orders) {
      orderitems.addAll(orderService.getAllOrderItemsByOrder(order));
    }
    model.addAttribute("orderitems", orderitems);
    model.addAttribute("orderstatuses",OrderStatus.CANCELED);

    return "/user/orders";

  }

  @GetMapping("/cancelOrder/{id}")
  public String deleteOrder(@PathVariable("id") int orderId,HttpSession session,Principal principal) {
    Order order = orderService.getOrderById(orderId);
    User user = userService.getUserByMail(principal.getName());
    if ("rayzorpay".equals(order.getPaymentMethod())) {
      walletService.AddToWallet(order.getTotalAmount(), user);

    }
    orderService.cancelOrder(orderId);
    session.setAttribute("message", new Message("order item cancelled", "alert-success"));
    return "redirect:/user/orders";
  }

  @GetMapping("/viewOrder/{id}")
  public String viewOrder(Model model,@PathVariable("id") int orderId,HttpSession session){
    Order order=orderService.getOrderById(orderId);
    Address address=order.getShippingAddress();
    String payment=order.getPaymentMethod();
    String date=order.getFormattedOrderDate();
    List<OrderItem> items =orderService.getAllOrderItemsByOrder(order);
    Double total=order.getTotalAmount();
    System.out.println(items);
    model.addAttribute("address",address);
    model.addAttribute("payment",payment);
    model.addAttribute("date",date);
    model.addAttribute("items", items);
    model.addAttribute("total",total);
    return "/user/vieworder";
  }
  
  @GetMapping("/search")
    public String searching(@RequestParam("searchQuery") String name, Model model) {
        model.addAttribute("product", productService.getproductbypname(name));
        return "/user/userhome";
    }

    @GetMapping("/sortedByprice")
    public String Sort(Model model){
      List<Product> products = productService.getallproduct();
      model.addAttribute("product", productService.getProductsSortedByPrice(products));
      return "/user/userhome";
    }

    

    @GetMapping("/productlist/{id}")
public String getCategoriesedProduct(
        @PathVariable("id") Integer id,
        Model model
) {

    Category category = catservice.getcategoryByid(id);

    if (category == null) {
        // Handle the case when category is null, maybe return an error page or redirect
        // For now, let's return an error message in the model
        model.addAttribute("errorMessage", "Category not found");
        return "error"; // create an error.html template for displaying errors
    }

    model.addAttribute("Categories", catservice.getallcategory());
    model.addAttribute("pageTitle", "Product Page");

    List<Product> productList = category.getProduct();

    // Apply sorting based on the 'sort' parameter
    // if ("lowToHigh".equals(sort)) {
    //     productList.sort(Comparator.comparing(Product::getPrice));
    // } else if ("highToLow".equals(sort)) {
    //     productList.sort(Comparator.comparing(Product::getPrice).reversed());
    // }

    model.addAttribute("product", productList);
    return "/user/userhome";
}




  @PostMapping("/applyCoupon")
  @ResponseBody
  public String applyCoupon(@RequestBody Map<String, Object> data, Principal principal, HttpSession session) {
    try {
      System.out.println(data + "NO DATA IS RECIEVED");
      String code = data.get("name").toString();
      Double amount = Double.parseDouble(data.get("totalprice").toString());
      Coupon coupon = couponService.findByCouponCode(code);
      User user = userService.getUserByMail(principal.getName());
      Boolean usage = couponService.checkCouponUsage(user, code);
      if (usage) {
        throw new MycustomException("coupon already used");
      }
      // amount=amount
      if (coupon == null) {
        throw new MycustomException("Invalid coupon code");
      }
      if (coupon.getStatus() != CouponStatus.ACTIVE) {
        throw new MycustomException("Coupon is inactive");
      }
      Double discount = coupon.getDiscountPercentage();
      amount = (amount * discount) / 100;
      session.setAttribute("appliedCoupon", coupon);
      session.setAttribute("discount", discount);
      // couponService.updateCouponUsage(user, coupon);

      return amount.toString();
    } catch (MycustomException e) {

      // TODO: handle exception
      return "Error occurred: " + e.getMessage();
    }

  }

  @PostMapping("/removeCoupon")
@ResponseBody
public String removeCoupon(@RequestBody Map<String, Object> data, HttpSession session, Principal principal) {
    try {
        Double amount = Double.parseDouble(data.get("totalprice").toString());
        System.out.println("dgfsdf          "+amount);
        Coupon appliedCoupon = (Coupon) session.getAttribute("appliedCoupon");
        
        if (appliedCoupon == null) {
            throw new MycustomException("No coupon applied");
        }
        
        Double discount = appliedCoupon.getDiscountPercentage();
        
        // Corrected formula to calculate the original amount
        amount = (amount * 100) / discount;
        
        // Remove the applied coupon from the session
        session.removeAttribute("appliedCoupon");
        
        return amount.toString();
    } catch (MycustomException e) {
        return "Error occurred: " + e.getMessage();
    }
}

  @PostMapping("/orderall")
  public String ordrall(Model model, @RequestParam("addressId") Long addressid,
      @RequestParam(value = "payment", defaultValue = "walletpayment") String payment,
      @RequestParam("cartprice") Double price, Principal principal,
      @RequestParam(value = "useWallet", defaultValue = "") String wallet, HttpSession session) {
    String userName = principal.getName();
    User user = userService.getUserByMail(userName);
    double prices = 0.0;
    if (!wallet.isEmpty()) {
      Wallet wallets = walletService.getWalletByUser(user);
      if (payment.equals("walletpayment")) {
        prices = (price > wallets.getCurrentBalance()) ? (price - wallets.getCurrentBalance())
            : (wallets.getCurrentBalance() - price);
        prices = Double.parseDouble(df.format(prices));
        wallets.setCurrentBalance(prices);

      } else if (payment.equals("rayzorpay")) {
        wallets.setCurrentBalance(0.0);
      }
      walletService.saveWallet(wallets);
      walletService.debitedTransaction(prices, wallets);
    }
    orderService.createOrder(user, addressid, payment, price);
    Coupon appliedCoupon = (Coupon) session.getAttribute("appliedCoupon");
    couponService.updateCouponUsage(user, appliedCoupon);
    model.addAttribute("pageTitle", "Order page");
    return "user/ordersuccess";
  }

  @GetMapping("/wallet")
  public String getWallet(Model model, Principal principal) {
    User user = userService.getUserByMail(principal.getName());
    Wallet wallets = walletService.getWalletByUser(user);
    model.addAttribute("pageTitle", "Wallet page");
    model.addAttribute("wallets", wallets);
    List<WalletHistory> wallethistorylist = walletService.findAllHistory(wallets);
    List<WalletHistory> sortedList = wallethistorylist.stream()
        .sorted(Comparator.comparing(WalletHistory::getTransactionTime).reversed())
        .collect(Collectors.toList());
    List<WalletTransactionStatus> walletstatus = new ArrayList<>();
    walletstatus.addAll(Arrays.asList(WalletTransactionStatus.values()));
    model.addAttribute("walletstatuses", walletstatus);
    model.addAttribute("wallethistorys", sortedList);
    return "user/wallet";
  }
  
  @GetMapping("/referEarn")
  public String referEarn(Model model) {
    model.addAttribute("pageTitle", "Referal page");
    return "user/refer";
  }

  @PostMapping("/showReferal")
  public String showReferal(Principal principal, Model model) {
    User user = userService.getUserByMail(principal.getName());
    Refferal referal = referalService.findByUser(user);
    if (referal == null) {
      referalService.CreateReferral(user);
    }
    model.addAttribute("referrals", referal);
    model.addAttribute("pageTitle", "Referal page");
    return "user/refer";
  }

}
