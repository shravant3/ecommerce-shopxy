package com.shopxy.ecom;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.security.Principal;
import java.util.List;
import java.util.Arrays;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import com.shopxy.ecom.controller.UserController;
import com.shopxy.ecom.helper.Message;
import com.shopxy.ecom.model.Product;
import com.shopxy.ecom.model.Country;
import com.shopxy.ecom.model.User;
import com.shopxy.ecom.service.AddressService;
import com.shopxy.ecom.service.CategoryService;
import com.shopxy.ecom.service.CountryService;
import com.shopxy.ecom.service.ProductService;
import com.shopxy.ecom.service.UserDtlsService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpSession;

public class UserControllerTest {

    @Mock
    private UserDtlsService userService;

    @Mock
    private ProductService productService;

    @Mock
    private AddressService addressService;

    @Mock
    private CategoryService catservice;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private CountryService countryService;

    @Mock
    private Model model;

    // ... (other dependencies)

    @InjectMocks
    private UserController userController;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUserhome() {
        Model model = mock(Model.class);

        String viewName = userController.userhome(model);

        assertEquals("user/userhome", viewName);
        verify(model).addAttribute(eq("pageTitle"), eq("Home Page"));
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testProductdescription() {
        Model model = mock(Model.class);

        when(productService.getproductbyid(anyLong())).thenReturn(new Product());

        String viewName = userController.productdescription(1L, model);

        assertEquals("user/productdescription", viewName);
        verify(model).addAttribute(eq("products"), any());
        verify(model).addAttribute(eq("productlist"), any());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testBuyNow1() {
        Model model = mock(Model.class);
        Principal principal = mock(Principal.class);

        when(productService.getproductbyid(anyLong())).thenReturn(new Product());
        when(principal.getName()).thenReturn("testuser");

        String viewName = userController.buyNow1(principal, 1L, 2, model);

        assertEquals("/user/buynow", viewName);
        verify(model).addAttribute(eq("stock"), anyInt());
        // Add more assertions based on the expected behavior
    }

    @Test
    public void testUserProfile() {
        // Mock the necessary dependencies
        User mockUser = new User();  // create a mock user
        when(userService.getUserByMail(anyString())).thenReturn(mockUser);

        // Call the userProfile method
        String viewName = userController.userProfile(model, mock(Principal.class));

        // Assert the expected behavior
        assertEquals("common/userprofile", viewName);
    }


    @Test
    public void testUpdatePassword() {
        // Arrange
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");

        User mockedUser = new User();
        mockedUser.setPassword(encoder.encode("oldPassword")); // Set an encoded password for testing

        when(userService.getUserByMail("testUser")).thenReturn(mockedUser);
        when(encoder.matches("oldPassword", mockedUser.getPassword())).thenReturn(true);

        HttpSession session = mock(HttpSession.class);

        // Act
        String result = userController.updatePassword(principal, "oldPassword", "newPassword", "newPassword", session);

        // Assert
        // Add your assertions here to verify the expected behavior

        // For example:
        assertEquals("redirect:/user/userprofile", result);
        verify(userService, times(1)).saveUser(mockedUser);
        verify(session, times(1)).setAttribute(eq("message"), any(Message.class));
    }

}
