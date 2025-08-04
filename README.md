# DFA Simulator in Java

A graphical simulator for Deterministic Finite Automata (DFA), using Java Swing for UI and Java AWT for visual rendering. The simulator allows users to define states, transitions, and simulate input strings to check for acceptance or rejection. This project was developed with a modular approach, separating logic and interface components. Graphical rendering is AI-assisted.

💡 Includes a built-in DFA to check if a binary number is divisible by 3 (MOD 3 DFA).

---

## 🧠 Features

- Interactive DFA visualizer with a GUI
- Add states, set start and accepting states
- Add labeled transitions (for symbols like 0, 1)
- Simulate input strings with animated traversal
- Displays **Accepted** or **Rejected** output based on final state
- Thread-safe animation using `SwingUtilities`
- Built-in sample DFA logic for quick testing (MOD 3)

---

## 📁 Project Structure

java/
├── DFASimulator.java # Main class (starts the GUI)
├── GUI/
│ ├── DFAPanel.java # Canvas component for visual state rendering (AI-aided)
│ └── InitDialog.java # Dialog for adding states and transitions
└── Model/
  ├── DFA.java # Core logic for DFA 
  ├── Transition.java
  └── State.java

---

## 💻 How to Run

### Requirements
- Java JDK 8 or higher
- Any IDE (IntelliJ, Eclipse, VS Code) or terminal access

### Using Terminal
```bash
javac DFASimulator.java GUI/*.java Model/*.java
java DFASimulator
```
###Using IDE

-Import or open the folder in your Java IDE.
-Set DFASimulator.java as the main class.
-Run it.

---

##⚙️ Built-In Example: MOD 3 DFA

The file DFA.java includes a method:
public void initialize(int x);

Calling this creates a DFA that accepts binary numbers divisible by 3.

DFA Design
States: q0 (Start/Accept), q1, q2

Input: Binary (0 or 1)

Logic:  -Tracks modulo 3 of the binary string
        -Accepts strings ending in state q0

---

##🛠 Technologies Used

Java – Programming Language
Swing – User interface and event handling
AWT – Canvas graphics (AI-assisted)
Threads – Simulated animation with delays
Object-Oriented Design – Separation of logic and UI

---

##👨‍💻 Author
Mohammed Ramshad T S
BTech in Computer Science, College of Engineering Cherthala
📧 mohammedramshad246@gmail.com
🌐 GitHub 
    
