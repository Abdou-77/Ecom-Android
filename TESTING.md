# Testing Documentation

## Overview

This document outlines the testing strategy and implementation details for the e-commerce Android application. The app uses a combination of unit tests, integration tests, and UI tests to ensure quality and reliability.

## Test Types

### 1. Unit Tests

Unit tests are written using JUnit 4 and MockK for mocking dependencies.

#### Example: ProductDetailViewModel Test

```kotlin
class ProductDetailViewModelTest {
    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var repository: ProductRepository
    
    @Before
    fun setup() {
        repository = mockk()
        viewModel = ProductDetailViewModel(repository)
    }
    
    @Test
    fun `when product is loaded successfully, state is updated`() = runTest {
        // Given
        val product = Product(
            id = 1,
            title = "Test Product",
            price = 99.99,
            description = "Test Description",
            category = "Test Category",
            image = "test.jpg",
            rating = Rating(4.5, 100)
        )
        coEvery { repository.getProduct(1) } returns product
        
        // When
        viewModel.loadProduct(1)
        
        // Then
        assertEquals(product, viewModel.product.value)
    }
}
```

### 2. Integration Tests

Integration tests verify the interaction between different components of the app.

#### Example: ProductRepository Integration Test

```kotlin
class ProductRepositoryIntegrationTest {
    private lateinit var repository: ProductRepositoryImpl
    private lateinit var apiService: ApiService
    
    @Before
    fun setup() {
        apiService = mockk()
        repository = ProductRepositoryImpl(apiService)
    }
    
    @Test
    fun `when API returns products, repository returns same products`() = runTest {
        // Given
        val products = listOf(
            Product(1, "Test", 99.99, "Desc", "Cat", "img.jpg", Rating(4.5, 100))
        )
        coEvery { apiService.getProducts() } returns products
        
        // When
        val result = repository.getProducts()
        
        // Then
        assertEquals(products, result)
    }
}
```

### 3. UI Tests

UI tests are written using Espresso to verify the user interface behavior.

#### Example: ProductDetailActivity Test

```kotlin
@RunWith(AndroidJUnit4::class)
class ProductDetailActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(ProductDetailActivity::class.java)
    
    @Test
    fun whenProductLoaded_displaysCorrectInformation() {
        // Verify product title is displayed
        onView(withId(R.id.productTitle))
            .check(matches(withText("Test Product")))
            
        // Verify price is displayed
        onView(withId(R.id.productPrice))
            .check(matches(withText("$99.99")))
            
        // Verify rating is displayed
        onView(withId(R.id.productRating))
            .check(matches(withRating(4.5f)))
    }
}
```

## Test Coverage

The app aims for high test coverage, particularly in critical areas:

- ViewModels: 90% coverage
- Repositories: 85% coverage
- Use Cases: 80% coverage
- Activities: 70% coverage

## Running Tests

### Unit Tests

```bash
./gradlew test
```

### Instrumentation Tests

```bash
./gradlew connectedAndroidTest
```

### All Tests

```bash
./gradlew check
```

## Test Data

Test data is managed through the following approaches:

1. **Mock Data**
   - Static test data in test classes
   - MockK for mocking dependencies

2. **Test Fixtures**
   - JSON files for API responses
   - Database fixtures for local storage tests

## Continuous Integration

Tests are automatically run on:

1. Every pull request
2. Every merge to main branch
3. Nightly builds

## Best Practices

1. **Test Naming**
   - Use descriptive names that explain the test scenario
   - Follow the pattern: `when[Condition]_then[ExpectedResult]`

2. **Test Organization**
   - Group related tests in test classes
   - Use `@Before` for common setup
   - Use `@After` for cleanup

3. **Test Independence**
   - Each test should be independent
   - Avoid test interdependencies
   - Clean up after each test

4. **Mocking**
   - Mock external dependencies
   - Use realistic test data
   - Verify mock interactions

## Common Test Scenarios

1. **Network Tests**
   - Success scenarios
   - Error scenarios
   - Timeout scenarios
   - Offline scenarios

2. **UI Tests**
   - Navigation flows
   - User interactions
   - Error states
   - Loading states

3. **Data Tests**
   - Data persistence
   - Data retrieval
   - Data updates
   - Data deletion

## Troubleshooting

Common issues and solutions:

1. **Test Failures**
   - Check test data
   - Verify mock setup
   - Check timing issues
   - Verify test environment

2. **CI Issues**
   - Check build configuration
   - Verify test dependencies
   - Check test environment setup
   - Review test logs 