package com.store.storeproductapi.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storeproductapi.models.CategoryModel;
import com.store.storeproductapi.repositories.CategoryRepository;
import com.store.storeproductapi.services.impl.CategoryServiceImpl;

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
class CategoryServiceTest {
    
    @MockBean
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @BeforeEach
    void setup() {
        categoryService = new CategoryServiceImpl(categoryRepository);
    }

    @AfterEach
    void after() {
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void findById() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final String id = category.getId();

        when(this.categoryRepository.findById(id)).thenReturn(Optional.of(category));

        final CategoryModel result = this.categoryService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(category);
        assertThat(result.getId()).isEqualTo(category.getId());
        assertThat(result.getTitle()).isEqualTo(category.getTitle());
        assertThat(result.getProductIds()).isEqualTo(category.getProductIds());
        assertThat(result.getVisible()).isEqualTo(category.getVisible());

        verify(this.categoryRepository).findById(id);
    }

    @Test
    void findById_categoryNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.categoryService.findById(id))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Category with id %s is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.categoryRepository).findById(id);
    }

    @Test
    void findByTitle() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final String title = category.getTitle();

        when(this.categoryRepository.findByTitle(title)).thenReturn(Optional.of(category));

        final CategoryModel result = this.categoryService.findByTitle(title);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(category);
        assertThat(result.getId()).isEqualTo(category.getId());
        assertThat(result.getTitle()).isEqualTo(category.getTitle());
        assertThat(result.getProductIds()).isEqualTo(category.getProductIds());
        assertThat(result.getVisible()).isEqualTo(category.getVisible());

        verify(this.categoryRepository).findByTitle(title);
    }

    @Test
    void findByTitle_categoryNotFound() {

        final String title = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.categoryRepository.findByTitle(title)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.categoryService.findByTitle(title))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Category with provided title %s is not found",
                    title
                )
            )
            .hasNoCause();

        verify(this.categoryRepository).findByTitle(title);
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll() {

        final List<CategoryModel> categories = PODAM_FACTORY.manufacturePojo(List.class, CategoryModel.class);

        when(this.categoryRepository.findAll()).thenReturn(categories);

        final List<CategoryModel> result = this.categoryService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSameSizeAs(categories);
        assertThat(result).isEqualTo(categories);

        verify(this.categoryRepository).findAll();
    }

    @Test
    void assignProductToCategory() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final String categoryId = category.getId();
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(this.categoryRepository.save(category)).thenReturn(category);

        final CategoryModel result = this.categoryService.assignProductToCategory(productId, categoryId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(category.getId());
        assertThat(result.getProductIds()).hasSameSizeAs(category.getProductIds());
        assertThat(result.getProductIds()).isEqualTo(category.getProductIds());

        verify(this.categoryRepository).findById(categoryId);
        verify(this.categoryRepository).save(category);
    }

    @Test
    void assignProductToCategory_categoryNotFound() {

        final String categoryId = PODAM_FACTORY.manufacturePojo(String.class);
        final String productId = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.categoryService.assignProductToCategory(productId, categoryId))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Category with id %s is not found",
                    categoryId
                )
            )
            .hasNoCause();

        verify(this.categoryRepository).findById(categoryId);
    }

    @Test
    void assignProductToCategory_productNotAdded() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final String categoryId = category.getId();
        final String productId = category.getProductIds().stream()
            .findAny().get();

        when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> this.categoryService.assignProductToCategory(productId, categoryId))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasMessageStartingWith(
                String.format(
                    "Not possible to add productId %s to the category with id %s",
                    productId,
                    categoryId
                )
            )
            .hasNoCause();

        verify(this.categoryRepository).findById(categoryId);
    }

    @Test
    @SuppressWarnings("unchecked")
    void assignProductsToCategory() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final Set<String> productIds = PODAM_FACTORY.manufacturePojo(Set.class, String.class);
        final String categoryId = category.getId();

        when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(this.categoryRepository.save(category)).thenReturn(category);

        final CategoryModel result = this.categoryService.assignProductsToCategory(productIds, categoryId);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(category);
        assertThat(result.getProductIds()).containsAll(productIds);
        assertThat(result.getProductIds()).isEqualTo(category.getProductIds());

        verify(this.categoryRepository).findById(categoryId);
        verify(this.categoryRepository).save(category);
    }

    @Test
    @SuppressWarnings("unchecked")
    void assignProductsToCategory_categoryNotFound() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final Set<String> productIds = PODAM_FACTORY.manufacturePojo(Set.class, String.class);
        final String categoryId = category.getId();

        when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.categoryService.assignProductsToCategory(productIds, categoryId))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Category with id %s is not found",
                    categoryId
                )
            )
            .hasNoCause();

        verify(this.categoryRepository).findById(categoryId);
    }

    @Test
    void assignProductsToCategory_productsExistsInCategory() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final String categoryId = category.getId();
        final Set<String> productIds = category.getProductIds();

        when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> this.categoryService.assignProductsToCategory(productIds, categoryId))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasMessageStartingWith(
                String.format(
                    "Not possible to add all productIds [%s] to the category with id %s",
                    productIds,
                    categoryId
                )
            )
            .hasNoCause();

        verify(this.categoryRepository).findById(categoryId);
    }

    @Test
    void createCategory() {

        final String title = PODAM_FACTORY.manufacturePojo(String.class);
        final String description = PODAM_FACTORY.manufacturePojo(String.class);
        final CategoryModel categoryModel = new CategoryModel()
            .title(title)
            .description(description);

        when(this.categoryRepository.findOne(any())).thenReturn(Optional.empty());
        when(this.categoryRepository.insert(categoryModel)).thenReturn(categoryModel);

        final CategoryModel result = this.categoryService.createCategory(title, description);

        assertThat(result.getTitle()).isEqualTo(categoryModel.getTitle());
        assertThat(result.getDescription()).isEqualTo(categoryModel.getDescription());

        verify(this.categoryRepository).findOne(any());
        verify(this.categoryRepository).insert(categoryModel);
    }

    @Test
    void removeCategory() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class);
        final String id = category.getId();

        when(this.categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(this.categoryRepository.save(category)).thenReturn(category);

        final CategoryModel result = this.categoryService.removeCategory(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(category);
        assertThat(result.getId()).isEqualTo(category.getId());
        assertThat(result.getVisible()).isFalse();

        verify(this.categoryRepository).findById(id);
        verify(this.categoryRepository).save(category);
    }

    @Test
    void removeCategory_categoryNotFound() {

        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.categoryService.removeCategory(id))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Category with id %s is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.categoryRepository).findById(id);
    }

    @Test
    void hardRemoveCategory() {
        
        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class)
            .visible(false);
        final String id = category.getId();

        when(this.categoryRepository.findById(id)).thenReturn(Optional.of(category));

        this.categoryService.hardRemoveCategory(id);

        verify(this.categoryRepository).findById(id);
        verify(this.categoryRepository).delete(category);
    }

    @Test
    void hardRemoveCategory_categoryNotFound() {
        
        final String id = PODAM_FACTORY.manufacturePojo(String.class);

        when(this.categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.categoryService.hardRemoveCategory(id))
            .isExactlyInstanceOf(StoreResourceNotFoundException.class)
            .hasMessageStartingWith(
                String.format(
                    "Category with id %s is not found",
                    id
                )
            )
            .hasNoCause();

        verify(this.categoryRepository).findById(id);
    }

    @Test
    void hardRemoveCategory_categoryIsVisible() {

        final CategoryModel category = PODAM_FACTORY.manufacturePojo(CategoryModel.class)
            .visible(true);
        final String id = category.getId();

        when(this.categoryRepository.findById(id)).thenReturn(Optional.of(category));

        assertThatThrownBy(() -> this.categoryService.hardRemoveCategory(id))
            .isExactlyInstanceOf(ResourceStateException.class)
            .hasMessageStartingWith(
                String.format(
                    "Removing category with id %s is not possible. Category: %s",
                    id,
                    category
                )
            )
            .hasNoCause();

        verify(this.categoryRepository).findById(id);
    }

}
