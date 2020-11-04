package ru.rybinskov.gb.springshop.shop.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.rybinskov.gb.springshop.shop.domain.Product;
import ru.rybinskov.gb.springshop.shop.service.*;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private ProductServiceImpl productService;
    @MockBean
    private BucketService bucketService;


    private Product product1 = new Product(1000L, "TestCertificate", 5000.00, null);
    private Product product2 = new Product(1001L, "TestCertificate", 10000.00, null);

    ProductControllerTest() {
    }

    @BeforeEach
    void setUp() {
        given(productService.getAll()).willReturn(Arrays.asList(product1, product2));
    }

    @Test
    @WithMockUser(username = "User", roles = {"USER"})
    void checkUserProductList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + product1.getTitle() + "</td>")))
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + product2.getTitle() + "</td>")));

    }

    @Test
    @WithMockUser(username = "Manager", roles = {"MANAGER"})
    void checkManagerProductList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + product1.getTitle() + "</td>")))
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + product2.getTitle() + "</td>")));

    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    void checkAdminProductList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + product1.getTitle() + "</td>")))
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + product2.getTitle() + "</td>")));

    }

    @Test
    @WithMockUser(username = "HZ", roles = {"fictionalRole"})
    void checkFictionalRoleProductList() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/products")
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isForbidden());
    }
}