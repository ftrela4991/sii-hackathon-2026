# TC-061: Product with Price Zero (Free Product)

**ID:** TC-061
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @product @pricing @boundary

---

## Feature

Free Product (Price = 0)

## User Story

As a shop admin, I want to create a free product (price = 0) so that customers can add it to their cart without increasing the total.

## Scenario

**Given** the admin creates a product "Free Test Item <uuid>" with price "0.00"
**When** a customer opens the product detail page
**Then** the price displayed is "£0.00" or "Free"
**And** the customer can add it to the cart
**And** the cart grand total does not increase after adding the free product

---

## Acceptance Criteria

- [ ] Price displays as £0.00 or "Free"
- [ ] "Add to cart" is active
- [ ] Cart total unchanged after adding the product
- [ ] Product is deleted via API after the test
