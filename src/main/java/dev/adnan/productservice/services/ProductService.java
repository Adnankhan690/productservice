package dev.adnan.productservice.services;

import dev.adnan.productservice.thirdPartyClients.productservice.FakeStoreProductDTO;
import dev.adnan.productservice.DTO.GenericProductDTO;
import dev.adnan.productservice.exceptions.NotFoundException;

import java.util.List;

public interface ProductService {
    GenericProductDTO getProductById(Long id) throws NotFoundException;
    GenericProductDTO createProduct(GenericProductDTO product);
    GenericProductDTO updateProductById(GenericProductDTO product,Long id) throws NotFoundException;
    GenericProductDTO deleteById(Long id);
    List<GenericProductDTO> getAllProducts();
}
