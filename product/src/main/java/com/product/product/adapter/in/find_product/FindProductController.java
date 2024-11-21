package com.product.product.adapter.in.find_product;

import com.product.product.application.port.out.FindProductPort;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindProductController {

    private final FindProductPort findProductPort;

    @GetMapping("/find")
    void findProduct(Long id) {
        findProductPort.findById(id);
    }
}
