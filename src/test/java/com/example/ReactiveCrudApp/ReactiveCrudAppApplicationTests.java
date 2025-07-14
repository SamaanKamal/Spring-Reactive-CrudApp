package com.example.ReactiveCrudApp;

import com.example.ReactiveCrudApp.Controller.ProductController;
import com.example.ReactiveCrudApp.Dto.ProductDto;
import com.example.ReactiveCrudApp.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class ReactiveCrudAppApplicationTests {

	private final WebTestClient webTestClient;

	@Autowired
	public ReactiveCrudAppApplicationTests(WebTestClient webTestClient) {
		this.webTestClient = webTestClient;
	}

	@MockitoBean
	private ProductService productService;

	@Test
	public void createProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "type c" , 2,1500));
		ProductDto productDto = new ProductDto("101", "type c" , 2,1500);

		when(productService.saveProduct(productDto)).thenReturn(productDtoMono);

		webTestClient.post().uri("/products/create-product").body(productDtoMono, ProductDto.class).exchange().expectStatus().isCreated();

	}

	@Test
	public void getProductsTest(){
		Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("101", "type c" , 2,1500), new ProductDto("102", "type A" , 5,100));

		when(productService.getProducts()).thenReturn(productDtoFlux);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/products").exchange().expectStatus().isOk().returnResult(ProductDto.class).getResponseBody();

		StepVerifier.create(responseBody).expectNext(new ProductDto("101", "type c" , 2,1500)).expectNext(new ProductDto("102", "type A" , 5,100)).verifyComplete();
	}

	@Test
	public void getProductByIdTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "type c" , 2,1500));

		when(productService.getProductById(anyString())).thenReturn(productDtoMono);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/101").exchange().expectStatus().isOk().returnResult(ProductDto.class).getResponseBody();

		StepVerifier.create(responseBody).expectNext(new ProductDto("101", "type c" , 2,1500)).verifyComplete();
	}

	@Test
	public void updateProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("101", "type c" , 2,1500));
		ProductDto productDto = new ProductDto("101", "type c" , 2,1500);
		when(productService.updateProduct(productDto)).thenReturn(productDtoMono);

		Flux<ProductDto> responseBody = webTestClient.put().uri("/products/update-product/101").body(productDtoMono, ProductDto.class).exchange().expectStatus().isOk().returnResult(ProductDto.class).getResponseBody();

		StepVerifier.create(responseBody).expectNext(new ProductDto("101", "type c" , 2,1500)).verifyComplete();
	}

	@Test
	public void deleteProductByIdTest(){
		String response = "deleted Product with Id: "+ "101";
		Mono<String> StringMono = Mono.just(response);

		when(productService.deleteProduct(anyString())).thenReturn(StringMono);

		Flux<String> responseBody = webTestClient.delete().uri("/products/delete-product/101").exchange().expectStatus().isNoContent().returnResult(String.class).getResponseBody();

		StepVerifier.create(responseBody).expectNext(response).verifyComplete();
	}



}
