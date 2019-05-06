package com.store.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {

    private Long userId;
    private Long productId;
    private String orderDescription;
    private String orderProduct;
    private String orderCode;

}
