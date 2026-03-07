# TC-049: GDPR – Download Personal Data as Excel

**ID:** TC-049
**Category:** ACCOUNT
**Type:** positive
**Priority:** low
**Tags:** @account @gdpr

---

## Feature

GDPR Personal Data Download – Excel Format

## User Story

As a logged-in customer, I want to download my personal data in Excel format as required by GDPR.

## Background

The customer is logged in.

## Scenario

**Given** the customer is on the GDPR data page `/module/psgdpr/gdpr`
**When** the customer clicks the button to download data as Excel
**Then** a file download is initiated
**And** the downloaded file has an ".xlsx" or ".csv" extension

---

## Acceptance Criteria

- [ ] Download is triggered on button click
- [ ] File extension is .xlsx or .csv
- [ ] File is not empty
- [ ] No authentication error occurs during download
