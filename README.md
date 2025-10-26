# JADE_Coordination_Project

## Overview

This project demonstrates a **Multi-Agent System (MAS)** using **JADE** (Java Agent DEvelopment Framework) to simulate the **coordination of virtual robots** on a 10×10 grid.  

Each robot agent operates autonomously within a designated **non-overlapping zone**, moving randomly while avoiding collisions with other agents. A **Supervisor agent** assigns zones to the robots and orchestrates their start. The system visualizes all agents and their movements in real time using a **Java Swing GUI**.

---

## Features

- **Multi-Agent Architecture:** Supervisor + multiple Robot agents.  
- **Autonomous Movement:** Each robot moves independently within its assigned zone.  
- **Coordination:** Robots do not collide; each zone is non-adjacent.  
- **Staggered Start:** Robots start sequentially with a 4-second delay between each.  
- **GUI Visualization:**  
  - Robots displayed on a 10×10 grid.  
  - Robot names and positions are visible.  
  - Zone assignments and live positions shown in a side panel.  
- **Scalable:** Easily extendable to more robots or zones.  

---

## Project Structure
JADE_Coordination_Project/
│
├─ src/
│ ├─ agents/
│ │ ├─ SupervisorAgent.java
│ │ └─ RobotAgent.java
│ │
│ ├─ ui/
│ │ └─ GridGUI.java
│ │
│ └─ MainContainer.java
│
├─ lib/
│ └─ jade.jar (JADE library)
│
├─ README.md
└─ .gitignore


## Prerequisites

- **Java JDK 8+**  
- **JADE library** (`jade.jar`)  
- IDE: **VSCode / IntelliJ / Eclipse** recommended  

---

## How to Run

1. **Compile the project** in your IDE or using terminal:

javac -cp lib/jade.jar -d bin src/agents/*.java src/ui/*.java src/MainContainer.
java -cp bin:lib/jade.jar MainContainer 