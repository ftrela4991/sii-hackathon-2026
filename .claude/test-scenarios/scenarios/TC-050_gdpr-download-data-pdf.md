# TC-050: GDPR – Download Personal Data as PDF

**ID:** TC-050
**Category:** ACCOUNT
**Type:** positive
**Priority:** low
**Tags:** @account @gdpr

---

## Feature

GDPR Personal Data Download – PDF Format

## User Story

As a logged-in customer, I want to download my personal data in PDF format as required by GDPR.

## Background

The customer is logged in.

## Scenario

**Given** the customer is on the GDPR data page `/module/psgdpr/gdpr`
**When** the customer clicks the button to download data as PDF
**Then** a file download is initiated
**And** the downloaded file has a ".pdf" extension

---

## Acceptance Criteria

- [ ] Download is triggered on button click
- [ ] File extension is .pdf
- [ ] File is not empty and is a valid PDF
- [ ] No authentication error occurs during download
