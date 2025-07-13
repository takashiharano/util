# util.js

A self-contained JavaScript utility library that provides a wide range of general-purpose functions for web systems and browser-based applications — all in a **single file** with **no external dependencies**.

## ✨ Features

- **Single-file** drop-in utility (`util.js`)  
- **No external dependencies** (no jQuery, Moment.js, etc.)  
- Written in **ES5 syntax** for compatibility and portability  
- Broad coverage: date/time utilities, string manipulation, number formatting, DOM time display, random generators, and more  
- Minimal global pollution: all functions under the `util` namespace  

## 📦 Installation

Include the script in your HTML file:

```html
<script src="util.js"></script>
```

No build step, no import system required.

## 🛠 Usage Example

```js
var now = util.getDateTimeString(); // e.g., "2025-07-13 15:42:10.123 +09:00"
var uuid = util.generateUUID();
var elapsed = util.timecounter.delta('2025-07-13 15:00:00');
```

See `util.js` source for full function documentation.

## 🔧 Compatibility

- Fully **ES5-compliant**: works in older browsers and embedded systems  
- Tested in: latest Chrome, Firefox, Safari, and legacy WebViews  

## 📌 Design Policy

- **Maximum portability**: avoids ES6+ syntax intentionally  
- **Minimal dependencies**: no reliance on external libraries  
- Built to be embedded in existing business systems with minimal integration cost  

## ⚠️ Disclaimer

This library has been tested in local environments, but **does not include automated test coverage**.  
For large-scale or commercial use, **please conduct your own integration testing** as needed.

## 📄 License

MIT License  
Copyright (c) 2019  
Takashi Harano  
https://libutil.com/
