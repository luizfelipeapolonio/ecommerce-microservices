package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class CategoryEntityMapperTest {

  @Spy
  private CategoryEntityMapper entityMapper;

  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("convertEntityToDomainWithNoParentCategory - Should successfully convert a CategoryEntity to Category domain")
  void convertEntityToDomainWithNoParentCategory() {
    CategoryEntity categoryEntity = this.dataMock.getCategoriesEntity().getFirst();
    Category categoryDomain = this.dataMock.getCategoriesDomain().getFirst();

    Category convertedCategory = this.entityMapper.toDomain(categoryEntity);

    assertThat(convertedCategory.getId()).isEqualTo(categoryDomain.getId());
    assertThat(convertedCategory.getName()).isEqualTo(categoryDomain.getName());
    assertThat(convertedCategory.getCreatedAt()).isEqualTo(categoryDomain.getCreatedAt());
    assertThat(convertedCategory.getUpdatedAt()).isEqualTo(categoryDomain.getUpdatedAt());
    assertThat(convertedCategory.getParentCategory()).isNull();

    verify(this.entityMapper, times(1)).toDomain(any(CategoryEntity.class));
  }

  @Test
  @DisplayName("convertEntityToDomainWithParentCategory - Should successfully convert a CategoryEntity with parent category to Category domain")
  void convertEntityToDomainWithParentCategory() {
    CategoryEntity parentCategory = this.dataMock.getCategoriesEntity().getFirst();
    CategoryEntity subCategory = new CategoryEntity();
    subCategory.setId(2L);
    subCategory.setName("Motherboards");
    subCategory.setCreatedAt(LocalDateTime.parse("2025-07-19T19:11:19.974472280"));
    subCategory.setUpdatedAt(LocalDateTime.parse("2025-07-19T19:11:19.974472280"));
    subCategory.setParentCategory(parentCategory);

    Category convertedCategory = this.entityMapper.toDomain(subCategory);

    assertThat(convertedCategory.getId()).isEqualTo(subCategory.getId());
    assertThat(convertedCategory.getName()).isEqualTo(subCategory.getName());
    assertThat(convertedCategory.getCreatedAt()).isEqualTo(subCategory.getCreatedAt());
    assertThat(convertedCategory.getUpdatedAt()).isEqualTo(subCategory.getCreatedAt());
    // Parent category from converted category assertions
    assertThat(convertedCategory.getParentCategory()).isNotNull();
    assertThat(convertedCategory.getParentCategory().getId()).isEqualTo(parentCategory.getId());
    assertThat(convertedCategory.getParentCategory().getName()).isEqualTo(parentCategory.getName());
    assertThat(convertedCategory.getParentCategory().getCreatedAt()).isEqualTo(parentCategory.getCreatedAt());
    assertThat(convertedCategory.getParentCategory().getUpdatedAt()).isEqualTo(parentCategory.getUpdatedAt());
    assertThat(convertedCategory.getParentCategory().getParentCategory()).isNull();

    verify(this.entityMapper, times(2)).toDomain(any(CategoryEntity.class));
    verify(this.entityMapper, times(1)).toDomain(subCategory);
    verify(this.entityMapper, times(1)).toDomain(parentCategory);
  }

  @Test
  @DisplayName("convertDomainToEntityWithNoParentCategory - Should successfully convert a Category domain to CategoryEntity")
  void convertDomainToEntityWithNoParentCategory() {
    Category categoryDomain = this.dataMock.getCategoriesDomain().getFirst();

    CategoryEntity convertedCategory = this.entityMapper.toEntity(categoryDomain);

    assertThat(convertedCategory.getId()).isEqualTo(categoryDomain.getId());
    assertThat(convertedCategory.getName()).isEqualTo(categoryDomain.getName());
    assertThat(convertedCategory.getCreatedAt()).isEqualTo(categoryDomain.getCreatedAt());
    assertThat(convertedCategory.getUpdatedAt()).isEqualTo(categoryDomain.getUpdatedAt());
    assertThat(convertedCategory.getParentCategory()).isNull();

    verify(this.entityMapper, times(1)).toEntity(any(Category.class));
  }

  @Test
  @DisplayName("convertDomainToEntityWithParentCategory - Should successfully convert a Category domain with parent category to CategoryEntity")
  void convertDomainToEntityWithParentCategory() {
    Category categoryDomain = this.dataMock.getCategoriesDomain().get(1);

    CategoryEntity convertedCategory = this.entityMapper.toEntity(categoryDomain);

    assertThat(convertedCategory.getId()).isEqualTo(categoryDomain.getId());
    assertThat(convertedCategory.getName()).isEqualTo(categoryDomain.getName());
    assertThat(convertedCategory.getCreatedAt()).isEqualTo(categoryDomain.getCreatedAt());
    assertThat(convertedCategory.getUpdatedAt()).isEqualTo(categoryDomain.getUpdatedAt());
    assertThat(convertedCategory.getParentCategory()).isNotNull();
    assertThat(convertedCategory.getParentCategory().getId()).isEqualTo(categoryDomain.getParentCategory().getId());
    assertThat(convertedCategory.getParentCategory().getName()).isEqualTo(categoryDomain.getParentCategory().getName());
    assertThat(convertedCategory.getParentCategory().getCreatedAt()).isEqualTo(categoryDomain.getParentCategory().getCreatedAt());
    assertThat(convertedCategory.getParentCategory().getUpdatedAt()).isEqualTo(categoryDomain.getParentCategory().getUpdatedAt());
    assertThat(convertedCategory.getParentCategory().getParentCategory()).isNull();

    verify(this.entityMapper, times(2)).toEntity(any(Category.class));
    verify(this.entityMapper, times(1)).toEntity(categoryDomain);
    verify(this.entityMapper, times(1)).toEntity(categoryDomain.getParentCategory());
  }
}
