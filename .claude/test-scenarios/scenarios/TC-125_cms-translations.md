# TC-125: CMS Pages Translated per Language

**ID:** TC-125
**Category:** PRODUCTS_AND_API
**Type:** positive
**Priority:** low
**Tags:** @admin @i18n @cms

---

## Feature

CMS Page Translations

## User Story

As a shop admin, I want CMS pages (e.g. About Us) to display in the customer's selected language.

## Background

English and French are active languages.

## Scenario

**Given** the admin enters the "About Us" page content in English and French
**When** a customer views the "About Us" page in English
**Then** the English content is shown
**When** the customer switches to French
**Then** the French content is shown

---

## Acceptance Criteria

- [ ] English CMS content displays for English language
- [ ] French CMS content displays for French language
- [ ] No empty or missing content when switching language
