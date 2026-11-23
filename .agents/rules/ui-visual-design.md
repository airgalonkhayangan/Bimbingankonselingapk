---
trigger: model_decision
description: "Rules for UI styling, color palette, visual hierarchy, components, decoration, motion, and assets. Apply when creating or modifying user interfaces, themes, stylesheets, or visual components."
---

# UI & Visual Design Rules

Constraints and invariants to eliminate generic AI aesthetic defaults ("anti-slop") and ensure production-grade visual design.

## 1. Visual & Color Constraints

- **FORBIDDEN: Generic Default Gradients**:
  - Never use blue-to-purple, blue-to-cyan, or purple-to-pink gradients as default accents, nor full-page blurred radial glows.
  - Palettes must be derived from the product's explicit design system or brand guidelines. Gradients are permitted only when fulfilling an explicit hierarchy function with documented intent.
- **Dose Cap: Glassmorphism**:
  - Maximum 1-2 glassmorphic / backdrop-filter elements per page. Background surfaces must remain solid to preserve visual hierarchy.
- **Border Radius Discipline**:
  - Avoid uniform pill shapes for all inputs, cards, and modals. Maintain a clear system scale (e.g., subtle radius for cards/inputs, distinct radius for primary action buttons).
- **Shadows as Elevation Only**:
  - Do not apply soft floating shadows across every card and component. Most elements must sit flat on the surface plane; reserve elevation shadows strictly for raised layers (menus, modals, focused cards).
- **Dose Cap: Glow Effects**:
  - Strictly cap glow effects to at most 1-2 key focus elements. Never apply ambient glow to cards, borders, badges, and icons simultaneously.
- **Color Restraint**:
  - Restrict the active palette to 2-3 core colors plus 1 accent.
  - Never scatter the accent color across every icon, badge, border, and button. The accent must highlight primary focal points only.
- **No Unjustified Dark Mode**:
  - Do not default to dark mode purely to look "technical" unless mandated by the product domain (e.g. code editors, media viewers) or user preferences. Provide functional theme support where required.

## 2. Decorative Elements & Badges

- **FORBIDDEN: Generic AI Glyphs & Icons**:
  - Do not use sparkles, magic wands, robots, floating cubes, or generic AI orbs as generic feature icons. Use semantically accurate icons directly representing the feature domain.
- **FORBIDDEN: Decorative Emojis in UI**:
  - Do not scatter literal emoji (🚀, ✅, 🔥, 📈) in headings, feature lists, and buttons. Use formal typography or dedicated vector iconography.
- **FORBIDDEN: Small Arrows on Every CTA**:
  - Never attach `→` or `↗` indiscriminately to every button. Reserve direction arrows only for navigation steps that genuinely represent forward transitions.
- **Meaningful State Indicators**:
  - Status dots must mark real, verifiable states (e.g., online, syncing, recording).
  - Never use decorative colored dots or endlessly pulsing status lights on static headings or labels.
- **FORBIDDEN: AI Capsule Badges & Redundant Eyebrows**:
  - Do not place decorative pill badges like "AI Powered" or category labels that merely duplicate the headline immediately below them.
  - Badges must represent actionable tags, counts, or genuine system state.

## 3. Layout Structure & Composition

- **FORBIDDEN: Monotonous Template Grids**:
  - Do not blindly generate standard boilerplate sections (Hero → 3-item feature grid → Bento grid → Testimonial row → 3-tier pricing → 4-column footer).
  - Structure sections strictly based on actual product narrative and functional requirements.
- **Feature Presentation Hierarchy**:
  - Do not render every feature as an identical uniform card with an icon and paragraph. Vary presentation (e.g., hero split-screen for flagship features, compact lists for secondary capabilities).
- **Honest Testimonials and Social Proof**:
  - Never fabricate fake customer logos, fake review quotes, or generic "Trusted by" rows. Only display genuine, verifiable customer data.

## 4. Motion & Performance

- **No Endless Loops**:
  - Never introduce infinite auto-pulses, floating bobs, or looping animations without direct user interaction or active processing state.
- **Motion Restraint**:
  - Animation must serve a specific functional purpose (state transition, focus guidance). Respect user preferences for reduced motion (`prefers-reduced-motion`).
- **Media Sizing & Performance**:
  - Always specify intrinsic dimensions (`width`/`height` or aspect-ratio) on images and media containers to prevent cumulative layout shifts (CLS).
  - Prioritize above-the-fold hero media (`fetchpriority="high"`); never lazy-load the primary hero asset.

## UI Design Checklist
- [ ] Is the color palette tied to the real product identity, free of generic purple-cyan gradients?
- [ ] Are shadows, glassmorphism, and glow restricted to specific focal elements?
- [ ] Is UI copy free of decorative emojis and redundant eyebrow pills?
- [ ] Do interactive elements have distinct active, focus, and hover states?
- [ ] Is animation purposeful, non-looping, and respectful of reduced-motion settings?
