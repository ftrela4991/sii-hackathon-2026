# TC-044: My Account Page Accessible After Login

**ID:** TC-044
**Category:** ACCOUNT
**Type:** positive
**Priority:** high
**Tags:** @account

---

## Feature

My Account Page Content

## User Story

As a logged-in customer, I want to access My Account and see links to all my personal sections.

## Background

The customer is logged in.

## Scenario

**Given** the customer is logged in
**When** the customer navigates to `/my-account`
**Then** the My Account page is displayed without errors
**And** links to Orders, Addresses, Personal Information, and GDPR are visible

---

## Acceptance Criteria

- [ ] My Account page loads successfully
- [ ] All account section links are present
- [ ] Customer name is shown on the page
- [ ] No redirect to login occurs
