# E-Commerce Order Management System: Architecture and Refactoring Solutions

## Project Overview

This repository contains the backend implementation for a mini E-Commerce platform handling products, shopping carts, orders, payments, and notifications. The primary objective of this project is to evolve a legacy, tightly-coupled codebase into a maintainable, testable, and robust system adhering to Clean Architecture principles.

The project is divided into three main architectural milestones. This document outlines the general solution strategy and design decisions applied across all modules to resolve existing design flaws and technical debt.

---

## Architectural Solutions Breakdown

### Module 01: SOLID Principles Refactoring

**Directory:** `assignment_1`

**Context & Challenges:**
The initial system relied on a highly cohesive "God Class" (`OrderService`) that violated multiple design principles. It managed everything from validation and pricing to payment processing and notifications. Furthermore, the use of generic `Object[]` arrays compromised type safety and domain modeling.

**Solution Strategy:**

* **Single Responsibility Principle (SRP):** The `OrderService` was decomposed into specialized, focused classes (e.g., `ValidationService`, `PricingService`, `NotificationService`). This isolates changes; modifying the email provider now only affects the notification module.
* **Open/Closed Principle (OCP):** Hardcoded `if-else` chains for discount and payment logic were replaced with abstractions. New discount rules (e.g., "Black Friday") or payment methods (e.g., Crypto) can now be introduced by adding new classes that implement a common interface, without modifying existing core logic.
* **Domain Modeling:** Generic `Object[]` arrays were replaced with strongly typed Domain Driven Design (DDD) entities and Data Transfer Objects (DTOs), ensuring compile-time safety and clearer data contracts.

### Module 02: Creational Design Patterns

**Directory:** `assignment_2/`

**Context & Challenges:**
Object instantiation was uncontrolled and error-prone. The application configuration was susceptible to race conditions in multi-threaded environments, order creation required managing unwieldy constructors, and template duplication led to state-mutation bugs.

**Solution Strategy:**

* **Singleton Pattern:** Applied to the `AppConfig` class to guarantee a single, globally accessible instance across the entire application lifecycle. Thread-safe implementation ensures consistent configuration values even under concurrent server requests.
* **Builder Pattern:** Implemented for the `Order` class. This abstracts the complexity of the 13-parameter constructor, providing a fluent and readable API for constructing orders step-by-step, making the codebase highly legible and less prone to parameter-swapping errors.
* **Prototype Pattern:** Applied to the `OrderTemplate` system. Instead of manual field copying which risks mutating a shared baseline configuration, the system now utilizes cloning mechanisms. This safely yields exact copies of pre-defined configurations for new orders while protecting the original template's state.

### Module 03: Structural Design Patterns

**Directory:** `assignment_03/`

**Context & Challenges:**
The system required integration with incompatible legacy systems, flexible business rules for dynamic pricing, and performance optimizations for data retrieval, all without exploding the class hierarchy or violating SRP.

**Solution Strategy:**

* **Adapter Pattern:** Created a wrapper around the `LegacyPaymentGateway`. This adapter translates the modern system's USD/boolean expectations into the legacy system's VND/integer requirements, ensuring seamless integration without modifying the closed legacy source code.
* **Decorator Pattern:** Solved the class explosion problem for cart calculations. Instead of creating exhaustive subclasses for every combination of fees (e.g., `CartWithShippingAndGift`), decorators dynamically wrap the base cart object at runtime. This allows "Express Shipping Fee" and "Gift Wrapping Fee" to be stacked modularly.
* **Proxy Pattern:** Introduced a caching layer for the `ProductCatalog`. The proxy intercepts database queries; if a frequently accessed product is already in memory, it returns the cached instance. This drastically reduces database load while keeping the core `ProductCatalog` class purely focused on data retrieval, preserving SRP.

---

## Navigation and Execution

To review the specific implementations, please navigate to the respective assignment directories. Each directory contains a localized `README.md` detailing the exact class-level transformations, alongside the refactored source code demonstrating the applied patterns and principles.
