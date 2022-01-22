package com.store.storeproductapi.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import com.store.storeproductapi.models.ProductModel;
import com.store.storeproductapi.models.WishlistModel;
import com.store.storeproductapi.transferobjects.ProductTO;
import com.store.storeproductapi.transferobjects.WishlistTO;
import com.store.storesharedmodule.utils.ArgumentVerifier;

public class WishlistMapper {
    
    private WishlistMapper() {

    }

    public static WishlistTO mapRepoToWishlistTO(final WishlistModel wishlistModel, final Set<ProductModel> productModels) {
        ArgumentVerifier.verifyNotNull(wishlistModel, productModels);

        final Set<ProductTO> productTOs = productModels.stream().map(ProductMapper::mapRepoToProductTO)
                .collect(Collectors.toSet());

        return new WishlistTO()
                .id(wishlistModel.getId())    
                .products(productTOs);
    }

}
