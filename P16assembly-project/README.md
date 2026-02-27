# Traffic Light Controller – P16 Assembly

This project implements a **traffic light controller for a pedestrian crossing**, developed as part of the **Computer Architecture** (AC) course at Instituto Superior de Engenharia de Lisboa (ISEL).

The main objective of the project was to explore low-level programming concepts by developing a complete embedded system using **assembly language** for the **P16 processor**, a custom educational processor designed for academic purposes.

---

## Project Overview

The system controls a pedestrian crossing with:

- A traffic light for vehicles (red, yellow, blinking yellow)
- A traffic light for pedestrians (red and green)
- A pedestrian push button to request crossing
- Configuration switches to define pedestrian crossing time
- Two operating modes: **operation mode** and **configuration mode**

The implementation is based on a **software state machine**, supported by timers and interrupts.

---

## Technical Context

- **Processor:** P16 (academic processor designed at ISEL)
- **Programming Language:** P16 Assembly
- **Development Board:** SDP16 with ATB and LAPI modules
- **Timer:** Pico Timer/Counter (pTC)
- **Paradigms explored:**
  - Input/Output handling
  - External and timer-based interrupts
  - Time measurement and delays
  - State machines implemented in assembly
  - Modular program structure using routines

---

## System Functionality

### Operation Mode (`CONFIG = 0`)
- Traffic light displays **blinking yellow** (0.5 s period)
- Pedestrian light displays **steady red**
- When the pedestrian button is pressed:
  - Traffic light turns **red**
  - Pedestrian light turns **green**
- The green pedestrian light remains active for at least `CROSSING_TIME`
- Each new button press extends the crossing time by the same period
- After time expires, the system returns to the initial state
- Mode indicator LED remains **green**

### Configuration Mode (`CONFIG = 1`)
- Mode indicator LED turns **yellow**
- Traffic light blinks yellow
- Pedestrian light blinks green
- The pedestrian crossing time can be configured using DIP switches
- Supported crossing times range from **10 to 60 seconds**
- The system returns to operation mode when `CONFIG = 0`

---

## Authors

This project was developed as a **group assignment** by:

- **Pilar Gonçalves**
- **Margarita Sysoeva**
- **Cecilia Marino**

---

## License

This project is **not open-source**.

All rights are reserved by the authors.  
The source code is provided **for viewing and evaluation purposes only**, as part of an academic portfolio.

Any reproduction, modification, or reuse without explicit permission from **all authors** is strictly prohibited.

---

## Notes

This project focuses on low-level system design and hardware–software interaction, rather than portability or real-world deployment.

It is intended to demonstrate understanding of computer architecture concepts, assembly programming, and embedded system behavior.

---

*Thank you for taking the time to review this project.*