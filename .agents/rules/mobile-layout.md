---
trigger: model_decision
description: "Rules for mobile layout, responsive breakpoints, touch targets, overflow handling, grids, and mobile navigation. Apply when designing, building, or refactoring layouts for mobile or multi-screen viewports."
---

# Mobile & Responsive Layout Rules

Constraints and invariants governing mobile and responsive interface design. A mobile layout is a distinct, deliberate layout, not a squeezed desktop layout.

## 1. Breakpoints & Viewport Reflow

- **Content-Driven Breakpoints**:
  - Breakpoints must be determined by content reflow points (where text lines become too short/long, or cards crowd), not hardcoded arbitrarily to specific device models.
- **Continuous Multi-State Responsiveness**:
  - Do not design a simplistic two-state layout (mobile stack vs. wide desktop grid) that neglects tablet/intermediate viewports (600px - 1024px).
  - Intermediate screen sizes must have dedicated scaling (e.g., 2-column intermediate grid) rather than an overly stretched single column.
- **Mobile First-Class Treatment**:
  - Do not treat mobile as an afterthought or trailing override stylesheet. Design narrow-viewport states with intentional typography scales, section padding, and component stacking.

## 2. Scale & Sizing Constraints

- **Viewport-Appropriate Scaling**:
  - Desktop section padding (e.g., 96px-128px) and large hero heights must scale down to mobile registers (e.g., 24px-48px).
  - Typography must scale appropriately via fluid sizing (`clamp()`) or discrete breakpoint scales.
- **Viewport Height Units**:
  - Avoid unconditional `100vh` on mobile containers where browser navigation chrome causes vertical overflow. Use content-sized heights (`auto`) or dynamic viewport units (`dvh`).

## 3. Grids, Flexbox & Stacking

- **Mandatory Column Collapse**:
  - Multi-column grids and side-by-side card rows must collapse cleanly to a single reflowing stack on narrow viewports to avoid column collision.
- **Fluid Grid Tracks**:
  - Avoid fixed pixel track widths (`grid-template-columns: 300px 300px`). Use flexible units (`minmax(0, 1fr)`, `auto-fit`, or flex wrapping).
- **No Forced 12-Column Grids on Mobile**:
  - Do not force 12-column math onto mobile viewports; mobile layouts require 1 or at most 2 content columns.

## 4. Overflow & Clipping Prevention

- **FORBIDDEN: Horizontal Scroll Leak**:
  - The viewport must have zero unintentional horizontal scrolling on mobile.
  - Large tables must use dedicated scroll containers or reflow cards; code blocks and preformatted text must wrap or contain scrolling; images must enforce `max-width: 100%`.
- **FORBIDDEN: Content Clipping via `overflow: hidden`**:
  - Never apply `overflow: hidden` to mask layout overflow that truncates readable text or interactive controls. Content must reflow naturally.
- **Fluid Child Elements**:
  - Flex and grid children must permit shrinking (e.g., `min-width: 0` on flex items) to prevent bursting parent boundaries.

## 5. Touch Targets & Spacing

- **Minimum 44x44 px Touch Targets**:
  - All interactive controls (buttons, navigation icons, checkboxes, links) must provide a touch target of at least 44x44 px / dp. If the visual icon is smaller, enlarge the hit box with padding.
- **Target Clearance**:
  - Ensure adequate spacing between adjacent touch targets to prevent accidental mis-taps.
- **No Mouse-Exclusive / Hover-Only Actions**:
  - Every interaction available on hover (e.g., tooltips, popover menus, secondary action reveals) must have an explicit tap/click trigger for touch devices.

## 6. Mobile Navigation Invariants

- **Adaptive Navigation Pattern**:
  - Desktop top nav bars with horizontal link lists must reflow on mobile into an accessible drawer, bottom navigation bar, or labeled menu sheet.
- **Clear Navigation Affordance**:
  - Avoid cryptic, unlabeled icons. If using a drawer trigger, provide a clear label ("Menu") or accessible name.
- **Safe Area Insets & Overlay Protection**:
  - Fixed headers, floating action buttons, and bottom navigation bars must respect device safe areas (`env(safe-area-inset-bottom)`) and must never obscure interactive content or final list items. Reserve scroll padding equal to the bar height.

## Mobile Layout Checklist
- [ ] Does the layout reflow into a distinct mobile state rather than desktop shrunk down?
- [ ] Are touch targets at least 44x44 px with clear touch separation?
- [ ] Is the page completely free of horizontal scroll leaks and accidental clipping?
- [ ] Are hover-only interactions accessible via tap?
- [ ] Do fixed bottom bars respect safe-area insets without obscuring content?
