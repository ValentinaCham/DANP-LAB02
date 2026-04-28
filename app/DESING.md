---
name: Vibrant Habit System
colors:
  surface: '#f7f9fb'
  surface-dim: '#d8dadc'
  surface-bright: '#f7f9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f6'
  surface-container: '#eceef0'
  surface-container-high: '#e6e8ea'
  surface-container-highest: '#e0e3e5'
  on-surface: '#191c1e'
  on-surface-variant: '#464554'
  inverse-surface: '#2d3133'
  inverse-on-surface: '#eff1f3'
  outline: '#767586'
  outline-variant: '#c7c4d7'
  surface-tint: '#494bd6'
  primary: '#4648d4'
  on-primary: '#ffffff'
  primary-container: '#6063ee'
  on-primary-container: '#fffbff'
  inverse-primary: '#c0c1ff'
  secondary: '#6950a2'
  on-secondary: '#ffffff'
  secondary-container: '#c0a5fe'
  on-secondary-container: '#4f3687'
  tertiary: '#006577'
  on-tertiary: '#ffffff'
  tertiary-container: '#008096'
  on-tertiary-container: '#f9fdff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#e1e0ff'
  primary-fixed-dim: '#c0c1ff'
  on-primary-fixed: '#07006c'
  on-primary-fixed-variant: '#2f2ebe'
  secondary-fixed: '#eaddff'
  secondary-fixed-dim: '#d1bcff'
  on-secondary-fixed: '#24005b'
  on-secondary-fixed-variant: '#503788'
  tertiary-fixed: '#acedff'
  tertiary-fixed-dim: '#4cd7f6'
  on-tertiary-fixed: '#001f26'
  on-tertiary-fixed-variant: '#004e5c'
  background: '#f7f9fb'
  on-background: '#191c1e'
  surface-variant: '#e0e3e5'
typography:
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  body-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  label-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  unit: 8px
  container-margin: 24px
  gutter: 16px
  card-padding: 20px
  stack-sm: 8px
  stack-md: 16px
  stack-lg: 32px
---

## Brand & Style

This design system is built to evoke a sense of "technological zen"—combining the precision of a productivity tool with the soft, welcoming nature of a wellness app. The brand personality is motivating, futuristic, and encouraging.

The visual style leverages **Glassmorphism** and **Modern Minimalism**. By utilizing translucent layers and vibrant background blurs, the interface feels deep and immersive. Large radii on corners remove the clinical "edge" of traditional habit trackers, making the app feel more organic and approachable. The aesthetic is high-tech but maintains accessibility through high-contrast typography and clear functional signifiers.

## Colors

The palette is anchored by **Deep Purples** and **Electric Blues** to create a focused, high-energy environment.

- **Primary & Secondary:** Use the deep purples for navigation and primary actions to ground the experience.
- **Electric Blue:** Reserved for progress indicators and highlights to maintain a "high-tech" feel.
- **Soft Mint Green:** Strictly for success states, completed habits, and positive trend lines.
- **Glass Surfaces:** Use white with 60-80% opacity and a 20px-40px backdrop blur to create the glassmorphic effect over subtle background gradients.

## Typography

This design system uses **Plus Jakarta Sans** for headlines to provide a friendly, modern geometric touch, while **Inter** is used for body and labels to ensure maximum legibility and a systematic, clean look.

Hierarchy is maintained through significant weight shifts. Headlines should use the secondary deep purple for maximum "pop," while body text should remain a dark slate or neutral charcoal to prevent eye fatigue during long interactions.

## Layout & Spacing

The layout philosophy follows a **Fluid Grid** model optimized for mobile. A 4-column system is used for most internal components, with a standard 24px outer margin to give content "room to breathe."

Spacing follows a strict 8px rhythm. Content clusters (like a habit title and its frequency) should use 8px spacing, while distinct sections (like "Today's Habits" and "Weekly Progress") should be separated by 32px or more to maintain a clean, airy aesthetic.

## Elevation & Depth

Elevation is achieved through a combination of **Glassmorphism** and **Ambient Shadows**.

1.  **Level 1 (Base):** Subtle background gradients (Deep Purple to Electric Blue at low saturation).
2.  **Level 2 (Cards):** Semi-transparent white surfaces with `backdrop-filter: blur(20px)`. These cards use a very soft, diffused shadow (`0px 10px 30px rgba(0,0,0,0.05)`).
3.  **Level 3 (Interactive):** Active buttons and floating action buttons (FABs) use a colored glow shadow that matches their background color (e.g., a purple button with a soft purple shadow) to imply energy and "vibrancy."

## Shapes

The shape language is defined by high-radius curves. Most containers (cards, modals) should use a **24px (rounded-xl)** corner radius. Buttons and input fields should use a **16px (rounded-lg)** radius. This consistency in "roundness" reinforces the soft, accessible brand personality and makes the UI feel like a tactile, premium object.

## Components

- **Habit Cards:** These are the primary interface element. They should feature a glassmorphic background, a large habit title, and a "Strike" or "Check" area that transitions to Soft Mint Green upon completion.
- **Progress Rings:** Use thin, electric blue strokes for tracking daily completion percentages.
- **Action Buttons:** Large, pill-shaped or heavily rounded buttons. Primary actions use the Electric Blue or Deep Purple with a white label.
- **Chips (Day Selectors):** Circular or highly rounded containers. Unselected states should be ghost-style (outline only), while selected states should fill with the Primary Color.
- **Input Fields:** Minimalist design with a subtle bottom border or a very light, translucent fill. The focus state should trigger a glow effect in Electric Blue.
- **Micro-interactions:** Use "spring" physics for checkboxes and list transitions to reinforce the "motivating" brand feel.