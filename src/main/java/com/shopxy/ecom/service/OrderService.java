package com.shopxy.ecom.service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.OrderItemRepository;
import com.shopxy.ecom.repository.OrderRepository;
import com.shopxy.ecom.Util.OrderStatus;
import com.shopxy.ecom.helper.MycustomException;
import com.shopxy.ecom.model.Address;
import com.shopxy.ecom.model.Cart;
import com.shopxy.ecom.model.CartItem;
import com.shopxy.ecom.model.Order;
import com.shopxy.ecom.model.OrderItem;
import com.shopxy.ecom.model.User;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    DecimalFormat df = new DecimalFormat("#.00");

    public Order createOrder(Long id, Double price, User user, CartItem cartItem, Cart cart) {
        // Assuming addressService is a valid service for getting the address by ID
        Address address = addressService.getAddressById(id);

        // Create and save an OrderItem
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());

        orderItemRepository.save(orderItem);

        // Create the Order
        Order order = new Order();
        order.setShippingAddress(address);
        order.setOrderDateTime(LocalDateTime.now());
        order.setPaymentMethod(null);
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        // Add the OrderItem to the Order's list of items
        order.addOrderItem(orderItem);
        order.setTotalAmount(cart.getTotalamount());
        // Save the Order (assuming you have a method like saveOrder in your
        // service/repository)
        this.saveOrder(order);
        shoppingCartService.removeCartByCartItem(cartItem);

        return order;
    }

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    public Order createSingleorder(Long addressid, double price, User user, String payment, int quantity,
            Long productid) {

        Address address = addressService.getAddressById(addressid);
        OrderItem orderitem = new OrderItem();
        orderitem.setProduct(productService.getproductbyid(productid));
        orderitem.setQuantity(quantity);

        orderItemRepository.save(orderitem);
        Order order = new Order();

        order.setShippingAddress(address);
        order.setOrderDateTime(LocalDateTime.now());
        order.setPaymentMethod(payment);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setUser(user);
        order.addOrderItem(orderitem);
        order.setTotalAmount(price);
        this.saveOrder(order);
        return order;
    }

    @Transactional
    public Order createOrder(User user, Long addressId, String payment, Double grandTotal) {
        try {
            Address address = addressService.getAddressById(addressId);
            if (address == null) {
                // Handle the case where the address is not found
                throw new MycustomException("Address not found for id: " + addressId);
            }

            Cart cart = shoppingCartService.getCartByUserId(user.getId());
            if (cart == null) {
                // Handle the case where the cart is not found
                throw new MycustomException("Cart not found for user: " + user.getId());
            }

            List<CartItem> cartItems = shoppingCartService.getCartItemByUserDtls(user);

            Order order = new Order();
            order.setShippingAddress(address);
            order.setOrderDateTime(LocalDateTime.now());
            order.setPaymentMethod(payment); // Set the payment method accordingly
            order.setUser(user);

            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(cartItem.getProduct());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItemRepository.save(orderItem);
                order.addOrderItem(orderItem);
            }
            order.setStatus(OrderStatus.CONFIRMED);
            order.setTotalAmount(Double.parseDouble(df.format(grandTotal)));
            this.saveOrder(order);

            // Clear the user's shopping cart after successful order creation
            shoppingCartService.clearCart(user);

            return order;
        } catch (MycustomException e) {
            // Handle exceptions and log appropriately
            // throw new MycustomException("Error creating order for user: " + user.getId(),
            // e);
            return null;
        }

    }

    public List<Order> getOrderByUser(User user) {
        return orderRepository.findByUser(user);
    }

    public List<OrderItem> getAllOrderItemsByOrder(Order order) {

        return orderItemRepository.findByOrder(order);
    }

    

    public List<Order> getAllOrderByOrderDateTimeDesc() {
        return orderRepository.findAllByOrderByOrderDateTimeDesc();
    }

    public void deleteOrderByOrderId(int orderId) {
        List<OrderItem> orderitems=orderItemRepository.findByOrder(orderRepository.findById(orderId));
        for(OrderItem orderitem:orderitems){
            orderItemRepository.deleteById(orderitem.getId());
        }
        orderRepository.deleteById(orderId);
    }

    public Order getOrderById(int orderId) {
        return orderRepository.findById(orderId);
    }

    public void cancelOrder(int orderId) {
        Order orders=orderRepository.findById(orderId);
        orders.setStatus(OrderStatus.CANCELED);
        orderRepository.save(orders);
        
    }

    public Integer getOrderCount(){
        return orderRepository.countorder();
    }

    public List<Order> getAllOrderByDate(LocalDate dates,LocalDate endDates){
        LocalDateTime startDateTime = dates.atStartOfDay(); // Start of the day
        LocalDateTime endDateTime = endDates.atTime(23, 59, 59);
        return orderRepository.findByDateRange(startDateTime,endDateTime);
    }

    // public Address findAddressByOrderId(int orderId) {
    //     return 
    // }

}
