package com.shopxy.ecom.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shopxy.ecom.Util.CouponStatus;
import com.shopxy.ecom.dto.CouponDto;
import com.shopxy.ecom.helper.Message;
import com.shopxy.ecom.helper.MycustomException;
import com.shopxy.ecom.model.Category;
import com.shopxy.ecom.model.Coupon;
import com.shopxy.ecom.model.Offer;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.User;
import com.shopxy.ecom.repository.CategoryRepository;
import com.shopxy.ecom.repository.UserRepository;
import com.shopxy.ecom.service.CategoryService;
import com.shopxy.ecom.service.CouponService;
import com.shopxy.ecom.service.OfferService;
import com.shopxy.ecom.service.OrderService;
import com.shopxy.ecom.service.ProductService;
import com.shopxy.ecom.service.UserDtlsService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CategoryService catservice;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ProductService proserv;

    @Autowired
    private UserDtlsService userdtlsService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private OrderService orderService;

    @ModelAttribute
    public void UserDetails(Model m, Principal p) {
        String email = p.getName();
        User user = userrepo.findByEmail(email);
        m.addAttribute("user", user);
    }

    
    @GetMapping("/")
    public String adminhome(Model model){
        int count = userdtlsService.getusercount() - (userdtlsService.getadmincount() - userdtlsService.getsellercount());
        model.addAttribute("totalsale", orderService.getOrderCount());
        model.addAttribute("procount", proserv.getproductcount());
        model.addAttribute("usercount", count);
        return "admin/adminhome";
    }

    @GetMapping("/a")
    public String adminhomea(Model model){
        int count = userdtlsService.getusercount() - (userdtlsService.getadmincount() - userdtlsService.getsellercount());
        model.addAttribute("totalsale", orderService.getOrderCount());
        model.addAttribute("procount", proserv.getproductcount());
        model.addAttribute("usercount", count);
        return "admin/adminhomea";
    }

    @GetMapping("/b")
    public String adminhomeb(Model model){
        int count = userdtlsService.getusercount() - (userdtlsService.getadmincount() - userdtlsService.getsellercount());
        model.addAttribute("totalsale", orderService.getOrderCount());
        model.addAttribute("procount", proserv.getproductcount());
        model.addAttribute("usercount", count);
        return "admin/adminhomeb";
    }

    @GetMapping("/categorymanagement")
    public String categorymanag(Model model){
        model.addAttribute("categories", catservice.getNotdeletedCategories());
        return "admin/categorymanag";
    }

    @PostMapping("/categorymanagement")
    public String postcatadd(@ModelAttribute("category")Category category,
    @RequestParam("catname") String catname, HttpSession session){
      try {
        if (catname.equals(categoryRepository.getcatname(catname))) {
            throw new MycustomException("Duplicate Category Cannot be Added");
        }   else {
            catservice.createcat(category);
            }
          } catch (MycustomException e) {

            session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
        }
        return "redirect:/admin/categorymanagement";
    }

    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam("catid") int id) {
        Category category = catservice.getcategoryByid(id);

        // category.removeProduct();
        // category.removeSubcategory();

        // System.out.println(category.getProduct());
        catservice.deleteCategoryByid(id);
        return "redirect:/admin/categorymanagement";
    }

    @GetMapping("/restoredeleted")
    public String restoreDeleted() {
        List<Category> cate = catservice.getdeletedCategories(true);
        if (cate != null) {
            for (Category cat : cate) {
                cat.setDeleted(false);
                catservice.save(cat);

            }
        }

        return "redirect:/admin/categorymanagement";
    }

    //user management

    @GetMapping("/usermanagement")
    public String manageCustomer(Model model) {
        // model.addAttribute("user", userdtlsService.getalluser());
        List<User> tbuser=new ArrayList<>(userdtlsService.getalluser());
        model.addAttribute("tuser", tbuser);

        return "admin/manageuser";
    }

    @GetMapping("/search")
    public String searching(@RequestParam("searchQuery") String name, Model model) {
        model.addAttribute("tuser", userdtlsService.finduser(name));
        return "admin/manageuser";
    }

    @PostMapping("/updatecust")
    public String updateCustomer(@RequestParam(value = "lock", defaultValue = "false") Boolean locked,
            @RequestParam("userId") int id) {
        userdtlsService.changelock(id, locked);
        System.out.println(locked);
        return "redirect:/admin/usermanagement";
    }

    @GetMapping("/userprofile")
    public String userProfile(Model model, Principal principal) {
    User user = userdtlsService.getUserByMail(principal.getName());
    model.addAttribute("users", user);
    return "common/userprofile";
  }

  @PostMapping("/updateprofile")
  public String updateProfile(Principal principal, @RequestParam("name") String usename,
      @RequestParam("email") String email, @RequestParam("phonenumber") String phonenumber,
      HttpSession session) throws IOException {
    User principalUser = userdtlsService.getUserByMail(principal.getName());
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
    userdtlsService.saveUser(principalUser);

    session.setAttribute("message", new Message("updated successfully", "alert-success"));
    return "redirect:/admin/userprofile";
  }

  @PostMapping("/updatepassword")
  public String updatePassword(Principal principal, @RequestParam("currentPassword") String currentPassword,
      @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
      HttpSession session) {
    User user = userdtlsService.getUserByMail(principal.getName());
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
      userdtlsService.saveUser(user);
      session.setAttribute("message", new Message("password updated successfully", "alert-success"));

    } catch (MycustomException e) {

      session.setAttribute("message", new Message(e.getMessage() + "", "alert-danger"));

    }
    return "redirect:/admin/userprofile";
  }

  @GetMapping("/managecoupons/add")
    public String CouponAdd(Model model) {
        model.addAttribute("coupons", new CouponDto());
        
        return "/admin/addcoupon";
    }

    @PostMapping("/createCoupon")
    public String createCoupon( CouponDto coupondto,HttpSession session){
        try {
            if(coupondto==null){
                throw new MycustomException("Enter the coupon details properly");
            }
            Coupon coupon=couponService.convertCouponDto(coupondto);
            if(couponService.checkExistCode(coupon.getCode())){
                throw new MycustomException("coupon already exist");
            }
            coupon.setStatus(CouponStatus.ACTIVE);
            couponService.saveCoupon(coupon);
             session.setAttribute("message", new Message("Coupon created successfully","alert-success"));
                
            
        } catch (MycustomException e) {
            // TODO: handle exception
            session.setAttribute("message", new Message(e.getMessage()+"","alert-danger"));
        }
        
        return "redirect:/admin/managecoupons/add";
       
    }

    @GetMapping("/managecoupons")
    public String editCoupon(Model model)
    {
        List<Coupon> coupons=couponService.getAllCoupons();
        model.addAttribute("coupons",coupons);
        return "/admin/managecoupons";
    }

    @PostMapping("/removecoupons")
    public String removeCoupon(@RequestParam("CouponCode")String code,Model model)
    {
        couponService.deleteCouponByCode(code);
        return "redirect:/admin/managecoupons";
    }

    @GetMapping("/updateCoupon")
    public String updateCoupon(@RequestParam(value = "CouponCode",required = false) String code, Model model){
        Coupon coupon=couponService.findByCouponCode(code);
        model.addAttribute("coupons", coupon);
        return "/admin/updatecoupon";
    }

    @PostMapping("/updateCoupon")
    public String updateTest(Model model,@RequestParam(value = "CouponCode",required = false) String code,
    @RequestParam("newcode") String newcode, @RequestParam("discountPercentage") Double discount,
    @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
        Coupon coupon=couponService.findByCouponCode(code);
        coupon.setCode(newcode);
        coupon.setDiscountPercentage(discount);
        coupon.setStartDate(startDate);
        coupon.setEndDate(endDate);
        couponService.saveCoupon(coupon);
        return "redirect:/admin/managecoupons";
    }



    @GetMapping("/manageoffer/add")
    private String addOffer(Model model){
        model.addAttribute("categories", catservice.getallcategory());
        return "/admin/addoffer";
    }

    @PostMapping("/createOffer")
    public String createOffer(@RequestParam("paracat")String catname,@RequestParam("discount")double discount,HttpSession session){
        Category category=catservice.getCategorybyname(catname);
        Boolean check=offerService.findExistOffer(category);
        if(check){
            session.setAttribute("message", new Message("offer already set for category","alert-danger"));
            return "redirect:/admin/manageoffer/add";
        }
        offerService.CreateCategoryOffer(discount, category);
            session.setAttribute("message", new Message("offer added successfully","alert-success"));
       
        return "redirect:/admin/manageoffer/add";
    }

    @GetMapping("/manageoffer")
    private String showOffer(Model model){
        List<Offer> offerlist=offerService.getAllOffer();
        model.addAttribute("offers", offerlist);
        return "/admin/manageoffer";
    }

    @PostMapping("/removeOffer")
    private String deleteOffer(@RequestParam("offerid")int id){
        offerService.deleteOffer(id);
        return "redirect:/admin/manageoffer";
    }

}
