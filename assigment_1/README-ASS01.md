# Assignment 01: SOLID Principles & Clean Architecture Refactoring

## Problem Analysis

The legacy `OrderService` class was a textbook "God Class" that violated all five SOLID principles and tangled high-level business rules with low-level implementation details. To resolve this, the system was refactored using **Clean Architecture**


, ensuring that the core domain logic remains completely isolated from infrastructure concerns (databases, UI, external services).

Below is an honest breakdown of each problem and the solution applied.

## Problems & Solutions

### 1. Single Responsibility Principle (SRP) Violations

**Problem:** `OrderService` handled at least 6 distinct responsibilities in one class:

* Product lookup and validation
* Discount/pricing calculation
* Stock management
* Payment processing logic and fee calculation
* Order persistence (in-memory arrays)
* Notification dispatch (logging, emails, analytics)

Every concern was entangled. Changing the email format meant reading through the entire class and hoping not to break the payment logic sitting three methods away.

**Solution:** Responsibilities were split into focused classes and abstracted behind interfaces:

| Class / Interface | Single Responsibility |
| --- | --- |
| `IDiscountStrategy` | Calculate discounted item price based on category rules. |
| `IPaymentProcessor` | Calculate fees and execute payment gateway logic. |
| `INotificationService` | Fan out events (email, SMS, analytics, logs) to output channels. |
| `IOrderRepository` | Persist and retrieve orders. |
| `IProductRepository` | Persist and retrieve products. |
| `OrderProcessingService` | **Thin orchestrator** — coordinate the above components to execute the "Create Order" use case. |

`OrderProcessingService` now contains only orchestration logic. Each collaborator can be modified, replaced, or tested independently.

### 2. Open/Closed Principle (OCP) Violations

**Problem:** Adding a new discount rule required opening `OrderService.createOrder()` and inserting another `else if` branch into an already-fragile chain. The same applied to adding a new payment method like Crypto or Bank Transfer.

**Solution — Discounts:** Extracted an `IDiscountStrategy` interface located in the Core layer:

```java
public interface IDiscountStrategy {
    double applyDiscount(Product product);
}

```

Existing rules became concrete classes (`ElectronicsDiscount`, `ClothingDiscount`). A new rule is added by writing one new class — zero edits to existing core code:

```java
// New rule — no existing class modified
public class BlackFridayDiscount implements IDiscountStrategy { ... }

```

**Solution — Payments:** Extracted an `IPaymentProcessor` interface:

```java
public interface IPaymentProcessor {
    double calculateFee(double amount);
    boolean processPayment();
    String getMethodName();
}

```

Adding Crypto support simply requires creating a `CryptoProcessor` class in the Infrastructure layer. Registering it in `Main.java` is the only line that changes in the existing codebase.

### 3. Liskov Substitution Principle (LSP)

**Problem:** Not directly violated via inheritance, but `Object[]` arrays made it impossible to reason about substitutability at all — there were no types to substitute, risking `ClassCastException` at runtime.

**Solution:** All `Object[]` arrays were replaced with strongly typed domain entities (`Product`, `Order`, `OrderItem`). Furthermore, interfaces like `IPaymentProcessor` and `IDiscountStrategy` now provide proper substitution contracts — any implementation (e.g., `PayPalProcessor` replacing `CreditCardProcessor`) can substitute another without breaking the `OrderProcessingService` caller.

### 4. Interface Segregation Principle (ISP)

**Problem:** There were no interfaces at all. The implicit "interface" of the God Class was enormous, forcing consumers (the `main` method) to depend on a monolithic structure.

**Solution:** Each concern is now behind a narrow, specific interface within the Core layer. `INotificationService` declares only notification-related methods. `IProductRepository` declares only data access methods. Clients and Services depend only on the specific contracts they actually use.

### 5. Dependency Inversion Principle (DIP)

**Problem:** High-level policy (`OrderService`) instantiated low-level data storage (`new ArrayList<>()`) and infrastructure details (`System.out.println` for emails) directly. High-level policy was fused with low-level details.

**Solution:** `OrderProcessingService` now depends strictly on abstractions (`IProductRepository`, `IOrderRepository`, `INotificationService`), injected via its constructor. Concrete implementations (`InMemoryProductRepository`, `ConsoleNotificationService`) reside in the Infrastructure layer and are wired in `Main.java`. Swapping to a PostgreSQL database requires zero changes to the Core layer.

---

## Architecture After Refactoring (Clean Architecture)

The codebase is now strictly divided into three architectural layers based on the ASP.NET Core Clean Architecture model :

```text
com.ecommerce
├── core/                                   ← APPLICATION CORE (No external dependencies)
│   ├── entities/
│   │   ├── Order.java                      ← Rich domain entity
│   │   ├── OrderItem.java
│   │   └── Product.java                    ← Encapsulates stock logic
│   │
│   ├── interfaces/                         ← Abstractions (DIP)
│   │   ├── IProductRepository.java
│   │   ├── IOrderRepository.java
│   │   ├── INotificationService.java
│   │   └── IPaymentProcessor.java          
│   │
│   ├── strategies/                         
│   │   ├── IDiscountStrategy.java          ← Interface (OCP)
│   │   ├── ElectronicsDiscount.java        ← Concrete rule
│   │   ├── ClothingDiscount.java           
│   │   └── DiscountStrategyFactory.java    ← Factory for strategy resolution
│   │
│   └── services/
│       └── OrderProcessingService.java     ← SRP: orchestration only (Use Case)
│
├── infrastructure/                         ← INFRASTRUCTURE (Implementation details)
│   ├── data/                               
│   │   ├── InMemoryProductRepository.java  ← Concrete (swap to DB here)
│   │   └── InMemoryOrderRepository.java    
│   │
│   ├── payment/
│   │   ├── CreditCardProcessor.java        ← Concrete Third-Party API
│   │   ├── PayPalProcessor.java            
│   │   └── BankTransferProcessor.java      
│   │
│   └── services/
│       └── ConsoleNotificationService.java ← Concrete implementation of messaging
│
└── presentation/                           ← PRESENTATION / UI
    └── Main.java                           ← Composition root: wires all dependencies together

```

## Domain Modeling

`Object[]` arrays were completely removed. Every piece of data now has a named type with clear semantics and encapsulated behaviors:

| Before (Legacy Code) | After (Clean Architecture) |
| --- | --- |
| `Object[] product` with index-based access | `Product` entity with typed getters/setters |
| `product[3] = stock - 1` (Manual calculation) | `product.decreaseStock()` (Encapsulated behavior) |
| `order[4].equals("CONFIRMED")` (Magic index/string) | `order.getStatus().equals("CONFIRMED")` |
| Scattered null checks and type casting | Type safety at compile time |

## Key Design Decisions

* **Clean Architecture Boundaries:** The `core` package defines the business rules and holds all Interfaces. It is completely ignorant of the `infrastructure` package. The `infrastructure` package depends on `core` to implement those interfaces. This guarantees isolation.
* **Strategy over switch/if-else:** Both discounts and payment processing used to rely on brittle if-else chains. Replacing them with the Strategy pattern means every new variant is isolated, independently testable, and added without touching proven code.
* **Constructor Injection throughout:** All dependencies flow into the Core services via constructors. This makes `OrderProcessingService` trivially unit-testable — swap real implementations for stubs/mocks with no reflection or framework required.
* **Composition Root in Main.java (Presentation Layer):** All wiring happens in one place. The entire shape of the application — which database is used, which payment method is currently active, and which notification channel is listening — is resolved and injected at application startup.
