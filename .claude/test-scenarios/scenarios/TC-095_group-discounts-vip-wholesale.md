# TC-095: Group Discounts Applied Correctly for VIP and Wholesale

**ID:** TC-095
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** medium
**Tags:** @groups @discount

---

## Feature

Group-Specific Pricing on Product Page

## User Story

As a shop admin, I want VIP and Wholesale customers to see prices with their group discount already applied on the product page.

## Background

VIP group has 10% discount. Wholesale group has 15% discount. Product price is £100.00.

## Scenario

**Given** a product is priced at "£100.00"
**When** a VIP customer views the product
**Then** the price shown is "£90.00" (10% off)
**When** a Wholesale customer views the same product
**Then** the price shown is "£85.00" (15% off)

---

## Acceptance Criteria

- [ ] VIP customer sees £90.00
- [ ] Wholesale customer sees £85.00
- [ ] Regular customer or guest sees £100.00
- [ ] Discount is applied at product display, not only at checkout
