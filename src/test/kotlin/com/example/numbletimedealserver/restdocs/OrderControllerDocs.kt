package com.example.numbletimedealserver.restdocs

import com.example.numbletimedealserver.DATETIME
import com.example.numbletimedealserver.RestDocsConfig
import com.example.numbletimedealserver.STRING
import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Order
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.repository.customer.CustomerRepository
import com.example.numbletimedealserver.repository.order.OrderRepository
import com.example.numbletimedealserver.repository.product.ProductRepository
import com.example.numbletimedealserver.request.ProductRegisterRequest
import com.example.numbletimedealserver.request.SignUpRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Import(RestDocsConfig::class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class, RestDocumentationExtension::class)
@Transactional
class OrderControllerDocs @Autowired constructor(
    private val mapper: ObjectMapper,
    private val orderRepository: OrderRepository,
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository
) {
    @Autowired
    lateinit var mockMvc: MockMvc
    fun myIdentifier(methodName: String) = "{class-name}/$methodName"
    lateinit var customer:Customer
    lateinit var product :Product
    lateinit var order:Order
    val signUpRequest = SignUpRequest("inu", "12345", ROLE.ADMIN)
    val registerRequest = ProductRegisterRequest("book1", "empty", LocalTime.now(), 100L)

    @BeforeEach
    fun setup() {
        customer = customerRepository.save(Customer(signUpRequest.name, signUpRequest.pw, signUpRequest.role))

        product = productRepository.save(
            Product(
                registerRequest.name,
                registerRequest.description,
                registerRequest.appointedTime,
                registerRequest.appointedQuantity,
                customer
            )
        )

    }

    @AfterEach
    fun delete() {
        orderRepository.deleteAll()
        productRepository.deleteAll()
        customerRepository.deleteAll()

    }
    /*
    *  @PostMapping("/order/{productId}")
    fun order(@SessionLogin loginUser:CustomerDto, @PathVariable("productId") productId:UUID):ResponseEntity<OrderDto> =orderService.createOrder(loginUser.id,productId).let { ResponseEntity.ok(it) }

    * */
    @Test
    fun `createOrder`(){
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/order/{productId}", product.id)
                .accept(MediaType.APPLICATION_JSON)
              .sessionAttrs(mapOf("user" to CustomerDto(customer)))
        ).andExpect(MockMvcResultMatchers.status().isOk)

            .andExpect(MockMvcResultMatchers.jsonPath("$.id").isString)
            .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(customer.id.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.productId").value(product.id.toString()))
            .andDo(
                MockMvcRestDocumentation.document(
                    myIdentifier("주문 등록"),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("productId").description("상품 id")
                    ),
                    responseFields(
                        fieldWithPath("id").optional().type(STRING).description("주문 id"),
                        fieldWithPath("customerId").optional().type(STRING).description("주문 고객 id"),
                        fieldWithPath("productId").optional().type(DATETIME)
                            .description("주문 상품 id"),

                    ),
                )
            )
    }
}