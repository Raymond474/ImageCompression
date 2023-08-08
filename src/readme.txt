CS4551 Multimedia Software Systems
@ Author: Raymond Martinez
California State University, Los Angeles

Compile requirement
======================================
JDK Version 7.0 or above


Compile Instruction on Command Line:
======================================
javac CS4551_Martinez.java MImage.java ImageFilter.java
or 
javac *.java


Execution Instruction on Command Line:
======================================
java CS4551_Martinez.java

output: Usage: java CS4551_Main [input_ppm_file]

java CS4551_Martinez.java Dune.ppm
Main Menu-----------------------------------
1. 8-bit UCQ and Error Diffusion
2. Generic UCQ and Error Diffusion
3. Quit
Please enter the task number [1-3]:

if input out is out of bounds it reprints the menu

if input is 1 it outputs Dune-ucq8bit and reprints the menu

if input is 2 it prints "Please enter the number of index bits for each R"
then after another input it prints "Please enter the number of index bits for each G"
then after another input it prints "Please enter the number of index bits for each B"
finally it outputs Dune-r1g1b1 and reprints the menu. (with the name changing based on
your input)