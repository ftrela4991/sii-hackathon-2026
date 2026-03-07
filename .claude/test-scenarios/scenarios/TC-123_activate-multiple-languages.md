# TC-123: Activate Multiple Store Languages

**ID:** TC-123
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @i18n

---

## Feature

Multi-Language Store Activation

## User Story

As a shop admin, I want to enable multiple store languages so international customers can browse in their own language.

## Scenario

**Given** the admin installs and activates languages English and French in the Localisation > Languages section
**When** a customer selects French from the language switcher
**Then** the navigation, labels, and static content are displayed in French

---

## Acceptance Criteria

- [ ] Language switcher is visible on the storefront
- [ ] Switching to French updates navigation and static labels
- [ ] English content is restored when switching back to English
