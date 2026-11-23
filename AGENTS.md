# Project Overview & Development Guidelines

This workspace is an Android application project (`apkkonseling`).
Persistent rules and constraints are managed in `.agents/rules/` adhering to Antigravity Rules standards.

## Rule System Architecture

When designing, implementing, or reviewing user interfaces, layouts, dashboards, and accessibility features, the following specialized rules govern code quality and prevent generic AI-generated anti-patterns ("slop"):

- **UI & Visual Design Rules**: `@[UI & Visual Design Rules](.agents/rules/ui-visual-design.md)`
- **Mobile & Responsive Layout Rules**: `@[Mobile & Responsive Layout Rules](.agents/rules/mobile-layout.md)`
- **Accessibility & Human-Centered UX Rules**: `@[Accessibility Rules](.agents/rules/accessibility-human.md)`
- **App & Dashboard Design Rules**: `@[App & Dashboard Rules](.agents/rules/app-dashboard.md)`

## Core Invariants
1. **Never Ship AI Slop**: Avoid stereotypical AI defaults (generic purple/blue gradients, glowing badges, unconfigured bento grids, fabricated data, empty 3-step templates).
2. **Design with Purpose**: Every layout decision, color accent, elevation shadow, or status dot must serve a concrete functional need or genuine brand requirement.
3. **Respect Device & Human Constraints**: Provide WCAG AA contrast (4.5:1 normal text, 3:1 non-text/large text), minimum 44x44px touch targets, full keyboard/screen-reader compatibility, and adaptive reflow across screen sizes.
