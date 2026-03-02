# Roulette Game – Hardware & Software Integrated System

This project implements a **Roulette Game** using a hybrid **hardware–software architecture**, developed as part of the **Laboratory of Informatics and Computers (LIC)** course at Instituto Superior de Engenharia de Lisboa (ISEL).

The main goal of the project was to design, implement, and integrate multiple hardware modules described in **VHDL** with a software control system developed in **Kotlin**, simulating a real embedded gaming system.

---

## Project Overview

The roulette game supports numbers **0–9** and letters **A–D**.  
Players place bets using a **16-key matrix keyboard**, insert credits through a **coin acceptor**, and interact with the system via a **LCD display** and a **roulette display**.

The system operates in two main modes:

- **Game Mode**
- **Maintenance Mode**

All interactions between hardware modules and the software controller are performed through **well-defined interfaces and serial communication protocols**.

---

## System Architecture

The system is composed of the following main modules:

### Hardware (VHDL)
- **Keyboard Reader**
  - Key Decode
  - Ring Buffer (FIFO)
  - Output Buffer
- **Coin Acceptor**
- **Serial LCD Controller (SLCDC)**
- **Serial Roulette Controller (SRC)**

### Software (Kotlin)
- **Control Module**
- **Hardware Abstraction Layer (HAL)**
- **Keyboard Interface (KBD)**
- **LCD Driver**
- **Serial Emitter**
- **Roulette Display Controller**

The Control module runs on a PC and coordinates all system behavior, while the remaining modules simulate dedicated hardware components.

---

## Game Mode Functionality

- The game starts when the `*` key is pressed and credits are available
- Bets are placed using keys `0–9` and `A–D`
- Each bet consumes one credit (multiple bets per number are allowed)
- The `#` key ends the betting phase and starts the roulette draw
- The roulette spins for a random period, accepting bets until 5 seconds before stopping
- The drawn number and winnings are displayed
- Credits are updated and shown on the LCD after a short delay

---

## Maintenance Mode Functionality

Activated via a maintenance switch (`M`), this mode allows:

- Running test games without credits
- Viewing and resetting coin and game counters
- Viewing and resetting statistics of drawn numbers
- Safely shutting down the system, persisting data to text files:
  - Game and coin counters
  - Drawn numbers and associated prizes

Stored data is automatically loaded on system startup.

---

## Data Persistence

On shutdown, the system stores:
- Number of games played
- Coin counters
- Statistics of drawn numbers and prizes

Data is saved in **text files**, using one record per line with fields separated by `;`, and reloaded during system initialization.

---

## Technologies & Tools

- **Programming Language (Software):** Kotlin
- **Hardware Description Language:** VHDL
- **Architecture:** Modular hardware–software co-design
- **Communication:** Serial protocols with parity checking
- **Development Tools:** FPGA simulation tools, PC-based Kotlin execution

---

## Authors

This project was developed as a **group assignment** by:

- **Pilar Gonçalves**
- **Margarita Sysoeva**

---

## License

This project is **not open-source**.

All rights are reserved by the authors.  
The source code is provided **for viewing and evaluation purposes only**, as part of an academic portfolio.

Any reproduction, modification, or reuse without explicit written permission from **all authors** is strictly prohibited.

---

## Notes

This project emphasizes:
- Hardware–software integration
- Modular system design
- Low-level communication protocols
- Clear separation of responsibilities between components

It is intended to demonstrate practical skills in both digital hardware design and software engineering within an academic context.

---

*Thank you for taking the time to review this project.*