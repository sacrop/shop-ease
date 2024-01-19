package com.shop.ease.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.shop.ease.Repository.UserRepository;
import com.shop.ease.Util.CouponStatus;
import com.shop.ease.Util.OrderStatus;
import com.shop.ease.dto.CategoryDto;
import com.shop.ease.dto.CouponDto;
import com.shop.ease.helper.Message;
import com.shop.ease.helper.MycustomException;
import com.shop.ease.helper.SalesData;
import com.shop.ease.model.Category;
import com.shop.ease.model.Coupon;
import com.shop.ease.model.Offer;
import com.shop.ease.model.Order;
import com.shop.ease.model.OrderItem;
import com.shop.ease.model.Product;
import com.shop.ease.model.ProductOffer;
import com.shop.ease.model.Subcategory;
import com.shop.ease.model.UserDtls;

import com.shop.ease.service.Catservice;
import com.shop.ease.service.CouponService;
import com.shop.ease.service.OfferService;
import com.shop.ease.service.OrderService;
import com.shop.ease.service.ProductPriceHistoryService;
import com.shop.ease.service.ProductService;
import com.shop.ease.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userrepo;

    @Autowired
    private Catservice catservice;

    @Autowired
    private ProductService proserv;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private ProductPriceHistoryService productPriceHistoryService;

    DecimalFormat df = new DecimalFormat("#.##");

    @ModelAttribute
    public void UserDetails(Model m, Principal p) {
        String email = p.getName();
        UserDtls user = userrepo.findByEmail(email);
        m.addAttribute("user", user);

    }

    @GetMapping("/")
    public String home(Model model) {
        // admin dashboard
        int count = userService.getusercount() - userService.getadmincount();
        model.addAttribute("procount", proserv.getproductcount());
        model.addAttribute("usercount", count);
        model.addAttribute("totalSale", orderService.getTotalSale());
        model.addAttribute("pageTitle", "Admin home");
        return "admin/adminbase";
    }

    @GetMapping("/profile")
    public String getAdminProfile(Model model,Principal principal) {
        model.addAttribute("pageTitle", "Profile page");
    UserDtls user = userService.getUserByMail(principal.getName());
    model.addAttribute("users", user);
        return "admin/adminProfile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(Principal principal, @RequestParam("name") String usename,
            @RequestParam("email") String email, @RequestParam("phonenumber") String phonenumber, HttpSession session,
            Model model) throws IOException {
        UserDtls principalUser = userService.getUserByMail(principal.getName());
        principalUser.setUsename(usename);
        model.addAttribute("pageTitle", "Profile page");
        principalUser.setEmail(email);
        principalUser.setPhonenumber(phonenumber);
        userService.saveUser(principalUser);
        session.setAttribute("message", new Message("updated successfully", "alert-success"));
        return "redirect:/admin/profile";
    }

    @PostMapping("/updatepassword")
    public String updatePassword(Principal principal, @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
            HttpSession session, Model model) {
        UserDtls user = userService.getUserByMail(principal.getName());
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
        return "redirect:/admin/profile";
    }

    // search customer in manage customer page
    @GetMapping("/search")
    public String searching(@RequestParam("searchQuery") String name, Model model) {
        model.addAttribute("user", userService.finduser(name));
        model.addAttribute("pageTitle", "User management");
        return "admin/managecustomer";
    }

    @GetMapping("/managecust")
    public String manageCustomer(Model model) {
        model.addAttribute("user", userService.getalluser());
        model.addAttribute("pageTitle", "User management");
        return "admin/managecustomer";
    }

    @PostMapping("/updatecust")
    public String updateCustomer(@RequestParam(value = "lock", defaultValue = "false") Boolean locked, Model model,
            @RequestParam("userId") int id) {
        userService.changelock(id, locked);

        return "redirect:/admin/managecust";
    }

    // creating and adding category
    @GetMapping("/addcategory")
    public String addCategory(Model model) {
        model.addAttribute("pageTitle", "Add category");
        return "admin/add_category";
    }

    @PostMapping("/createcat")
    public String createCategory(@ModelAttribute CategoryDto cat, @RequestParam("imagefile") MultipartFile files,
            @RequestParam("iconClass") String icon,
            HttpSession session, Model model) throws IOException {
        try {
            model.addAttribute("pageTitle", "Add category");
            if (icon.isEmpty()) {
                throw new MycustomException("icon class is not filled");
            }

            if (cat.getCatname().isEmpty()) {
                throw new MycustomException("Category field cannot be empty");
            } else {
                Boolean check = catservice.existsByCatname(cat.getCatname());
                if (check) {
                    session.setAttribute("message", new Message("category already exist", "alert-danger"));
                } else {
                    String imagepath = files.getOriginalFilename();
                    File filestore = new File("src/main/resources/static/img/");
                    if (!filestore.exists()) {
                        filestore.mkdirs();
                    }
                    Path path = Paths.get(filestore.getAbsolutePath() + File.separator + imagepath);
                    Files.copy(files.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    Category category = catservice.convertCategoryDtotCategory(cat);
                    category.setImagePath(imagepath);
                    category.setIconClass(icon);
                    catservice.createcat(category);
                    session.setAttribute("message", new Message("category created successfully", "alert-success"));
                }
            }

        } catch (MycustomException e) {
            session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
        }

        return "redirect:/admin/addcategory";
    }

    // creating and adding subcategory
    @GetMapping("/addsubcategory")
    public String addSubcategory(Model model) {
        model.addAttribute("pageTitle", "Category page");
        List<String> list = new ArrayList<>();
        list.addAll(catservice.getAllCategoryNames());
        model.addAttribute("categories", list);
        return "admin/add_subcategory";
    }

    @PostMapping("/createsubcat")
    public String createSubcategory(@ModelAttribute Subcategory subcat, @RequestParam("paracat") String catname,
            HttpSession session, Model model) {
        // Boolean check = catservice.existsBySubcatname(subcat.getSubcatname());
        Boolean verify = catservice.existByCatnameAndSubcatname(subcat.getSubcatname(), catname);
        model.addAttribute("pageTitle", "Subcategory page");
        try {
            if (catname.isEmpty()) {
                throw new MycustomException("select one category");
            }
            if (subcat.getSubcatname().isEmpty()) {
                throw new MycustomException("enter a subcategory name");
            }
            if (verify) {
                throw new MycustomException("Subcategory already exist");
            } else {
                subcat.setCategory(catservice.getCategorybyname(catname));
                catservice.createsubcat(subcat);
                session.setAttribute("message", new Message("Subcategory created successfully", "alert-success"));
            }
        } catch (MycustomException e) {
            session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
        }
        return "redirect:/admin/addsubcategory";
    }

    @GetMapping("/editcategory")
    public String editCategory(Model model) {
        model.addAttribute("categories", catservice.getNotdeletedCategories());
        model.addAttribute("pageTitle", "Subcategory page");

        return "admin/editcategory";
    }

    @PostMapping("/deleteCategory")
    public String deleteCategory(@RequestParam("catid") int id, Model model) {
        model.addAttribute("pageTitle", "Subcategory page");
        catservice.deleteCategoryByid(id);
        return "redirect:/admin/editcategory";
    }

    @GetMapping("/restoredeleted")
    public String restoreDeleted(Model model) {
        List<Category> cate = catservice.getdeletedCategories(true);
        if (cate != null) {
            for (Category cat : cate) {
                model.addAttribute("pageTitle", "restore page");
                cat.setDeleted(false);
                for (Subcategory subcat : cat.getSubcategory()) {
                    subcat.setDeleted(false);
                }
                catservice.save(cat);
            }
        }
        return "redirect:/admin/editcategory";
    }

    @GetMapping("/updateCategory")
    public String updateCategory(Model model, @RequestParam("catid") int categoryId) {
        Category category = catservice.getcategoryByid(categoryId);
        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "category page");
        return "admin/updateCategory";
    }

    @PostMapping("/updatedCategories")
    public String updatedCategory(@RequestParam("catid") int catid, @RequestParam("catname") String catname,
            @RequestParam("iconClass") String icon,
            @RequestParam("imagefile") MultipartFile files,
            HttpSession session) throws IOException {
        try {
            Category category = catservice.getcategoryByid(catid);
            if (!catname.equals(category.getCatname())) {
                boolean existcatname = catservice.existsByCatname(catname);
                if (existcatname) {
                    throw new MycustomException("category name already exist");
                }
            }
            // if(files.isEmpty()){
            // throw new MycustomException("photo not found");
            // }
            String imagepath = files.getOriginalFilename();
            File filestore = new File("src/main/resources/static/img/");
            if (!filestore.exists()) {
                filestore.mkdirs();
            }
            Path path = Paths.get(filestore.getAbsolutePath() + File.separator + imagepath);
            Files.copy(files.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            category.setImagePath(imagepath);
            category.setIconClass(icon);
            category.setCatname(catname);
            catservice.save(category);
            session.setAttribute("message", new Message("category name updated successfully", "alert-success"));
            return "redirect:/admin/editcategory";
        } catch (Exception e) {
            session.setAttribute("message", new Message(e.getMessage() + "", "alert-danger"));
            return "redirect:/admin/editcategory";
        }
    }

    @GetMapping("/addproduct")
    public String addProduct(Model m) {
        List<Category> catList = catservice.getallcategory();
        m.addAttribute("category", catList);
        m.addAttribute("pageTitle", "Product page");
        return "admin/addproduct";
    }

    @GetMapping("/getsubcategories/{id}")
    public ResponseEntity<List<Map<String, Object>>> getSubCategories(@PathVariable("id") int id) {
        // this is the api to sent the subcategory list based on category id
        List<Map<String, Object>> subcatlist = catservice.getSubcategoryNamesIdsByCategoryId(id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(subcatlist);
    }

    @PostMapping("/creatproduct")
    public String creatProduct(@RequestParam("productname") String pname,
            @RequestParam("productdescription") String pdesc,
            @RequestParam("productSpecification") String specify,
            @RequestParam("procat") Integer catid,
            @RequestParam("prosubcat") Integer subcatid, @RequestParam("price") Double price,
            @RequestParam("imagefile") List<MultipartFile> files,
            HttpSession session) throws IOException {

        try {
            if (pname.isEmpty()) {
                throw new MycustomException("product name not added");

            } else if (pdesc.isEmpty()) {
                throw new MycustomException("product description not added");
            } else if (specify.isEmpty()) {
                throw new MycustomException("product Specification not added");
            } else if (catid == 0) {
                throw new MycustomException("category not selected");
            } else if (subcatid == 0) {
                throw new MycustomException("subcategory not selected");
            } else if (price == null || price.isNaN()) {
                throw new MycustomException("price not added");
            } else {
                Product product = new Product();
                product.setPname(pname);
                product.setPdescription(pdesc);
                product.setProductSpecification(specify);
                product.setPrice(price);
                product.setPrice(product.formattedPrice());
                // Product product = proserv.convertProductDtoProduct(products);
                product.setCategory(catservice.getcategoryByid(catid));
                product.setSubcategory(catservice.getSubcategoryById(subcatid));
                List<String> imagepaths = new ArrayList<>();
                for (MultipartFile file : files) {
                    if (file.isEmpty()) {
                        throw new MycustomException("one or more files is empty");
                    }
                    String imagepath = file.getOriginalFilename();
                    imagepaths.add(imagepath);
                    // File filestore = new ClassPathResource("static/img/").getFile();
                    File filestore = new File("src/main/resources/static/img/");
                    if (!filestore.exists()) {
                        filestore.mkdirs();
                    }
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
        return "redirect:/admin/addproduct";
    }

    @GetMapping("/editproduct")
    public String editProduct(Model model) {
        model.addAttribute("pageTitle", "Product page");
        model.addAttribute("product", proserv.getNotDeletedProducts());
        return "admin/editproduct";
    }

    @GetMapping("/restoredeletedproducts")
    public String restoreDeletedProducts(Model model) {
        proserv.restoredeleted();
        model.addAttribute("pageTitle", "Product page");
        return "redirect:/admin/editproduct";
    }

    @GetMapping("/deleteproduct")
    public String deleteProduct(@RequestParam("productId") long id, Model model) {
        proserv.deleteproductbyid(id);
        model.addAttribute("pageTitle", "Product page");
        return "redirect:/admin/editproduct";
    }

    @GetMapping("/updatedproduct")
    public String updateProduct(@RequestParam("productId") int id, Model model) {
        Product product = proserv.getproductbyid(id);
        List<Category> catList = catservice.getallcategory();
        model.addAttribute("category", catList);
        model.addAttribute("pageTitle", "Product page");
        model.addAttribute("subcategorys", catservice.getallsubcategory());
        model.addAttribute("products", product);
        return "admin/updateproducts";
    }

    // update the products
    @PostMapping("/update")
    public String updateTest(@ModelAttribute Product products, @RequestParam("cate") Integer catid,
            @RequestParam("subcat") Integer subcatid, @RequestParam("imagefile") List<MultipartFile> files,
            HttpSession session, @RequestParam("productId") long id) {

        try {
            Product product = proserv.getproductbyid(id);
            product.setPname(products.getPname());
            product.setPdescription(products.getPdescription());
            product.setProductSpecification(products.getProductSpecification());
            product.setPrice(products.getPrice());
            if (catservice.getcategoryByid(catid) == null) {
                throw new Exception("category should be selected");
            }
            product.setCategory(catservice.getcategoryByid(catid));
            if (catservice.getSubcategoryById(subcatid) == null) {
                throw new Exception("subcategory should be selected");
            }
            product.setSubcategory(catservice.getSubcategoryById(subcatid));

            List<String> imagepaths = new ArrayList<>();

            if (!files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (file.isEmpty()) {

                        throw new MycustomException("one or more files is empty");
                    }

                    String imagepath = file.getOriginalFilename();
                    imagepaths.add(imagepath);

                    File filestore = new File("src/main/resources/static/img/");
                    if (!filestore.exists()) {
                        filestore.mkdirs();
                    }
                    Path path = Paths.get(filestore.getAbsolutePath() + File.separator + imagepath);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                }
                product.setImagepath(imagepaths);

            } else {
                product.setImagepath(product.getImagepath());
            }
            proserv.createproduct(product);
            productPriceHistoryService.updatePriceHistory(product);
            session.setAttribute("message", new Message("product added successfully", "alert-success"));

        } catch (Exception e) {

            session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
        }
        return "redirect:/admin/editproduct";
    }

    @PostMapping("/getsalesReport")
    @ResponseBody
    public List<SalesData> findsales()
    // @RequestBody Map<String, Object> data)
    // @RequestParam("startDate") LocalDate startDate,
    // @RequestParam("endDate") LocalDate endDate
    {
        // String startDates=data.get("startDate").toString();
        // LocalDate starDate=LocalDate.parse(startDates);
        // String endDates=data.get("endDate").toString();
        // LocalDate endDate=LocalDate.parse(endDates);
        // List<Order> orders = orderService.getAllOrderByDate(starDate, endDate);
        List<Order> orders = orderService.getAllOrders();

        List<SalesData> sales = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderit : orderItems) {
                double rate = orderit.getProduct().getPrice() * orderit.getQuantity();
                sales.add(new SalesData(order.getFormattedOrderDate(), rate));
            }

        }

        return sales;
    }

    @GetMapping("/orderlist")
    public String getorderList(Model model) {
        model.addAttribute("pageTitle", "OrderList");

        List<Order> orders = orderService.getAllOrderByOrderDateTimeDesc();
        model.addAttribute("orders", orders);
        List<OrderItem> orderitems = new ArrayList<>();
        for (Order order : orders) {

            orderitems.addAll(orderService.getAllOrderItemsByOrder(order));
        }
        model.addAttribute("orderitems", orderitems);
        model.addAttribute("statuses", OrderStatus.values());

        return "admin/orders";

    }

    @PostMapping("/changeorderStatus")
    public String updateOrderStatus(@RequestParam("status") OrderStatus status, @RequestParam("orderId") Integer id,
            HttpSession session) {
        Order order = orderService.getOrderById(id);
        order.setStatus(status);
        orderService.saveOrder(order);
        session.setAttribute("message", new Message("order status updated successfully", "alert-success"));
        return "redirect:/admin/orderlist";

    }

    @GetMapping("/stockmanagement")
    private String getstockManagement(Model model) {
        model.addAttribute("product", proserv.getallproduct());
        model.addAttribute("pageTitle", "Stock page");
        return "admin/Stockmanagement";

    }

    @PostMapping("/stockmanagement/set/{id}")
    public String setStockManagement(@RequestParam(value = "newqty", defaultValue = "") Integer qty,
            @PathVariable("id") Long id, Model model,
            HttpSession session) {
        model.addAttribute("pageTitle", "Stock page");
        if (qty != null) {
            Product product = proserv.getproductbyid(id);
            int count = product.getStockQuantity() + qty;
            product.setStockQuantity(count);
            proserv.saveproduct(product);
            session.setAttribute("message", new Message("STOCK ADDED SUCCESSFULLY", "alert-success"));
        } else {
            session.setAttribute("message", new Message("no input given", "alert-danger"));
        }
        return "redirect:/admin/stockmanagement";
    }

    @PostMapping("/stockmanagement/add/{id}")
    public String addStockManagement(@RequestParam(value = "newqty", defaultValue = "") Integer qty,
            @PathVariable("id") Long id,
            HttpSession session) {
        if (qty != null) {

            Product product = proserv.getproductbyid(id);
            product.setStockQuantity(qty);
            proserv.saveproduct(product);
            session.setAttribute("message", new Message("STOCK SET SUCCESSFULLY", "alert-success"));
        } else {
            session.setAttribute("message", new Message("no input given", "alert-danger"));
        }

        return "redirect:/admin/stockmanagement";
    }

    @GetMapping("/addCoupon")
    public String CouponAdd(Model model) {
        model.addAttribute("pageTitle", "Coupon page");
        model.addAttribute("coupons", new CouponDto());

        return "admin/addCoupon";
    }

    @PostMapping("/createCoupon")
    public String createCoupon(CouponDto coupondto, HttpSession session, Model model) {
        try {
            model.addAttribute("pageTitle", "Coupon page");
            if (coupondto == null) {
                throw new MycustomException("Enter the coupon details properly");
            }
            Coupon coupon = couponService.convertCouponDto(coupondto);
            if (couponService.checkExistCode(coupon.getCode())) {
                throw new MycustomException("coupon already exist");
            }
            coupon.setStatus(CouponStatus.ACTIVE);
            couponService.saveCoupon(coupon);
            session.setAttribute("message", new Message("Coupon created successfully", "alert-success"));

        } catch (MycustomException e) {
            session.setAttribute("message", new Message(e.getMessage() + "", "alert-danger"));
        }

        return "redirect:/admin/addCoupon";

    }

    @GetMapping("/editCoupon")
    public String editCoupon(Model model) {
        model.addAttribute("pageTitle", "Coupon page");
        List<Coupon> coupons = couponService.getAllCoupons();
        model.addAttribute("coupons", coupons);
        return "admin/EditCoupon";
    }

    @PostMapping("/updateCoupon")
    public String updateCoupon(@RequestParam("CouponCode") String code, Model model) {
        Coupon coupon = couponService.findByCouponCode(code);
        model.addAttribute("coupons", coupon);
        model.addAttribute("stats", CouponStatus.values());

        return "admin/updateCouponForm";
    }

    @PostMapping("/updateCouponForm")
    public String updateCouponFormSubmit(CouponDto coupondto, @RequestParam("status") CouponStatus status,
            @RequestParam("couponId") Long id, HttpSession session) {

        if (coupondto == null) {
            return "redirect:/admin/updateCouponForm";
        }
        Coupon coupon = couponService.findByCouponId(id);
        coupon.setStartDate(coupondto.getStartDate());
        coupon.setEndDate(coupondto.getEndDate());
        coupon.setCode(coupondto.getCode());
        coupon.setMinPurchase(coupondto.getMinPurchase());
        coupon.setStatus(status);
        couponService.saveCoupon(coupon);

        return "redirect:/admin/editCoupon";
    }

    // model attribute for product and category offer setting
    @GetMapping("/addOffer")
    private String addOffer(Model model) {
        model.addAttribute("pageTitle", "Offer page");
        model.addAttribute("categories", catservice.getallcategory());
        model.addAttribute("products", proserv.getallproduct());
        return "admin/addOffer";
    }

    @PostMapping("/createOffer")
    public String createOffer(@RequestParam("paracat") String catname, @RequestParam("discount") double discount,
            @RequestParam("offerdescription") String description,
            HttpSession session) {
        Category category = catservice.getCategorybyname(catname);
        Boolean check = offerService.findExistOffer(category);
        if (check) {
            session.setAttribute("message", new Message("offer already set for category", "alert-danger"));
            return "redirect:/admin/addOffer";
        }
        offerService.createCategoryOffer(discount, category, description);
        session.setAttribute("message", new Message("offer added successfully", "alert-success"));

        return "redirect:/admin/addOffer";
    }

    @GetMapping("/showOffer")
    private String ShowOffer(Model model) {
        model.addAttribute("pageTitle", "Coupon page");
        List<Offer> offerlist = offerService.getAllOffer();
        model.addAttribute("offers", offerlist);
        List<ProductOffer> productOfferList = offerService.getAllProductOffer();
        model.addAttribute("productOffers", productOfferList);
        return "admin/editOffers";
    }

    @PostMapping("/removeOffer")
    private String deleteOffer(@RequestParam("offerid") int id) {
        offerService.deleteOffer(id);
        return "redirect:/admin/showOffer";
    }

    @GetMapping("/updateOffer")
    public String updateOffer(Model model, @RequestParam("offerId") Integer id) {
        Offer offer = offerService.findOffer(id);
        model.addAttribute("pageTitle", "Coupon page");
        model.addAttribute("offers", offer);
        return "admin/updateOffer";
    }

    @PostMapping("/updatedOffer")
    public String updatingOffer(@RequestParam("offerid") int id, @RequestParam("discount") double discount,
            HttpSession session) {
        Offer offer = offerService.findOffer(id);
        offer.setDiscount(discount);
        offerService.saveOffer(offer);
        session.setAttribute("message", new Message("offer update success", "alert-success"));
        return "redirect:/admin/showOffer";
    }

    @PostMapping("/addproductoffer")
    public String createProductOFfer(@RequestParam("discount") double discount,
            @RequestParam("productname") Long productId, @RequestParam("offerdescription") String description,
            HttpSession session) {
        try {
            Product product = proserv.getproductbyid(productId);
            offerService.createProductOffer(product, discount, description);
            session.setAttribute("message", new Message("offer set successfully", "alert-success"));
            return "/admin/addOffer";
        } catch (Exception e) {
            session.setAttribute("message", new Message(e.getMessage(), "alert-danger"));

        }
        return "redirect:/admin/addOffer";
    }

    // update product offer form
    @GetMapping("/getupdateProduct")
    public String getUpdatingProfile(@RequestParam("productId") Long id, Model model) {
        Product product = proserv.getproductbyid(id);
        model.addAttribute("offers", offerService.findByProduct(product));
        return "admin/updateProductOffer";
    }

    @PostMapping("/updateProductOffer")
    public String updateProductOffer(@RequestParam("discount") double discount,
            @RequestParam("productId") Long productId, @RequestParam("offerdescription") String description,
            HttpSession session) {
        Product product = proserv.getproductbyid(productId);
        offerService.updateProductOffer(discount, description, product);
        session.setAttribute("message", new Message("offer updated successfully", "alert-success"));
        return "redirect:/admin/showOffer";
    }

    @GetMapping("/deleteProductOffer/{id}")
    public String deleteProductOffer(@PathVariable("id") Integer id, HttpSession session) {
        session.setAttribute("message", new Message("offer deleted successfully", "alert-success"));
        offerService.deleteProductOffer(id);

        return "redirect:/admin/showOffer";
    }

}
