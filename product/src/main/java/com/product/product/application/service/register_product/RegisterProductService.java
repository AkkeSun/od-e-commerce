package com.product.product.application.service.register_product;

import com.product.global.util.JwtUtil;
import com.product.global.util.SnowflakeGenerator;
import com.product.product.application.port.in.RegisterProductUseCase;
import com.product.product.application.port.in.command.RegisterProductCommand;
import com.product.product.application.port.out.RegisterProductEsPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.Product;
import io.jsonwebtoken.Claims;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class RegisterProductService implements RegisterProductUseCase {

    private final JwtUtil jwtUtil;
    private final SnowflakeGenerator snowflakeGenerator;
    private final RegisterProductPort registerProductPort;
    private final RegisterProductEsPort registerProductEsPort;

    @Override
    @NewSpan
    public RegisterProductServiceResponse registerProduct(RegisterProductCommand command) {
        Claims claims = jwtUtil.getClaims(command.authorization());
        Product product = new Product().of(command, claims);
        product.updateProductId(snowflakeGenerator.nextId());
        Product savedProduct = registerProductPort.register(product);
        registerProductEsPort.register(savedProduct);
        return RegisterProductServiceResponse.builder()
            .productId(savedProduct.getProductId())
            .sellerEmail(savedProduct.getSellerEmail())
            .productName(savedProduct.getProductName())
            .productImg(savedProduct.getProductImg())
            .description(savedProduct.getDescription())
            .price(savedProduct.getPrice())
            .quantity(savedProduct.getQuantity())
            .category(savedProduct.getCategory())
            .build();
    }
}
