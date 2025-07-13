# util.py

A **self-contained Python utility module** for general-purpose scripting, automation, and backend tasks.  
Designed from an **embedded systems perspective**, it emphasizes *explicit control*, *zero dependencies*, and *broad applicability*.

---

## 🧭 Design Philosophy

### Overview

`util.py` consolidates a wide range of essential functions into a single file with no external dependencies.  
It is engineered with an embedded systems mindset—favoring minimalism, reliability, and operational control.

### Core Principles

- **Single-file utility** — Easy to drop into any project.
- **Standard library only** — No need for pip or package managers.
- **Explicit behavior** — Avoids abstraction that obscures control flow.
- **Memory-conscious** — Low heap usage, predictable behavior.
- **Stateless functions** — Minimal global state, reusable anywhere.
- **System-aware** — Handles stdin, encoding, timestamps, binary, etc.

---

## 🛠 Use Cases

- Backend scripting and job orchestration
- CLI tool development
- Log processing and data transformation
- Serverless / container runtime utilities
- Embedded Linux / Raspberry Pi scripting
- Lightweight web or API backend logic

---

## 📦 Features

- ✅ String utilities (padding, matching, replacing, quoting, escaping)
- ✅ Date/time handling (formatting, parsing, rounding, offsets)
- ✅ CLI/system I/O (stdin, stdout encoding, argv handling)
- ✅ Path operations (filename, extension, parent path)
- ✅ Binary encoding (hex, base64, base64s, binary strings)
- ✅ JSON/dict processing and file persistence
- ✅ Configuration file (Java-style `.properties`) loading/saving
- ✅ Time arithmetic (clock time math, conversions)
- ✅ Utility classes: `DateTime`, `ClockTime`

---

## 🚫 Limitations

- Not optimized for concurrency or massive datasets.
- Pythonic idioms (e.g. list comprehensions, `set()`) are sometimes avoided intentionally.
- External validation or test wrapping recommended for production-critical use.

---

## 🧪 Example

```python
import util

print(util.now())                      # Current Unix timestamp
print(util.format_number(-1234.56789)) # '-1,234.56789'
print(util.get_file_name('a/b/c.txt')) # 'c'
print(util.to_set(['a', 'b', 'a']))    # ['a', 'b']
print(util.clock2sec('01:30:00'))      # 5400.0
