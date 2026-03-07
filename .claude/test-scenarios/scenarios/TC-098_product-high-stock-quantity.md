# TC-098: Product with High Stock Quantity (1000 Units)

**ID:** TC-098
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @product @stock

---

## Feature

High Stock Product Availability

## User Story

As a shop admin, a product with 1000 units must be available for purchase and stock must decrement correctly.

## Scenario

**Given** the admin creates a product with quantity "1000"
**When** a customer views the product detail page
**Then** the product is available and the "Add to cart" button is active
**When** the customer adds 1 unit to the cart and places an order
**Then** the available stock in the admin panel decrements by 1 (shows 999)

---

## Acceptance Criteria

- [ ] Product with 1000 quantity is orderable
- [ ] Stock decrements correctly after order
- [ ] No overflow or display error for large quantities
- [ ] Product is deleted via API after the test
