# Watermark Process Specification
*Based on TPS-CRS Management System Implementation*

## Overview
This document provides a complete specification for implementing a web-based watermark system based on the current watermark implementation in the TPS-CRS Management System. The watermark system provides text overlays with tamper-resistant features and DOM monitoring capabilities.

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Core Features](#core-features)
3. [Configuration Parameters](#configuration-parameters)
4. [API Specification](#api-specification)
5. [Implementation Requirements](#implementation-requirements)
6. [Usage Examples](#usage-examples)
7. [Security Considerations](#security-considerations)
8. [Browser Compatibility](#browser-compatibility)

## Architecture Overview

### Module Structure
```
watermark/
├── watermark.js          # Core watermark engine (UMD module)
├── common.js            # Helper functions and initialization
└── implementation/      # System-specific integrations
```

### Design Pattern
- **Universal Module Definition (UMD)**: Supports AMD, CommonJS, and browser globals
- **Shadow DOM**: Enhanced security and isolation
- **MutationObserver**: Real-time DOM monitoring and tamper detection
- **Event-driven**: Responsive to window resize and load events

## Core Features

### 1. Text Watermark Generation
- Dynamic text rendering across web pages
- Configurable positioning, spacing, and appearance
- Multi-row and multi-column layout support
- Rotational angle support for diagonal watermarks

### 2. Tamper Resistance
- **Shadow DOM Protection**: Watermarks rendered in isolated shadow roots
- **MutationObserver Monitoring**: Automatic detection and restoration of removed watermarks
- **Style Protection**: Prevention of watermark style modifications
- **Random Insertion**: Watermarks inserted at random DOM positions

### 3. Responsive Design
- Automatic recalculation on window resize
- Dynamic row/column adjustment based on viewport size
- Parent element constraint support

## Configuration Parameters

### Default Configuration Structure
```javascript
{
  // Basic Properties
  watermark_id: 'wm_div_id',              // Main watermark container ID
  watermark_prefix: 'mask_div_id',        // Individual watermark element prefix
  watermark_txt: "Default Watermark",     // Watermark text content
  
  // Positioning
  watermark_x: 20,                        // X-axis starting position (px)
  watermark_y: 20,                        // Y-axis starting position (px)
  watermark_rows: 0,                      // Number of rows (0 = auto-calculate)
  watermark_cols: 0,                      // Number of columns (0 = auto-calculate)
  
  // Spacing
  watermark_x_space: 50,                  // Horizontal spacing between watermarks (px)
  watermark_y_space: 50,                  // Vertical spacing between watermarks (px)
  
  // Appearance
  watermark_font: '微软雅黑',              // Font family
  watermark_color: 'black',               // Text color
  watermark_fontsize: '18px',             // Font size
  watermark_alpha: 0.15,                  // Opacity (0.005 - 1.0)
  watermark_width: 100,                   // Individual watermark width (px)
  watermark_height: 100,                  // Individual watermark height (px)
  watermark_angle: 15,                    // Rotation angle (degrees)
  
  // Container Properties
  watermark_parent_width: 0,              // Container width (0 = auto-detect)
  watermark_parent_height: 0,             // Container height (0 = auto-detect)
  watermark_parent_node: null,            // Parent element ID (null = body)
  
  // Security
  monitor: true                           // Enable tamper detection (true/false)
}
```

### Production Configuration Example
```javascript
// Based on CRS system settings
{
  watermark_x: 150,
  watermark_y: 60,
  watermark_rows: 5,
  watermark_cols: 16,
  watermark_x_space: 57,
  watermark_y_space: 55,
  watermark_alpha: 0.27275425370009687,
  watermark_width: 300,
  watermark_height: 74,
  watermark_angle: 15,
  watermark_fontsize: '22px'
}
```

## API Specification

### Core Methods

#### `watermark.init(settings)`
Initialize watermark system with configuration.

**Parameters:**
- `settings` (Object): Configuration object

**Example:**
```javascript
watermark.init({
  watermark_txt: "Confidential Document"
});
```

#### `watermark.load(settings)`
Manually load/reload watermarks with new settings.

**Parameters:**
- `settings` (Object): Configuration object

**Example:**
```javascript
watermark.load({
  watermark_alpha: 0.3,
  watermark_fontsize: '24px'
});
```

#### `watermark.remove()`
Safely remove all watermarks and disable monitoring.

**Example:**
```javascript
watermark.remove();
```

### Helper Functions

#### `getWatermarkInit(text)`
Convenience function for quick watermark initialization with default production settings.

**Parameters:**
- `text` (String): Watermark text content

**Implementation:**
```javascript
function getWatermarkInit(str) {
  watermark.init({
    watermark_txt: str
  });

  watermark.load({
    watermark_x: 150,
    watermark_y: 60,
    watermark_rows: 5,
    watermark_cols: 16,
    watermark_x_space: 57,
    watermark_y_space: 55,
    watermark_alpha: 0.27275425370009687,
    watermark_width: 300,
    watermark_height: 74,
    watermark_angle: 15,
    watermark_fontsize: '22px'
  });
}
```

## Implementation Requirements

### 1. Browser Feature Detection
```javascript
// MutationObserver support
const MutationObserver = window.MutationObserver || 
                        window.WebKitMutationObserver || 
                        window.MozMutationObserver;

// Shadow DOM support
const hasShadowDOM = typeof Element.prototype.attachShadow === 'function';
```

### 2. DOM Structure Creation
```javascript
// Main container with shadow DOM
const container = document.createElement('div');
container.id = settings.watermark_id;
container.setAttribute('style', 'pointer-events: none !important; display: block !important');

// Shadow root creation
let shadowRoot;
if (hasShadowDOM) {
  shadowRoot = container.attachShadow({mode: 'open'});
} else {
  shadowRoot = container; // Fallback for older browsers
}
```

### 3. Positioning Algorithm
```javascript
// Calculate grid dimensions
const cols = parseInt((pageWidth - startX) / (itemWidth + spacingX));
const rows = parseInt((pageHeight - startY) / (itemHeight + spacingY));

// Position calculation for each watermark
for (let i = 0; i < rows; i++) {
  for (let j = 0; j < cols; j++) {
    const x = startX + (itemWidth + spacingX) * j;
    const y = startY + (itemHeight + spacingY) * i;
    
    // Create and position watermark element
    const element = createWatermarkElement(x, y, text, settings);
    shadowRoot.appendChild(element);
  }
}
```

### 4. Tamper Detection Setup
```javascript
const observer = new MutationObserver((mutations) => {
  // Check for watermark removal or modification
  mutations.forEach((mutation) => {
    if (mutation.type === 'childList' || mutation.type === 'attributes') {
      // Restore watermarks if tampered
      restoreWatermarks();
    }
  });
});

observer.observe(document.body, {
  childList: true,
  attributes: true,
  subtree: true,
  attributeFilter: ['style']
});
```

## Usage Examples

### Basic Implementation
```html
<!DOCTYPE html>
<html>
<head>
  <script src="watermark.js"></script>
</head>
<body>
  <script>
    // Simple watermark
    watermark.init({
      watermark_txt: "Internal Use Only"
    });
  </script>
</body>
</html>
```

### Advanced Configuration
```javascript
// Corporate document watermarking
watermark.init({
  watermark_txt: "CONFIDENTIAL - " + username + " - " + timestamp
});

watermark.load({
  watermark_x: 100,
  watermark_y: 80,
  watermark_rows: 6,
  watermark_cols: 8,
  watermark_x_space: 200,
  watermark_y_space: 150,
  watermark_alpha: 0.2,
  watermark_width: 250,
  watermark_height: 60,
  watermark_angle: 20,
  watermark_fontsize: '16px',
  watermark_color: '#FF0000',
  monitor: true
});
```

### Dynamic Watermarks
```javascript
// User-specific watermark
function initUserWatermark(userId, department) {
  const watermarkText = `${department} - ${userId} - ${new Date().toISOString()}`;
  getWatermarkInit(watermarkText);
}

// Responsive watermark
window.addEventListener('resize', () => {
  watermark.load(currentSettings);
});
```

## Security Considerations

### 1. Tamper Resistance Levels
- **Level 1**: Basic CSS-based watermarks (easily removable)
- **Level 2**: Shadow DOM isolation (moderate protection)
- **Level 3**: MutationObserver monitoring (high protection)
- **Level 4**: Server-side verification (maximum protection)

### 2. Bypass Prevention
```javascript
// Prevent developer tools manipulation
const protectionConfig = {
  monitor: true,
  shadowDOM: true,
  randomInsertion: true,
  styleProtection: true
};

// Additional protection measures
Object.freeze(watermark);
Object.seal(defaultSettings);
```

### 3. Performance Considerations
- Limit watermark density to prevent performance degradation
- Use requestAnimationFrame for smooth updates
- Implement debouncing for resize events

## Browser Compatibility

### Supported Browsers
- **Chrome**: 53+ (full Shadow DOM v1 support)
- **Firefox**: 63+ (full Shadow DOM v1 support)
- **Safari**: 10+ (Shadow DOM support)
- **Edge**: 79+ (Chromium-based, full support)

### Fallback Strategy
```javascript
// Progressive enhancement
if (!hasShadowDOM) {
  // Use regular DOM with enhanced monitoring
  console.warn('Shadow DOM not supported, using fallback mode');
}

if (!MutationObserver) {
  // Use polling-based detection
  console.warn('MutationObserver not supported, using polling fallback');
}
```

## Integration Guidelines

### 1. Module Loading
```javascript
// AMD
define(['watermark'], function(watermark) {
  return watermark;
});

// CommonJS
const watermark = require('./watermark');

// Browser Global
// watermark is available globally
```

### 2. Framework Integration

#### React Integration
```jsx
import watermark from './watermark';

useEffect(() => {
  watermark.init({
    watermark_txt: props.watermarkText
  });
  
  return () => watermark.remove();
}, [props.watermarkText]);
```

#### Vue Integration
```javascript
mounted() {
  this.initWatermark();
},
beforeDestroy() {
  watermark.remove();
},
methods: {
  initWatermark() {
    watermark.init({
      watermark_txt: this.watermarkText
    });
  }
}
```

### 3. Server-Side Configuration
```javascript
// Express.js middleware example
app.use((req, res, next) => {
  res.locals.watermarkConfig = {
    watermark_txt: `${req.user.name} - ${req.user.department}`,
    watermark_alpha: req.user.role === 'admin' ? 0.1 : 0.3
  };
  next();
});
```

## Testing and Validation

### Unit Tests
```javascript
describe('Watermark System', () => {
  test('should initialize with default settings', () => {
    watermark.init({});
    expect(document.getElementById('wm_div_id')).toBeTruthy();
  });
  
  test('should restore watermarks after removal', () => {
    watermark.init({ monitor: true });
    const container = document.getElementById('wm_div_id');
    container.remove();
    
    setTimeout(() => {
      expect(document.getElementById('wm_div_id')).toBeTruthy();
    }, 100);
  });
});
```

### Performance Benchmarks
- **Initialization time**: < 100ms for 50 watermarks
- **Memory usage**: < 5MB for typical configurations
- **CPU impact**: < 2% during active monitoring

## Deployment Checklist

### Pre-deployment
- [ ] Configure watermark text and positioning
- [ ] Test across target browsers
- [ ] Verify tamper detection functionality
- [ ] Performance testing with realistic load
- [ ] Security audit and penetration testing

### Production Configuration
- [ ] Enable monitoring (`monitor: true`)
- [ ] Set appropriate opacity levels
- [ ] Configure user-specific watermark text
- [ ] Implement server-side logging
- [ ] Set up alerting for bypass attempts

## Maintenance and Updates

### Regular Tasks
- Monitor browser compatibility changes
- Update security measures as needed
- Performance optimization based on usage analytics
- User feedback integration

### Version Control
- Semantic versioning for watermark library updates
- Configuration version tracking
- Rollback procedures for problematic updates

---

*This specification is based on the watermark implementation in the TPS-CRS Management System and provides a foundation for implementing similar functionality in other systems.*