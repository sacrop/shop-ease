package com.shop.ease.Controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.ease.model.Cart;
import com.shop.ease.model.CartItem;
import com.shop.ease.model.Product;
import com.shop.ease.model.UserDtls;
import com.shop.ease.service.ProductService;
import com.shop.ease.service.ShoppingCartService;
import com.shop.ease.service.UserService;

@Controller
@RequestMapping("/user")
public class CartController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    DecimalFormat df = new DecimalFormat("#.00");

    @ModelAttribute
    public void productdisplay(Model pro) {
        pro.addAttribute("product", productService.getallproduct());
    }

    @ModelAttribute
    public void Userdetails(Model m, Principal p) {
        String email = p.getName();
        UserDtls user = userService.getUserByMail(email);
        m.addAttribute("user", user);

    }

    @GetMapping("/cartItems")
    public String cart(Model model, Principal principal) {
        String name = principal.getName();
        UserDtls user = userService.getUserByMail(name);
        Cart cart = shoppingCartService.getCartByUserId(user.getId());
        if (cart != null) {
            cart.setItems(shoppingCartService.getCartItemByUserDtls(user));
            double totalcartamout = Double.parseDouble(df.format(cart.getTotalamount()));
            cart.setTotalamount(totalcartamout);
            shoppingCartService.saveCart(cart);
        }
        if (cart == null) {
            cart = new Cart();
            cart.setUserDtls(user);
            cart.setTotalamount(0.0);
            shoppingCartService.saveCart(cart);
        }
        cart.setTotalamount(Double.parseDouble(df.format(cart.getTotalamount())));
        shoppingCartService.saveCart(cart);
        List<CartItem> cartlist = shoppingCartService.getCartItemByUserDtls(user);
        model.addAttribute("carts", cart);
        model.addAttribute("totalamount",Double.parseDouble(df.format(cart.getTotalamount())));
        
        model.addAttribute("cartitems", cartlist);

        return "cart";
    }

    @PostMapping("/addtocart")
    public String getcartitems(@RequestParam("productId") Long id, Principal principal,
            @RequestParam(value = "quantities", defaultValue = "1") int quantity) {

        if (principal == null || principal.getName() == null) {
            return "redirect:/";
        }
        String userName = principal.getName();

        UserDtls user = userService.getUserByMail(userName);
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
            cart.setTotalamount(Double.parseDouble(df.format(cart.getTotalamount())));
            shoppingCartService.saveCart(cart);
            cartItem.setCart(cart);
            shoppingCartService.saveCartItem(cartItem);
        }
        shoppingCartService.UpdateStockItem(cart);
        return "redirect:/user/";
    }

    @PostMapping("/updatecart")
    public String updatecart(Principal principal, @RequestParam(value = "quantities", defaultValue = "1") int quantity,
            @RequestParam("cartId") Long id) {
        UserDtls user = userService.getUserByMail(principal.getName());
        CartItem cartItem = shoppingCartService.getCartItemById(id);
        // int stock=cartItem.getProduct().getStockQuantity()-(int)quantity;
        // Product product=cartItem.getProduct();
        // product.setStockQuantity(stock);
        if (quantity == 0) {
            cartItem.setQuantity(1);
        } else if (quantity >= 5) {
            cartItem.setQuantity(5);
        } else {
            cartItem.setQuantity(quantity);
        }
        // cartItem.setProduct(product);

        shoppingCartService.saveCartItem(cartItem);
        Cart cart = shoppingCartService.getCartByUserId(user.getId());
        cart.setTotalamount(Double.parseDouble(df.format(cart.getTotalamount())));
        shoppingCartService.saveCart(cart);

        return "redirect:/user/cartItems";
    }

    @PostMapping("/deletecart")
    public String deletecart(Principal principal, @RequestParam(value = "quantities", defaultValue = "1") int quantity,
            @RequestParam("cartId") Long id) {

        UserDtls user = userService.getUserByMail(principal.getName());
        shoppingCartService.deleteCartById(id);
        Cart cart = shoppingCartService.getCartByUserId(user.getId());
        cart.setTotalamount(Double.parseDouble(df.format(cart.getTotalamount())));
        shoppingCartService.saveCart(cart);
        // int stock=cartItem.getProduct().getStockQuantity()-(int)quantity;
        // Product product=cartItem.getProduct();
        // product.setStockQuantity(stock);
        // cartItem.setProduct(product);
        return "redirect:/user/cartItems";
    }
}
