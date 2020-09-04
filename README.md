# DFA-Machine-Toolbox
Here we have a simple DFA machine toolbox with two operations:
- DFA string detector
- NFA to DFA conversion
### Intro
This project is a part of Automata & Lanuages Theory course in AmirKabir University of Technology

Let's see how these codes work
### How it's work
Each of these codes read machine attributes from a file.

> The file name can be changed in code by your wish

Here is the format of machines in file:
- In first line we have alphabets
- In second line we have states
- In thied line we have starter state
- In fourth line we have final states
- And in the other lines we have transition functions in each line
#### Example of input
```
a b
Q0 Q1 Q2
Q0
Q1
Q0 a Q1
Q0 b Q1
Q1 a Q2
Q1 b Q2
Q2 a Q2
Q2 b Q2
```
> **Important** in conversion part, the result DFA machine will save in a file

**Hope you enjoy**
