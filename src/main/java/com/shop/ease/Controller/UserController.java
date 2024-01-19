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
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

import com.razorpay.RazorpayClient;

import com.shop.ease.Util.CouponStatus;
import com.shop.ease.Util.OrderStatus;
import com.shop.ease.Util.WalletTransactionStatus;
import com.shop.ease.dto.AddressDto;
import com.shop.ease.helper.Message;
import com.shop.ease.helper.MycustomException;
import com.shop.ease.model.Address;
import com.shop.ease.model.Cart;
import com.shop.ease.model.CartItem;
import com.shop.ease.model.Category;
import com.shop.ease.model.Country;
import com.shop.ease.model.Coupon;
import com.shop.ease.model.Offer;
import com.shop.ease.model.Order;
import com.shop.ease.model.OrderItem;
import com.shop.ease.model.Payment;
import com.shop.ease.model.Product;
import com.shop.ease.model.ProductOffer;
import com.shop.ease.model.ProductPriceHistory;
import com.shop.ease.model.Refferal;
import com.shop.ease.model.Review;
import com.shop.ease.model.UserDtls;
import com.shop.ease.model.Wallet;
import com.shop.ease.model.WalletHistory;
import com.shop.ease.service.AddressService;
import com.shop.ease.service.Catservice;
import com.shop.ease.service.CouponService;
import com.shop.ease.service.OfferService;
import com.shop.ease.service.OrderService;
import com.shop.ease.service.PaymentService;
import com.shop.ease.service.ProductService;
import com.shop.ease.service.ReferalService;
import com.shop.ease.service.ReviewService;
import com.shop.ease.service.ShoppingCartService;
import com.shop.ease.service.UserService;
import com.shop.ease.service.WalletService;
import com.shop.ease.service.ProductPriceHistoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private ProductService productService;

  @Autowired
  private AddressService addressService;

  @Autowired
  private ShoppingCartService shoppingCartService;

  @Autowired
  private OrderService orderService;

  @Autowired
  private PasswordEncoder encoder;

  @Autowired
  private PaymentService paymentService;

  @Autowired
  private Catservice catservice;

  @Autowired
  private CouponService couponService;

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private ProductPriceHistoryService productPriceHistoryService;

  @Autowired
  private WalletService walletService;

  @Autowired
  private ReferalService referalService;

  @Autowired
  private OfferService offerService;

  DecimalFormat df = new DecimalFormat("#.00");

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("pageTitle", "Home Page");
    List<Product> productLis = productService.getallproduct();
    List<ProductPriceHistory> historyList = productPriceHistoryService.getPriceHistories();
    model.addAttribute("prohistory", historyList);
    model.addAttribute("product", productLis);
    model.addAttribute("catlist", catservice.getallcategory());
    model.addAttribute("avgRatings", reviewService.getProductReview(productLis));
    model.addAttribute("pageTitle", "Home page");
    return "user/userhome";
  }

  @ModelAttribute
  public void Userdetails(Model m, Principal p) {
    String email = p.getName();
    UserDtls user = userService.getUserByMail(email);
    m.addAttribute("user", user);
  }

  @GetMapping("/productdescription/{id}")
  public String productdescription(@PathVariable("id") Long id, Model model, Principal principal) {

    UserDtls user = userService.getUserByMail(principal.getName());
    Product product = productService.getproductbyid(id);
    model.addAttribute("products", product);
    ProductPriceHistory productPriceHistory = productPriceHistoryService.getOriginalPriceOfProduct(product);
    Offer offer = offerService.findOfferByCategory(product.getCategory());
    model.addAttribute("Catdescriptions", offer);
    ProductOffer productOffer = offerService.findByProduct(product);
    model.addAttribute("prodDescription", productOffer);
    model.addAttribute("Categories", catservice.getallcategory());
    model.addAttribute("productPriceHistory", productPriceHistory);
    List<Product> productlist = productService.getallproduct();
    model.addAttribute("productlist", productlist);
    List<Review> reviews = reviewService.findAllReviewProduct(product);
    model.addAttribute("review", reviews);
    model.addAttribute("canOrder", orderService.userProductDelivered(user, product));
    model.addAttribute("revRate", reviewService.getAvgRating(product));
    model.addAttribute("pageTitle", product.getPname());
    return "productDescription";
  }

  // product order directly
  @PostMapping("/buynow")
  public String buyNow(Principal principal, @RequestParam("productId") Long id,
      @RequestParam(name = "quantities", defaultValue = "1") int quantity, Model m) {
    Product product = productService.getproductbyid(id);
    int stock = product.getStockQuantity() - quantity;
    String email = principal.getName();
    UserDtls user = userService.getUserByMail(email);
    m.addAttribute("stock", stock);
    m.addAttribute("user", user);
    m.addAttribute("product", product);
    m.addAttribute("quantity", quantity);
    double updatedPrice = Double.parseDouble(df.format(product.getPrice() * quantity + 40));
    m.addAttribute("totalprice", updatedPrice);
    m.addAttribute("deliverycharge", 40.0);
    m.addAttribute("wallets", walletService.getWalletByUser(user));
    Offer offer = offerService.findOfferByCategory(product.getCategory());
    m.addAttribute("Catdescriptions", offer);
    ProductOffer productOffer = offerService.findByProduct(product);
    m.addAttribute("prodDescription", productOffer);
    List<Address> addresslist = addressService.getAlladdress();
    m.addAttribute("addresslist", addresslist);
    m.addAttribute("coupons", couponService.getAllCoupons());
    m.addAttribute("pageTitle", "checkout");
    return "user/buynow";

  }

  @PostMapping("/orderproduct")
  public String orderProduct(Model model, @RequestParam("addressId") Long addressid, Principal principal,
      @RequestParam(value = "payment", defaultValue = "walletpayment") String payment,
      @RequestParam("quantity") int quantity,
      @RequestParam("totalprice") double price, @RequestParam("productId") Long productid,
      @RequestParam(value = "useWallet", defaultValue = "") String wallet) {

    String username = principal.getName();
    double prices = 0.0;
    UserDtls user = userService.getUserByMail(username);
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
    orderService.createSingleorder(addressid, price, user, payment, quantity,
        productid);
    model.addAttribute("pageTitle", "Order Page");
    return "user/ordersucced";
  }

  // checking out all items from cart
  @PostMapping("/orderall")
  public String ordrall(Model model, @RequestParam("addressId") Long addressid,
      @RequestParam(value = "payment", defaultValue = "walletpayment") String payment,
      @RequestParam("cartprice") Double price, Principal principal,
      @RequestParam(value = "useWallet", defaultValue = "") String wallet) {
    String userName = principal.getName();
    UserDtls user = userService.getUserByMail(userName);
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
    model.addAttribute("pageTitle", "Order page");
    return "user/ordersucced";
  }

  @PostMapping("/checkoutall")
  public String getallcartItemsCheckout(Model model, Principal principal) {
    String userName = principal.getName();
    UserDtls user = userService.getUserByMail(userName);
    Cart cart = shoppingCartService.getCartByUserId(user.getId());
    List<CartItem> cartitemslist = cart.getItems();
    model.addAttribute("cartItems", cartitemslist);
    Double updatedPrice = Double.parseDouble(df.format(cart.getTotalamount()));
    model.addAttribute("totalprice", updatedPrice);
    List<Address> addresslist = addressService.getAlladdress();
    model.addAttribute("addresslist", addresslist);
    model.addAttribute("coupons", couponService.getAllCoupons());
    model.addAttribute("wallets", walletService.getWalletByUser(user));
    model.addAttribute("categoryOffer", offerService.getAllOffer());
    model.addAttribute("productOffer", offerService.getAllProductOffer());
    model.addAttribute("pageTitle", "checkout all");
    return "user/checkoutall";
  }

  @GetMapping("/userprofile")
  public String userProfile(Model model, Principal principal) {
    model.addAttribute("pageTitle", "Profile page");
    UserDtls user = userService.getUserByMail(principal.getName());
    model.addAttribute("users", user);
    return "user/userprofile";
  }

  @PostMapping("/updateprofile")
  public String updateProfile(Principal principal, @RequestParam("name") String usename,
      @RequestParam("email") String email, @RequestParam("phonenumber") String phonenumber,
      @RequestParam("imagefile") MultipartFile file, HttpSession session, Model model) throws IOException {
    UserDtls principalUser = userService.getUserByMail(principal.getName());
    principalUser.setUsename(usename);
    model.addAttribute("pageTitle", "Profile page");
    principalUser.setEmail(email);
    principalUser.setPhonenumber(phonenumber);

    if (!file.isEmpty()) {
      String imagepath = file.getOriginalFilename();
      File filestore = new ClassPathResource("static/img/").getFile();
      Path path = Paths.get(filestore.getAbsolutePath() + File.separator + imagepath);
      Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
      principalUser.setImagepath(imagepath);
    }
    userService.saveUser(principalUser);

    session.setAttribute("message", new Message("updated successfully", "alert-success"));
    return "redirect:/user/userprofile";
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
    return "redirect:/user/userprofile";
  }

  @GetMapping("/orders")
  public String getOrders(Model model, Principal principal) {
    String username = principal.getName();
    UserDtls user = userService.getUserByMail(username);
    List<Order> orders = orderService.getOrderByUser(user);
    model.addAttribute("orders", orders);
    List<OrderItem> orderitems = new ArrayList<>();
    for (Order order : orders) {
      orderitems.addAll(orderService.getAllOrderItemsByOrder(order));
    }
    model.addAttribute("orderitems", orderitems);

    List<OrderStatus> orderstatus = new ArrayList<>();
    orderstatus.addAll(Arrays.asList(OrderStatus.values()));
    model.addAttribute("orderstatuses", orderstatus);
    model.addAttribute("pageTitle", "Orders page");
    return "user/orders";

  }

  @GetMapping("/cancelOrder/{id}")
  public String deleteOrder(@PathVariable("id") int orderId, HttpSession session, Principal principal, Model model) {
    model.addAttribute("pageTitle", "Orders page");
    Order order = orderService.getOrderById(orderId);
    UserDtls user = userService.getUserByMail(principal.getName());
    if ("rayzorpay".equals(order.getPaymentMethod())) {
      walletService.AddToWallet(order.getTotalAmount(), user);

    }
    orderService.cancelOrder(orderId);
    session.setAttribute("message", new Message("order item cancelled", "alert-success"));
    return "redirect:/user/orders";
  }

  @GetMapping("/address")
  public String getAddress(Model model) {
    List<Address> addresslist = addressService.getNonDeletedAddress();
    model.addAttribute("pageTitle", "Address page");
    model.addAttribute("addresslist", addresslist);
    return "user/address";
  }

  @GetMapping("/deleteAddress/{id}")
  public String deleteAddress(@PathVariable("id") Long id, Model model) {
    model.addAttribute("pageTitle", "Address page");
    addressService.deleteById(id);
    return "redirect:/user/address";
  }

  @GetMapping("/editaddress/{id}")
  public String editAddress(Model model, @PathVariable("id") Long id) {
    Address address = addressService.getAddressById(id);
    model.addAttribute("pageTitle", "Address page");
    model.addAttribute("address", address);
    List<Country> countrylist = addressService.getAllCountry();
    model.addAttribute("country", countrylist);
    return "user/editaddress";
  }

  // modified address using edit address
  @PostMapping("/getmodaddress")
  public String getmodifiedAddress(@ModelAttribute AddressDto addressdto, @RequestParam("country") String countryname,
      Principal principal,@RequestParam("addressId")Long id,
      @RequestParam(value = "defaultAddresscheck", defaultValue = "false") boolean defaultAddresscheck, Model model,
      HttpSession session) {
    Address address = addressService.addressDtoToAddress(addressdto);
    address.setId(id);
    address.setDefaultAddress(defaultAddresscheck);
    addressService.updateAddress(address, countryname);
    model.addAttribute("pageTitle", "Address page");
    return "redirect:/user/address";
  }

  @GetMapping("/newaddress")
  public String newAddress(Model model) {
    List<Country> countrylist = addressService.getAllCountry();
    model.addAttribute("pageTitle", "Address page");
    model.addAttribute("country", countrylist);
    return "user/newaddress";
  }

  // creation of new address
  @PostMapping("/addresscreation")
  public String newAddressCreated(@ModelAttribute AddressDto addressdto, @RequestParam("country") String countryname,
      Principal principal,
      @RequestParam(value = "defaultAddresscheck", defaultValue = "false") boolean defaultAddresscheck,
      HttpSession session, Model model) {
    model.addAttribute("pageTitle", "Address page");
    String username = principal.getName();
    Address address = addressService.addressDtoToAddress(addressdto);
    address.setUserDtls(userService.getUserByMail(username));
    address.setDefaultAddress(defaultAddresscheck);
    addressService.updateAddress(address, countryname);
    return "redirect:/user/address";
  }

  @GetMapping("/wallet")
  public String getWallet(Model model, Principal principal) {
    UserDtls user = userService.getUserByMail(principal.getName());
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

  @PostMapping("/create_order")
  @ResponseBody
  public String createOrder(@RequestBody Map<String, Object> data, Principal principal) {
    try {
      double amount = Double.parseDouble(data.get("amount").toString());
      var client = new RazorpayClient("rzp_test_XlwVkMu1Zy8MgK", "OI59CwwGzTjupDR74sBsKsCi");
      JSONObject orderRequest = new JSONObject();
      orderRequest.put("amount", amount * 100);
      orderRequest.put("currency", "INR");
      orderRequest.put("receipt", "txn_123435");
      // creating new order

      com.razorpay.Order newOrder = client.Orders.create(orderRequest);

      // save the order in database
      Payment myOrder = new Payment();
      myOrder.setAmount(newOrder.get("amount") + "");
      myOrder.setOrderId(newOrder.get("id"));
      myOrder.setPaymentId(null);
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

  // product search and filter
  @GetMapping("/searchProduct")
  public String SearchProductlist(@RequestParam(value = "searchQuery", defaultValue = "") String name, Model model) {

    model.addAttribute("pageTitle", "Product Page");
    List<Product> productlist = productService.getProductsBypname(name);
    model.addAttribute("Categories", catservice.getallcategory());
    model.addAttribute("product", productlist);
    return "searchDisplay";
  }

  @GetMapping("/productlist")
  public String getproductlist(Model model, @RequestParam(defaultValue = "0") int page) {
    Page<Product> productPage = productService.getProduct(page, 9);
    model.addAttribute("pageTitle", "Product Page");
    model.addAttribute("product", productPage);
    model.addAttribute("Categories", catservice.getallcategory());
    return "productDisplay";
  }

  @GetMapping("/productlist/{id}")
  public String getCategoriesedProduct(@PathVariable("id") Integer id, Model model) {
    model.addAttribute("Categories", catservice.getallcategory());
    model.addAttribute("pageTitle", "Product Page");
    Category category = catservice.getcategoryByid(id);
    List<Product> productList = category.getProduct();
    model.addAttribute("product", productList);
    return "searchDisplay";

  }

  @PostMapping("/applyCoupon")
  @ResponseBody
  public String applyCoupon(@RequestBody Map<String, Object> data, Principal principal) {
    try {
      if (data == null) {
        throw new MycustomException("enter some coupon code");
      }

      String code = data.get("name").toString();
      Double amount = Double.parseDouble(data.get("totalprice").toString());
      Coupon coupon = couponService.findByCouponCode(code);

      if (coupon == null) {
        throw new MycustomException("Invalid coupon code");
      }
      if (coupon.getMinPurchase() > amount) {
        throw new MycustomException("minimum purchase amount is not reached");
      }
      UserDtls user = userService.getUserByMail(principal.getName());
      Boolean usage = couponService.checkCouponUsage(user, code);
      if (usage) {
        throw new MycustomException("coupon already used");
      }
      // amount=amount

      if (coupon.getStatus() != CouponStatus.ACTIVE) {
        throw new MycustomException("Coupon is inactive");
      }
      Double discount = coupon.getDiscountPercentage();
      double rate = (amount * discount) / 100;
      amount = amount - rate;
      amount = Double.parseDouble(df.format(amount));
      couponService.updateCouponUsage(user, coupon);
      return amount.toString();
    } catch (MycustomException e) {
      return "Error occurred: " + e.getMessage();
    }
  }

  @GetMapping("/referEarn")
  public String referEarn(Model model) {
    model.addAttribute("pageTitle", "Referal page");
    return "user/referAndEarn";
  }

  @PostMapping("/showReferal")
  public String showReferal(Principal principal, Model model) {
    UserDtls user = userService.getUserByMail(principal.getName());
    Refferal referal = referalService.findByUser(user);
    if (referal == null) {
      referalService.CreateReferral(user);
    }
    model.addAttribute("referrals", referal);
    model.addAttribute("pageTitle", "Referal page");
    return "user/referAndEarn";
  }

  @PostMapping("/submitReview")
  @ResponseBody
  public ResponseEntity<String> reviewSubmitted(@RequestBody Map<String, Object> data, Principal principal) {
    try {
      UserDtls user = userService.getUserByMail(principal.getName());
      String ratings = (String) data.get("ratings");
      String name = (String) data.get("name");
      String email = (String) data.get("email");
      String description = (String) data.get("description");
      String productId = (String) data.get("productid");

      if (name.isEmpty() || ratings.isEmpty() || email.isEmpty() || description.isEmpty() || productId.isEmpty()) {
        throw new MycustomException("form is not filled properly");
      }

      Product product = productService.getproductbyid(Long.parseLong(productId));
      Review review = reviewService.createReview(name, email, description, product, user, ratings);

      // Create a ResponseEntity with the review details and status OK
      return new ResponseEntity<>(review.toString(), HttpStatus.OK);
    } catch (MycustomException e) {
      // Handle exception and return a ResponseEntity with status 400 (Bad Request)
      return new ResponseEntity<>("failure" + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
