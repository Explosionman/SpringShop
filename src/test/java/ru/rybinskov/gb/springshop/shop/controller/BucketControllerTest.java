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
import ru.rybinskov.gb.springshop.shop.dto.BucketDetailDto;
import ru.rybinskov.gb.springshop.shop.dto.BucketDto;
import ru.rybinskov.gb.springshop.shop.service.BucketService;
import ru.rybinskov.gb.springshop.shop.service.ProductServiceImpl;
import ru.rybinskov.gb.springshop.shop.service.UserService;

import java.util.Arrays;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BucketController.class)
class BucketControllerTest {
    private BucketDto bucketDto;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BucketService bucketService;
    @MockBean
    private ProductServiceImpl productService;
    @MockBean
    private UserService userService;

    private Product product1 = new Product(1000L, "TestCertificate", 5000.0, null);
    private Product product2 = new Product(1001L, "TestCertificate1", 10000.0, null);

    public BucketControllerTest() {
    }

    @BeforeEach
    void setUp() {
        BucketDetailDto bucketDetailDto1 = new BucketDetailDto(product1);
        BucketDetailDto bucketDetailDto2 = new BucketDetailDto(product2);
        BucketDetailDto bucketDetailDto3 = new BucketDetailDto(product2);
        bucketDto = new BucketDto();
        bucketDto.setBucketDetails(Arrays.asList(bucketDetailDto1, bucketDetailDto2, bucketDetailDto3));
        bucketDto.aggregate();
        given(bucketService.getBucketByUser("testUser")).willReturn(bucketDto);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void checkGetBucketByUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/bucket")
                        .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + bucketDto.getBucketDetails().get(0).getTitle() + "</td>")))
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<td>" + bucketDto.getBucketDetails().get(1).getTitle() + "</td>")))
                .andExpect(MockMvcResultMatchers
                        .content().string(Matchers.containsString("<b>" + bucketDto.getSum() + "</b>")));

    }
}