---
trigger: model_decision
description: "Rules for accessibility (a11y), WCAG standards, color contrast, keyboard navigation, focus management, and interactive states. Apply when developing components, forms, views, or interaction flows."
---

# Accessibility & Human-Centered UX Rules

Constraints and invariants ensuring that software remains accessible, operable, and perceivable by all users regardless of abilities, input methods, or visual setups.

## 1. Color Contrast (WCAG AA Compliance)

- **Strict Text Contrast Ratios**:
  - Normal text (< 18px / regular weight): Minimum **4.5:1** contrast ratio against its background.
  - Large text (≥ 18px bold or ≥ 24px regular): Minimum **3.0:1** contrast ratio.
  - Never eyeball or assume grey-on-grey/grey-on-black passes contrast. Compute or verify with standard contrast formulas.
- **Text Over Imagery or Gradients**:
  - Text placed over photos, illustrations, or gradients must include a high-contrast backing scrim or solid container. Contrast must pass across the entire text area, not just at the brightest/darkest point.
- **Non-Text Contrast (WCAG 1.4.11)**:
  - Component boundaries, input borders, icons that convey meaning, and active state indicators must maintain at least **3.0:1** contrast against adjacent backgrounds.

## 2. Keyboard & Input Accessibility

- **FORBIDDEN: Stripping Focus Outlines**:
  - Never set `outline: none` or `outline: 0` without immediately providing an equally or more visible `:focus-visible` indicator (minimum 3:1 contrast against adjacent background).
- **Full Keyboard Operability**:
  - Every interactive component (buttons, inputs, links, dropdowns, modal dialogs) must be fully operable via standard keyboard controls (`Tab`, `Shift+Tab`, `Enter`, `Space`, `Escape`).
  - Modal sheets and dialogs must trap focus when opened and close gracefully on `Escape`, restoring focus to the opening trigger.
- **Logical Tab & DOM Order**:
  - Navigation sequence must follow visual reading order. Do not manipulate `tabindex` to positive values; use standard DOM structure and `tabindex="0"` or `tabindex="-1"` strictly for programmatic focus management.

## 3. UI State Affordances & Robustness

- **Multi-Modal Feedback (Never Color-Only)**:
  - Never communicate status, errors, or alerts solely through color (e.g. red border alone). Always pair color signals with explicit text messages, icons, or distinguishable patterns.
- **Three Essential Data View States**:
  - Every data list, feed, or dashboard view must implement 3 distinct states:
    1. **Empty State**: Explain why content is absent and provide a clear call to action.
    2. **Loading State**: Provide clear visual progress (skeleton screens or labeled indicators).
    3. **Error State**: State clearly what went wrong and provide an actionable retry option.
- **Interactive Affordance Indicators**:
  - Web & desktop controls: Enabled interactive elements must exhibit `cursor: pointer`; disabled controls must reflect disabled state visually and programmatically (`disabled` or `aria-disabled="true"`).
- **Accessible Names for Icon Controls**:
  - Every icon-only button (close, search, back, settings) must have an explicit accessible text label (`aria-label`, tooltip, or contentDescription in native Android).

## 4. Zoom & Assistive Adaptations

- **Text Zoom & Scaling**:
  - Layouts must tolerate 200% text scaling without clipping text or rendering controls inaccessible. Avoid fixed-height containers with hidden overflow on text blocks.
- **Virtual Keyboard Accommodations**:
  - On mobile viewports, focused text fields must automatically scroll into view above on-screen software keyboards with sufficient clearance.
- **Reduced Motion Support**:
  - Honor `prefers-reduced-motion` settings. Disable or replace large decorative animations, auto-scrolling carousels, and parallax movements for users requesting reduced motion.

## Accessibility Checklist
- [ ] Do text pairings meet 4.5:1 (normal) or 3:1 (large) contrast ratio?
- [ ] Do non-text UI elements and active focus rings meet 3:1 contrast?
- [ ] Are all controls keyboard navigable with visible focus indicators?
- [ ] Do errors and states present clear text labels, not color alone?
- [ ] Do all icon-only buttons include accessible names / descriptions?
- [ ] Does the UI adapt to text zoom and respect reduced motion preferences?
