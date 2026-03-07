# TC-124: Product Name and Description Translated per Language

**ID:** TC-124
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @i18n @product

---

## Feature

Product Translations

## User Story

As a shop admin, I want products to display in the customer's selected language so the shopping experience is localised.

## Background

English and French are active languages.

## Scenario

**Given** the admin sets product name in English as "Blue Mug" and in French as "Tasse Bleue"
**When** a customer views the product page in English
**Then** the product name is "Blue Mug"
**When** the customer switches to French
**Then** the product name is "Tasse Bleue"

---

## Acceptance Criteria

- [ ] English name shown when English is selected
- [ ] French name shown when French is selected
- [ ] Product description also switches language
- [ ] Product is deleted via API after the test
