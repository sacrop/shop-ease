package com.shop.ease.Controller;

import java.security.Principal;
import java.util.List;
import java.util.Random;

import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.ease.dto.UserDto;
import com.shop.ease.helper.Message;
import com.shop.ease.helper.MycustomException;
import com.shop.ease.model.Category;
import com.shop.ease.model.MyUserDetails;
import com.shop.ease.model.Product;
import com.shop.ease.model.ProductPriceHistory;
import com.shop.ease.service.CategoryService;
import com.shop.ease.service.ProductPriceHistoryService;
import com.shop.ease.service.ProductService;
import com.shop.ease.service.ReferalService;
import com.shop.ease.service.ReviewService;
import com.shop.ease.service.TwilioOtpservice;
import com.shop.ease.service.UserService;
import com.shop.ease.service.WalletService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    Random randotp = new Random();

    String savedotp;
    
    private TwilioOtpservice twilioOtpservice;
    private UserService userService;
    private PasswordEncoder encoder;
    private ProductService productService;
    private ReferalService referalService;
    private WalletService walletService;
    private CategoryService categoryService;
    private ProductPriceHistoryService productPriceHistoryService;
    private ReviewService reviewService;

    public HomeController(
        TwilioOtpservice twilioOtpservice,
        UserService userService,
        PasswordEncoder encoder,
        ProductService productService,
        ReferalService referalService,
        WalletService walletService,
        CategoryService categoryService,
        ProductPriceHistoryService productPriceHistoryService,
        ReviewService reviewService
    ) {
        this.twilioOtpservice = twilioOtpservice;
        this.userService = userService;
        this.encoder = encoder;
        this.productService = productService;
        this.referalService = referalService;
        this.walletService = walletService;
        this.categoryService = categoryService;
        this.productPriceHistoryService = productPriceHistoryService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Home Page");
        List<ProductPriceHistory> historyList = productPriceHistoryService.getPriceHistories();
        List<Product> productLis = productService.getallproduct();
        model.addAttribute("prohistory", historyList);
        model.addAttribute("product", productLis);
        model.addAttribute("catlist", categoryService.getallcategory());
        model.addAttribute("avgRatings", reviewService.getProductReview(productLis));
        return "index";
    }

    @GetMapping("/register")
    public String homeview(Model model) {
        model.addAttribute("pageTitle", "Registeration");
        return "register";
    }

    @GetMapping("/forgetpassword")
    public String forgetPasswd() {
        return "forgetPasswd";
    }

    @PostMapping("/resetpassword")
    public String recieveUpdatepasswd() {
        return "resetpasswd";
    }

    @PostMapping("/SuccessResetPasswd")
    public String successReset(
        Principal principal,
        @RequestParam("currentPassword") String currentPassword,
        @RequestParam("newPassword") String newPassword,
        @RequestParam("confirmPassword") String confirmPassword,
        HttpSession session,
        Model model,
        @RequestParam("email") String email) {
        MyUserDetails user = userService.getUserByMail(email);
        try {
            model.addAttribute("pageTitle", "Profile page");
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

        return "redirect:/";
    }

    // loginmapping
    @GetMapping("/signin")
    public String login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"))) {

            return "redirect:/admin/";
        } else {
            return "redirect:/user/";
        }

    }

    @PostMapping("/createUser")
    public String createuser(@ModelAttribute UserDto user,
            @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, HttpSession session,
            @RequestParam("confirmpassword") String password, @RequestParam("referralcode") String referalcode) {

        Boolean check = userService.checkUserMail(user.getEmail());
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
                if (!referalcode.isEmpty()) {
                    Boolean valid = referalService.getValidcode(referalcode);
                    if (!valid)
                        throw new Exception("referal code is invalid");
                }

                MyUserDetails userdtls = userService.createUser(userService.convertUserDtoUserDtls(user));
                if (userdtls != null && agreement) {
                    session.setAttribute("email", user.getEmail());
                    session.setAttribute("phoneNumber", user.getPhoneNumber());
                    session.setAttribute("message", new Message("successfull registration !!", "alert-success"));
                    if (!referalcode.isEmpty()) {
                        Boolean refer = referalService.UseRefferalByCode(referalcode);
                        MyUserDetails referer = referalService.getUserBycode(referalcode);
                        if (!refer) {
                            throw new Exception("referal code is expired");
                        }
                        walletService.referalAmount(referer, userdtls);
                    }
                    return "redirect:/otpserve";
                }
            }
            return "redirect:/register";

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
        }

        return "redirect:/register";
    }

    // otp form
    @GetMapping("/otpserve")
    public String otpservice(HttpSession session, Model model) {

        MyUserDetails users = userService.getUserByMail((String) session.getAttribute("email"));
        session.setAttribute("users", users);
        String phoneNumber = "+91" + "8921428919";
        int otp = 1000 + randotp.nextInt(9000);
        savedotp = "" + otp;
        twilioOtpservice.sendOtp(phoneNumber, savedotp);
        model.addAttribute("pageTitle", "Otp verification");
        return "otpverify";
    }

    @PostMapping("/otpverify")
    public String otpverify(@RequestParam("otp") String otp, HttpSession session) {

        if (savedotp.equals(otp)) {
            MyUserDetails user = (MyUserDetails) session.getAttribute("users");
            user.setOtpverified(true);
            userService.saveUser(user);
            session.setAttribute("message", new Message("otp verification successfull", "alert-success"));
            return "otpverified";

        } else {
            session.setAttribute("message", new Message("invalid otp", "alert-danger"));

        }
        return "redirect:/otpserve";
    }

    @GetMapping("/productdescription/{id}")
    public String productdescription(@PathVariable("id") int id, Model model) {

        Product product = productService.getproductbyid((long) id);
        model.addAttribute("products", product);
        model.addAttribute("pageTitle", "product page");
        model.addAttribute("Categories", categoryService.getallcategory());
        List<Product> productlist = productService.getallproduct();
        model.addAttribute("productlist", productlist);

        return "productDescription";
    }

    // product search and filter
    @GetMapping("/searchProduct")
    public String SearchProductlist(@RequestParam(value = "searchQuery", defaultValue = "") String name, Model model) {
        model.addAttribute("pageTitle", "Products");
        model.addAttribute("pageTitle", "Product Page");
        List<Product> productlist = productService.getProductsBypname(name);
        model.addAttribute("Categories", categoryService.getallcategory());
        model.addAttribute("product", productlist);
        return "searchDisplay";
    }

    @GetMapping("/productlist")
    public String getproductlist(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Product> productPage = productService.getProduct(page, 9);
        model.addAttribute("pageTitle", "Products");
        model.addAttribute("pageTitle", "Product Page");
        model.addAttribute("product", productPage);
        model.addAttribute("Categories", categoryService.getallcategory());
        return "productDisplay";
    }

    @GetMapping("/productlist/{id}")
    public String getCategoriesedProduct(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("Categories", categoryService.getallcategory());
        Category category = categoryService.getCategoryById(id);
        List<Product> productList = category.getProduct();
        model.addAttribute("pageTitle", "Products");
        model.addAttribute("product", productList);
        return "searchDisplay";
    }
}
