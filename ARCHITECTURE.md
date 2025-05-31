# Architecture Documentation

## Overview

The application follows the MVVM (Model-View-ViewModel) architecture pattern, which is part of the Android Architecture Components. This architecture provides a clean separation of concerns and makes the app more maintainable and testable.

## Architecture Diagram

```plantuml
@startuml
package "Presentation Layer" {
    [Activity/Fragment] as UI
    [ViewModel] as VM
}

package "Domain Layer" {
    [UseCase] as UC
    [Repository Interface] as RI
}

package "Data Layer" {
    [Repository Implementation] as RI
    [API Service] as API
    [Local Database] as DB
}

UI --> VM
VM --> UC
UC --> RI
RI --> API
RI --> DB

note right of UI
  Handles UI events and
  displays data
end note

note right of VM
  Manages UI state and
  business logic
end note

note right of UC
  Contains business rules
  and use cases
end note

note right of RI
  Coordinates data sources
  and caching
end note
@enduml
```

## Component Details

### 1. Presentation Layer

#### Activities
- `MainActivity`: Main entry point, displays product list
- `ProductDetailActivity`: Shows detailed product information
- `CartActivity`: Manages shopping cart

#### ViewModels
- `ProductListViewModel`: Manages product list state
- `ProductDetailViewModel`: Manages product details state
- `CartViewModel`: Manages shopping cart state

### 2. Domain Layer

#### Use Cases
- `GetProductsUseCase`: Fetches product list
- `GetProductDetailsUseCase`: Fetches single product details
- `AddToCartUseCase`: Adds product to cart

#### Repository Interfaces
- `ProductRepository`: Defines product data operations
- `CartRepository`: Defines cart operations

### 3. Data Layer

#### Repositories
- `ProductRepositoryImpl`: Implements product data operations
- `CartRepositoryImpl`: Implements cart operations

#### Data Sources
- Remote: Fake Store API
- Local: Room Database

## Data Flow

1. **Product List Flow**
```plantuml
@startuml
actor User
participant "MainActivity" as MA
participant "ProductListViewModel" as VM
participant "ProductRepository" as REPO
participant "API Service" as API

User -> MA: Launch App
MA -> VM: Initialize
VM -> REPO: Get Products
REPO -> API: Fetch Products
API --> REPO: Return Products
REPO --> VM: Update Products
VM --> MA: Update UI
@enduml
```

2. **Product Detail Flow**
```plantuml
@startuml
actor User
participant "ProductDetailActivity" as PDA
participant "ProductDetailViewModel" as VM
participant "ProductRepository" as REPO
participant "API Service" as API

User -> PDA: Select Product
PDA -> VM: Load Product
VM -> REPO: Get Product Details
REPO -> API: Fetch Product
API --> REPO: Return Product
REPO --> VM: Update Product
VM --> PDA: Update UI
@enduml
```

## Navigation Flow

```plantuml
@startuml
[*] --> MainActivity
MainActivity --> ProductDetailActivity: Select Product
MainActivity --> CartActivity: View Cart
ProductDetailActivity --> CartActivity: Add to Cart
CartActivity --> [*]
@enduml
```

## Dependency Injection

The app uses manual dependency injection for simplicity. Each component is responsible for creating its dependencies:

```kotlin
class ProductListViewModel(
    private val repository: ProductRepository
) : ViewModel() {
    // ViewModel implementation
}
```

## Error Handling

The app implements a consistent error handling strategy:

1. **Network Errors**
   - Retry mechanism with exponential backoff
   - Offline caching
   - User-friendly error messages

2. **Data Validation**
   - Input validation in ViewModels
   - Data sanitization in Repositories

3. **UI Error States**
   - Loading states
   - Error states
   - Empty states

## Testing Strategy

### Unit Tests
- ViewModel tests
- Repository tests
- Use case tests

### Integration Tests
- Repository integration tests
- API integration tests

### UI Tests
- Activity tests
- Fragment tests
- Navigation tests

## Performance Considerations

1. **Image Loading**
   - Glide for efficient image loading
   - Image caching
   - Lazy loading in RecyclerView

2. **Network Optimization**
   - Response caching
   - Request batching
   - Pagination

3. **Memory Management**
   - ViewModel scoping
   - Resource cleanup
   - Memory leak prevention 