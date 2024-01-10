package com.shopxy.ecom.controller;

import java.io.File;
import java.io.IOException;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.nio.file.Files;

import java. util. ArrayList;

import com.shopxy.ecom.Util.OrderStatus;
import com.shopxy.ecom.helper.Message;
import com.shopxy.ecom.model.Category;
import com.shopxy.ecom.model.Order;
import com.shopxy.ecom.model.OrderItem;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.User;
import com.shopxy.ecom.repository.ProductRepository;
import com.shopxy.ecom.repository.UserRepository;
import com.shopxy.ecom.service.CategoryService;
import com.shopxy.ecom.service.OrderService;
import com.shopxy.ecom.helper.MycustomException;
import com.shopxy.ecom.service.ProductService;
import com.shopxy.ecom.service.UserDtlsService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private ProductService proserv;

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private CategoryService catservice;

    @Autowired
    private UserDtlsService userservice;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserDtlsService userdtlsService;

    @Autowired
    private PasswordEncoder encoder;

    @ModelAttribute
    public void UserDetails(Model m, Principal p) {
        String email = p.getName();
        User user = userrepo.findByEmail(email);
        m.addAttribute("user", user);
    }

    public double totalsaleamount(){
        List<Order> items=orderService.getAllOrderByOrderDateTimeDesc();
        double totalamount=0.0;
        for(Order item:items){
            totalamount+=(item.getTotalAmount());
        }
        return totalamount;
    }
    
    @GetMapping("/")
    public String sellerhome(Model model){
        double totalamount=totalsaleamount();
        model.addAttribute("saleamount", totalamount);
        model.addAttribute("totalsale", orderService.getOrderCount());
        model.addAttribute("procount", proserv.getproductcount());
        return "seller/sellerhome";
    }

    @GetMapping("/productmanagement")
    public String productmanag(Model model){
        model.addAttribute("product", proserv.getNotDeletedProducts());
        return "seller/productmanag";
    }

    @GetMapping("/productmanagement/add")
    public String addproduct(Model m){
        List<Category> catlist = catservice.getallcategory();
        m.addAttribute("category", catlist);
        return "seller/addproduct";
    }

    @PostMapping("/creatproduct")
    public String creatProduct(@RequestParam("productname") String pname,
            @RequestParam("productdescription") String pdesc,
            @RequestParam("procat") Integer catid,
            @RequestParam("price") Double price,
            @RequestParam("imagefile") List<MultipartFile> files,
            HttpSession session ,Principal p) throws IOException {
                

        try {
            if (pname.isEmpty()) {
                throw new MycustomException("product name not added");
            } else if(pname.equals(productRepository.getpname(pname))){
                throw new MycustomException("Duplicate product Cannot be Added");
            } else if (pdesc.isEmpty()) {
                throw new MycustomException("product description not added");
            } else if (catid == 0) {
                throw new MycustomException("category not selected");
            } else if (price == null || price.isNaN()) {
                throw new MycustomException("price not added");
            } else if (price<=0){
                throw new MycustomException("Price cannot be 0 or negative");
            } else {
                Product product = new Product();
                product.setPname(pname);
                product.setUser(userservice.getUserByMail(p.getName()));
                product.setPdescription(pdesc);
                product.setPrice(price);
                // Product product = proserv.convertProductDtoProduct(products);
                product.setCategory(catservice.getcategoryByid(catid));

                List<String> imagepaths = new ArrayList<>();

                for (MultipartFile file : files) {
                    if (file.isEmpty()) {
                        throw new MycustomException("one or more files is empty");
                    }

                    String imagepath = file.getOriginalFilename();
                    imagepaths.add(imagepath);

                    File filestore = new ClassPathResource("static/img/").getFile();
                    Path path = Paths.get(filestore.getAbsolutePath() + File.separator + imagepath);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImagepath(imagepaths);
                proserv.createproduct(product);
                session.setAttribute("message", new Message("product added successfully", "alert-success"));
            }

        } catch (MycustomException e) {

            session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
        }

        return "redirect:/seller/productmanagement/add";
    }

    @GetMapping("/deleteproduct")
    public String deleteProduct(@RequestParam("productId") long id) {

        proserv.deleteproductbyid(id);

        return "redirect:/seller/productmanagement";
    }

    @GetMapping("/restoredeletedproducts")
    public String restoreDeletedProducts() {
        proserv.restoredeleted();
        return "redirect:/seller/productmanagement";
    }

    @GetMapping("/updateproduct")
    public String updateProduct(@RequestParam("productId") int id, Model model) {
        Product product = proserv.getproductbyid(id);
        model.addAttribute("category", catservice.getAllCategoryNames());
        model.addAttribute("products", product);
        return "/seller/updateproduct";
    }

    @PostMapping("/updateproduct")
    public String updateTest(@ModelAttribute Product products, @RequestParam("cate") String catname,
            @RequestParam("imagefile") List<MultipartFile> files,
            HttpSession session, @RequestParam("productId") long id) {
        try {
            // uploading file

            Product product = proserv.getproductbyid(id);
            product.setPname(products.getPname());
            product.setPdescription(products.getPdescription());
            product.setPrice(products.getPrice());
            // Product product = proserv.convertProductDtoProduct(products);
            product.setCategory(catservice.getCategorybyname(catname));

            List<String> imagepaths = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    throw new MycustomException("one or more files is empty");
                }

                String imagepath = file.getOriginalFilename();
                imagepaths.add(imagepath);

                File filestore = new ClassPathResource("static/img/").getFile();
                Path path = Paths.get(filestore.getAbsolutePath() + File.separator + imagepath);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            product.setImagepath(imagepaths);
            proserv.createproduct(product);
            session.setAttribute("message", new Message("product added successfully", "alert-success"));

        } catch (Exception e) {

            session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
        }

        return "redirect:/seller/productmanagement";
    }

    @GetMapping("/ordermanagement")
    public String getorderList(Model model) {
        List<Order> orders = orderService.getAllOrderByOrderDateTimeDesc();
        model.addAttribute("orders", orders);

        List<OrderItem> orderitems = new ArrayList<>();
        for (Order order : orders) {

            orderitems.addAll(orderService.getAllOrderItemsByOrder(order));
        }

        model.addAttribute("orderitems", orderitems);
        model.addAttribute("statuses",OrderStatus.values());

        return "seller/orders";

    }

    @PostMapping("/changeorderStatus")
    public String updateOrderStatus(@RequestParam("status")OrderStatus status,@RequestParam("orderId")Integer id,HttpSession session) {
        Order order = orderService.getOrderById(id);
        order.setStatus(status);
        orderService.saveOrder(order);
        session.setAttribute("message", new Message("order status updated successfully","alert-success"));
        return "redirect:/seller/ordermanagement";

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
    return "redirect:/seller/userprofile";
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
    return "redirect:/seller/userprofile";
  }

  @GetMapping("/sales")
    public String sales(){
        return "/seller/sales.html";
    }

}