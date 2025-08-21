package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(value = "test")
public class CategoryRepositoryTest {

  @Autowired
  EntityManager entityManager;

  @Autowired
  CategoryRepository categoryRepository;

  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("findAllSubcategoriesReturnsAllFoundSubcategories - Should find and return a list with all subcategories")
  void findAllSubcategoriesReturnsAllFoundSubcategories() {
    List<CategoryEntity> allCategories = this.dataMock.getCategoriesEntity()
      .stream()
      .peek(category -> category.setId(null))
      .toList();
    List<CategoryEntity> allSubcategories = allCategories.stream()
      .filter(category -> category.getParentCategory() != null)
      .toList();

    // Persisting category entities
    allCategories.forEach(this.entityManager::persist);

    List<CategoryEntity> subcategories = this.categoryRepository.findAllSubcategories();

    assertThat(subcategories.size()).isEqualTo(allSubcategories.size());
    assertThat(subcategories)
      .allSatisfy(subcategory -> assertThat(subcategory.getParentCategory()).isNotNull());
  }
}
