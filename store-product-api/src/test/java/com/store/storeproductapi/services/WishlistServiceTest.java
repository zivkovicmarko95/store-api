package com.store.storeproductapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.WishlistModel;
import com.store.storeproductapi.repositories.WishlistRepository;
import com.store.storeproductapi.services.impl.WishlistServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
class WishlistServiceTest {

    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @MockBean
    private WishlistRepository wishlistRepository;

    private WishlistService wishlistService;

    @BeforeEach
    void setup() {
        wishlistService = new WishlistServiceImpl(wishlistRepository);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(this.wishlistRepository);
    }
    
    @Test
    void findById() {

        final WishlistModel wishlist = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final String id = wishlist.getId();

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));

        final WishlistModel result = this.wishlistService.findById(id);

        assertWishlist(result, wishlist);

        verify(this.wishlistRepository).findById(id);
    }

    @Test
    void findById_wishlistNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.wishlistService.findById(id))
                .isExactlyInstanceOf(StoreResourceNotFoundException.class)
                .hasMessageStartingWith(
                    String.format("Wishlist with id %s is not found.", id)
                )
                .hasNoCause();

        verify(this.wishlistRepository).findById(id);
    }

    @Test
    void findByAccountId() {

        final WishlistModel wishlist = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final String accountId = wishlist.getAccountId();

        when(this.wishlistRepository.findByAccountId(accountId)).thenReturn(Optional.of(wishlist));

        final WishlistModel result = this.wishlistService.findByAccountId(accountId);

        assertWishlist(result, wishlist);

        verify(this.wishlistRepository).findByAccountId(accountId);
    }

    @Test
    void createWishlist() {

        final String accountId = PODAM_FACTORY.manufacturePojo(String.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final WishlistModel wishlist = new WishlistModel(accountId, Set.of(productId));

        when(this.wishlistRepository.insert(wishlist)).thenReturn(wishlist);

        final WishlistModel result = this.wishlistService.createWishlist(accountId, productId);

        assertWishlist(result, wishlist);

        verify(this.wishlistRepository).insert(wishlist);
    }

    @Test
    void addProductToWishlist() {

        final WishlistModel wishlist = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final String id = wishlist.getId();

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));
        when(this.wishlistRepository.save(wishlist)).thenReturn(wishlist);

        final WishlistModel result = this.wishlistService.addProductToWishlist(id, productId);

        assertWishlist(result, wishlist);

        verify(this.wishlistRepository).findById(id);
        verify(this.wishlistRepository).save(wishlist);
    }

    @Test
    void addProductToWishlist__wishlistNotFound() {
        
        final String id = PODAM_FACTORY.manufacturePojo(String.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.wishlistService.addProductToWishlist(id, productId))
                .isExactlyInstanceOf(StoreResourceNotFoundException.class)
                .hasMessageStartingWith(
                    String.format("Wishlist with id %s is not found.", id)
                )
                .hasNoCause();

        verify(this.wishlistRepository).findById(id);
    }

    @Test
    void addProductToWishlist_productExists() {
        
        final WishlistModel wishlist = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final String productId = wishlist.getProductIds().stream()
                .findAny().get();
        final String id = wishlist.getId();

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));

        assertThatThrownBy(() -> this.wishlistService.addProductToWishlist(id, productId))
                .isExactlyInstanceOf(ResourceStateException.class)
                .hasMessageStartingWith(
                    String.format("In the wishlist with id %s, product with id %s already exists.", id, productId)
                )
                .hasNoCause();

        verify(this.wishlistRepository).findById(id);
    }

    @Test
    void removeProductFromWishlist() {

        final WishlistModel wishlist = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final String productId = wishlist.getProductIds().stream()
                .findAny().get();
        final String id = wishlist.getId();

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));
        when(this.wishlistRepository.save(wishlist)).thenReturn(wishlist);

        final WishlistModel result = this.wishlistService.removeProductFromWishlist(id, productId);

        assertWishlist(result, wishlist);

        verify(this.wishlistRepository).findById(id);
        verify(this.wishlistRepository).save(wishlist);
    }

    @Test
    void removeProductFromWishlist_wishlistNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.wishlistService.removeProductFromWishlist(id, productId))
                .isExactlyInstanceOf(StoreResourceNotFoundException.class)
                .hasMessageStartingWith(
                    String.format("Wishlist with id %s is not found.", id)
                )
                .hasNoCause();

        verify(this.wishlistRepository).findById(id);
    }

    @Test
    void removeProductFromWishlist_productNotFound() {

        final WishlistModel wishlist = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);
        final String id = wishlist.getId();

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));

        assertThatThrownBy(() -> this.wishlistService.removeProductFromWishlist(id, productId))
                .isExactlyInstanceOf(ResourceStateException.class)
                .hasMessageStartingWith(
                    String.format("In the wishlist with id %s, product with id %s does not exist.", id, productId)
                )
                .hasNoCause();

        verify(this.wishlistRepository).findById(id);
    }

    @Test
    void deleteWishlistById() {

        final WishlistModel wishlist = PODAM_FACTORY.manufacturePojo(WishlistModel.class);
        final String id = wishlist.getId();

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.of(wishlist));

        this.wishlistService.deleteWishlistById(id);

        verify(this.wishlistRepository).findById(id);
        verify(this.wishlistRepository).delete(wishlist);
    }

    @Test
    void deleteWishlistById_wishlistNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.wishlistRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.wishlistService.deleteWishlistById(id))
                .isExactlyInstanceOf(StoreResourceNotFoundException.class)
                .hasMessageStartingWith(
                    String.format("Wishlist with id %s is not found.", id)
                )
                .hasNoCause();

        verify(this.wishlistRepository).findById(id);
    }

    private void assertWishlist(final WishlistModel result, final WishlistModel wishlist) {

        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(wishlist.getAccountId());
        assertThat(result.getId()).isEqualTo(wishlist.getId());
        assertThat(result.getProductIds()).containsAll(wishlist.getProductIds());

    }

} 
