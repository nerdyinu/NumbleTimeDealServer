package com.example.numbletimedealserver.restdocs

import com.example.numblebankingserverchallenge.config.SessionLogin
import com.example.numbletimedealserver.*
import com.example.numbletimedealserver.domain.Customer
import com.example.numbletimedealserver.domain.Product
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.dto.ProductDto
import com.example.numbletimedealserver.exception.CustomException
import com.example.numbletimedealserver.repository.customer.CustomerRepository
import com.example.numbletimedealserver.repository.product.ProductRepository
import com.example.numbletimedealserver.request.*
import com.example.numbletimedealserver.service.product.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
@Import(RestDocsConfig::class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class, RestDocumentationExtension::class)
@Transactional
class ProductControllerDocs @Autowired constructor(
    private val customerRepository: CustomerRepository,
    private val productRepository: ProductRepository,
    private val mapper: ObjectMapper
) {

    @Autowired
    lateinit var mockMvc: MockMvc


    val signUpRequest = SignUpRequest("inu", "test", ROLE.ADMIN)
    lateinit var customer: Customer
    val loginRequest = LoginRequest(signUpRequest.name, signUpRequest.pw)
    lateinit var product: Product
    val registerRequest = ProductRegisterRequest("book1", "book:product", LocalTime.of(10, 10), 100L)
    fun myIdentifier(methodName: String) = "{class-name}/$methodName"


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
        val found =
            productRepository.findById(product.id).orElse(null) ?: throw CustomException.ProductNotFoundException()

    }

    @AfterEach
    fun delete() {
        productRepository.deleteAll()
        customerRepository.deleteAll()

    }

    /*@PostMapping("/register")
    fun register(
        @RequestBody productRegisterRequest: ProductRegisterRequest,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ProductDto = productService.register(admin.id, productRegisterRequest)
*/
    @Test
    fun register() {
        val registerRequest = ProductRegisterRequest("book2", "product:book2", LocalTime.of(11, 30, 0), 100L)
        mockMvc.perform(
            RestDocumentationRequestBuilders.post(
                "/register"
            ).content(mapper.writeValueAsString(registerRequest)).sessionAttrs(mapOf("user" to CustomerDto(customer)))
                .contentType(
                    MediaType.APPLICATION_JSON
                ).accept(MediaType.APPLICATION_JSON)
        ).andExpect(
            status().isOk
        ).andExpect(jsonPath("$.id").isString)
            .andExpect(jsonPath("$.name").value(registerRequest.name))
            .andExpect(jsonPath("$.description").value(registerRequest.description))
            .andExpect(
                jsonPath("$.appointedTime").value(
                    registerRequest.appointedTime.format(
                        DateTimeFormatter.ofPattern(
                            "HH:mm:ss"
                        )
                    )
                )
            )
            .andExpect(jsonPath("$.appointedQuantity").value(registerRequest.appointedQuantity))
            .andDo(
                document(
                    myIdentifier("상품등록"),
                    requestFields(
                        fieldWithPath("name").type(STRING).description("상품명"),
                        fieldWithPath("description").type(STRING).description("상품 설명"),
                        fieldWithPath("appointedTime").type(DATETIME).description("상품 거래 시간"),
                        fieldWithPath("appointedQuantity").type(NUMBER).description("상품 거래 수량")
                    ),
                    responseFields(
                        fieldWithPath("id").type(STRING).description("상품 id"),
                        fieldWithPath("name").type(STRING).description("상품명"),
                        fieldWithPath("description").type(STRING).description("상품 설명"),
                        fieldWithPath("appointedTime").type(DATETIME).description("상품 거래 시간"),
                        fieldWithPath("appointedQuantity").type(NUMBER).description("상품 거래 수량")

                    )
                )
            )
    }

    /*@PutMapping("/product/{productId}")
    fun update(
        @PathVariable("productId") productId: UUID,
        @RequestBody productUpdateRequest: ProductUpdateRequest,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ProductDto = productService.update(productId, admin.id, productUpdateRequest)
    */
    @Test
    fun update() {
        val updateRequest = ProductUpdateRequest(
            "book3",
            "product:book3",
            registerRequest.appointedTime.plusHours(1),
            registerRequest.appointedQuantity.plus(10L)
        )
        mockMvc.perform(
            RestDocumentationRequestBuilders.put("/product/{productId}", product.id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest)).sessionAttrs(mapOf("user" to CustomerDto(customer)))
        ).andExpect(status().isOk)

            .andExpect(jsonPath("$.id").value(product.id.toString()))
            .andExpect(jsonPath("$.name").value(updateRequest.name))
            .andExpect(jsonPath("$.description").value(updateRequest.description))
            .andExpect(
                jsonPath("$.appointedTime").value(
                    mapper.writeValueAsString(updateRequest.appointedTime).trim { it == '\"' })
            )
            .andDo(
                document(
                    myIdentifier("상품수정"),
                    pathParameters(parameterWithName("productId").description("상품 id")),
                    requestFields(
                        fieldWithPath("name").optional().type(STRING).description("상품명"),
                        fieldWithPath("description").optional().type(STRING).description("상품 설명"),
                        fieldWithPath("appointedTime").optional().type(DATETIME).description("상품 거래 시간"),
                        fieldWithPath("appointedQuantity").optional().type(NUMBER).description("상품 거래 수량")
                    ),
                    responseFields(
                        fieldWithPath("id").type(STRING).description("상품 id"),
                        fieldWithPath("name").type(STRING).description("상품명"),
                        fieldWithPath("description").type(STRING).description("상품 설명"),
                        fieldWithPath("appointedTime").type(DATETIME).description("상품 거래 시간"),
                        fieldWithPath("appointedQuantity").type(NUMBER).description("상품 거래 수량")
                    )
                )
            )
    }

    /*
    @DeleteMapping("/product/{productId}")
    fun delete(
        @PathVariable("productId") productId: UUID,
        @SessionLogin(admin = true) admin: CustomerDto
    ): ResponseEntity<String> {
        productService.delete(productId, admin.id)
        return ResponseEntity.ok().build()
    }*/
    @Test
    fun deleteUser() {
        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/user").contentType(MediaType.APPLICATION_JSON)
                .sessionAttrs(mapOf("user" to CustomerDto(customer)))
        ).andExpect(status().isOk)
            .andDo(
                document(
                    myIdentifier("회원탈퇴"),
                    requestBody(), responseBody()
                )
            )
    }

    /*
        @GetMapping("/product/{productId}")
        fun productDetail(
            @PathVariable("productId") productId: UUID,
            @SessionLogin customer: CustomerDto
        ): ResponseEntity<ProductDto> = productService.productDetail(productId).let { ResponseEntity.ok(it) }
        */
    @Test
    fun productDetail() {
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/product/{productId}", product.id)
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttrs(mapOf("user" to CustomerDto(customer)))
        ).andExpect(status().isOk)

            .andExpect(jsonPath("$.id").value(product.id.toString()))
            .andExpect(jsonPath("$.name").value(product.name))
            .andExpect(jsonPath("$.description").value(product.description))
            .andExpect(
                jsonPath("$.appointedTime").value(mapper.writeValueAsString(product.appointedTime).trim('\"'))
            )
            .andDo(
                document(
                    myIdentifier("상품상세"),
                    pathParameters(parameterWithName("productId").description("상품 id")),
                    responseFields(
                        fieldWithPath("id").type(STRING).description("상품 id"),
                        fieldWithPath("name").type(STRING).description("상품명"),
                        fieldWithPath("description").type(STRING).description("상품 설명"),
                        fieldWithPath("appointedTime").type(DATETIME).description("상품 거래 시간"),
                        fieldWithPath("appointedQuantity").type(NUMBER).description("상품 거래 수량")
                    )
                )
            )
    }

    /*
        @GetMapping("/products/admin")
        fun productListAdmin(
            @SessionLogin(admin = true) admin: CustomerDto,
            pageable: Pageable
        ): ResponseEntity<Page<ProductDto>> =
            productService.getAllProductsRegistered(admin.id, pageable).let { ResponseEntity.ok(it) }
        */
    @Test
    fun productListAdmin() {
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/products/admin").accept(MediaType.APPLICATION_JSON)
                .sessionAttrs(mapOf("user" to CustomerDto(customer)))
        ).andExpect(status().isOk)
            .andDo(
                document(
                    myIdentifier("상품목록/어드민"),
                    queryParameters(
                        parameterWithName("page").optional().description("페이지 번호"),
                        parameterWithName("size").optional().description("페이지당 크기"),
                        parameterWithName("sort").optional().description("정렬 기준")
                    ),
                    relaxedResponseFields(
                        fieldWithPath("content[].id").type(STRING).description("상품 id"),
                        fieldWithPath("content[].name").type(STRING).description("상품명"),
                        fieldWithPath("content[].description").type(STRING).description("상품 설명"),
                        fieldWithPath("content[].appointedTime").type(DATETIME).description("상품 거래 시간"),
                        fieldWithPath("content[].appointedQuantity").type(NUMBER).description("상품 거래 수량"),
                        fieldWithPath("last").type(BOOLEAN).description("Whether this is the last page"),
                        fieldWithPath("totalPages").type(NUMBER).description("The total number of pages"),
                        fieldWithPath("totalElements").type(NUMBER).description("The total number of elements"),
                        fieldWithPath("size").type(NUMBER).description("The size of the page"),
                        fieldWithPath("number").type(NUMBER).description("The current page number"),
                        fieldWithPath("first").type(BOOLEAN).description("Whether this is the first page"),
                        fieldWithPath("numberOfElements").type(NUMBER)
                            .description("The number of elements on this page"),
                        fieldWithPath("empty").type(BOOLEAN).description("Whether this page is empty")
                    ),
                )
            )
    }

    /*

    @GetMapping("/products/user")
    fun productListUser(
        @SessionLogin loginUser: CustomerDto,
        @ModelAttribute productListCondition: ProductListCondition,
        pageable: Pageable
    ): ResponseEntity<Page<ProductDto>> = productService.getAllProductsBought(loginUser.id, productListCondition, pageable)
            .let { ResponseEntity.ok(it) }
    }*/
    @Test
    fun productListUser() {
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/products/user").accept(MediaType.APPLICATION_JSON)
                .sessionAttrs(mapOf("user" to CustomerDto(customer)))
        ).andExpect(status().isOk)
            .andDo(
                document(
                    myIdentifier("상품목록/일반"),
                    queryParameters(
                        parameterWithName("page").optional().description("페이지 번호"),
                        parameterWithName("size").optional().description("페이지당 크기"),
                        parameterWithName("sort").optional().description("정렬 기준"),
                        parameterWithName("from").optional().description("날짜 시작 기준"),
                        parameterWithName("to").optional().description("날짜 종료 기준"),
                    ),
                    relaxedResponseFields(
                        fieldWithPath("content[].id").type(STRING).description("상품 id"),
                        fieldWithPath("content[].name").type(STRING).description("상품명"),
                        fieldWithPath("content[].description").type(STRING).description("상품 설명"),
                        fieldWithPath("content[].appointedTime").type(DATETIME).description("상품 거래 시간"),
                        fieldWithPath("content[].appointedQuantity").type(NUMBER).description("상품 거래 수량"),
                        fieldWithPath("last").type(BOOLEAN).description("Whether this is the last page"),
                        fieldWithPath("totalPages").type(NUMBER).description("The total number of pages"),
                        fieldWithPath("totalElements").type(NUMBER).description("The total number of elements"),
                        fieldWithPath("size").type(NUMBER).description("The size of the page"),
                        fieldWithPath("number").type(NUMBER).description("The current page number"),
                        fieldWithPath("first").type(BOOLEAN).description("Whether this is the first page"),
                        fieldWithPath("numberOfElements").type(NUMBER)
                            .description("The number of elements on this page"),
                        fieldWithPath("empty").type(BOOLEAN).description("Whether this page is empty")
                    ),
                )
            )
    }
}