# TC-070: Default Store Currency Applied to All Prices

**ID:** TC-070
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @currency

---

## Feature

Default Currency Configuration

## User Story

As a shop admin, I want to set a default store currency so all prices are shown in that currency for customers who have not switched.

## Scenario

**Given** the admin changes the default store currency to EUR in Localisation settings
**When** a customer visits the storefront without manually switching currency
**Then** all product prices are displayed in EUR (€ symbol) by default

---

## Acceptance Criteria

- [ ] Euro symbol (€) is shown next to prices by default
- [ ] No currency switch by the customer is required
- [ ] Customer can still switch to other enabled currencies
