---
trigger: model_decision
description: "Rules for app screens, dashboards, admin panels, tables, metrics, forms, and data views. Apply when building authenticated interfaces, dashboards, or data-driven applications."
---

# App & Dashboard Design Rules

Constraints and invariants governing application layouts, administrative dashboards, data visualization, and authenticated workflows.

## 1. Purpose-Driven Information Architecture

- **FORBIDDEN: Default Dashboard Template Shell**:
  - Never default to the generic AI layout: (Left rail sidebar + top bar + 4 metric stat cards + arbitrary chart + generic table) without understanding user workflow.
  - Determine the primary decision or action the user needs to make on this screen, and elevate that task to the visual center.
- **Designed Navigation Shells**:
  - Application sidebars and navigation rails must be treated as first-class components:
    - Group navigation destinations logically with quiet category labels.
    - Clearly mark the current active destination with shape/weight, not merely a faint background tint.
    - Active markers must persist when navigation rails collapse to icon-only mode.
    - Ensure full responsiveness: collapsible to a sheet or drawer on mobile with an accessible trigger.

## 2. Real Metrics & Honest Data

- **FORBIDDEN: Fabricated Numbers & Baseless Deltas**:
  - Never display fake metric cards with invented numbers (e.g. `12,483`, `94.2%`) and fake positive delta indicators (`+12% this week`) to make empty mockups look active.
  - Display real metrics, or clearly identify prototype metrics with visible `[Placeholder]` labels.
  - Delta indicators are prohibited unless backed by an actual comparison time window.
- **FORBIDDEN: Fabricated Activity Feeds**:
  - Never populate activity feeds or audit logs with fake user names and stock events ("Sarah Chen updated a document 2 hours ago").
  - Show genuine user activity or provide an honest, helpful empty state instructing the user on how events are generated.

## 3. Data Presentation & Tables

- **Decision-Driven Table Columns**:
  - Do not default to boilerplate column headers (`Name`, `Status`, `Date`, `Actions`).
  - Columns must display the specific data points needed for user decision-making, placing the primary identifying attribute in the first column.
  - Action menus (`...`) must contain only currently supported, functional actions. Remove non-functional placeholder actions.
- **No Deceptive Filler Data**:
  - In form inputs and table cells, avoid fake realistic data (`John Doe`, `johndoe@example.com`, `"Let's build something"`).
  - Use clear field placeholders (`Your Name`, `name@example.com`) or leave unpopulated fields empty with appropriate fallback indicators.
- **Actionable Data Charts**:
  - Charts must answer a specific business question clearly stated in the title or caption (e.g., "Daily active consultations, past 30 days").
  - Do not insert purely decorative charts with vague titles like "Overview" or "Analytics". If a plain metric or summary sentence conveys the answer better, use text instead of an unreadable chart.

## 4. Application Flow & Functional Integrity

- **No Dead Navigation or Fake Controls**:
  - Every button, dropdown, and menu item must perform its intended operation.
  - If a feature is scheduled for future implementation, label it clearly with "Coming Soon" or disable it with an informative tooltip; do not leave dead, non-responsive click handlers.
- **Informative Empty, Loading, and Error States**:
  - When tables or views are empty, explain the context clearly and offer an actionable next step ("No consultations scheduled for today. Click 'New Appointment' to create one.").
  - Loading states must clearly communicate ongoing operations.
  - Error states must detail the error reason and provide clear recovery options (e.g., "Retry connection").

## Dashboard & App Checklist
- [ ] Is the screen hierarchy organized around the user's primary decision rather than a generic 4-card template?
- [ ] Are all metrics, deltas, and activity feeds backed by real data or explicitly labeled as placeholders?
- [ ] Are table columns tailored to actionable data?
- [ ] Does every control and navigation item perform a real action or display an explicit "Coming Soon" status?
- [ ] Do empty, loading, and error states provide clear causes and next actions?
