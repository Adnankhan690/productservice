package dev.adnan.productservice.services;

import dev.adnan.productservice.DTO.FakeStoreProductDTO;
import dev.adnan.productservice.DTO.GenericProductDTO;
import dev.adnan.productservice.exceptions.NotFoundException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {
    RestTemplateBuilder restTemplateBuilder;
    private final String baseSpecificProductRequestUrl = "https://fakestoreapi.com/products/{id}";
    private final String baseProductRequestUrl = "https://fakestoreapi.com/products";
    public FakeStoreProductService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }
    public GenericProductDTO convertFakeStoreProductIntoGenericProduct(FakeStoreProductDTO fakeStoreProductDTO) {
        GenericProductDTO genericProductDTO = new GenericProductDTO();

        genericProductDTO.setId(fakeStoreProductDTO.getId());
        genericProductDTO.setDescription(fakeStoreProductDTO.getDescription());
        genericProductDTO.setCategory(fakeStoreProductDTO.getCategory());
        genericProductDTO.setImage(fakeStoreProductDTO.getImage());
        genericProductDTO.setPrice(fakeStoreProductDTO.getPrice());
        genericProductDTO.setTitle(fakeStoreProductDTO.getTitle());
        return genericProductDTO;
    }
    @Override
    public GenericProductDTO createProduct(GenericProductDTO product) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<GenericProductDTO> response = restTemplate.postForEntity(baseProductRequestUrl,
                product, GenericProductDTO.class);

        return response.getBody();
    }
    @Override
    public GenericProductDTO getProductById(Long id) throws NotFoundException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        //Gets the response/calls the 3party APIs
        ResponseEntity<FakeStoreProductDTO> response =  restTemplate.getForEntity(baseSpecificProductRequestUrl,
                FakeStoreProductDTO.class, id);
        //Then 3party APIs JSON is converted into our FakeStoreProductDTO
        FakeStoreProductDTO fakeStoreProductDTO = response.getBody();
        //Exception
        if(fakeStoreProductDTO == null) {
            throw new NotFoundException("Product with id "+id+" doesn't exist");
        }
        GenericProductDTO product = convertFakeStoreProductIntoGenericProduct(fakeStoreProductDTO);
        //Now we will forward this information from 3party API which is converted into FakeStoreDTO to our clients

        return product;
    }

    @Override
    public GenericProductDTO deleteById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        RequestCallback requestCallback = restTemplate.acceptHeaderRequestCallback(FakeStoreProductDTO.class);
        ResponseExtractor<ResponseEntity<FakeStoreProductDTO>> responseExtractor =
                restTemplate.responseEntityExtractor(FakeStoreProductDTO.class);
        ResponseEntity<FakeStoreProductDTO> response = restTemplate.execute(baseSpecificProductRequestUrl, HttpMethod.DELETE,
                requestCallback, responseExtractor, id);

        FakeStoreProductDTO fakeStoreProductDTO = response.getBody();

        GenericProductDTO genericProductDTO = convertFakeStoreProductIntoGenericProduct(fakeStoreProductDTO);

        return genericProductDTO;
    }
    @Override
    public List<GenericProductDTO> getAllProducts() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        ResponseEntity<FakeStoreProductDTO[]> response = restTemplate.getForEntity(baseProductRequestUrl,
                FakeStoreProductDTO[].class);

        List<GenericProductDTO> productList = new ArrayList<>();

        for(FakeStoreProductDTO productVar : response.getBody()) {
            GenericProductDTO product = convertFakeStoreProductIntoGenericProduct(productVar);
            productList.add(product);
        }
        return productList;
    }

    @Override
    public GenericProductDTO updateProductById(Long id, FakeStoreProductDTO product) {
        return null;
    }

}
